package Pages;

import DB.Query;
import Entity.Player;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddPlayer extends JFrame {
    private JTextField playerName;
    private JComboBox footComboBox;
    private JCheckBox midfenderCheckBox;
    private JCheckBox forwardCheckBox;
    private JCheckBox goalkeaperCheckBox;
    private JCheckBox defenderCheckBox;
    private JTextField playerLastName;
    private JTextField playerBornDate_s;
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
        goalkeaperCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(goalkeaperCheckBox.isSelected())
                {
                    goalConcededSpinner.setEnabled(true);

                    forwardCheckBox.setEnabled(false);
                    forwardCheckBox.setSelected(false);

                    midfenderCheckBox.setEnabled(false);
                    midfenderCheckBox.setSelected(false);

                    defenderCheckBox.setEnabled(false);
                    defenderCheckBox.setSelected(false);
                }
                else
                {
                    goalConcededSpinner.setEnabled(false);
                    goalConcededSpinner.setValue(0);

                    forwardCheckBox.setEnabled(true);
                    midfenderCheckBox.setEnabled(true);
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
        midfenderCheckBox.addActionListener(new ActionListener() {
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
                Date bornDate = new Date();
                int goalScorer;
                int goalConceded;
                if(playerName.getText().isBlank() || playerLastName.getText().isBlank() || (!forwardCheckBox.isSelected() && !midfenderCheckBox.isSelected() && !defenderCheckBox.isSelected() && !goalkeaperCheckBox.isSelected() ) || playerBornDate_s.getText().isBlank() || footComboBox.getSelectedItem().equals('\0') ) {
                    JOptionPane.showMessageDialog(null, "Errore, tutti i campi sono obbligatori!", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else
                {
                    try
                    {
                        SimpleDateFormat varDate = new SimpleDateFormat("MM/dd/yyyy");
                        bornDate = varDate.parse(playerBornDate_s.getText());
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
                if(goalkeaperCheckBox.isSelected())
                {
                    positions = "G";
                }
                else {
                    if (defenderCheckBox.isSelected()) {
                        positions = "D";
                        first = false;
                        defenderCheckBox.setSelected(false);
                    }
                    if (midfenderCheckBox.isSelected()) {
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
                Player playerRequest = new Player(playerName.getText(), playerLastName.getText(), positions, bornDate, null, foot, goalScorer, goalConceded);
                var query = new Query();
                Player playerResponse = query.InsertPlayer(playerRequest);
            }
        });
    }
    private void verify_roles()
    {
        if(defenderCheckBox.isSelected() || midfenderCheckBox.isSelected() || forwardCheckBox.isSelected())
        {
            goalConcededSpinner.setEnabled(false);
            goalConcededSpinner.setValue(0);

            goalkeaperCheckBox.setEnabled(false);
            goalkeaperCheckBox.setSelected(false);
        }
        else
        {
            goalConcededSpinner.setEnabled(true);
            goalkeaperCheckBox.setEnabled(true);
        }
    }
}
