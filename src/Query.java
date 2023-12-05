import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Query {
    private final JTable resultsTable;
    public Query(JTable resultsTable) {
        this.resultsTable = resultsTable;
    }

    public void queryTable(String name, String lastname, char ageMath, String age, String position, char foot, boolean isRetired, String team) {
        String query = getQuery(name, lastname, ageMath, age, position, foot, isRetired, team);
        Connection connection = DBconnection.connect();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int index = 1;
            if (!name.isBlank()) statement.setString(index++, "%"+name+"%");                // parsing dei valori inseriti nella query
            if (!lastname.isBlank()) statement.setString(index++, "%"+lastname+"%");
            if (!position.isBlank()) statement.setString(index++, position);
            if (!age.isBlank()) statement.setInt(index++, Integer.parseInt(age));
            if (Character.isLetter(foot)) statement.setString(index++, Character.toString(foot));
            if (!team.isBlank()) statement.setString(index, "%"+team+"%");

            System.out.println(statement);
            ResultSet rs = statement.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();       // dati del risultato della query

            DefaultTableModel tableModel = new DefaultTableModel();     // tabella di output
            int cols = metaData.getColumnCount();
            for (int i = 1; i <= cols; i++) {              // per ogni colonna, eventualmente modifica il suo nome con la funzione mapToCustomColumnName ad aggiungila alla tabella di output
                String colName = metaData.getColumnName(i);
                String tableColName = updateColName(colName);
                tableModel.addColumn(tableColName);
            }

            while (rs.next()) {                      // inserisci i dati nella tabella
                Object[] rowData = new Object[cols];
                for (int i = 1; i <= cols; i++) {
                    String columnName = metaData.getColumnName(i);
                    rowData[i-1] = rs.getObject(columnName);
                }
                tableModel.addRow(rowData);
            }

            resultsTable.setModel(tableModel);
        }

        catch (SQLException se) {
            se.printStackTrace();
        }
        finally {
            DBconnection.disconnect(connection);
        }
    }

    private String getQuery(String name, String lastname, char ageMath, String age, String position, char foot, boolean isRetired, String team) {          // costruzione della query in base ai valori inseriti
        StringBuilder query = new StringBuilder("SELECT calciatore.*, squadra.nome_squadra FROM calciatore LEFT JOIN calciatoresquadra ON calciatore.idcalciatore = calciatoresquadra.idcalciatore LEFT JOIN squadra ON calciatoresquadra.idsquadra = squadra.idsquadra WHERE ");
        boolean conditionAND = false;         // condizione per verificare se la query deve aggiornare altri valori e aggiungere AND, oppure se la query è terminata

        if (!name.isBlank()) {
            query.append(" LOWER(nome_calciatore) LIKE LOWER(?)");
            conditionAND = true;
        }
        if (!lastname.isBlank()) {
            if (conditionAND) query.append(" AND");
            query.append(" LOWER(cognome) LIKE LOWER(?)");
            conditionAND = true;
        }
        if (!position.isBlank()) {
            if (conditionAND) query.append(" AND");
            query.append(" ruoli LIKE ?");
            conditionAND = true;
        }
        if (!age.isBlank()) {
            if (conditionAND) query.append(" AND");
            query.append(" EXTRACT(YEAR FROM CURRENT_DATE) - EXTRACT(YEAR FROM datanascita)" + ageMath + "?");           // verifica se anno attuale - anno della data di nascita = l'età inserita nel form
            conditionAND = true;
        }
        if (foot != '\0') {
            if (conditionAND) query.append(" AND");
            query.append(" piede = ?");
            conditionAND = true;
        }
        if (isRetired) {
            if (conditionAND) query.append(" AND");
            query.append(" dataritiro IS NOT NULL");
            conditionAND = true;
        } else {
            if (conditionAND) query.append(" AND");
            query.append(" dataritiro IS NULL");
            conditionAND = true;
        }
        if (!team.isBlank()) {
            if (conditionAND) query.append(" AND");
            query.append(" LOWER(squadra.nome_squadra) LIKE LOWER(?)");
        }

        query.append(";");
        return query.toString();
    }

    private String updateColName(String columnName) {           // modifica dei nomi delle colonne della tabella di output (altrimenti nome della colonna del database)
        switch (columnName) {
            case "idcalciatore": return "ID";
            case "nome_calciatore": return "Nome";
            case "cognome": return "Cognome";
            case "ruoli": return "Ruoli";
            case "datanascita": return "Nato";
            case "piede": return "Piede";
            case "dataritiro": return "Ritirato";
            case "golfatti": return "Gol Segnati";
            case "golsubiti": return "Gol Subiti";
            case "nome_squadra": return "Squadra";
            default: return columnName;
        }
    }
}
