package s3.individual.vinylo.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import s3.individual.vinylo.domain.*;
import s3.individual.vinylo.domain.dtos.ProfileAndUserDTO;
import s3.individual.vinylo.domain.dtos.ProfileDTO;
import s3.individual.vinylo.domain.dtos.UserDTO;
import s3.individual.vinylo.domain.mappers.AuctionMapper;
import s3.individual.vinylo.domain.mappers.ProfileMapper;
import s3.individual.vinylo.exceptions.CustomGlobalException;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.persistence.BidRepo;
import s3.individual.vinylo.services.AuctionService;
import s3.individual.vinylo.services.ProfileService;
import s3.individual.vinylo.services.UserService;
import s3.individual.vinylo.services.VinylService;
import s3.individual.vinylo.persistence.entity.RoleEnum;
import s3.individual.vinylo.persistence.entity.SpeedEnum;
import s3.individual.vinylo.persistence.entity.StateEnum;
import s3.individual.vinylo.persistence.entity.VinylColorEnum;
import s3.individual.vinylo.persistence.entity.VinylTypeEnum;
import s3.individual.vinylo.serviceimpl.BidServiceIMPL;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BidServiceIMPLTest {

    @Mock
    private BidRepo bidRepoMock;

    @Mock
    private AuctionService auctionServiceMock;

    @Mock
    private ProfileService profileServiceMock;

    @Mock
    private UserService userServiceMock;

    @Mock
    private VinylService vinylServiceMock;

    @InjectMocks
    private BidServiceIMPL bidServiceMock;

    @InjectMocks
    private ProfileMapper profileMapper;

    @InjectMocks
    private AuctionMapper auctionMapper;

    private Auction testAuction;
    private Vinyl testVinyl;
    private Artist testArtist;
    private Profile testProfile;
    private ProfileAndUserDTO testProfileAndUserDTO;
    private ProfileDTO testProfileDTO;
    private UserDTO testUserDTO;
    private User testUser;
    private User testUser2;

    @BeforeEach
    void setUp() {
        profileMapper = new ProfileMapper(userServiceMock); // Inject the mock UserService into ProfileMapper

        auctionMapper = new AuctionMapper(vinylServiceMock, userServiceMock, bidServiceMock);

        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testUser");
        testUser.setEmail("testUser@gmail.com");
        testUser.setPassword("testUser");
        testUser.setRole(RoleEnum.REGULAR);

        testUser2 = new User();
        testUser2.setId(2);
        testUser2.setUsername("testUser2");
        testUser2.setEmail("testUser@gmail.com");
        testUser2.setPassword("testUser2");
        testUser2.setRole(RoleEnum.PREMIUM);

        testUserDTO = new UserDTO();
        testUserDTO.setId(1);
        testUserDTO.setUsername("testUser");
        testUserDTO.setEmail("testUser@gmail.com");
        testUserDTO.setPassword("testUser");
        testUserDTO.setRole("REGULAR");

        testProfile = new Profile();
        testProfile.setId(1);
        testProfile.setUser(testUser);
        testProfile.setBio("I am USER 1");
        testProfile.setBalance(5000);

        testProfileDTO = new ProfileDTO();
        testProfileDTO.setId(1);
        testProfileDTO.setUser_id(1);
        testProfileDTO.setBio("I am USER 1");
        testProfileDTO.setBalance(5000);

        testProfileAndUserDTO = new ProfileAndUserDTO();
        testProfileAndUserDTO.setProfile(testProfileDTO);
        testProfileAndUserDTO.setUser(testUserDTO);

        testArtist = new Artist();
        testArtist.setId(1);
        testArtist.setName("Artist1");
        testArtist.setBio("I am an Artist");

        testVinyl = new Vinyl();
        testVinyl.setId(1);
        testVinyl.setVinylType(VinylTypeEnum.LP_12_INCH);
        testVinyl.setSpeed(SpeedEnum.RPM_45);
        testVinyl.setTitle("Test Vinyl");
        testVinyl.setDescription("Test Desc");
        testVinyl.setState(StateEnum.ORIGINAL);
        testVinyl.setColor(VinylColorEnum.WHITE);
        testVinyl.setIsReleased(true);
        testVinyl.setArtist(testArtist);
        testVinyl.setSpotifyAlbumId("afwqf123214");

        testAuction = new Auction();
        testAuction.setId(1);
        testAuction.setTitle("Test Auction");
        testAuction.setVinyl(testVinyl);
        testAuction.setSeller(testUser2);
        testAuction.setDescription("Test Auction");
        testAuction.setStartingPrice(45.00);
        testAuction.setCurrentPrice(50.0);
        testAuction.setStartTime(LocalDate.now().minusDays(1));
        testAuction.setEndTime(LocalDate.now().plusDays(1));

    }

    @Test
    void testPlaceBid_ShouldPlaceBidSuccessfully() {
        // Arrange
        when(auctionServiceMock.getAuctionsById(1)).thenReturn(testAuction);

        when(profileServiceMock.getProfileAndUserById(1)).thenReturn(testProfileAndUserDTO);

        when(userServiceMock.getUserById(1)).thenReturn(testUser2);

        when(bidRepoMock.saveBid(any(Bid.class))).thenReturn(new Bid());

        // Act
        Bid result = bidServiceMock.placeBid(1, 1, 60.0);

        // Assert
        assertNotNull(result);
        verify(bidRepoMock).saveBid(any(Bid.class));
        verify(auctionServiceMock).getAuctionsById(1);
        verify(profileServiceMock).getProfileAndUserById(1);
    }

    @Test
    void testPlaceBid_ShouldDeductBidAmountAndFeeForRegularUsers() {
        // Arrange
        when(auctionServiceMock.getAuctionsById(1)).thenReturn(testAuction);
        when(profileServiceMock.getProfileAndUserById(1)).thenReturn(testProfileAndUserDTO);
        when(userServiceMock.getUserById(1)).thenReturn(testUser);
        when(bidRepoMock.saveBid(any(Bid.class))).thenReturn(new Bid());

        // Act
        bidServiceMock.placeBid(1, 1, 60.0);
        testProfile.setBalance(5000 - 60 - 15);

        // Assert
        assertEquals(5000 - 60 - 15, testProfile.getBalance());
        verify(profileServiceMock).updateProfile(eq(1), any(ProfileDTO.class));
        verify(bidRepoMock).saveBid(any(Bid.class));
        verify(auctionServiceMock).getAuctionsById(1);
        verify(profileServiceMock).getProfileAndUserById(1);
    }

    @Test
    void testPlaceBid_ShouldThrowExceptionWhenAuctionNotFound() {
        // Arrange
        when(auctionServiceMock.getAuctionsById(1)).thenReturn(null);

        // Act & Assert
        assertThrows(CustomNotFoundException.class, () -> bidServiceMock.placeBid(1, 1, 60.0));
        verify(auctionServiceMock).getAuctionsById(1);
        verifyNoInteractions(profileServiceMock, userServiceMock, bidRepoMock);
    }

    @Test
    void testPlaceBid_ShouldThrowExceptionWhenAuctionEnded() {
        // Arrange
        testAuction.setEndTime(LocalDate.now().minusDays(1));
        when(auctionServiceMock.getAuctionsById(1)).thenReturn(testAuction);

        // Act & Assert
        assertThrows(CustomGlobalException.class, () -> bidServiceMock.placeBid(1, 1, 60.0));
        verify(auctionServiceMock).getAuctionsById(1);
        verifyNoInteractions(profileServiceMock, userServiceMock, bidRepoMock);
    }

    @Test
    void testPlaceBid_ShouldThrowExceptionWhenAuctionEndTimeIsTodayOrBefore() {
        // Arrange
        testAuction.setEndTime(LocalDate.now()); // End time is today
        when(auctionServiceMock.getAuctionsById(1)).thenReturn(testAuction);

        // Act & Assert
        assertThrows(CustomGlobalException.class, () -> bidServiceMock.placeBid(1, 1, 60.0));
        verify(auctionServiceMock).getAuctionsById(1);
        verifyNoInteractions(profileServiceMock, userServiceMock, bidRepoMock);

        // Arrange
        testAuction.setEndTime(LocalDate.now().minusDays(1)); // End time is before today
        when(auctionServiceMock.getAuctionsById(1)).thenReturn(testAuction);

        // Act & Assert
        assertThrows(CustomGlobalException.class, () -> bidServiceMock.placeBid(1, 1, 60.0));
        verify(auctionServiceMock, times(2)).getAuctionsById(1);
        verifyNoInteractions(profileServiceMock, userServiceMock, bidRepoMock);
    }

    @Test
    void testPlaceBid_ShouldThrowExceptionWhenBidAmountIsLowerThanCurrentPrice() {
        // Arrange
        when(auctionServiceMock.getAuctionsById(1)).thenReturn(testAuction);

        // Act & Assert
        assertThrows(CustomGlobalException.class, () -> bidServiceMock.placeBid(1, 1, 40.0));
        verify(auctionServiceMock).getAuctionsById(1);
        verifyNoInteractions(profileServiceMock, userServiceMock, bidRepoMock);
    }

    @Test
    void testPlaceBid_ShouldThrowExceptionWhenAuctionNotStarted() {
        // Arrange
        testAuction.setStartTime(LocalDate.now().plusDays(1));
        when(auctionServiceMock.getAuctionsById(1)).thenReturn(testAuction);

        // Act & Assert
        assertThrows(CustomGlobalException.class, () -> bidServiceMock.placeBid(1, 1, 60.0));
        verify(auctionServiceMock).getAuctionsById(1);
        verifyNoInteractions(profileServiceMock, userServiceMock, bidRepoMock);
    }

    @Test
    void testPlaceBid_ShouldThrowExceptionWhenUserHasInsufficientFunds() {
        // Arrange
        when(auctionServiceMock.getAuctionsById(1)).thenReturn(testAuction);
        when(profileServiceMock.getProfileAndUserById(1)).thenReturn(testProfileAndUserDTO);
        testProfile.setBalance(10); // Insufficient funds
        testProfileDTO.setBalance(10); // Update the balance in the DTO as well
        when(userServiceMock.getUserById(1)).thenReturn(testUser);

        // Act & Assert
        assertThrows(CustomGlobalException.class, () -> bidServiceMock.placeBid(1, 1, 60.0));
        verify(auctionServiceMock).getAuctionsById(1);
        verify(profileServiceMock).getProfileAndUserById(1);
        verifyNoInteractions(bidRepoMock);
    }

    @Test
    void testPlaceBid_ShouldThrowExceptionWhenProfileNotFound() {
        // Arrange
        when(auctionServiceMock.getAuctionsById(1)).thenReturn(testAuction);
        when(profileServiceMock.getProfileAndUserById(1)).thenThrow(new CustomNotFoundException("Profile not found"));

        // Act & Assert
        assertThrows(CustomNotFoundException.class, () -> bidServiceMock.placeBid(1, 1, 60.0));
        verify(auctionServiceMock).getAuctionsById(1);
        verify(profileServiceMock).getProfileAndUserById(1);
        verifyNoInteractions(userServiceMock, bidRepoMock);
    }

    @Test
    void testGetHighestBid_ShouldReturnHighestBid() {
        // Arrange
        Bid highestBid = new Bid();
        highestBid.setBidAmount(100.0);
        when(bidRepoMock.getBidsByAuctionId(1)).thenReturn(List.of(
                new Bid() {
                    {
                        setBidAmount(50.0);
                    }
                },
                highestBid,
                new Bid() {
                    {
                        setBidAmount(75.0);
                    }
                }));

        // Act
        Bid result = bidServiceMock.getHighestBid(1);

        // Assert
        assertEquals(highestBid, result);
        verify(bidRepoMock).getBidsByAuctionId(1);
    }

    @Test
    void testGetHighestBid_ShouldReturnNullWhenNoBids() {
        // Arrange
        when(bidRepoMock.getBidsByAuctionId(1)).thenReturn(List.of());

        // Act
        Bid result = bidServiceMock.getHighestBid(1);

        // Assert
        assertNull(result);
        verify(bidRepoMock).getBidsByAuctionId(1);
    }

}
