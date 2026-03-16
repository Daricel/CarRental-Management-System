package service;

import domain.Car;
import repo.IRepository;
import validator.CarValidator;
import validator.IValidator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CarService {

    private final IRepository<Integer, Car> repo;
    private final IValidator<Car> validator;

    public CarService(IRepository<Integer, Car> repository) {
        this.repo = repository;
        this.validator = new CarValidator();
    }

    public CarService(IRepository<Integer, Car> repository, IValidator<Car> validator) {
        this.repo = repository;
        this.validator = validator;
    }

    private void validateCar(Car car) {
        validator.validate(car);
    }

    public void addCar(Car car) {
        validateCar(car);
        repo.add(car.getId(), car);
    }

    public void deleteCar(Integer id) {
        repo.delete(id);
    }

    public void modifyCar(Car car) {
        validateCar(car);
        repo.modify(car.getId(), car);
    }

    public Car findCarById(Integer id) {
        return repo.findById(id);
    }

    public Iterable<Car> getAllCars() {
        return repo.getAll();
    }

    public List<Car> filterCarsByMake(String make) {
        List<Car> cars = new ArrayList<>();
        repo.getAll().forEach(cars::add);

        return cars.stream()
                .filter(car -> car.getMake().equalsIgnoreCase(make))
                .collect(Collectors.toList());
    }

    public List<Car> filterCarsByRentalPrice(Double maxPrice) {
        List<Car> cars = new ArrayList<>();
        repo.getAll().forEach(cars::add);

        return cars.stream()
                .filter(car -> car.getRentalPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    public List<Car> getCarsOfAGivenModel(String model) {
        List<Car> cars = new ArrayList<>();
        repo.getAll().forEach(cars::add);

        return cars.stream()
                .filter(car -> car.getModel().equalsIgnoreCase(model))
                .collect(Collectors.toList());
    }

    public List<Car> getCarsSortedDescendingByPrice() {
        List<Car> cars = new ArrayList<>();
        repo.getAll().forEach(cars::add);

        return cars.stream()
                .sorted(Comparator.comparing(Car::getRentalPrice).reversed())
                .collect(Collectors.toList());
    }

    public IRepository<Integer, Car> getRepository() {
        return repo;
    }
}