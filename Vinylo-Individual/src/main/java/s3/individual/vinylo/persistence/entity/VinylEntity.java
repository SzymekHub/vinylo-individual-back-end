package s3.individual.vinylo.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "vinyl")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VinylEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull
    @Column(name = "vinylType")
    private String vinylType;

    @NotNull
    @Column(name = "speed")
    private String speed;

    @NotNull
    @Column(name = "title")
    private String title;

    @NotNull
    @Length(max = 150)
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "state")
    private String state;

    @NotNull
    @Column(name = "color")
    private String color;

    @NotNull
    @Column(name = "isReleased")
    private Boolean isReleased;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "artist_id")
    private ArtistEntity artist;

}
