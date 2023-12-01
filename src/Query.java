import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Calciatore.*;

public class Query {
    public List<Calciatore> getCalciatoriByRuolo(String ruolo) {
        List<Calciatore> calciatori = new ArrayList<>();
        DBconnection db = new DBconnection("postgres");
        Connection connection = db.connect();
        String query = "SELECT nome, cognome FROM calciatore WHERE ruoli = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, ruolo);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Calciatore calciatore = new Calciatore();
                calciatore.setNome(rs.getString(1));
                calciatore.setCognome(rs.getString(2));
                calciatori.add(calciatore);
            }
        }

        catch (SQLException se) {
            se.printStackTrace();
        }

        return  calciatori;
    }
}
