package gui;

// Enum for car sorting types in the GUI
public enum CarSortType {
    PRICE_HIGH_TO_LOW("Price (High to Low)");  // Sort by rental price descending

    private final String displayName;

    CarSortType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}