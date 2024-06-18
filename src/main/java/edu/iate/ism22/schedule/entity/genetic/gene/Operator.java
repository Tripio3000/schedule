package edu.iate.ism22.schedule.entity.genetic.gene;

public class Operator implements User {
    
    private String login;
    private ScheduleContainer scheduleContainer;
    
    public Operator(String login, ScheduleContainer scheduleContainer) {
        this.login = login;
        this.scheduleContainer = scheduleContainer;
    }
    
    @Override
    public String getLogin() {
        return login;
    }
    
    @Override
    public ScheduleContainer getScheduleContainer() {
        return scheduleContainer;
    }
}
