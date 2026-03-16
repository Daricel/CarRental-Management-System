package validator;

import repo.ValidationException;

public interface IValidator<T> {
    void validate(T entityToValidate) throws ValidationException;
}
