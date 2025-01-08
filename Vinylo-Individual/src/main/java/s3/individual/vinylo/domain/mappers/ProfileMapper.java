package s3.individual.vinylo.domain.mappers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import s3.individual.vinylo.domain.Profile;
import s3.individual.vinylo.domain.User;
import s3.individual.vinylo.domain.dtos.ProfileDTO;
import s3.individual.vinylo.domain.dtos.ProfilesDTO;
import s3.individual.vinylo.exceptions.CustomNotFoundException;
import s3.individual.vinylo.services.UserService;

@Component
public class ProfileMapper {

    private static UserService userService;

    public ProfileMapper(UserService userService) {
        ProfileMapper.userService = userService;
    }

    public static Profile toProfile(ProfileDTO profileDTO) {

        if (profileDTO == null) {
            return null;
        }

        if (userService == null) {
            throw new CustomNotFoundException("UserService not set in ProfileMapper");
        }

        User user = userService.getUserById(profileDTO.getUser_id());

        return new Profile(
                profileDTO.getId(),
                user,
                profileDTO.getBio(),
                profileDTO.getBalance());
    }

    public static ProfileDTO toProfileDTO(Profile profile) {

        if (profile == null) {
            return null;
        }

        ProfileDTO profileDTO = new ProfileDTO();

        profileDTO.setId(profile.getId());
        profileDTO.setUser_id(profile.getUser().getId());
        profileDTO.setBio(profile.getBio());
        profileDTO.setBalance(profile.getBalance());

        return profileDTO;
    }

    public static ProfilesDTO toProfilesDTO(List<Profile> profiles) {
        ProfilesDTO profilesDTO = new ProfilesDTO();
        for (Profile p : profiles) {
            profilesDTO.getProfiles().add(toProfileDTO(p));
        }
        return profilesDTO;
    }

    public static List<Profile> toProfiles(ProfilesDTO profilesDTO) {
        List<Profile> profiles = new ArrayList<>();
        for (ProfileDTO p : profilesDTO.getProfiles()) {
            profiles.add(toProfile(p));
        }
        return profiles;
    }
}
