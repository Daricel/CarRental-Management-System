package gui;

// Enum for car filter types in the GUI
public enum CarFilterType {
    MAKE("Make"),                  // Filter by car brand/make
    MAX_PRICE("Max Price"),        // Filter by maximum rental price
    MODEL("Model"),                // Filter by car model
    RENTED_BY_CUSTOMER("Rented by Customer"); // Filter by customer who rented it

    private final String displayName;

    CarFilterType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}