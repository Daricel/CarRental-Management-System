package repo;

import domain.IIdentifiable;

import java.util.HashMap;
import java.util.Map;

public class MemoryRepository<ID, T extends IIdentifiable<ID>> implements IRepository<ID, T> {
    protected Map<ID, T> entitiesById = new HashMap<>();

    @Override
    public void add(ID id, T entity) throws RepositoryException {
        if (id == null) {
            throw new RepositoryException("The id cannot be null!");
        }
        if (entity == null) {
            throw new RepositoryException("The object cannot be null!");
        }
        if (!id.equals(entity.getId())) {
            throw new RepositoryException("The id is a mismatch!");
        }
        if (entitiesById.containsKey(id)) {
            throw new RepositoryException("An object with the same id already exists!");
        }
        entitiesById.put(id, entity);
    }

    @Override
    public void delete(ID id) throws RepositoryException {
        if (id == null) {
            throw new RepositoryException("The id cannot be null!");
        }
        if (!entitiesById.containsKey(id)) {
            throw new RepositoryException("An object with this id doesn't exist!");
        }
        entitiesById.remove(id);
    }

    @Override
    public void modify(ID id, T entity) throws RepositoryException {
        if (id == null) {
            throw new RepositoryException("The id cannot be null!");
        }
        if (entity == null) {
            throw new RepositoryException("The object cannot be null!");
        }
        if (!entitiesById.containsKey(id)) {
            throw new RepositoryException("An object with this id doesn't exist!");
        }
        if (!id.equals(entity.getId())) {
            throw new RepositoryException("The id is a mismatch!");
        }
        entitiesById.put(id, entity);
    }

    @Override
    public T findById(ID id) throws RepositoryException {
        if (id == null) {
            throw new RepositoryException("The id cannot be null!");
        }
        if (!entitiesById.containsKey(id)) {
            throw new RepositoryException("An object with this id doesn't exist!");
        }
        return entitiesById.get(id);
    }

    @Override
    public Iterable<T> getAll() {
        return entitiesById.values();
    }
}