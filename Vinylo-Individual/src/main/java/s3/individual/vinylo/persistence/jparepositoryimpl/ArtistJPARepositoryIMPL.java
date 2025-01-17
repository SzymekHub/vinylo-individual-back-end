package s3.individual.vinylo.persistence.jparepositoryimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import s3.individual.vinylo.domain.Artist;
import s3.individual.vinylo.domain.mappers.ArtistEntityMapper;
import s3.individual.vinylo.persistence.ArtistRepo;
import s3.individual.vinylo.persistence.jparepository.ArtistJPARepo;

@Repository
public class ArtistJPARepositoryIMPL implements ArtistRepo {

    private final ArtistJPARepo artistJPARepo;

    public ArtistJPARepositoryIMPL(ArtistJPARepo artistJPARepo) {
        this.artistJPARepo = artistJPARepo;
    }

    @Override
    public List<Artist> getArtists() {
        return artistJPARepo.findAll().stream()
                // If found, map the ArtistEntity to a Artist domain object using
                // ArtistEntityMapper
                .map(ArtistEntityMapper::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Artist getArtistByName(String name) {
        // Look for a VinylEntity in the database by its ID
        return artistJPARepo.findByName(name)
                .map(ArtistEntityMapper::fromEntity)
                // If not found, return null
                .orElse(null);
    }

    @Override
    public Artist getArtistById(int id) {
        // Look for a ArtistEntity in the database by its ID
        return artistJPARepo.findById(id)
                // If found, map the ArtistEntity to a Artist domain object using
                // ArtistEntityMapper
                .map(ArtistEntityMapper::fromEntity)
                .orElse(null);
    }

    @Override
    public Artist saveArtist(Artist artist) {
        artistJPARepo.save(ArtistEntityMapper.toEntity(artist));
        return artist;
    }

    @Override
    public boolean deactivateArtistById(int id) {
        if (artistJPARepo.existsById(id)) {
            artistJPARepo.deleteById(id);
            return true;
        }
        return false;
    }

}
