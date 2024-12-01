package s3.individual.vinylo.persistence.jparepositoryIMPL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import s3.individual.vinylo.domain.Auction;
import s3.individual.vinylo.persistence.entity.ArtistEntity;
import s3.individual.vinylo.persistence.entity.AuctionEntity;
import s3.individual.vinylo.persistence.entity.UserEntity;
import s3.individual.vinylo.persistence.entity.VinylEntity;
import s3.individual.vinylo.persistence.jparepository.AuctionJPARepo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuctionJPARepositoryIMPLTest {
    @Mock
    private AuctionJPARepo auctionJPARepo;

    @InjectMocks
    private AuctionJPARepositoryIMPL auctionJPARepoImpl;

    private AuctionEntity auctionEntity;
    private Auction auction;

    @BeforeEach
    void setUp() {

        ArtistEntity artistEntity = ArtistEntity.builder()
                .id(1)
                .name("TestArtist")
                .bio("bio")
                .build();

        VinylEntity vinylEntity = VinylEntity.builder()
                .id(1)
                .vinylType("EP")
                .title("Test Vinyl")
                .description("TestDesc")
                .isReleased(false)
                .artist(artistEntity)
                .build();

        UserEntity userEntity = UserEntity.builder()
                .id(1)
                .username("TestUser")
                .build();

        auctionEntity = AuctionEntity.builder()
                .id(1)
                .title("Test Auction")
                .vinyl(vinylEntity)
                .seller(userEntity)
                .description("Test Description")
                .startingPrice(50.0)
                .currentPrice(100.0)
                .startTime(LocalDate.of(2024, 1, 1))
                .endTime(LocalDate.of(2024, 2, 1))
                .build();

        auction = Auction.builder()
                .id(1)
                .title("Test Auction")
                .description("Test Description")
                .startingPrice(50.0)
                .currentPrice(100.0)
                .startTime(LocalDate.of(2024, 1, 1))
                .endTime(LocalDate.of(2024, 2, 1))
                .build();
    }

    // !! to be fixed
    // @Test
    // void testSaveAuction_SuccessfulSave() {
    // // Arrange
    // when(auctionJPARepo.save(any(AuctionEntity.class))).thenReturn(auctionEntity);

    // // Act
    // Auction savedAuction = auctionJPARepoImpl.saveAuction(auction);

    // // Assert
    // assertNotNull(savedAuction);
    // assertEquals(auction.getTitle(), savedAuction.getTitle());
    // verify(auctionJPARepo, times(1)).save(any(AuctionEntity.class));
    // }

    // @Test
    // void testSaveAuction_NullAuction() {
    // // Arrange
    // when(auctionJPARepo.save(null)).thenThrow(new
    // IllegalArgumentException("AuctionEntity cannot be null"));

    // // Act & Assert
    // assertThrows(IllegalArgumentException.class, () ->
    // auctionJPARepoImpl.saveAuction(null));
    // verify(auctionJPARepo, times(1)).save(null);
    // }

    // @Test
    // void testGetAuctions_Success() {
    // // Arrange
    // when(auctionJPARepo.findAll()).thenReturn(List.of(auctionEntity));

    // // Act
    // List<Auction> auctions = auctionJPARepoImpl.getAuctions();

    // // Assert
    // assertFalse(auctions.isEmpty());
    // assertEquals(1, auctions.size());
    // assertEquals(auction.getTitle(), auctions.get(0).getTitle());
    // verify(auctionJPARepo, times(1)).findAll();
    // }

    // @Test
    // void testGetAuctionById_AuctionExists() {
    // // Arrange
    // when(auctionJPARepo.findById(1)).thenReturn(Optional.of(auctionEntity));

    // // Act
    // Auction foundAuction = auctionJPARepoImpl.getAuctionById(1);

    // // Assert
    // assertNotNull(foundAuction);
    // assertEquals(auction.getTitle(), foundAuction.getTitle());
    // verify(auctionJPARepo, times(1)).findById(1);
    // }

    // @Test
    // void testGetAuctionById_AuctionNotFound() {
    // // Arrange
    // when(auctionJPARepo.findById(99)).thenReturn(Optional.empty());

    // // Act
    // Auction foundAuction = auctionJPARepoImpl.getAuctionById(99);

    // // Assert
    // assertNull(foundAuction);
    // verify(auctionJPARepo, times(1)).findById(99);
    // }

    // @Test
    // void testGetAuctionByTitle_AuctionExists() {
    // // Arrange
    // when(auctionJPARepo.findByTitle("Test
    // Auction")).thenReturn(Optional.of(auctionEntity));

    // // Act
    // Auction foundAuction = auctionJPARepoImpl.getAuctionByTitle("Test Auction");

    // // Assert
    // assertNotNull(foundAuction);
    // assertEquals(auction.getTitle(), foundAuction.getTitle());
    // verify(auctionJPARepo, times(1)).findByTitle("Test Auction");
    // }

    @Test
    void testGetAuctionByTitle_AuctionNotFound() {
        // Arrange
        when(auctionJPARepo.findByTitle("NonExistentAuction")).thenReturn(Optional.empty());

        // Act
        Auction foundAuction = auctionJPARepoImpl.getAuctionByTitle("NonExistentAuction");

        // Assert
        assertNull(foundAuction);
        verify(auctionJPARepo, times(1)).findByTitle("NonExistentAuction");
    }

    @Test
    void testDeactivateAuctionById_AuctionExists() {
        // Arrange
        when(auctionJPARepo.existsById(1)).thenReturn(true);

        // Act
        boolean isDeactivated = auctionJPARepoImpl.deativateAuctionById(1);

        // Assert
        assertTrue(isDeactivated);
        verify(auctionJPARepo, times(1)).deleteById(1);
    }

    @Test
    void testDeactivateAuctionById_AuctionNotFound() {
        // Arrange
        when(auctionJPARepo.existsById(99)).thenReturn(false);

        // Act
        boolean isDeactivated = auctionJPARepoImpl.deativateAuctionById(99);

        // Assert
        assertFalse(isDeactivated);
        verify(auctionJPARepo, times(0)).deleteById(99);
    }

}
