package Entity;

public class Feature {
    String name, description;
    char type;

    public void setName(String s) {name = s;}
    public void setDescription(String s) {description = s;}
    public void setType(char c) {type = c;}
    
    public String getName() {
        return name;
    }
    public String getDescription() {return description;}
    public char getType() {return type;}
}