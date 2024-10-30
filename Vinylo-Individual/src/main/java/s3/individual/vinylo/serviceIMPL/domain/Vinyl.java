package s3.individual.vinylo.serviceIMPL.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Builder
@NoArgsConstructor
public class Vinyl {

    @NotNull
    public int id;
    public String vinylType;
    public String title;
    public String description;
    public Boolean isReleased;
    public Artist artist;

    public Vinyl(int id, String vinylType, String title, String description, Boolean isReleased, Artist artist) {
        this.id = id;
        this.vinylType = vinylType;
        this.title = title;
        this.description = description;
        this.isReleased = isReleased;
        this.artist = artist;
    }

    //I can also just use @Getters but i wanted to do it manually so that i know i can chose which one to use.
    public int getId() { return id; }

    public String getvinylType() { return vinylType; }

    public String getTitle() { return title; }

    public String getDescription() { return description; }

    public Boolean getisReleased() { return isReleased; }

    public Artist getArtist() { return artist; }

}
