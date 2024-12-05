package s3.individual.vinylo.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import s3.individual.vinylo.domain.User;
import s3.individual.vinylo.domain.Vinyl;
import s3.individual.vinylo.domain.VinylCollection;
import s3.individual.vinylo.domain.dtos.VinylCollectionDTO;
import s3.individual.vinylo.domain.dtos.VinylCollectionsDTO;
import s3.individual.vinylo.domain.dtos.VinylDTO;
import s3.individual.vinylo.domain.dtos.VinylsDTO;
import s3.individual.vinylo.domain.mappers.VinylCollectionMapper;
import s3.individual.vinylo.domain.mappers.VinylEntityMapper;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.persistence.entity.VinylEntity;
import s3.individual.vinylo.services.UserService;
import s3.individual.vinylo.services.VinylCollectionService;
import s3.individual.vinylo.services.VinylService;

@RestController
@RequestMapping("/collections")
public class VinylCollectionController {

    private final VinylCollectionService vinylCollectionService;
    private final VinylService vinylService;
    private final UserService userService;

    public VinylCollectionController(VinylCollectionService vinylCollectionService, VinylService vinylService,
            UserService userService) {
        this.vinylCollectionService = vinylCollectionService;
        this.vinylService = vinylService;
        this.userService = userService;
    }

    @PostMapping
    @RolesAllowed({ "REGULAR", "ADMIN", "PREMIUM" })
    public ResponseEntity<?> addVinylToCollection(@Valid @RequestBody VinylCollectionDTO dto) {

        User user = userService.getUserById(dto.getUser_id());

        Vinyl vinyl = vinylService.getVinylById(dto.getVinyl_id());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with ID " + dto.getUser_id() + " not found.");
        }

        VinylCollection vinylCollection = VinylCollectionMapper.fromDTO(dto);

        vinylCollection.setUser(user);
        vinylCollection.setVinyl(vinyl);

        vinylCollectionService.addVinylToCollection(null, vinylCollection);

        return ResponseEntity.status(HttpStatus.CREATED).body("Vinyl added to the users collection successfully");

    }

    @GetMapping
    @RolesAllowed({ "REGULAR", "ADMIN", "PREMIUM" })
    public VinylCollectionsDTO getAllCollections() {
        List<VinylCollection> collections = vinylCollectionService.getAllCollections();

        return VinylCollectionMapper.toCollectionsDTO(collections);
    }

    @GetMapping("/user/{userId}")
    @RolesAllowed({ "REGULAR", "ADMIN", "PREMIUM" })
    public ResponseEntity<VinylsDTO> getCollectionByUserId(@PathVariable("userId") int userId) {

        List<VinylEntity> collection = vinylCollectionService.getCollectionByUserId(userId);

        if (collection == null) {
            throw new CustomNotFoundException("Collection was not found");
        }

        VinylsDTO cdto = VinylEntityMapper.toVinylSummaryDTO(collection);

        return ResponseEntity.ok(cdto);
    }

    @GetMapping("/userVinyl/{userId}/{vinylId}")
    @RolesAllowed({ "REGULAR", "ADMIN", "PREMIUM" })
    public ResponseEntity<VinylDTO> getCollectionByUserIdAndVinylID(
            @PathVariable("userId") int userId, @PathVariable("vinylId") int vinylId) {

        VinylEntity collection = vinylCollectionService.getByUserIdAndVinylId(userId, vinylId);

        if (collection == null) {
            throw new CustomNotFoundException("Vinyl was not found");
        }

        VinylDTO cdto = VinylEntityMapper.toVinylDTO(collection);

        return ResponseEntity.ok(cdto);
    }

    @GetMapping("/{id}")
    @RolesAllowed({ "REGULAR", "ADMIN", "PREMIUM" })
    public ResponseEntity<VinylCollectionDTO> getCollectionById(@PathVariable("id") int id) {

        VinylCollection collection = vinylCollectionService.getCollectionById(id);

        if (collection == null) {
            throw new CustomNotFoundException("Collection was not found");
        }
        VinylCollectionDTO cdto = VinylCollectionMapper.toDTO(collection);

        return ResponseEntity.ok(cdto);
    }

    @DeleteMapping("/{id}")
    @RolesAllowed({ "REGULAR", "ADMIN", "PREMIUM" })
    public ResponseEntity<String> deleteCollection(@PathVariable("id") int id) {
        boolean isDeleted = vinylCollectionService.deleteCollection(id);
        if (isDeleted) {
            return ResponseEntity.ok("Collection with id " + id + " was successfully deleted.");
        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Collection with id " + id + " was not found.");
        }
    }
}
