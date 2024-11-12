package s3.individual.vinylo.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "artist")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank
    @Column(name = "name")
    private String name;

    @Column(name = "bio")
    @Length(max = 150)
    private String bio;

    public String ToString() {
        return "Id: " + id + " name: " + name + " bio: " + bio;
    }

}
