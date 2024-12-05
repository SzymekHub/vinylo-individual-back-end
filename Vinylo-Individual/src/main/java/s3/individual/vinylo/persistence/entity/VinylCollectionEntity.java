package s3.individual.vinylo.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "vinyl_collection")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VinylCollectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "vinyl_id")
    private VinylEntity vinyl;
}
