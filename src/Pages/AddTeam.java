package Pages;

import DB.Query;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddTeam extends JFrame {
    public static void main(String[] args) {
        new AddTeam();
    }
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
                    JOptionPane.showMessageDialog(null, "Errore, il campo nation non può essere vuoto!", "Errore", JOptionPane.ERROR_MESSAGE);
                }
                else if(Integer.parseInt(levelSpinner.getValue().toString()) < 0) {
                    JOptionPane.showMessageDialog(null, "Errore, il valore di level non può essere < 0!", "Errore", JOptionPane.ERROR_MESSAGE);
                }
                else if(teamField.getText().isBlank()) {
                    JOptionPane.showMessageDialog(null, "Errore, il campo team name non può essere vuoto!", "Errore", JOptionPane.ERROR_MESSAGE);
                }
                else if(nationField.getText().length() != 2) {
                    JOptionPane.showMessageDialog(null, "La nazione deve essere espressa attraverso il suo codice!", "Errore", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    DB.Query query = new Query();
                    query.insert_team(new Entity.Team(teamField.getText(), nationField.getText(), Integer.parseInt(levelSpinner.getValue().toString()), -1));
                }
            }
        });

    }

}
