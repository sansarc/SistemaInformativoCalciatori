package Entity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlayerTransfer {
    private Player player;
    private final List<Team> teamList = new ArrayList<>();
    private final List<Date> startList = new ArrayList<>();
    private final List<Date> endList = new ArrayList<>();

    public void setPlayer(Player c) {player = c;}
    public void addTeam(Team s) {teamList.add(s);}
    public void addStart(Date d) {startList.add(d);}
    public void addEnd(Date d) {
        endList.add(d);}

    public Player getPlayer() {return player;}
    public List<Team> getSquadraList() {return teamList;}
    public Team getTeam(int index) {return teamList.get(index);}
    public List<Date> getStartList() {return startList;}
    public List<Date> getEndList() {return endList;}
    public Date getStart(int index) {return startList.get(index);}
    public Date getEnd(int index) {return endList.get(index);}
}
