package DB;
import javax.swing.*;
import java.util.List;
import java.util.Date;

public class QueryTools {
    public static String getQuery(String name, String lastname, char ageMath, String age, List<String> positions, char foot, boolean isRetired, String team) {
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
    }

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
