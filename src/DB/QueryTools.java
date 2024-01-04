package DB;
import java.util.List;

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
                query.append(" ruoli LIKE ?");
                conditionAND = true;
            }
        }
        if (!age.isBlank()) {
            if (conditionAND) query.append(" AND");
            query.append(" DATEDIFF(CURDATE(), borndate) ").append(ageMath).append("?");
            conditionAND = true;
        }
        if (foot != '\0') {
            if (conditionAND) query.append(" AND");
            query.append(" piede = ?");
            conditionAND = true;
        }
        if (!team.isBlank()) {
            if (conditionAND) query.append(" AND");
            query.append(" LOWER(teams.team_name) LIKE LOWER(?)");
        }
        if (isRetired) {
            if (conditionAND) query.append(" AND");
            query.append(" dataritiro IS NOT NULL AND player_team.EndDate IS NOT NULL");
        } else {
            query.append(" AND dataritiro IS NULL AND player_team.EndDate IS NULL");
        }

        query.append(" ORDER BY lastname;");
        return query.toString();
    }

    public static String updateColumnName(String columnName) {
        return switch (columnName) {
            case "idcalciatore" -> "";
            case "player_name" -> "Name";
            case "lastname" -> "Last Name";
            case "team_name" -> "Team";
            default -> columnName;
        };
    }
}
