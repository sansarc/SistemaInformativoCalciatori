import Calciatore.Calciatore;

import javax.swing.*;

public class Profile {
    private JPanel panel;
    private JLabel playerNameLabel;
    QueryPlayerDetails query;
    Calciatore calciatore;
    public Profile(int id) {
        query = new QueryPlayerDetails();
        calciatore = query.queryProfile(id);
        playerNameLabel.setText(calciatore.getNome() + " " + calciatore.getCognome());
    }
}
