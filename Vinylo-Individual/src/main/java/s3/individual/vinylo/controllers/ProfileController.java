package s3.individual.vinylo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import s3.individual.vinylo.domain.Profile;
import s3.individual.vinylo.domain.dtos.ProfileDTO;
import s3.individual.vinylo.domain.mappers.ProfileMapper;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.services.ProfileService;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{id}")
    @RolesAllowed({ "REGULAR", "ADMIN", "PREMIUM" })
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable("id") int id) {

        Profile p = profileService.getProfileById(id);

        if (p == null) {
            throw new CustomNotFoundException("Profile record not found");
        }

        ProfileDTO pd = ProfileMapper.toProfileDTO(p);

        return ResponseEntity.ok(pd);

    }

    @PutMapping("/{id}")
    @RolesAllowed({ "REGULAR", "ADMIN", "PREMIUM" })
    public ResponseEntity<?> updateProfile(@Valid @RequestBody ProfileDTO profileDTO,
            @PathVariable("id") int id) {
        Profile newProfile = ProfileMapper.toProfile(profileDTO);
        Profile updatedProfile = profileService.saveProfile(id, newProfile);

        return ResponseEntity.ok(ProfileMapper.toProfileDTO(updatedProfile));
    }
}
