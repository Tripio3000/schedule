package edu.iate.ism22.schedule.exception;

public class EmptyScheduleContainer extends RuntimeException {
    public EmptyScheduleContainer(String message) {
        super(message);
    }
}
