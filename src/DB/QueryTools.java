package DB;
import Entity.Player;
import Entity.Player_Profile;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Date;

public class QueryTools {
    public static String getQuerySearchPlayer(Player_Profile playerRequest, Entity.Team actualTeam, boolean freeagent, boolean retired,
                                                List<String> positions, char goals_c, char conceded_c, char apparences_c, char age_c, String feature, int age, PreparedStatement statement) {

        String query = "SELECT DISTINCT p.player_name, p.lastname";
        try {
        String whereString = "";
        boolean conditional_and = false;
        boolean forQuery = statement == null;
        int index = 0;
        if(!playerRequest.getName().isBlank()) {
            if(forQuery) {
                whereString = "LOWER(P.PLAYER_NAME) LIKE LOWER(?) ";
                conditional_and = true;
            }
            else {
                statement.setString(++index, "%" + playerRequest.getName() + "%");
            }
        }
        if(!playerRequest.getLastName().isBlank()) {
            if(forQuery) {
                if(conditional_and) whereString += "AND ";
                whereString += "LOWER(P.LASTNAME) LIKE LOWER(?) ";
                conditional_and = true;
            }
            else {
                statement.setString(++index,"%" + playerRequest.getLastName() +"%");
            }
        }
        if(freeagent || retired) {
            if (conditional_and) whereString += "AND ";
            whereString += "NOT EXISTS (SELECT * FROM PLAYER_CARREER PC2 WHERE PC2.IDPLAYER = P.IDPLAYER AND PC2.ENDDATE IS NULL) ";
            whereString += (retired) ? "AND P.RETIREMENTDATE IS NOT NULL " : "AND P.RETIREMENTDATE IS NULL ";
            conditional_and = true;
        }
        else {
            query += ", pc.team_name";
            if(conditional_and) whereString += "AND ";
            whereString += "PC.ENDDATE IS NULL ";
            if(actualTeam != null) {
                if(!actualTeam.getName().isBlank()) {
                    if(forQuery) {
                        whereString += "AND PC.TEAM_NAME = ? AND T.NATION = ? ";
                    }
                    else {
                        statement.setString(++index, actualTeam.getName());
                        statement.setString(++index, actualTeam.getNationality());
                    }
                }
                else if(actualTeam.getCategory() > 0 ) {
                    if(forQuery) {
                        whereString += "AND T.NATION = ? AND T.LEVEL = ? ";
                    }
                    else {
                        statement.setString(++index, actualTeam.getNationality());
                        statement.setInt(++index, actualTeam.getCategory());
                    }
                }
                else {
                    if(forQuery) {
                        whereString += "AND T.NATION = ? ";
                    }
                    else {
                        statement.setString(++index, actualTeam.getNationality());
                    }
                }
            }
            conditional_and = true;
        }
        if (!positions.isEmpty()) {
            for (int i = 0; i < positions.size(); i++) {
                if(forQuery) {
                    if(conditional_and) whereString += "AND ";
                    whereString += "P.positions LIKE ? ";
                    conditional_and = true;
                }
                else {
                    statement.setString(++index,  "%" + positions.get(i).toString() + "%");
                }
            }
        }
        if(goals_c != '\0') {
            if(forQuery) {
                if(conditional_and)  whereString += "AND ";
                whereString += "P.GOALSSCORED " + goals_c + " ? ";
                conditional_and = true;
            }
            else {
                statement.setInt(++index, playerRequest.getGoals());
            }
        }
        if(conceded_c != '\0') {
            if(forQuery) {
                if(conditional_and) whereString += "AND ";
                whereString += "P.GOALSCONCEDED " + conceded_c + " ? ";
                conditional_and = true;
            }
            else {
                statement.setInt(++index, playerRequest.getGoals());
            }
        }
        if(apparences_c != '\0') {
            if(forQuery) {
                if(conditional_and) whereString += "AND ";
                whereString += "P.APPARENCES " + apparences_c + " ? ";
                conditional_and = true;
            }
            else {
                statement.setInt(++index, playerRequest.getApparences());
            }
        }
        if(age_c != '\0') {
            if(forQuery) {
                if (conditional_and) whereString += "AND ";
                whereString += "EXTRACT(YEAR FROM AGE(CURRENT_DATE, birthdate)) " + age_c + " ? ";
                conditional_and = true;
            }
            else {
                statement.setInt(++index, age);
            }
        }
        if(playerRequest.getFoot() != '\0') {
            if(forQuery) {
                if(conditional_and) whereString += "AND ";
                whereString += "P.FOOT = ? ";
                conditional_and = true;
            }
            else {
                statement.setString(++index, String.valueOf(playerRequest.getFoot()));
            }
        }
        if(!feature.isBlank()) {
            if(forQuery) {
                if(conditional_and) whereString += "AND ";
                whereString += "F.IDFEATURE = ?";
            }
            else {
                statement.setString(++index, feature);
            }
        }
        query += ", p.idPlayer FROM  PLAYER_PROFILE P LEFT JOIN PLAYER_CARREER PC ON PC.IDPLAYER = P.IDPLAYER LEFT JOIN TEAMS T ON T.IDTEAM = PC.IDTEAM LEFT JOIN PLAYER_FEATURE F ON F.IDPLAYER = P.IDPLAYER WHERE " + whereString;

        //select pc.idplayer, pc.player_name, pc.lastname from player_carreer pc join teams t on t.idteam = pc.idteam join player_profile pp on pc.idplayer = pp.idplayer join player_feature f on f.idplayer = pc.idplayer where pc.player_name LIKE 'Vittorio' AND pc.lastname LIKE 'Osimenno' and t.team_name = 'SSC NAPOLI' and t.nation = 'IT' and pc.enddate is null AND pp.goalsscored > 80 and f.idfeature = 'head shot'

        }catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            return query;
        }
    }


    /*public static String getQuery(String name, String lastname, char ageMath, String age, List<String> positions, char foot, boolean isRetired, String team) {
        StringBuilder query = new StringBuilder("SELECT player_name, lastname, team_name, players.idPlayer FROM players LEFT JOIN player_team ON players.idplayer = player_team.idPlayer LEFT JOIN teams ON player_team.idTeam = teams.idTeam WHERE");
        boolean conditionAND = false;

        if (!name.isBlank()) {
            query.append(" LOWER(player_name) LIKE LOWER(?)");
            conditionAND = true;
        }
        if (!lastname.isBlank()) {
            if (conditionAND) query.append(" AND");
            query.append(" LOWER(lastname) LIKE LOWER(?)");
            conditionAND = true;
        }
        if (!positions.isEmpty()) {
            for (int i = 0; i < positions.size(); i++) {
                if (conditionAND) query.append(" AND");
                query.append(" positions LIKE ?");
                conditionAND = true;
            }
        }
        if (!age.isBlank()) {
            if (conditionAND) query.append(" AND");
            query.append(" EXTRACT(YEAR FROM AGE(CURRENT_DATE, birthdate)) ").append(ageMath).append("?");
            conditionAND = true;
        }
        if (foot != '\0') {
            if (conditionAND) query.append(" AND");
            query.append(" foot = ?");
            conditionAND = true;
        }
        if (!team.isBlank()) {
            if (conditionAND) query.append(" AND");
            query.append(" LOWER(teams.team_name) LIKE LOWER(?)");
        }
        if (isRetired) {
            if (conditionAND) query.append(" AND");
            query.append(" retirementDate  IS NOT NULL AND player_team.EndDate IS NOT NULL");
        } else {
            query.append(" AND retirementDate  IS NULL AND player_team.EndDate IS NULL");
        }

        query.append(" ORDER BY lastname;");
        return query.toString();
    }*/

    public static String updateColumnName(String columnName) {
        return switch (columnName) {
            case "idplayer" -> "";
            case "player_name" -> "Name";
            case "lastname" -> "Last Name";
            case "team_name" -> "Team";
            case "startdate" -> "Start Date";
            case "enddate" -> "End Date";
            case "idteam" -> "";
            case "goalsscored" -> "Goals Scored";
            case "goalsconceded" -> "Goals Conceded";
            case "apparences" -> "Apparences";
            case "name" -> "Name";
            case "windate" -> "Date";
            default -> columnName;
        };
    }
    public static void selectNationTool(JComboBox selectNationBox, JComboBox selectLevelBox, List<String> levels) {
        Query query = new Query();
        try {
            if(selectNationBox.getSelectedIndex() != 0) {
                if(selectLevelBox.getItemCount() > 0)
                    selectLevelBox.removeAllItems();
                levels.clear();
                levels.addAll(query.SelectLevelsFromNation(selectNationBox.getSelectedItem().toString()));
                for (var l : levels) {
                    selectLevelBox.addItem(l);
                }
                selectLevelBox.setSelectedIndex(0);
                selectLevelBox.setEnabled(true);
            }
            else {
                selectLevelBox.setEnabled(false);
                selectLevelBox.setSelectedIndex(0);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void selectLevelTool(JComboBox selectNationBox, JComboBox selectLevelBox, JComboBox selectTeamBox, List<Entity.Team> teams) {
        Query query = new Query();
        try {
            if (selectLevelBox.getSelectedIndex() != 0) {
                if (selectTeamBox.getItemCount() > 0)
                    selectTeamBox.removeAllItems();
                teams.clear();
                teams.addAll(query.TeamsFromNationAndLevel(selectNationBox.getSelectedItem().toString(), Integer.parseInt(selectLevelBox.getSelectedItem().toString())));
                for (var t : teams) {
                    selectTeamBox.addItem(t.getName());
                }
                selectTeamBox.setEnabled(true);
            } else {
                selectTeamBox.setEnabled(false);
                selectTeamBox.setSelectedIndex(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static boolean selectTeamTool(JComboBox selectTeamBox) {
        try {
            if(selectTeamBox.getSelectedIndex() != 0) {
                return true;
            }
            else {
                return false;
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    public static boolean selectTeamTool(JComboBox selectTeamBox, JComboBox selectPlayerBox, List<Entity.Player> players, int idTeam, Date dt) {
        Query query = new Query();
        try {
            if (selectTeamBox.getSelectedIndex() != 0) {
                if (selectPlayerBox.getItemCount() > 0)
                    selectPlayerBox.removeAllItems();
                players.clear();
                players.addAll(query.player_from_team(idTeam, dt));
                boolean first = true;
                for (var p : players) {
                    if(first) {
                        selectPlayerBox.addItem("");
                        first = false;
                    }
                    else
                        selectPlayerBox.addItem(p.getName() + " " + p.getLastName() + " $" + p.getId());
                }
                return true;
            } else {
                selectPlayerBox.setSelectedIndex(0);
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


}
