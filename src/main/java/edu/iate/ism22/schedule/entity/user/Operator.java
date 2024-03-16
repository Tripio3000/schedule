package edu.iate.ism22.schedule.entity.user;

public class Operator implements User {
    
    private String login;
    private Scheme scheme;
    
    public Operator(String login, Scheme scheme) {
        this.login = login;
        this.scheme = scheme;
    }
    
    @Override
    public String getLogin() {
        return login;
    }
    
    @Override
    public Scheme getScheme() {
        return scheme;
    }
}
