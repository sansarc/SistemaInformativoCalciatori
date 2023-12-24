package Entity;
import java.time.Year;
import java.util.Base64;
import java.util.Date;

public class Player {
    private String name, lastName, position;
    private Date dateOfBirth, retirementDate;
    private char foot;
    private int goals, goalsConceded;
    private byte[] image;

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
    public int getAge() {
        return (Year.now().getValue() - Integer.parseInt(getDateOfBirth().toString().substring(0, 4)));
    }
}
