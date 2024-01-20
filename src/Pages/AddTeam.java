package Pages;

import DB.Query;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddTeam extends JFrame {
    /*public static void main(String[] args) {
        new AddTeam();
    }*/
    private JPanel panel;
    private JTextField nationField;
    private JSpinner levelSpinner;
    private JTextField teamField;
    private JButton insertButton;
    private JTextPane Instructions;

    public AddTeam() {
        setContentPane(panel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setSize(850, 500);
        Instructions.setEnabled(false);
        //query.select_player_career(player.getId());
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(nationField.getText().isBlank())
                {
                    JOptionPane.showMessageDialog(null, "Error: the nation field cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if(Integer.parseInt(levelSpinner.getValue().toString()) < 0) {
                    JOptionPane.showMessageDialog(null, "Error: the level value cannot be a negative number!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if(teamField.getText().isBlank()) {
                    JOptionPane.showMessageDialog(null, "Error: the team name field cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if(nationField.getText().length() != 2) {
                    JOptionPane.showMessageDialog(null, "Error: the nation must be expressed through its ISO-3166-1 code!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    DB.Query query = new Query();
                    query.insert_team(new Entity.Team(teamField.getText(), nationField.getText(), Integer.parseInt(levelSpinner.getValue().toString()), -1));

                }
            }
        });

    }

}
