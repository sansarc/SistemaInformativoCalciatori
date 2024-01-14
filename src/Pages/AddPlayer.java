package Pages;

import DB.Query;
import Entity.Player;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPlayer extends JFrame {
    private JTextField playerName;
    private JComboBox footComboBox;
    private JCheckBox midfielderCheckBox;
    private JCheckBox forwardCheckBox;
    private JCheckBox goalkeeperCheckBox;
    private JCheckBox defenderCheckBox;
    private JTextField playerLastName;
    private JTextField playerBirthDate_s;
    private JPanel panel;
    private JButton insertButton;
    private JSpinner goalScoredSpinner;
    private JSpinner goalConcededSpinner;

    public AddPlayer() {
        setContentPane(panel);
        setSize(500, 400);
        setTitle("SIC - Add New Player");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        goalConcededSpinner.setEnabled(false);
        String[] footOpt = {"\0", "Left", "Right", "Ambidextrous"};
        for (String i : footOpt) footComboBox.addItem(i);
        goalkeeperCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(goalkeeperCheckBox.isSelected())
                {
                    goalConcededSpinner.setEnabled(true);

                    forwardCheckBox.setEnabled(false);
                    forwardCheckBox.setSelected(false);

                    midfielderCheckBox.setEnabled(false);
                    midfielderCheckBox.setSelected(false);

                    defenderCheckBox.setEnabled(false);
                    defenderCheckBox.setSelected(false);
                }
                else
                {
                    goalConcededSpinner.setEnabled(false);
                    goalConcededSpinner.setValue(0);

                    forwardCheckBox.setEnabled(true);
                    midfielderCheckBox.setEnabled(true);
                    defenderCheckBox.setEnabled(true);
                }

            }
        });
        defenderCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                verify_roles();
            }
        });
        midfielderCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                verify_roles();
            }
        });
        forwardCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                verify_roles();
            }
        });
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                char foot = '\0';
                Date birthdate = new Date();
                int goalScorer;
                int goalConceded;
                if(playerName.getText().isBlank() || playerLastName.getText().isBlank() || (!forwardCheckBox.isSelected() && !midfielderCheckBox.isSelected() && !defenderCheckBox.isSelected() && !goalkeeperCheckBox.isSelected() ) || playerBirthDate_s.getText().isBlank() || footComboBox.getSelectedItem().equals('\0') ) {
                    JOptionPane.showMessageDialog(null, "Errore, tutti i campi sono obbligatori!", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else
                {
                    try
                    {
                        SimpleDateFormat varDate = new SimpleDateFormat("MM/dd/yyyy");
                        birthdate = varDate.parse(playerBirthDate_s.getText());
                    }
                    catch (Exception exception)
                    {
                        JOptionPane.showMessageDialog(null, "Errore, la data non è espressa in un formato corretto!", "Errore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try
                    {
                        goalScorer = Integer.parseInt(goalScoredSpinner.getValue().toString());
                        if(goalScorer < 0)
                        {
                            JOptionPane.showMessageDialog(null, "Errore, il valore inserito per goal scorer non è valido!", "Errore", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    catch(Exception exception)
                    {
                        JOptionPane.showMessageDialog(null, "Errore, il valore inserito per goal scorer non è valido!", "Errore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    try
                    {
                        goalConceded = Integer.parseInt(goalConcededSpinner.getValue().toString());
                        if(goalConceded < 0)
                        {
                            JOptionPane.showMessageDialog(null, "Errore, il valore inserito per goal conceded non è valido!", "Errore", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    catch(Exception exception)
                    {
                        JOptionPane.showMessageDialog(null, "Errore, il valore inserito per goal conceded non è valido!", "Errore", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                if (footComboBox.getSelectedItem().equals("Right"))
                    foot = 'R';
                else if (footComboBox.getSelectedItem().equals("Left"))
                    foot = 'L';
                else if (footComboBox.getSelectedItem().equals("Ambidextrous"))
                    foot = 'A';
                String positions = "";
                boolean first = true;
                if(goalkeeperCheckBox.isSelected())
                {
                    positions = "G";
                }
                else {
                    if (defenderCheckBox.isSelected()) {
                        positions = "D";
                        first = false;
                        defenderCheckBox.setSelected(false);
                    }
                    if (midfielderCheckBox.isSelected()) {
                        if (first) {
                            positions = "M";
                            first = false;
                        }
                        else {
                            positions += ",M";
                        }
                    }
                    if(forwardCheckBox.isSelected()) {
                        if(first)
                            positions = "F";
                        else {
                            positions += ",F";
                        }
                    }
                }
                Player playerRequest = new Player(playerName.getText(), playerLastName.getText(), positions, birthdate, null, foot, goalScorer, goalConceded);
                var query = new Query();
                int playerId = query.InsertPlayer(playerRequest);
                if(playerId == -1) {
                    JOptionPane.showMessageDialog(null, "Errore, non è stato possibile inserire il calciatore!", "Errore", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(null, "Calciatore inserito con successo!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    Player player_i = new Player();
                    player_i.setId(playerId);
                    player_i.setName(playerName.getText());
                    player_i.setLastName(playerLastName.getText());
                    new Pages.AddOrModifyCarreer(player_i);
                    dispose();
                }
            }
        });
    }
    private void verify_roles()
    {
        if(defenderCheckBox.isSelected() || midfielderCheckBox.isSelected() || forwardCheckBox.isSelected())
        {
            goalConcededSpinner.setEnabled(false);
            goalConcededSpinner.setValue(0);

            goalkeeperCheckBox.setEnabled(false);
            goalkeeperCheckBox.setSelected(false);
        }
        else
        {
            goalConcededSpinner.setEnabled(true);
            goalkeeperCheckBox.setEnabled(true);
        }
    }
}
