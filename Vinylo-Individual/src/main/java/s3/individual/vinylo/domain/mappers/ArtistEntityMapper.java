package s3.individual.vinylo.domain.mappers;

import org.springframework.stereotype.Component;

import s3.individual.vinylo.domain.Artist;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.persistence.entity.ArtistEntity;

@Component
public class ArtistEntityMapper {

    // Converts Artist domain object to ArtistEntity
    public static ArtistEntity toEntity(Artist artist) {

        if (artist == null) {
            throw new CustomNotFoundException("Artist not set in ArtistEntityMapper");
        }

        ArtistEntity entity = new ArtistEntity();
        entity.setId(artist.getId());
        entity.setName(artist.getName());
        entity.setBio(artist.getBio());

        return entity;
    }

    // Converts ArtistEntity to Artist domain object
    public static Artist fromEntity(ArtistEntity entity) {
        if (entity == null) {
            throw new CustomNotFoundException("ArtistEntity not set in ArtistEntityMapper");
        }

        Artist artist = new Artist();
        artist.setId(entity.getId());
        artist.setName(entity.getName());
        artist.setBio(entity.getBio());

        return artist;
    }
}
