package undo;

import domain.IIdentifiable;
import repo.IRepository;

public class ActionAdd<ID, T extends IIdentifiable<ID>> implements IAction<ID, T> {

    private IRepository<ID, T> repository;
    private T addedEntity;

    public ActionAdd(IRepository<ID, T> repository, T addedEntity) {
        this.repository = repository;
        this.addedEntity = addedEntity;
    }

    @Override
    public void executeUndo() {
        if (addedEntity == null) {
            throw new RuntimeException("The entity to undo cannot be null.");
        }
        repository.delete(addedEntity.getId());
    }

    @Override
    public void executeRedo() {
        if (addedEntity == null) {
            throw new RuntimeException("The entity to redo cannot be null.");
        }
        repository.add(addedEntity.getId(), addedEntity);
    }
}