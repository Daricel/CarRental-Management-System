package repo;

import domain.IIdentifiable;

public interface IRepository<ID,T extends IIdentifiable<ID>>  {
    void add(ID id, T entity) throws RepositoryException;
    void delete(ID id) throws RepositoryException;
    void modify(ID id, T entity) throws RepositoryException;
    T findById(ID id) throws RepositoryException;
    Iterable<T> getAll();
}
