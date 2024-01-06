package Entity;

public class User {
    private String email;
    private char type;
    public String getEmail()
    {
        return email;
    }
    public char getType() {return type;}
    public void setEmail(String e)
    {
        email = e;
    }
    public void setType(char t) {type = t;}

}
