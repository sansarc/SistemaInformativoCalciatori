import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Query {
    private JTable resultsTable;
    public Query(JTable resultsTable) {
        this.resultsTable = resultsTable;
    }

    public void getSearch(String name, String lastname, String age, String position, char foot, boolean isRetired) {
        String query = getQuery(name, lastname, age, position, foot, isRetired);
        Connection connection = DBconnection.connect();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int index = 1;

            if (!name.isBlank()) statement.setString(index++, name);                // parsing dei valori inseriti nella query
            if (!lastname.isBlank()) statement.setString(index++, lastname);
            if (!position.isBlank()) statement.setString(index++, position);
            if (!age.isBlank()) statement.setString(index++, age);
            statement.setString(index, String.valueOf(foot));
            System.out.println(statement);
            ResultSet resultSet = statement.executeQuery();

            DefaultTableModel tableModel = new DefaultTableModel();     // tabella di output
            ResultSetMetaData metaData = resultSet.getMetaData();       // dati del risultato della query

            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {              // per ogni colonna, eventualmente modifica il suo nome con la funzione mapToCustomColumnName ad aggiungila alla tabella di output
                String columnName = metaData.getColumnName(i);
                String customColumnName = mapToCustomColumnName(columnName);
                tableModel.addColumn(customColumnName);
            }

            while (resultSet.next()) {                      // inserisci i dati nella tabella
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    rowData[i - 1] = resultSet.getObject(columnName);
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

    private String getQuery(String name, String lastname, String age, String position, char foot, boolean isRetired) {          // costruzione della query in base ai valori inseriti
        StringBuilder query = new StringBuilder("SELECT * FROM calciatore WHERE");
        boolean conditionAdded = false;         // condizione per verificare se la query deve aggiornare altri valori e aggiungere AND, oppure se la query è terminata

        if (!name.isBlank()) {
            query.append(" nome = ?");
            conditionAdded = true;
        }
        if (!lastname.isBlank()) {
            if (conditionAdded) query.append(" AND");
            query.append(" cognome = ?");
            conditionAdded = true;
        }
        if (!position.isBlank()) {
            if (conditionAdded) query.append(" AND");
            query.append(" ruoli = ?");
            conditionAdded = true;
        }
        if (!age.isBlank()) {
            if (conditionAdded) query.append(" AND");
            query.append(" YEAR(CURDATE()) - YEAR(datanascita) = ?");           // verifica se anno attuale - anno della data di nascita = l'età inserita nel form
            conditionAdded = true;
        }
        if (foot != '\0') {
            if (conditionAdded) query.append(" AND");
            query.append(" piede = ?");
            conditionAdded = true;
        }
        if (isRetired) {
            if (conditionAdded) query.append(" AND");
            query.append(" dataritiro IS NOT NULL");
        }

        query.append(";");
        return query.toString();
    }

    private String mapToCustomColumnName(String columnName) {           // modifica dei nomi delle colonne della tabella di output (altrimenti nome della colonna del database)
        switch (columnName) {
            case "nome":
                return "Nome";
            case "cognome":
                return "Cognome";
            case "ruoli":
                return "Ruoli";
            case "datanascita":
                return "Nato";
            case "piede":
                return "Piede preferito";
            case "dataritiro":
                return "Ritirato";
            case "golfatti":
                return "Gol Segnati";
            case "golsubiti":
                return "Gol Subiti";
            default:
                return columnName;
        }
    }
}
