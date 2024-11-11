package s3.individual.vinylo.domain.mappers;

import s3.individual.vinylo.domain.Artist;
import s3.individual.vinylo.persistence.entity.ArtistEntity;

public class ArtistEntityMapper {

    private ArtistEntityMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    // Converts Artist domain object to ArtistEntity
    public static ArtistEntity toEntity(Artist artist) {
        if (artist == null) {
            return null;
        }

        ArtistEntity entity = new ArtistEntity();
        // Do not set ID, as it's auto-generated by the database
        entity.setName(artist.getName());
        entity.setBio(artist.getBio());

        return entity;
    }

    // Converts ArtistEntity to Artist domain object
    public static Artist fromEntity(ArtistEntity entity) {
        if (entity == null) {
            return null;
        }

        Artist artist = new Artist();
        artist.setId(entity.getId()); // Retrieve the auto-generated ID from the entity
        artist.setName(entity.getName());
        artist.setBio(entity.getBio());

        return artist;
    }
}
