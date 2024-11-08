package s3.individual.vinylo.persistence.JPArepository;

import org.springframework.data.jpa.repository.JpaRepository;
import s3.individual.vinylo.persistence.entity.VinylEntity;

public interface VinylJPARepo extends JpaRepository<VinylEntity, Integer> {

    boolean existsById(int id);
    // // Find all vinyls by a specific artist's ID
    // List<VinylEntity> findByArtistId(int artistId);

    // // Fetch all vinyls by artist name
    // @Query("select v from VinylEntity v where v.artist.name = :artistName")
    // List<VinylEntity> getVinylsByArtistName(@Param("artistName") String
    // artistName);

    // // Find all released vinyls
    // List<VinylEntity> findByIsReleasedTrue();

    // // Find a vinyl by its title
    // VinylEntity findByTitle(String title);

    // // Fetch vinyl by title
    // @Query("select v from VinylEntity v where v.title = :title")
    // VinylEntity getVinylByTitle(@Param("title") String title);

    // // Find vinyls with a description containing a specific substring
    // List<VinylEntity> findByDescriptionContaining(String substring);

    // // Fetch all vinyls by type
    // @Query("select v from VinylEntity v where v.vinylType = :vinylType")
    // List<VinylEntity> getVinylsByType(@Param("vinylType") String vinylType);

    // // !! This query selects only the title of the VinylEntity and the name of
    // the
    // // ArtistEntity.
    // // !! The result will be a List<Object[]>, where each element is an array
    // // containing the title and artist name
    // // Query to get all vinyls with just the artist name
    // @Query("SELECT v, a.name FROM VinylEntity v JOIN v.artist a")
    // List<Object[]> findAllVinylsWithArtistNameOnly();

    // // !! This query uses JOIN FETCH to eagerly load the entire ArtistEntity
    // // associated with each VinylEntity.
    // // !! The result will be a List<VinylEntity>, with each VinylEntity including
    // // the full ArtistEntity object.
    // // Query to get all vinyls with the entire artist object
    // @Query("SELECT v FROM VinylEntity v JOIN FETCH v.artist")
    // List<VinylEntity> findAllVinylsWithArtistObject();

    // // Count the number of vinyl records for a specific artist
    // int countByArtist(ArtistEntity artist);
}
