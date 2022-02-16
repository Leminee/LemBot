package tech.goodquestion.lembot.entity;

public final class OccurredException {

    public String occurredIn;
    public String type;
    public String details;

    public static OccurredException getOccurredExceptionData(final Exception exception, final String className) {

        OccurredException occurredException = new OccurredException();

        occurredException.occurredIn = className;
        occurredException.type = exception.getClass().getSimpleName();
        occurredException.details = exception.getMessage();

        return occurredException;

    }
}
