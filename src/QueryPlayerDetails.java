import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Calciatore.*;

public class QueryPlayerDetails {
    Calciatore calciatore = new Calciatore();
    public Calciatore queryProfile(int id) {
        String query = "SELECT calciatore.idcalciatore, calciatore.nome_calciatore, calciatore.cognome, squadra.nome_squadra, calciatoresquadra.dataInizio, calciatoresquadra.dataFine FROM calciatore JOIN calciatoresquadra ON calciatore.idcalciatore = calciatoresquadra.idcalciatore JOIN squadra ON calciatoresquadra.idsquadra = squadra.idsquadra;";
        Connection connection = DBconnection.connect();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            System.out.println(statement);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                calciatore.setNome(rs.getString(1));
                calciatore.setCognome(rs.getString(2));
                /* calciatore.setNomeSquadra(resultSet.getString("nome_squadra"));
                calciatore.setDataInizio(resultSet.getDate("dataInizio"));
                calciatore.setDataFine(resultSet.getDate("dataFine")); */
            }
        }

        catch (SQLException se) {
            se.printStackTrace();
        }
        finally {
            DBconnection.disconnect(connection);
        }

        return calciatore;
    }

}
