import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Calciatore.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Query {
    JTable resultsTable;
    public Query(JTable resultsTable) {
        this.resultsTable = resultsTable;
    }
    public List<Calciatore> getCalciatoriByRuolo(String ruolo) {
        List<Calciatore> calciatori = new ArrayList<>();
        Connection connection = DBconnection.connect();
        String query = "SELECT nome, cognome FROM calciatore WHERE ruoli = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, ruolo);
            ResultSet resultSet = statement.executeQuery();

            DefaultTableModel tableModel = new DefaultTableModel();

            // Get metadata to set column names
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Add column names to the table model
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                tableModel.addColumn(metaData.getColumnName(columnIndex));
            }

            // Add data rows to the table model
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    rowData[columnIndex - 1] = resultSet.getObject(columnIndex);
                }
                tableModel.addRow(rowData);
            }

            resultsTable.setModel(tableModel);
        }

        catch (SQLException se) {
            se.printStackTrace();
        } finally {
            DBconnection.disconnect(connection);
        }

        return calciatori;
    }
}