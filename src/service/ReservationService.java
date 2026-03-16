package service;

import domain.Car;
import domain.Reservation;
import repo.IRepository;
import repo.RepositoryException;
import validator.IValidator;
import validator.ReservationValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationService {

    private final IRepository<Integer, Reservation> reservationRepository;
    private final IRepository<Integer, Car> carRepository;
    private final IValidator<Reservation> validator;

    public ReservationService(IRepository<Integer, Reservation> reservationRepository, IRepository<Integer, Car> carRepository) {
        this.reservationRepository = reservationRepository;
        this.carRepository = carRepository;
        this.validator = new ReservationValidator(carRepository);
    }

    public ReservationService(IRepository<Integer, Reservation> reservationRepository, IRepository<Integer, Car> carRepository, IValidator<Reservation> validator) {
        this.reservationRepository = reservationRepository;
        this.carRepository = carRepository;
        this.validator = validator;
    }

    private void validateReservation(Reservation reservation) {
        validator.validate(reservation);
    }

    public void addReservation(Reservation reservation) {
        validateReservation(reservation);
        reservationRepository.add(reservation.getId(), reservation);
    }

    public void modifyReservation(Reservation reservation) {
        validateReservation(reservation);
        reservationRepository.modify(reservation.getId(), reservation);
    }

    public void deleteReservation(Integer id) {
        reservationRepository.delete(id);
    }

    public Reservation findReservationById(Integer id) {
        return reservationRepository.findById(id);
    }

    public Iterable<Reservation> getAllReservations() {
        return reservationRepository.getAll();
    }

    public IRepository<Integer, Reservation> getRepository() {
        return reservationRepository;
    }


    public List<Reservation> filterReservationByCustomerName(String customerName) {
        List<Reservation> reservations = new ArrayList<>();
        reservationRepository.getAll().forEach(reservations::add);

        return reservations.stream()
                .filter(res -> res.getCustomerName().equalsIgnoreCase(customerName))
                .collect(Collectors.toList());
    }

    public List<Reservation> filterReservationByCarId(Integer carId) {
        List<Reservation> reservations = new ArrayList<>();
        reservationRepository.getAll().forEach(reservations::add);

        return reservations.stream()
                .filter(res -> res.getCarId().equals(carId))
                .collect(Collectors.toList());
    }

    public List<Car> getAllCarsRentedByCustomer(String customerName) {
        List<Reservation> reservations = new ArrayList<>();
        reservationRepository.getAll().forEach(reservations::add);

        return reservations.stream()
                .filter(res -> res.getCustomerName().equalsIgnoreCase(customerName))
                .map(res -> {
                    try {
                        return carRepository.findById(res.getCarId());
                    } catch (RepositoryException e) {
                        return null;
                    }
                })
                .filter(car -> car != null)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getCustomerNameByCarId(Integer carId) {
        List<Reservation> reservations = new ArrayList<>();
        reservationRepository.getAll().forEach(reservations::add);

        return reservations.stream()
                .filter(res -> res.getCarId().equals(carId))
                .map(Reservation::getCustomerName)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Reservation> getReservationsThatAreActiveAtACertainDate(LocalDate dateToCheck) {
        List<Reservation> reservations = new ArrayList<>();
        reservationRepository.getAll().forEach(reservations::add);

        return reservations.stream()
                .filter(res -> {
                    boolean startsBeforeOrOn = !res.getStartDate().isAfter(dateToCheck);
                    boolean endsAfterOrOn = !res.getEndDate().isBefore(dateToCheck);
                    return startsBeforeOrOn && endsAfterOrOn;
                })
                .collect(Collectors.toList());
    }
}