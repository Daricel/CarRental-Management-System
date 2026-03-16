package undo;

import domain.IIdentifiable;
import repo.IRepository;

public class ActionRemove<ID, T extends IIdentifiable<ID>> implements IAction<ID, T> {

    private IRepository<ID, T> repository;
    private T removedEntity;

    public ActionRemove(IRepository<ID, T> repository, T removedEntity) {
        this.repository = repository;
        this.removedEntity = removedEntity;
    }

    @Override
    public void executeUndo() {
        if (removedEntity == null) {
            throw new RuntimeException("The entity to restore cannot be null.");
        }
        repository.add(removedEntity.getId(), removedEntity);
    }

    @Override
    public void executeRedo() {
        if (removedEntity == null) {
            throw new RuntimeException("The entity to remove cannot be null.");
        }
        repository.delete(removedEntity.getId());
    }
}