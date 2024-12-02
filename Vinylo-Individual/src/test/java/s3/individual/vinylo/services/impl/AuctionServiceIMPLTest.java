package s3.individual.vinylo.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import s3.individual.vinylo.persistence.AuctionRepo;
import s3.individual.vinylo.persistence.entity.RoleEnum;
import s3.individual.vinylo.persistence.entity.SpeedEnum;
import s3.individual.vinylo.persistence.entity.StateEnum;
import s3.individual.vinylo.persistence.entity.VinylColorEnum;
import s3.individual.vinylo.persistence.entity.VinylTypeEnum;
import s3.individual.vinylo.serviceimpl.AuctionServiceIMPL;
import s3.individual.vinylo.domain.Artist;
import s3.individual.vinylo.domain.Vinyl;
import s3.individual.vinylo.domain.Auction;
import s3.individual.vinylo.domain.User;
import s3.individual.vinylo.exceptions.CustomInternalServerErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.List;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class AuctionServiceIMPLTest {

        @Mock
        private AuctionRepo auctionRepoMock;

        @InjectMocks
        private AuctionServiceIMPL auctionService;

        private Artist createArtist(int id, String name, String bio) {
                return Artist.builder()
                                .id(id)
                                .name(name)
                                .bio(bio)
                                .build();
        }

        private Vinyl createVinyl(int id, VinylTypeEnum vinylType, SpeedEnum speed, String title, String description,
                        StateEnum state, VinylColorEnum color, boolean isReleased,
                        Artist artist) {
                return Vinyl.builder()
                                .id(id)
                                .vinylType(vinylType)
                                .speed(speed)
                                .title(title)
                                .description(description)
                                .state(state)
                                .color(color)
                                .isReleased(isReleased)
                                .artist(artist)
                                .build();
        }

        private User createSeller(Integer id, String username, String email, String password, RoleEnum role) {
                return User.builder()
                                .id(id)
                                .username(username)
                                .email(email)
                                .password(password)
                                .role(RoleEnum.PREMIUM)
                                .build();
        }

        private Auction createAuction(int id, String title, Vinyl vinyl, User seller, String description,
                        double startingPrice,
                        double currentPrice, LocalDate startTime, LocalDate endTime) {
                return Auction.builder()
                                .id(id)
                                .title(title)
                                .vinyl(vinyl)
                                .seller(seller)
                                .description(description)
                                .startingPrice(startingPrice)
                                .currentPrice(currentPrice)
                                .startTime(startTime)
                                .endTime(endTime)
                                .build();
        }

        @Test
        void testSaveAuction_shouldSaveAndReturnNewAuction() {
                // Arrange
                Auction newAuction = createAuction(34,
                                "Rubber Soul vinyl auction!!",
                                createVinyl(2,
                                                VinylTypeEnum.EP,
                                                SpeedEnum.RPM_45,
                                                "Rubber Soul",
                                                "ROCK&ROLL",
                                                StateEnum.NEW,
                                                VinylColorEnum.COLORED,
                                                true,
                                                createArtist(2, "The Beatles", "Yeah yeah yeah")),
                                createSeller(2,
                                                "Username1",
                                                "UseerName1@gmail.com",
                                                "User1Password",
                                                RoleEnum.REGULAR),
                                "Fresh Rubber Soul vinyl!!",
                                35.00,
                                45.50,
                                LocalDate.now(),
                                LocalDate.now().plusDays(7));

                when(auctionRepoMock.saveAuction(newAuction)).thenReturn(newAuction);

                // Act
                Auction savedAuction = auctionService.saveAuction(null, newAuction);

                // Assert
                assertEquals(newAuction, savedAuction);
                verify(auctionRepoMock).saveAuction(newAuction);

        }

        @Test
        void testSaveAuction_shouldUpdateExistingAuction() {
                // Arrange
                int auctionId = 2;

                Auction existingAuction = createAuction(auctionId,
                                "Rubber Soul vinyl auction!!",
                                createVinyl(2,
                                                VinylTypeEnum.EP,
                                                SpeedEnum.RPM_45,
                                                "Rubber Soul",
                                                "ROCK&ROLL",
                                                StateEnum.NEW,
                                                VinylColorEnum.COLORED,
                                                true,
                                                createArtist(2, "The Beatles", "Yeah yeah yeah")),
                                createSeller(2,
                                                "Username1",
                                                "UseerName1@gmail.com",
                                                "User1Password",
                                                RoleEnum.REGULAR),
                                "Fresh Rubber Soul vinyl!!",
                                35.00,
                                45.50,
                                LocalDate.now(),
                                LocalDate.now().plusDays(7));

                Auction updatedAuction = createAuction(auctionId,
                                "Rubber Soul vinyl auction!!",
                                createVinyl(2,
                                                VinylTypeEnum.EP,
                                                SpeedEnum.RPM_45,
                                                "Rubber Soul",
                                                "ROCK&ROLL",
                                                StateEnum.USED,
                                                VinylColorEnum.COLORED,
                                                true,
                                                createArtist(2, "The Beatles", "Yeah yeah yeah")),
                                createSeller(2,
                                                "Username1",
                                                "UseerName1@gmail.com",
                                                "User1Password",
                                                RoleEnum.REGULAR),
                                "Used and bad Rubber Soul vinyl!!",
                                35.00,
                                25.50,
                                LocalDate.now(),
                                LocalDate.now().plusDays(3));

                when(auctionRepoMock.getAuctionById(auctionId)).thenReturn(existingAuction);
                when(auctionRepoMock.saveAuction(existingAuction)).thenReturn(updatedAuction);

                // Act
                Auction result = auctionService.saveAuction(auctionId, updatedAuction);

                // Assert
                assertEquals("Used and bad Rubber Soul vinyl!!", result.getDescription());
                assertEquals(25.50, result.getCurrentPrice());
                assertEquals(LocalDate.now().plusDays(3), result.getEndTime());

                verify(auctionRepoMock).saveAuction(existingAuction);
                verify(auctionRepoMock).getAuctionById(auctionId);

        }

        @Test
        void testSaveAuction_ShouldThrowExceptionWhenAuctionNotFoundForUpdate() {
                // Arrange
                int auctionId = 2;
                Auction updatedAuction = createAuction(auctionId,
                                "Rubber Soul vinyl auction!!",
                                createVinyl(2,
                                                VinylTypeEnum.EP,
                                                SpeedEnum.RPM_45,
                                                "Rubber Soul",
                                                "ROCK&ROLL",
                                                StateEnum.NEW,
                                                VinylColorEnum.COLORED,
                                                true,
                                                createArtist(2, "The Beatles", "Yeah yeah yeah")),
                                createSeller(2,
                                                "Username1",
                                                "UseerName1@gmail.com",
                                                "User1Password",
                                                RoleEnum.REGULAR),
                                "Used and bad Rubber Soul vinyl!!",
                                35.00,
                                25.50,
                                LocalDate.now(),
                                LocalDate.now().plusDays(3));

                when(auctionRepoMock.getAuctionById(auctionId)).thenReturn(null);

                // Act & Assert
                assertThrows(CustomInternalServerErrorException.class, () -> {
                        auctionService.saveAuction(auctionId, updatedAuction);
                });
        }

        @Test
        void testGetAuctions_shouldReturnAllAuctions() {
                // Arrange
                Auction auction = createAuction(69,
                                "Rubber Soul vinyl auction!!",
                                createVinyl(2,
                                                VinylTypeEnum.EP,
                                                SpeedEnum.RPM_45,
                                                "Rubber Soul",
                                                "ROCK&ROLL",
                                                StateEnum.NEW,
                                                VinylColorEnum.COLORED,
                                                true,
                                                createArtist(2, "The Beatles", "Yeah yeah yeah")),
                                createSeller(2,
                                                "Username1",
                                                "UseerName1@gmail.com",
                                                "User1Password",
                                                RoleEnum.REGULAR),
                                "Fresh Rubber Soul vinyl!!",
                                35.00,
                                45.50,
                                LocalDate.now(),
                                LocalDate.now().plusDays(7));
                Auction auction2 = createAuction(70,
                                "ALL RED vinyl auction!!",
                                createVinyl(2,
                                                VinylTypeEnum.SINGLE_7_INCH,
                                                SpeedEnum.RPM_45,
                                                "ALL RED",
                                                "Syrup",
                                                StateEnum.NEW,
                                                VinylColorEnum.COLORED,
                                                true,
                                                createArtist(1, "PLAYBOY CARTI", "syrup")),
                                createSeller(3,
                                                "Username3",
                                                "UseerName3@gmail.com",
                                                "User3Password",
                                                RoleEnum.PREMIUM),
                                "OPIUM AUCTION!!",
                                666.00,
                                888.00,
                                LocalDate.now(),
                                LocalDate.now().plusDays(30));

                when(auctionRepoMock.getAuctions()).thenReturn(List.of(auction, auction2));

                // Act
                List<Auction> actualAuctions = auctionService.getAuctions();

                // Assert
                assertEquals(List.of(auction, auction2), actualAuctions);
                verify(auctionRepoMock).getAuctions();

        }

        @Test
        void testGetAuctionsById_shouldReturnAuctionWithGivenId() {
                // Arrange
                int auctionId = 666;
                Auction expectedAuction = createAuction(auctionId,
                                "Rubber Soul vinyl auction!!",
                                createVinyl(2,
                                                VinylTypeEnum.EP,
                                                SpeedEnum.RPM_45,
                                                "Rubber Soul",
                                                "ROCK&ROLL",
                                                StateEnum.NEW,
                                                VinylColorEnum.COLORED,
                                                true,
                                                createArtist(2, "The Beatles", "Yeah yeah yeah")),
                                createSeller(2,
                                                "Username1",
                                                "UseerName1@gmail.com",
                                                "User1Password",
                                                RoleEnum.REGULAR),
                                "Fresh Rubber Soul vinyl!!",
                                35.00,
                                45.50,
                                LocalDate.now(),
                                LocalDate.now().plusDays(7));

                when(auctionRepoMock.getAuctionById(auctionId)).thenReturn(expectedAuction);

                // Act
                Auction actualAuction = auctionService.getAuctionsById(auctionId);

                // Assert
                assertEquals(expectedAuction, actualAuction);
                verify(auctionRepoMock).getAuctionById(auctionId);
        }

        @Test
        void testDeativateAuctionById_shouldReturnTrueWhenAuctionIsDeactivated() {
                // Arrange
                int auctionId = 55;
                when(auctionRepoMock.deativateAuctionById(auctionId)).thenReturn(true);

                // Act
                boolean isDeleted = auctionService.deativateAuctionById(auctionId);

                // Assert
                assertEquals(true, isDeleted);
                verify(auctionRepoMock).deativateAuctionById(auctionId);
        }

        @Test
        void testPlaceBid() {
                // Arrange
                int auctionId = 1;
                double initialPrice = 50.00;
                double bidAmount = 60.00;

                Auction auction = createAuction(auctionId,
                                "Rubber Soul vinyl auction!!",
                                createVinyl(2,
                                                VinylTypeEnum.EP,
                                                SpeedEnum.RPM_45,
                                                "Rubber Soul",
                                                "ROCK&ROLL",
                                                StateEnum.NEW,
                                                VinylColorEnum.COLORED,
                                                true,
                                                createArtist(2, "The Beatles", "Yeah yeah yeah")),
                                createSeller(2, "Username1", "UseerName1@gmail.com", "User1Password", RoleEnum.REGULAR),
                                "Fresh Rubber Soul vinyl!!",
                                35.00,
                                initialPrice,
                                LocalDate.now(),
                                LocalDate.now().plusDays(7));

                when(auctionRepoMock.getAuctionById(auctionId)).thenReturn(auction);
                when(auctionRepoMock.saveAuction(auction)).thenReturn(auction);

                // Act
                boolean bidPlaced = auctionService.placeBid(auctionId, bidAmount);

                // Assert
                assertEquals(true, bidPlaced);
                assertEquals(bidAmount, auction.getCurrentPrice());
                verify(auctionRepoMock).getAuctionById(auctionId);
                verify(auctionRepoMock).saveAuction(auction);
        }

        @Test
        void testPlaceBid_ShouldFailIfBidIsLowerThanCurrentPrice() {
                // Arrange
                int auctionId = 1;
                double initialPrice = 50.00;
                double lowerBidAmount = 40.00;

                Auction auction = createAuction(auctionId,
                                "Rubber Soul vinyl auction!!",
                                createVinyl(2,
                                                VinylTypeEnum.EP,
                                                SpeedEnum.RPM_45,
                                                "Rubber Soul",
                                                "ROCK&ROLL",
                                                StateEnum.NEW,
                                                VinylColorEnum.COLORED,
                                                true,
                                                createArtist(2, "The Beatles", "Yeah yeah yeah")),
                                createSeller(2, "Username1", "UseerName1@gmail.com", "User1Password", RoleEnum.REGULAR),
                                "Fresh Rubber Soul vinyl!!",
                                35.00,
                                initialPrice,
                                LocalDate.now(),
                                LocalDate.now().plusDays(7));

                when(auctionRepoMock.getAuctionById(auctionId)).thenReturn(auction);

                // Act
                boolean bidPlaced = auctionService.placeBid(auctionId, lowerBidAmount);

                // Assert
                assertEquals(false, bidPlaced);
                assertEquals(initialPrice, auction.getCurrentPrice());
                verify(auctionRepoMock).getAuctionById(auctionId);
                verify(auctionRepoMock, never()).saveAuction(auction);
        }

        @Test
        void testPlaceBid_ShouldFailIfAuctionDoesNotExist() {
                // Arrange
                int auctionId = 1;
                double bidAmount = 60.00;

                when(auctionRepoMock.getAuctionById(auctionId)).thenReturn(null);

                // Act
                boolean bidPlaced = auctionService.placeBid(auctionId, bidAmount);

                // Assert
                assertEquals(false, bidPlaced);
                verify(auctionRepoMock).getAuctionById(auctionId);
                verify(auctionRepoMock, never()).saveAuction(any(Auction.class));
        }

}
