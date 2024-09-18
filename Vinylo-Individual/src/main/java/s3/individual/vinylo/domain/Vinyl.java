package s3.individual.vinylo.domain;

import lombok.Setter;

@Setter
public class Vinyl {

    private String id;
    private String albumCapacity;
    private String name;
    private String description;
    private Boolean isReleased;
    private String aristName;

    public Vinyl(String id, String albumCapacity, String name, String description, Boolean isReleased, String aristName) {
        this.id = id;
        this.albumCapacity = albumCapacity;
        this.name = name;
        this.description = description;
        this.isReleased = isReleased;
        this.aristName = aristName;
    }

    public String getId() { return id; }

    public String getAlbumCapacity() { return albumCapacity; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public Boolean getisReleased() { return isReleased; }

    public String getAristName() { return aristName; }

}
