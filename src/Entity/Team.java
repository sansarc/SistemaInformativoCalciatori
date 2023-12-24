package Entity;

public class Team {
    private String name, nationality;
    int category;

    public void setName(String s) {name = s;}
    public void setNationality(String s) {nationality = s;}
    public void setCategory(int n) {category = n;}

    public String getName() {return name;}
    public String getNationality() {return nationality;}
    public int getCategory() {return category;}
}
