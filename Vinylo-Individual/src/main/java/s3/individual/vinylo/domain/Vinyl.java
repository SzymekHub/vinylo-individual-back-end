package s3.individual.vinylo.domain;

public class Vinyl {

    private String id;
    private String name;
    private String description;
    private Boolean hasArtist;

    public Vinyl(String id, String name, String description, Boolean hasArtist) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.hasArtist = hasArtist;
    }

    public String getId() { return id; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public Boolean getHasArtist() { return hasArtist; }
}
