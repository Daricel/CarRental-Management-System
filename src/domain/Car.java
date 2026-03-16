package domain;
import java.util.Objects;

public class Car implements IIdentifiable<Integer>{
    private Integer id;
    private String make;
    private String model;
    private double rentalPrice;
    public Car() {
    }
    public Car(int id, String make, String model, double rentalPrice) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.rentalPrice = rentalPrice;
    }
    @Override
    public Integer getId() {
        return id;
    }
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(double rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    @Override
    public boolean equals(Object carToCheck) {
        if (carToCheck == null || getClass() != carToCheck.getClass()) return false;
        Car  compareThisCar = (Car) carToCheck;
        return Double.compare(rentalPrice, compareThisCar.rentalPrice) == 0 && Objects.equals(id, compareThisCar.id) && Objects.equals(make, compareThisCar.make) && Objects.equals(model, compareThisCar.model);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, make, model, rentalPrice);
    }

    @Override
    public String toString() {
        return "Car{" +
                "id = '" + id + '\'' +
                ", make = '" + make + '\'' +
                ", model = '" + model + '\'' +
                ", rentalPrice = " + rentalPrice +
                '}';
    }
}
