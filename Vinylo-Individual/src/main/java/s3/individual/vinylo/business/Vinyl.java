package s3.individual.vinylo.business;

import lombok.Setter;

@Setter
public class Vinyl {

    private int id;
    private String vinylType;
    private String name;
    private String description;
    private Boolean isReleased;
    private String aristName;

    public Vinyl(int id, String vinylType, String name, String description, Boolean isReleased, String aristName) {
        this.id = id;
        this.vinylType = vinylType;
        this.name = name;
        this.description = description;
        this.isReleased = isReleased;
        this.aristName = aristName;
    }

    public int getId() { return id; }

    public String getvinylType() { return vinylType; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public Boolean getisReleased() { return isReleased; }

    public String getAristName() { return aristName; }

}
