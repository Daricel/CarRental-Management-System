package undo;

import domain.IIdentifiable;
import repo.IRepository;

public class ActionUpdate<ID, T extends IIdentifiable<ID>> implements IAction<ID, T> {
    private IRepository<ID, T> repository;
    private T oldEntity;
    private T newEntity;

    public ActionUpdate(IRepository<ID, T> repository, T oldEntity, T newEntity) {
        this.repository = repository;
        this.oldEntity = oldEntity;
        this.newEntity = newEntity;
    }

    @Override
    public void executeUndo() {
        if (oldEntity == null) {
            throw new RuntimeException("The entity to restore cannot be null.");
        }
        repository.modify(oldEntity.getId(), oldEntity);
    }

    @Override
    public void executeRedo() {
        if (newEntity == null) {
            throw new RuntimeException("The entity to update to cannot be null.");
        }
        repository.modify(newEntity.getId(), newEntity);
    }
}