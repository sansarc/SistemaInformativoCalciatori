package GUI;

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
    private JButton returnInMainPageButton;

    public AddTeam(boolean from_main) {
        setContentPane(panel);
        setDefaultCloseOperation( (from_main) ? EXIT_ON_CLOSE : DISPOSE_ON_CLOSE );
        returnInMainPageButton.setVisible(from_main);
        setLocationRelativeTo(null);
        setVisible(true);
        setTitle("SIC - Add new Team");
        setSize(850, 500);
        Instructions.setEnabled(false);
        //query.select_player_career(player.getId());
        returnInMainPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Main();
                dispose();
            }
        });
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
                    JOptionPane.showMessageDialog(null, "Error: the nation must be expressed by its ISO-3166-1 code!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    DB.Query query = new Query();
                    int idNewTeam = query.insert_team(new Entity.Team(teamField.getText(), nationField.getText(), Integer.parseInt(levelSpinner.getValue().toString()), -1));
                    if(idNewTeam != -1) {
                        JOptionPane.showMessageDialog(null, "New team successfully inserted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Error while inserting data!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

    }

}
