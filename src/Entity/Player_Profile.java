package Entity;

import java.util.Date;

public class Player_Profile extends Player {
    private int goals, goalsConceded;

    public Player_Profile(String _name, String _lastName, String _position, Date _dateOfBirth, Date _retirementDate, char _foot, int _goals, int _goalsConceded)
    {
        name = _name;
        lastName = _lastName;
        position = _position;
        birthDate = _dateOfBirth;
        retirementDate = _retirementDate;
        foot = _foot;
        goals = _goals;
        goalsConceded = _goalsConceded;
    }
    public Player_Profile() {}
    public void setGoals(int n) {
        goals = n;}
    public void setGoalsConceded(int n) {
        goalsConceded = n;}
    public int getGoals() {return goals;}
    public int getGoalsConceded() {return goalsConceded;}

}
