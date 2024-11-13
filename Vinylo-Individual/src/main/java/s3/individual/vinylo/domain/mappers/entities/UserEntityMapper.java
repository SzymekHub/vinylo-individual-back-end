package s3.individual.vinylo.domain.mappers.entities;

import org.springframework.stereotype.Component;

import s3.individual.vinylo.domain.User;
import s3.individual.vinylo.persistence.entity.UserEntity;

@Component // Make this class a Spring-managed bean
public class UserEntityMapper {

    public static UserEntity toEntity(User user) {

        if (user == null) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        // no id because, it's auto-generated by the database
        userEntity.setUsername(user.getUsername());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
        userEntity.setIsPremium(user.getIsPremium());

        return userEntity;
    }

    public static User fromEntity(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }

        User user = new User();
        user.setId(userEntity.getId());
        user.setEmail(userEntity.getEmail());
        user.setPassword(userEntity.getPassword());
        user.setIsPremium(userEntity.getIsPremium());

        return user;
    }
}
