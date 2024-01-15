package DB;

import Entity.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Query {
    private JTable resultsTable;
    public Query() {
    }

    public Query(JTable resultsTable) {
        this.resultsTable = resultsTable;
    }
    //Queries on Player
    public List<Integer> queryPlayers(String name, String lastname, char ageMath, String age, List<String> positions, char foot, boolean isRetired, String team, boolean isFromMain) {
        Connection connection = DBconnection.connect();
        String query = QueryTools.getQuery(name, lastname, ageMath, age, positions, foot, isRetired, team);
        List<Integer> ids = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int index = 1;
            if (!name.isBlank()) statement.setString(index++, "%" + name + "%");
            if (!lastname.isBlank()) statement.setString(index++, "%" + lastname + "%");
            if (!positions.isEmpty()) {
                for (String i : positions) statement.setString(index++, "%" + i + "%");
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
                rowData[cols - 1] = "<html><u>full profile</u></html>"; // Note: Use cols instead of cols - 1
                tableModel.addRow(rowData);
            }
            if(isFromMain)
                resultsTable.setModel(tableModel);

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DBconnection.disconnect(connection);
        }

        return ids;
    }
    public int InsertPlayer(Player playerRequest) {
        var resp = queryPlayers(playerRequest.getName(), playerRequest.getLastName(), '=', String.valueOf(playerRequest.getAge(false)), Arrays.stream(((playerRequest.getPosition()).split(","))).toList(), playerRequest.getFoot(), false, "", false);
        if (!resp.isEmpty()) {
            int choice = JOptionPane.showConfirmDialog(null, "Esiste già un calciatore corrispondente ai dati inseriti, si vuole proseguire comunque?", "Calciatore già presente", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION)
                return InsertPlayer_query(playerRequest);
        }
        else {
            return InsertPlayer_query(playerRequest);
        }
        return -1;
    }
    public boolean DeletePlayer(int playerId) {
        Connection connection = DBconnection.connect();
        boolean r = false;
        String query = "DELETE FROM PLAYERS WHERE idPlayer = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, playerId);
            System.out.println(statement);
            var rs = statement.executeQuery();
            r = true;
        } catch (SQLException se) {
            se.printStackTrace();
        } catch(Exception ex) {
            ex.printStackTrace();
        }finally {
            DBconnection.disconnect(connection);
            return r;
        }
    }

    public int UpldatePlayer(Player playerRequest) {
        int idPlayer = -1;
        Connection connection = DBconnection.connect();
        String query = "UPDATE PLAYERS SET Player_Name = ?,Lastname = ?,birthdate = ?,foot = ?,positions = ? WHERE idPlayer = ? RETURNING IDPLAYER";
        if(playerRequest.getRetirementDate() != null) {
            query += ", retirementDate = ?";
        }
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerRequest.getName());
            statement.setString(2, playerRequest.getLastName());
            statement.setDate(3, new java.sql.Date(playerRequest.getBirthDate().getTime()));
            statement.setString(4, Character.toString(playerRequest.getFoot()));
            statement.setString(5, playerRequest.getPosition());
            statement.setInt(6, playerRequest.getId());
            if(playerRequest.getRetirementDate() != null) {
                statement.setDate(7, new java.sql.Date(playerRequest.getRetirementDate().getTime()));
            }
            System.out.println(statement);
            var rs = statement.executeQuery();
            if (rs.next()) {
                idPlayer = rs.getInt(1);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch(Exception ex) {
            ex.printStackTrace();
        }finally {
            DBconnection.disconnect(connection);
            return idPlayer;
        }
    }
    private int InsertPlayer_query(Player playerRequest) {
        int idPlayer = -1;
        Connection connection = DBconnection.connect();
        String query = "INSERT INTO PLAYERS(player_name, lastname, birthdate, foot, positions) VALUES(?,?,?,?,?) RETURNING IDPLAYER";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerRequest.getName());
            statement.setString(2, playerRequest.getLastName());
            statement.setDate(3, new java.sql.Date(playerRequest.getBirthDate().getTime()));
            statement.setString(4, Character.toString(playerRequest.getFoot()));
            statement.setString(5, playerRequest.getPosition());
            System.out.println(statement);
            var rs = statement.executeQuery();
            if (rs.next()) {
                idPlayer = rs.getInt(1);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch(Exception ex) {
            ex.printStackTrace();
        }finally {
            DBconnection.disconnect(connection);
            return idPlayer;
        }
    }
    public Player_Profile GetPlayerProfileFromId(int id) {
        Connection connection = DBconnection.connect();
        String query = "SELECT * FROM PLAYER_PROFILE WHERE IDPlayer = ?";
        var player = new Player_Profile();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            System.out.println(statement);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                if (player.getName() == null) {
                    player.setId(rs.getInt(1));
                    player.setName(rs.getString(2));
                    player.setLastName(rs.getString(3));
                    player.setBirthDate(rs.getDate(4));
                    player.setFoot(rs.getString(5).charAt(0));
                    player.setRetirementDate(rs.getDate(6));
                    player.setGoals(rs.getInt(7));
                    player.setPosition(rs.getString(9));
                    player.setImage(rs.getBytes(10));
                    if (player.getPosition().contains("G")) player.setGoalsConceded(rs.getInt(8));
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        finally {
            DBconnection.disconnect(connection);
            return player;
        }
    }
    public PlayerFeature queryPlayerFeature(int id, Player player) {
        Connection connection = DBconnection.connect();
        String query = "SELECT feature_name, description, Type_feature  FROM features JOIN player_feature ON features.feature_name = player_feature.idFeature JOIN players ON players.idPlayer = player_feature.idPlayer WHERE players.idPlayer = ?;";
        var playerFeature = new PlayerFeature();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            System.out.println(statement);
            ResultSet rs = statement.executeQuery();
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
    //Query on User
    public char Login(String username, String password) {
        User usr = SelectUser(username, password);
        if (usr == null)
            return '1';
        else
            return usr.getType();
    }
    public boolean CreateUser(String username, String password) {
        var respSelect = this.SelectUser(username, "");
        if (respSelect != null) {
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
    private User SelectUser(String username, String password) {
        User usr = null;
        Connection connection = DBconnection.connect();
        String query = "SELECT email, type_user FROM SIC_Users WHERE email = ?";
        if (password.isBlank())
            query += ";";
        else
            query += " AND pwd = ?;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            if (!password.isBlank())
                statement.setString(2, password);
            System.out.println(statement);
            var rs = statement.executeQuery();
            if (rs.next()) {
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
    //Queries on Team
    public List<String> SelectAllNationsForTeams() {
        List<String> nations = new ArrayList<String>();
        nations.add("");
        Connection connection = DBconnection.connect();
        String query = "SELECT DISTINCT NATION FROM TEAMS";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            var rs = statement.executeQuery();
            while (rs.next()) {
                nations.add(rs.getString(1));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            DBconnection.disconnect(connection);
            return nations;
        }
    }
    public List<String> SelectLevelsFromNation(String nation) {
        List<String> levels = new ArrayList<String>();
        levels.add("");
        Connection connection = DBconnection.connect();
        String query = "SELECT DISTINCT LEVEL FROM TEAMS WHERE nation = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nation);
            var rs = statement.executeQuery();
            while (rs.next()) {
                levels.add(rs.getString(1));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            DBconnection.disconnect(connection);
            return levels;
        }
    }
    public List<Team> TeamsFromNationAndLevel(String nation, int level) {
        List<Team> teams = new ArrayList<Team>();
        teams.add(new Team("", nation, level, -1));
        Connection connection = DBconnection.connect();
        String query = "SELECT TEAM_NAME, NATION, LEVEL, IDTEAM FROM TEAMS WHERE nation = ? and level = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nation);
            statement.setInt(2, level);
            var rs = statement.executeQuery();
            while (rs.next()) {
                teams.add(new Team(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4) ) );
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            DBconnection.disconnect(connection);
            return teams;
        }
    }
    public boolean DeleteTeam(int idTeam) {
        Connection connection = DBconnection.connect();
        String query = "DELETE FROM TEAMS WHERE idTeam = ?";
        boolean r = false;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idTeam);
            System.out.println(statement);
            var rs = statement.executeQuery();
            r = true;
        } catch (SQLException se) {
            se.printStackTrace();
        } catch(Exception ex) {
            ex.printStackTrace();
        }finally {
            DBconnection.disconnect(connection);
            return r;
        }
    }

    public int update_team(Team team) {
        int idTeam = -1;
        Connection connection = DBconnection.connect();
        String query = "UPDATE TEAMS SET Level = ? WHERE IDTEAM = ? RETURNING IDTEAM";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, team.getCategory());
            statement.setInt(2, team.getId());
            System.out.println(statement);
            var rs = statement.executeQuery();
            if (rs.next()) {
                idTeam = rs.getInt(1);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch(Exception ex) {
            ex.printStackTrace();
        }finally {
            DBconnection.disconnect(connection);
            return idTeam;
        }
    }
    public int insert_player_team(int idPlayer, int idTeam, java.util.Date startDate, java.util.Date endDate, int goalsscored, int goalsconceded, boolean isGoalkeeper, int apparences) {
        int transferId = -1;
        Connection connection = DBconnection.connect();
        String query = "";
        if(endDate != null) {
            if(isGoalkeeper) {
                query = "INSERT INTO PLAYER_TEAM(idplayer,idteam,startdate,goalsscored,apparences,goalsconceded,enddate) VALUES(?,?,?,?,?,?,?) RETURNING IDTRANSFER";
            }
            else {
                query = "INSERT INTO PLAYER_TEAM(idplayer,idteam,startdate,goalsscored,apparences,enddate) VALUES(?,?,?,?,?,?) RETURNING IDTRANSFER";
            }
        }
        else {
            if(isGoalkeeper) {
                query = "INSERT INTO PLAYER_TEAM(idplayer,idteam,startdate,goalsscored,apparences,goalsconceded) VALUES(?,?,?,?,?,?) RETURNING IDTRANSFER";
            }
            else {
                query = "INSERT INTO PLAYER_TEAM(idplayer,idteam,startdate,goalsscored,apparences) VALUES(?,?,?,?,?) RETURNING IDTRANSFER";
            }
        }
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int i = 0;
            statement.setInt(++i, idPlayer);
            statement.setInt(++i, idTeam);
            statement.setDate(++i, new java.sql.Date(startDate.getTime()));
            statement.setInt(++i, goalsscored);
            statement.setInt(++i, apparences);
            if(isGoalkeeper) {
                statement.setInt(++i, goalsconceded);
            }
            if(endDate != null) {
                statement.setDate(++i, new java.sql.Date(endDate.getTime()));
            }
            System.out.println(statement);
            var rs = statement.executeQuery();
            if (rs.next()) {
                transferId = rs.getInt(1);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch(Exception ex) {
            ex.printStackTrace();
        }finally {
            DBconnection.disconnect(connection);
            return transferId;
        }
    }
    public void select_player_carreer(int idPlayer, boolean isGoalkeeper) {
        Connection connection = DBconnection.connect();
        String query = "SELECT idteam,team_name,TO_CHAR(startdate, 'MM/DD/YYYY') AS startdate, TO_CHAR(enddate, 'MM/DD/YYYY') AS enddate, goalsscored, goalsconceded AS enddate FROM PLAYER_CARREER WHERE IDPlayer = ? ORDER BY STARTDATE DESC";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idPlayer);
            ResultSet rs = statement.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();

            DefaultTableModel tableModel = new DefaultTableModel();
            int cols = metaData.getColumnCount();
            if(!isGoalkeeper) cols--;
            for (int i = 2; i <= cols; i++) {
                String colName = metaData.getColumnName(i);
                String tableColName = QueryTools.updateColumnName(colName);
                tableModel.addColumn(tableColName);
            }

            String columnName;
            while (rs.next()) {
                Object[] rowData = new Object[cols];
                for (int i = 2; i <= cols; i++) {
                    columnName = metaData.getColumnName(i);
                    if (i == 2) rowData[i - 2] = "<html><b>" + rs.getObject(columnName) + "</b></html>";
                    else rowData[i - 2] = rs.getObject(columnName);
                }
                tableModel.addRow(rowData);
            }
            resultsTable.setModel(tableModel);

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DBconnection.disconnect(connection);
        }
    }
    public int insert_team(Team teamRequest) {
        var resp = SearchTeam(teamRequest);
        if (resp != -1) {
            int scelta = JOptionPane.showConfirmDialog(null, "Esiste già una squadra chiamata " + teamRequest.getName() + " nella nazione indicata!", "Calciatore già presente", JOptionPane.ERROR_MESSAGE);
        }
        else {
            return InsertTeam_query(teamRequest);
        }
        return -1;
    }
    private int SearchTeam(Team teamRequest) {
        int idTeam = -1;
        Connection connection = DBconnection.connect();
        String query = "SELECT idTeam FROM TEAMS WHERE team_name = ? and nation= ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, teamRequest.getName());
            statement.setString(2, teamRequest.getNationality());
            System.out.println(statement);
            var rs = statement.executeQuery();
            if (rs.next()) {
                idTeam = rs.getInt(1);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch(Exception ex) {
            ex.printStackTrace();
        }finally {
            DBconnection.disconnect(connection);
            return idTeam;
        }
    }
    private int InsertTeam_query(Team teamRequest) {
        int idTeam = -1;
        Connection connection = DBconnection.connect();
        String query = "INSERT INTO TEAMS(team_name, nation, level) VALUES(?,?,?) RETURNING IDTEAM";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, teamRequest.getName());
            statement.setString(2, teamRequest.getNationality());
            statement.setInt(3, teamRequest.getCategory());
            System.out.println(statement);
            var rs = statement.executeQuery();
            if (rs.next()) {
                idTeam = rs.getInt(1);
                int scelta = JOptionPane.showConfirmDialog(null, "Team inserito con successo!", "OK", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch(Exception ex) {
            ex.printStackTrace();
        }finally {
            DBconnection.disconnect(connection);
            return idTeam;
        }
    }
}