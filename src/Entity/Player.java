package Entity;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.util.Base64;
import java.util.Date;

public class Player {
    private String name, lastName, position;
    private Date dateOfBirth, retirementDate;
    private char foot;
    private int goals, goalsConceded;
    private byte[] image;

    public Player(String _name, String _lastName, String _position, Date _dateOfBirth, Date _retirementDate, char _foot, int _goals, int _goalsConceded)
    {
        name = _name;
        lastName = _lastName;
        position = _position;
        dateOfBirth = _dateOfBirth;
        retirementDate = _retirementDate;
        foot = _foot;
        goals = _goals;
        goalsConceded = _goalsConceded;
    }
    public Player() {}
    public void setName(String s) {
        name = s;}
    public void setLastName(String s) {
        lastName = s;}
    public void setDateOfBirth(Date s) {
        dateOfBirth = s;}
    public void setRetirementDate(Date s) {
        retirementDate = s;}
    public void setFoot(char c) {
        foot = c;}
    public void setGoals(int n) {
        goals = n;}
    public void setGoalsConceded(int n) {
        goalsConceded = n;}
    public void setPosition(String s) {
        position = s;}
    public void setImage (byte[] b) {image = b;}

    public String getPosition() {return position;}
    public String getName() {return name;}
    public String getLastName() {return lastName;}
    public Date getDateOfBirth() {return dateOfBirth;}
    public Date getRetirementDate() {return retirementDate;}
    public char getFoot() {return foot;}
    public String getFootString() {
        if (foot == 'L') return "left";
        else if (foot == 'R') return "right";
        return "ambidextrous";
    }
    public int getGoals() {return goals;}
    public int getGoalsConceded() {return goalsConceded;}
    public byte[] getImage() {return Base64.getMimeDecoder().decode(image);}
    public int getAge(boolean from_db) {
        var actualDate = LocalDate.now();
        int birthMonth = 0;
        int dayOfBirth = 0;
        int year_difference = 0;
        if(from_db)
        {
            birthMonth = Integer.parseInt(getDateOfBirth().toString().substring(5, 7));
            dayOfBirth = Integer.parseInt(getDateOfBirth().toString().substring(8, 10));
            year_difference = ( Year.now().getValue() - Integer.parseInt(getDateOfBirth().toString().substring(0, 4)) );
        }
        else
        {
            String BirthDate_s = getDateOfBirth().toString();
            birthMonth = toMonthNumber(BirthDate_s.substring(4, 7));
            dayOfBirth = Integer.parseInt(BirthDate_s.substring(8, 10));
            year_difference = ( Year.now().getValue() - Integer.parseInt(BirthDate_s.substring(getDateOfBirth().toString().length() - 4, BirthDate_s.length())) );
        }

        if(actualDate.getMonth().getValue() == birthMonth)
        {
            if(actualDate.getDayOfMonth() >= dayOfBirth)
            {
                return  year_difference;
            }
            else
            {
                return  year_difference - 1;
            }
        }
        else if(actualDate.getMonth().getValue() > birthMonth)
        {
            return  year_difference;
        }
        else
        {
            return  year_difference - 1;
        }
    }
    private int toMonthNumber(String monthString)
    {
        int monthNum = 0;
        switch (monthString)
        {
            case "Jan":
                monthNum = 1;
                break;
            case "Feb":
                monthNum = 2;
                break;
            case "Mar":
                monthNum = 3;
                break;
            case "Apr":
                monthNum = 4;
                break;
            case "May":
                monthNum = 5;
                break;
            case "Jun":
                monthNum = 6;
                break;
            case "Jul":
                monthNum = 7;
                break;
            case "Aug":
                monthNum = 8;
                break;
            case "Sep":
                monthNum = 9;
                break;
            case "Oct":
                monthNum = 10;
                break;
            case "Nov":
                monthNum = 11;
                break;
            case "Dec":
                monthNum = 12;
                break;
            default:
                monthNum = 0;
                break;
        }
        return monthNum;
    }
}
