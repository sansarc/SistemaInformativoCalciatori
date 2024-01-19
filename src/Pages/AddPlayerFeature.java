package Pages;
import DB.Query;
import Entity.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AddPlayerFeature extends JFrame {
    private JComboBox featureComboBox;
    private JButton insertButton;
    private JLabel playerLabel;
    private JPanel panel;

    public AddPlayerFeature(Player_Profile player) {
        setContentPane(panel);
        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        Query query = new Query();
        playerLabel.setText("Aggiunta Features per giocatore: " + player.getName() + " " + player.getLastName());
        List<String> features = new ArrayList<String>();
        features.add("\0");
        features.addAll(query.selectAllFeatures());
        for(var f : features) {
            featureComboBox.addItem(f);
        }
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(featureComboBox.getSelectedIndex() > 1) {
                    query.insertPlayerFeature(player.getId(), featureComboBox.getSelectedItem().toString());
                }
            }
        });
    }
}
