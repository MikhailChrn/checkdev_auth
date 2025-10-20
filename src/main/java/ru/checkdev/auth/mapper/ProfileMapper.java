package ru.checkdev.auth.mapper;

import org.springframework.stereotype.Component;
import ru.checkdev.auth.domain.Profile;
import ru.checkdev.auth.dto.ProfileDTO;

@Component
public class ProfileMapper {
    public ProfileDTO getDtoFromEntity(Profile profile) {
        ProfileDTO result = new ProfileDTO();
        result.setId(profile.getId());
        result.setUsername(profile.getUsername());
        result.setEmail(profile.getEmail());
        result.setExperience(profile.getExperience());
        result.setPhotoId(profile.getPhoto() == null ? -1 : profile.getPhoto().getId());
        result.setUpdated(profile.getUpdated());
        result.setCreated(profile.getCreated());

        return result;
    }
}
