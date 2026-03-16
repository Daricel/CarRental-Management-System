package validator;

import domain.Car;
import repo.ValidationException;

public class CarValidator implements IValidator<Car> {
    private static final  int THE_MINIMUM_PRICE_THE_CAR_CAN_HAVE = 0;
    private static final int THE_MINIMUM_ID_THE_CAR_CAN_HAVE = 0;
    @Override
    public void validate(Car carToBeChecked) throws ValidationException {

        StringBuilder errors = new StringBuilder();

        if(carToBeChecked.getId() == null || carToBeChecked.getId() <= THE_MINIMUM_ID_THE_CAR_CAN_HAVE){
           errors.append("Car's id should be a positive integer!");
        }
        if(carToBeChecked.getMake() == null || carToBeChecked.getMake().trim().isEmpty()) {
           errors.append("Car's make cannot be empty!");
        }
        if (carToBeChecked.getModel() == null || carToBeChecked.getModel().trim().isEmpty()) {
            errors.append("Car model cannot be empty!");
        }

        if ( carToBeChecked.getRentalPrice() <= THE_MINIMUM_PRICE_THE_CAR_CAN_HAVE ) {

            errors.append("Rental price must be greater than 0!");
        }
        if(errors.length() > 0){
            throw new ValidationException(errors.toString().trim());
        }
    }
}
