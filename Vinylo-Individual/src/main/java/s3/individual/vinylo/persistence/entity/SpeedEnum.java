package s3.individual.vinylo.persistence.entity;

public enum SpeedEnum {
    RPM_33_1_3("33 1/3 RPM"),
    RPM_45("45 RPM"),
    RPM_78("78 RPM");

    private final String label;

    // Constructor to initialize the enum with a label
    SpeedEnum(String label) {
        this.label = label;
    }

    // Getter method to retrieve the label
    public String getLabel() {
        return label;
    }
}
