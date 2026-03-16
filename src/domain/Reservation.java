package domain;
import java.time.LocalDate;
import java.util.Objects;
public class Reservation implements IIdentifiable<Integer>{
    private Integer id;
    private Integer carId;
    private String customerName;
    private LocalDate startDate;
    private LocalDate endDate;
    public Reservation() {
    }
    public Reservation(Integer id, Integer carId, String customerName, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.carId = carId;
        this.customerName = customerName;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    @Override
    public Integer getId() {
        return id;
    }
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) && Objects.equals(carId, that.carId) && Objects.equals(customerName, that.customerName) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public String toString() {
        return "Reservations{" +
                "id=" + id +
                ", carId=" + carId +
                ", customerName='" + customerName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, carId, customerName, startDate, endDate);
    }
}
