package DB;

import Entity.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Query {
    private JTable resultsTable;
    private Player player;
    private Team team;
    private PlayerTransfer playerTransfer;
    private PlayerFeature playerFeature;
    private Connection connection;

    public Query() {}
    public Query(JTable resultsTable) {this.resultsTable = resultsTable;}

    public List<Integer> queryPlayers(String name, String lastname, char ageMath, String age, List<String> positions, char foot, boolean isRetired, String team) {
        String query = QueryTools.getQuery(name, lastname, ageMath, age, positions, foot, isRetired, team);
        Connection connection = DBconnection.connect();
        List<Integer> ids = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int index = 1;
            if (!name.isBlank()) statement.setString(index++, "%" + name + "%");
            if (!lastname.isBlank()) statement.setString(index++, "%" + lastname + "%");
            if (!positions.isEmpty()) {
                for (String i : positions) statement.setString(index++, i);
            }
            if (!age.isBlank()) statement.setInt(index++, Integer.parseInt(age));
            if (Character.isLetter(foot)) statement.setString(index++, Character.toString(foot));
            if (!team.isBlank()) statement.setString(index, "%" + team + "%");

            System.out.println(statement);
            ResultSet rs = statement.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();

            DefaultTableModel tableModel = new DefaultTableModel();
            int cols = metaData.getColumnCount();
            for (int i = 1; i <= cols; i++) {
                String colName = metaData.getColumnName(i);
                String tableColName = QueryTools.updateColumnName(colName);
                tableModel.addColumn(tableColName);
            }

            String columnName;
            while (rs.next()) {
                Object[] rowData = new Object[cols];
                for (int i = 1; i <= cols; i++) {
                    columnName = metaData.getColumnName(i);
                    if (i == 2) rowData[i - 1] = "<html><b>" + rs.getObject(columnName) + "</b></html>";
                    else if (i == 3 && isRetired) rowData[i - 1] = "";
                    else rowData[i - 1] = rs.getObject(columnName);
                }
                columnName = metaData.getColumnName(cols);
                ids.add(rs.getInt(columnName));
                rowData[cols-1] = "<html><u>full profile</u></html>"; // Note: Use cols instead of cols - 1
                tableModel.addRow(rowData);
            }
            resultsTable.setModel(tableModel);

        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            DBconnection.disconnect(connection);
        }

        return ids;
    }

    public PlayerTransfer queryPlayerProfile(int id) {
        connection = DBconnection.connect();
        String query = "SELECT DISTINCT calciatore.*, squadra.nome_squadra, calciatoresquadra.dataInizio, calciatoresquadra.dataFine, STRING_AGG(DISTINCT feature.nome_feature, ', ') AS lista_feature FROM calciatore JOIN calciatoresquadra ON calciatore.idcalciatore = calciatoresquadra.idcalciatore JOIN squadra ON calciatoresquadra.idsquadra = squadra.idsquadra JOIN calciatorefeature ON calciatore.idcalciatore = calciatorefeature.idcalciatore JOIN feature ON feature.idfeature = calciatorefeature.idfeature WHERE calciatore.idcalciatore = ? GROUP BY calciatore.idcalciatore, squadra.nome_squadra, calciatoresquadra.dataInizio, calciatoresquadra.dataFine ORDER BY calciatoresquadra.datainizio;";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            System.out.println(statement);
            ResultSet rs = statement.executeQuery();
            player = new Player();
            playerTransfer = new PlayerTransfer();

            while (rs.next()) {
                if (player.getName() == null) {
                    player.setName(rs.getString(2));
                    player.setLastName(rs.getString(3));
                    player.setDateOfBirth(rs.getDate(4));
                    player.setFoot(rs.getString(5).charAt(0));
                    player.setRetirementDate(rs.getDate(6));
                    player.setGoals(rs.getInt(7));
                    player.setPosition(rs.getString(9));
                    player.setImage(rs.getBytes(10));
                    if (player.getPosition().contains("Goalkeeper")) player.setGoalsConceded(rs.getInt(8));
                    playerTransfer.setPlayer(player);
                }
                team = new Team();
                team.setName(rs.getString(11));
                playerTransfer.addTeam(team);
                playerTransfer.addStart(rs.getDate(12));
                playerTransfer.addEnd(rs.getDate(13));
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }

        return playerTransfer;
    }

    public PlayerFeature queryPlayerFeature(int id) {
        String query = "SELECT nome_feature, descrizione, type FROM feature JOIN calciatorefeature ON feature.idfeature = calciatorefeature.idfeature JOIN calciatore ON calciatore.idcalciatore = calciatorefeature.idcalciatore WHERE calciatore.idcalciatore = ?;";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            System.out.println(statement);
            ResultSet rs = statement.executeQuery();
            playerFeature = new PlayerFeature();
            playerFeature.setPlayer(player);

            while (rs.next()) {
                Feature feature = new Feature();
                feature.setName(rs.getString(1));
                feature.setDescription(rs.getString(2));
                feature.setType(rs.getString(3).charAt(0));
                playerFeature.addFeature(feature);
            }

        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            DBconnection.disconnect(connection);
        }

        return playerFeature;
    }
}