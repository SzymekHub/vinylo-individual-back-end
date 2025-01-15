package s3.individual.vinylo.serviceimpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import s3.individual.vinylo.domain.Auction;
import s3.individual.vinylo.domain.Bid;
import s3.individual.vinylo.domain.Profile;
import s3.individual.vinylo.domain.User;
import s3.individual.vinylo.domain.dtos.ProfileAndUserDTO;
import s3.individual.vinylo.domain.dtos.ProfileDTO;
import s3.individual.vinylo.exceptions.CustomInternalServerErrorException;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.persistence.BidRepo;
import s3.individual.vinylo.services.AuctionService;
import s3.individual.vinylo.services.BidService;
import s3.individual.vinylo.services.ProfileService;
import s3.individual.vinylo.services.UserService;
import s3.individual.vinylo.persistence.entity.RoleEnum;
import s3.individual.vinylo.domain.mappers.ProfileMapper;

@Service
@RequiredArgsConstructor
public class BidServiceIMPL implements BidService {

    private final BidRepo bidRepo;
    private final AuctionService auctionService;
    private final ProfileService profileService;
    private final UserService userService;

    private static final int REGULAR_USER_FEE = 15;

    @Override
    @Transactional
    public boolean placeBid(int auctionId, int userId, double bidAmount) {
        Auction auction = auctionService.getAuctionsById(auctionId);

        if (auction == null) {
            throw new CustomNotFoundException("Auction with ID: " + auctionId + " not found.");
        }

        // check if the end time is today or before today
        if (auction.getEndTime().isBefore(LocalDate.now()) || auction.getEndTime().isEqual(LocalDate.now())) {
            throw new CustomInternalServerErrorException("Auction has ended. You cannot bid on a closed auction.");
        }

        if (auction.getStartTime().isAfter(LocalDate.now())) {
            throw new CustomInternalServerErrorException("Auction has not started yet.");
        }

        if (bidAmount <= auction.getCurrentPrice()) {
            throw new CustomInternalServerErrorException("Bid must be higher than the current price.");
        }

        ProfileAndUserDTO profileAndUser = profileService.getProfileAndUserById(userId);

        ProfileDTO profileDTO = profileAndUser.getProfile();
        User user = userService.getUserById(userId);

        if (profileDTO == null) {
            throw new CustomNotFoundException("Profile for user ID: " + userId + " not found.");
        }

        Profile profile = ProfileMapper.toProfile(profileDTO);

        // Check if user is premium user
        if (user.getRole().equals(RoleEnum.REGULAR)) {

            // Check if the user has enough balance for the bid + fee
            if (profile.getBalance() < bidAmount + REGULAR_USER_FEE) {
                throw new CustomInternalServerErrorException("Not enough funds to place bid.");
            }
            // Deduct the bid amount and fee for regular users.
            profile.setBalance(profile.getBalance() - (int) bidAmount - REGULAR_USER_FEE);
            profileService.updateProfile(userId, ProfileMapper.toProfileDTO(profile));
        } else {
            // Check if the user has enough balance for the bid
            if (profile.getBalance() < bidAmount) {
                throw new CustomInternalServerErrorException("Not enough funds to place bid.");
            }
            // Deduct the bid amount
            profile.setBalance(profile.getBalance() - (int) bidAmount);
            profileService.updateProfile(userId, ProfileMapper.toProfileDTO(profile));
        }

        Bid newBid = Bid.builder()
                .auctionId(auctionId)
                .userId(userId)
                .bidAmount(bidAmount)
                .bidTime(LocalDateTime.now())
                .build();

        bidRepo.saveBid(newBid);

        auction.setCurrentPrice(bidAmount);
        auctionService.saveAuction(auctionId, auction);
        return true;
    }

    @Override
    public Bid getHighestBid(int auctionId) {
        List<Bid> bids = bidRepo.getBidsByAuctionId(auctionId);

        return bids.stream()
                .max(Comparator.comparing(Bid::getBidAmount))
                .orElse(null);
    }
}