package undo;

import java.util.Stack;

public class UndoRedoManager<ID, T> {
    private Stack<IAction<ID, T>> undoStack;
    private Stack<IAction<ID, T>> redoStack;

    public UndoRedoManager() {
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    public void executeAction(IAction<ID, T> action) {
        undoStack.push(action);
        redoStack.clear();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            IAction<ID, T> action = undoStack.pop();
            action.executeUndo();
            redoStack.push(action);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            IAction<ID, T> action = redoStack.pop();
            action.executeRedo();
            undoStack.push(action);
        }
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
}