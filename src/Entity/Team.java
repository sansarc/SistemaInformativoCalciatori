package Entity;

public class Team {
    private int idTeam;
    private String name, nationality;
    private int category;
    public void setName(String s) {name = s;}
    public void setNationality(String s) {nationality = s;}
    public void setCategory(int n) {category = n;}

    public String getName() {return name;}
    public String getNationality() {return nationality;}
    public int getId()  { return idTeam; }
    public int getCategory() {return category;}
    public Team() {}
    public Team(String _name, String _nation, int _level, int _idTeam) {
        name = _name;
        nationality = _nation;
        category = _level;
        idTeam = _idTeam;
    }
}
