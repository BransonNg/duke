package exception;

public class TaskListException extends DukeException {
    public TaskListException(String acceptedFormat) {
        super(String.format("Dear user, your list is empty and thus we cannot perform this operation", acceptedFormat));
    }
}
