package exception;

public class DateTimeException extends UIException {
    public DateTimeException(String acceptedFormat) {
        super(String.format("Dear user, your date time is in the wrong format%n we only accept %s", acceptedFormat));
    }
}
