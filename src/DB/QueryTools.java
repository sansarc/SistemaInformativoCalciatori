package DB;
import java.util.List;

public class QueryTools {
    public static String getQuery(String name, String lastname, char ageMath, String age, List<String> positions, char foot, boolean isRetired, String team) {
        StringBuilder query = new StringBuilder("SELECT nome_calciatore, cognome, nome_squadra, calciatore.idcalciatore FROM calciatore LEFT JOIN calciatoresquadra ON calciatore.idcalciatore = calciatoresquadra.idcalciatore LEFT JOIN squadra ON calciatoresquadra.idsquadra = squadra.idsquadra WHERE");
        boolean conditionAND = false;

        if (!name.isBlank()) {
            query.append(" LOWER(nome_calciatore) LIKE LOWER(?)");
            conditionAND = true;
        }
        if (!lastname.isBlank()) {
            if (conditionAND) query.append(" AND");
            query.append(" LOWER(cognome) LIKE LOWER(?)");
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
            query.append(" EXTRACT(YEAR FROM CURRENT_DATE) - EXTRACT(YEAR FROM datanascita)").append(ageMath).append("?");
            conditionAND = true;
        }
        if (foot != '\0') {
            if (conditionAND) query.append(" AND");
            query.append(" piede = ?");
            conditionAND = true;
        }
        if (!team.isBlank()) {
            if (conditionAND) query.append(" AND");
            query.append(" LOWER(squadra.nome_squadra) LIKE LOWER(?)");
        }
        if (isRetired) {
            if (conditionAND) query.append(" AND");
            query.append(" dataritiro IS NOT NULL AND calciatoresquadra.dataFine IS NOT NULL");
        } else {
            query.append(" AND dataritiro IS NULL AND calciatoresquadra.dataFine IS NULL");
        }

        query.append(" ORDER BY cognome;");
        return query.toString();
    }

    public static String updateColumnName(String columnName) {
        return switch (columnName) {
            case "idcalciatore" -> "";
            case "nome_calciatore" -> "Name";
            case "cognome" -> "Last Name";
            case "nome_squadra" -> "Team";
            default -> columnName;
        };
    }
}
