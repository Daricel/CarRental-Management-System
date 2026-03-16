package validator;

import domain.Car;
import domain.Reservation;
import repo.IRepository;
import repo.RepositoryException;
import repo.ValidationException;

import java.time.LocalDate;

public class ReservationValidator implements IValidator<Reservation> {
    private static final int THE_MINIMUM_ID_THE_CAR_CAN_HAVE = 0;
    private static final int THE_MINIMUM_ID_THE_RESERVATION_CAN_HAVE = 0;
    private IRepository<Integer, Car> carRepository;
    public ReservationValidator(IRepository<Integer, Car> carRepository) {
        this.carRepository = carRepository;
    }
    @Override
    public void validate(Reservation reservationToBeChecked) throws ValidationException{
        StringBuilder errors = new StringBuilder();
        if (reservationToBeChecked.getId() == null || reservationToBeChecked.getId() <= THE_MINIMUM_ID_THE_RESERVATION_CAN_HAVE) {
            errors.append("Reservation id must be a positive integer");
        }
        if (reservationToBeChecked.getCarId() == null || reservationToBeChecked.getCarId() <= THE_MINIMUM_ID_THE_CAR_CAN_HAVE) {
            errors.append("Car id must be a positive integer");
        }
        try {
            if (carRepository.findById(reservationToBeChecked.getCarId()) == null) {
                errors.append("Car with id ").append(reservationToBeChecked.getCarId()).append(" does not exist");
            }
        } catch (RepositoryException e) {
            errors.append("Car with ID ").append(reservationToBeChecked.getCarId()).append(" does not exist");
        }
        if (reservationToBeChecked.getCustomerName() == null || reservationToBeChecked.getCustomerName().trim().isEmpty()) {
            errors.append("Customer name cannot be empty");
        }
        if (reservationToBeChecked.getStartDate() == null) {
            errors.append("Start date cannot be null");
        }
        if (reservationToBeChecked.getEndDate() == null) {
            errors.append("End date cannot be null");
        }
        if (reservationToBeChecked.getEndDate().isBefore(reservationToBeChecked.getStartDate())) {
            errors.append("End date cannot be before start date");
        }
        if (reservationToBeChecked.getStartDate().isBefore(LocalDate.now())) {
            errors.append("Start date cannot be in the past");
        }
        if(errors.length() > 0)
            {
            throw new ValidationException(errors.toString().trim());
            }
    }
}
