package s3.individual.vinylo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import s3.individual.vinylo.domain.dtos.ProfileAndUserDTO;
import s3.individual.vinylo.domain.dtos.ProfileDTO;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.services.ProfileService;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final SimpMessagingTemplate messagingTemplate;

    public ProfileController(ProfileService profileService, SimpMessagingTemplate messagingTemplate) {
        this.profileService = profileService;
        this.messagingTemplate = messagingTemplate;

    }

    @GetMapping("/{id}")
    @RolesAllowed({ "REGULAR", "ADMIN", "PREMIUM" })
    public ResponseEntity<ProfileAndUserDTO> getProfile(@PathVariable("id") int id) {

        ProfileAndUserDTO profileAndUser = profileService.getProfileAndUserById(id);

        if (profileAndUser == null) {
            throw new CustomNotFoundException("Profile record not found");
        }

        return ResponseEntity.ok(profileAndUser);

    }

    @PutMapping("/{id}")
    @RolesAllowed({ "REGULAR", "ADMIN", "PREMIUM" })
    public ResponseEntity<ProfileDTO> updateProfile(@Valid @RequestBody ProfileDTO profileDTO,
            @PathVariable("id") int id) {

        ProfileDTO updatedProfileDTO = profileService.updateProfile(id, profileDTO);

        messagingTemplate.convertAndSendToUser(String.valueOf(id), "/topic/profileUpdated", updatedProfileDTO);
        return ResponseEntity.ok(updatedProfileDTO);
    }

    @PutMapping("/upgrade/{id}")
    @RolesAllowed({ "REGULAR" })
    public ResponseEntity<ProfileDTO> upgradeToPremium(@PathVariable("id") int id) {
        ProfileDTO upgradedProfileDTO = profileService.upgradeToPremium(id);
        messagingTemplate.convertAndSendToUser(String.valueOf(id), "/topic/profileUpdated", upgradedProfileDTO);
        return ResponseEntity.ok(upgradedProfileDTO);
    }
}