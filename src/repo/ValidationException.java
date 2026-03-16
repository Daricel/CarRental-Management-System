package repo;


public class ValidationException extends RuntimeException {
    public ValidationException(String messageToBeShown ) {
        super(messageToBeShown );
    }
}