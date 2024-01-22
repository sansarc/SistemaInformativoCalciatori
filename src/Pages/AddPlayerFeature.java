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
        playerLabel.setText("Insert Feature for: " + player.getName() + " " + player.getLastName());
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
                    var ret = query.insertPlayerFeature(player.getId(), featureComboBox.getSelectedItem().toString());
                    if(ret != -1) {
                        JOptionPane.showMessageDialog(null, "Instance added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Error: instance could not be inserted!", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                }
            }
        });
    }
}
