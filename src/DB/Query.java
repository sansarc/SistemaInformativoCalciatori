package DB;

import Entity.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.text.SimpleDateFormat;
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
    public List<Integer> queryPlayers(Player_Profile playerRequest, Team actualTeam, boolean freeagent, boolean retired,
                                      List<String> positions, char goals_c, char conceded_c, char apparences_c, char age_c, int age, String feature, boolean isFromMain) {
        Connection connection = DBconnection.connect();
        String query = QueryTools.getQuerySearchPlayer(playerRequest, actualTeam, freeagent, retired, positions, goals_c, conceded_c, apparences_c, age_c, feature, age, null);
        List<Integer> ids = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            int index = 1;
            QueryTools.getQuerySearchPlayer(playerRequest, actualTeam, freeagent, retired, positions, goals_c, conceded_c, apparences_c, age_c, feature, age, statement);
            System.out.println(statement);
            ResultSet rs = statement.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();

            DefaultTableModel tableModel = new DefaultTableModel();
            int cols = metaData.getColumnCount();
            for (int i = 1; i <= cols-1; i++) {
                String colName = metaData.getColumnName(i);
                String tableColName = QueryTools.updateColumnName(colName);
                tableModel.addColumn(tableColName);
            }

            String columnName;
            while (rs.next()) {
                Object[] rowData = new Object[cols];
                for (int i = 1; i <= cols-1; i++) {
                    columnName = metaData.getColumnName(i);
                    if (i == 2) rowData[i - 1] = "<html><b>" + rs.getObject(columnName) + "</b></html>";
                    else if (i == 3 && retired) rowData[i - 1] = "";
                    else rowData[i - 1] = rs.getObject(columnName);
                }
                columnName = metaData.getColumnName(cols);
                ids.add(rs.getInt(columnName));
                //rowData[cols - 1] = "<html><u>full profile</u></html>"; // Note: Use cols instead of cols - 1
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
        var resp = queryPlayers((Player_Profile)playerRequest, null, false, false, null, '\0','\0','\0','\0',-1,null,false);
        //var resp = queryPlayers(playerRequest.getName(), playerRequest.getLastName(), '=', String.valueOf(playerRequest.getAge(false)), Arrays.stream(((playerRequest.getPosition()).split(","))).toList(), playerRequest.getFoot(), false, "", false);
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
    public List<Player> player_from_team(int idTeam, java.util.Date dt) {
        List<Player> players = new ArrayList<Player>();
        players.add(new Player());
        Connection connection = DBconnection.connect();
        String query = "SELECT idPlayer, player_name, lastname FROM PLAYER_CARREER WHERE idTeam = ? AND ? BETWEEN startdate AND enddate";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idTeam);
            statement.setDate(2, new java.sql.Date(dt.getTime()));
            var rs = statement.executeQuery();
            while (rs.next()) {
                Player p = new Player();
                p.setId(rs.getInt(1));
                p.setName(rs.getString(2));
                p.setLastName(rs.getString(3));
                players.add(p);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            DBconnection.disconnect(connection);
            return players;
        }

    }
    public List<Integer> select_player_carreer(int idPlayer, boolean isGoalkeeper) {
        List<Integer> ids = new ArrayList<Integer>();
        Connection connection = DBconnection.connect();
        String query = "SELECT idTransfer,team_name,TO_CHAR(startdate, 'MM/DD/YYYY') AS startdate, TO_CHAR(enddate, 'MM/DD/YYYY') AS enddate, goalsscored, apparences, goalsconceded AS enddate FROM PLAYER_CARREER WHERE IDPlayer = ? ORDER BY STARTDATE DESC";

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
                ids.add(rs.getInt(1));
                tableModel.addRow(rowData);
            }
            resultsTable.setModel(tableModel);

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DBconnection.disconnect(connection);
            return ids;
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
    //Queries on Awards
    public int insertAward(Award awardRequest) {
        int awardId = -1;
        Connection connection = DBconnection.connect();
        String query = "";
        if(awardRequest.getIdPlayer() == -1) {
            query = "INSERT INTO AWARDS(WinDate, Name, IdTeam) VALUES(?,?,?) RETURNING IDAWARD";
        }
        else {
            query = "INSERT INTO AWARDS(WinDate, Name, IdPlayer) VALUES(?,?,?) RETURNING IDAWARD";
        }
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, new java.sql.Date(awardRequest.getWinDate().getTime()));
            statement.setString(2, awardRequest.getName());
            if(awardRequest.getIdPlayer() != -1) {
                statement.setInt(3, awardRequest.getIdPlayer());
            }
            else {
                statement.setInt(3, awardRequest.getIdTeam());
            }
            System.out.println(statement);
            var rs = statement.executeQuery();
            if (rs.next()) {
                awardId = rs.getInt(1);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch(Exception ex) {
            ex.printStackTrace();
        }finally {
            DBconnection.disconnect(connection);
            return awardId;
        }
    }
    public List<Award> select_palmares_from_player(int idPlayer) {
        List<Award> awardsResponse = new ArrayList<Award>();
        int awardId = -1;
        Connection connection = DBconnection.connect();
        String query = "(SELECT idaward,name,windate FROM AWARDS WHERE IDPLAYER = ?) UNION (SELECT A.idaward, A.name, A.windate FROM AWARDS A, PLAYER_CARREER PC WHERE A.IDTEAM = PC.IDTEAM AND PC.IDPLAYER = ? AND A.WINDATE BETWEEN PC.STARTDATE AND PC.ENDDATE) ORDER BY WINDATE DESC";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idPlayer);
            statement.setInt(2, idPlayer);
            System.out.println(statement);
            var rs = statement.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();

            DefaultTableModel tableModel = new DefaultTableModel();
            int cols = metaData.getColumnCount();
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
                    if (i == 3) {
                        Date dt = (Date) rs.getObject(columnName);
                        var formatteddata = dt.toString().substring(5, 7) + "/" + dt.toString().substring(8, 10) + "/" + dt.toString().substring(0, 4);
                        rowData[i - 2] = formatteddata;
                    } else rowData[i - 2] = rs.getObject(columnName);
                }
                tableModel.addRow(rowData);
                awardsResponse.add(new Award(rs.getString(2), rs.getDate(3), rs.getInt(1)));
            }
            resultsTable.setModel(tableModel);
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DBconnection.disconnect(connection);
            return awardsResponse;
        }
    }
    public void DeleteFromId(String tableName, String idName, int idValue){
        String query = "DELETE FROM " + tableName + " WHERE " + idName + " = ?";
        Connection connection = DBconnection.connect();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idValue);
            System.out.println(statement);
            var rs = statement.executeQuery();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch(Exception ex) {
            ex.printStackTrace();
        }finally {
            DBconnection.disconnect(connection);
        }
    }
    //Queries on Features
    public List<String> selectAllFeatures() {
        List<String> features = new ArrayList<String>();
        String query = "SELECT feature_name FROM FEATURES";
        Connection connection = DBconnection.connect();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            var rs = statement.executeQuery();
            while(rs.next()) {
                features.add(rs.getString(1));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch(Exception ex) {
            ex.printStackTrace();
        }finally {
            DBconnection.disconnect(connection);
            return features;
        }
    }
}