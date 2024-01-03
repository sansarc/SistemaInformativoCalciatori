package DB;

import Entity.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Query {
    private JTable resultsTable;
    private Player player;
    private Team team;
    private PlayerTransfer playerTransfer;
    private PlayerFeature playerFeature;
    public Query() { }
    public Query(JTable resultsTable)
    {
        this.resultsTable = resultsTable;
    }

    public List<Integer> queryPlayers(String name, String lastname, char ageMath, String age, List<String> positions, char foot, boolean isRetired, String team) {
        Connection connection = DBconnection.connect();
        String query = QueryTools.getQuery(name, lastname, ageMath, age, positions, foot, isRetired, team);
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
        Connection connection = DBconnection.connect();
        String query = "SELECT DISTINCT players.*, teams.team_name, Player_Team.StartDate, player_team.EndDate, STRING_AGG(DISTINCT features.feature_name, ', ') AS feature_list FROM players JOIN Player_Team ON players.idPlayer = Player_team.idPlayer JOIN teams ON player_team.idTeam = teams.idTeam JOIN player_feature ON players.idPlayer = player_feature.idPlayer JOIN features ON features.Feature_Name = player_feature.idFeature WHERE players.idPlayer = ? GROUP BY players.idPlayer, teams.team_name, player_team.StartDate, player_team.EndDate ORDER BY player_team.startdate;";

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
        Connection connection = DBconnection.connect();
        String query = "SELECT feature_name, description, Type_feature  FROM features JOIN player_feature ON features.feature_name = player_feature.idFeature JOIN players ON players.idPlayer = player_feature.idPlayer WHERE players.idPlayer = ?;";
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
    public char Login(String username, String password) {
        User usr = SelectUser(username, password);
        if(usr == null)
            return '1';
        else
            return usr.getType();
    }
    public boolean CreateUser(String username, String password) {
        var respSelect = this.SelectUser(username, "");
        if(respSelect != null)
        {
            return false;
        }
        boolean ret = false;
        Connection connection = DBconnection.connect();
        String query = "INSERT INTO SIC_Users(email, pwd, type_user) VALUES (?, ?, 'U');";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            System.out.println(statement);
            statement.executeUpdate();
            DBconnection.disconnect(connection);
            respSelect = this.SelectUser(username, password);
            ret = respSelect != null;
        } catch (SQLException se) {
            DBconnection.disconnect(connection);
        }
        return ret;
    }

    private User SelectUser(String username, String password)
    {
        User usr = null;
        Connection connection = DBconnection.connect();
        String query = "SELECT email, type_user FROM SIC_Users WHERE email = ?";
        if(password.isBlank())
            query += ";";
        else
            query += " AND pwd = ?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            if(! password.isBlank())
                statement.setString(2, password);
            System.out.println(statement);
            var rs = statement.executeQuery();
            if(rs.next()) {
             usr = new User();
             usr.setEmail(rs.getString(1));
             usr.setType(rs.getString(2).charAt(0));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            DBconnection.disconnect(connection);
        }
        return usr;
    }

}