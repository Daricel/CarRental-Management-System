package gui;

// Enum for reservation filter types in the GUI
public enum ReservationFilterType {
    CUSTOMER_NAME("Customer Name"),       // Filter by customer's name
    CAR_ID("Car ID"),                     // Filter by car ID
    ACTIVE_AT_DATE("Active at Date"),     // Filter by reservations active on a specific date
    CUSTOMERS_BY_CAR_ID("Customers by Car ID"); // Filter customers by car ID

    private final String displayName;

    ReservationFilterType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}