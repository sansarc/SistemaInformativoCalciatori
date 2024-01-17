package Entity;
import java.util.Date;
public class Award {
    private String awardName;
    private Date winDate;
    private int idPlayer, idAward,idTeam;
    public void setName(String name) {
        awardName = name;
    }
    public String getName() {
        return awardName;
    }
    public void setWinDate(Date dt) {
        winDate = dt;
    }
    public Date getWinDate() {
        return winDate;
    }
    public void setId(int id) {
        idAward = id;
    }
    public int getId() {
        return idAward;
    }
    public void setIdPlayer(int id) {
        idPlayer = id;
    }
    public int getIdPlayer() {
        return idPlayer;
    }
    public void setIdTeam(int id) {
        idTeam = id;
    }
    public int getIdTeam() {
        return idTeam;
    }
    public Award() {
        idTeam = -1;
        idPlayer = -1;
        idAward = -1;
    }
    public Award(String name, Date dt, int id) {
        awardName = name;
        winDate = dt;
        idAward = id;
    }
}
