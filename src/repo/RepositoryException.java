package repo;

public class RepositoryException extends RuntimeException {
    String messageOfTheException;

    RepositoryException(String messageOfRepositoryException) {
        this.messageOfTheException = messageOfRepositoryException;
    }

    @Override
    public String getMessage() {
        return this.messageOfTheException;
    }
}
