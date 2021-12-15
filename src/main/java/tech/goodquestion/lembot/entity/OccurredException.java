package tech.goodquestion.lembot.entity;

public class OccurredException {

    public String occurredIn;
    public String exceptionType;
    public String details;

    public static OccurredException getOccurredExceptionData(Exception exception, String className) {

        OccurredException occurredException = new OccurredException();

        occurredException.occurredIn = className;
        occurredException.exceptionType = exception.getClass().getSimpleName();
        occurredException.details = exception.getMessage();

        return occurredException;

    }

}
