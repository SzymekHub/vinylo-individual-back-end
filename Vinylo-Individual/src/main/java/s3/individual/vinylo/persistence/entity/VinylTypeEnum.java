package s3.individual.vinylo.persistence.entity;

public enum VinylTypeEnum {
    LP_12_INCH("12-Inch Albums (LP or Long-playing)"),
    EP("Extended Play Vinyl Records (EP)"),
    SINGLE_7_INCH("7 Inch Singles"),
    SINGLE_12_INCH("12-inch Singles"),
    RECORD_10_INCH("10-Inch Records");

    private final String label;

    // Constructor to initialize the enum with a label
    VinylTypeEnum(String label) {
        this.label = label;
    }

    // Getter method to retrieve the label
    public String getLabel() {
        return label;
    }
}
