package s3.individual.vinylo.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;

import s3.individual.vinylo.persistence.entity.ArtistEntity;
import s3.individual.vinylo.persistence.entity.VinylEntity;
import s3.individual.vinylo.persistence.jparepository.ArtistJPARepo;
import s3.individual.vinylo.persistence.jparepository.VinylJPARepo;

@DataJpaTest
@ActiveProfiles("test")
class VinylJPARepoIntegrationTest {
    @Autowired
    private VinylJPARepo vinylJPARepo;

    @Autowired
    private ArtistJPARepo artistJPARepo;

    private VinylEntity testVinyl;

    @BeforeEach
    void setUp() {
        ArtistEntity artist = ArtistEntity.builder()
                .name("John")
                .bio("Bio")
                .build();
        artist = artistJPARepo.save(artist);

        testVinyl = VinylEntity.builder()
                .vinylType("EP")
                .title("Test")
                .description("Test")
                .isReleased(false)
                .artist(artist)
                .build();

        testVinyl = vinylJPARepo.save(testVinyl);
    }

    @Test
    void TestCreateNewVinyl_ShouldReturnANewVinyl() {
        VinylEntity savedVinyl = vinylJPARepo.save(testVinyl);
        assertEquals(1, savedVinyl.getId());
        assertNotEquals(("LP"), savedVinyl.getVinylType());
    }

    @Test
    void TestFindVinylById_ShouldReturnVinylById() {
        Optional<VinylEntity> foundVinyl = vinylJPARepo.findById(testVinyl.getId());
        assertTrue(foundVinyl.isPresent());
    }
}
