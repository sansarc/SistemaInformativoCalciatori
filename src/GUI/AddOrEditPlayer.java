package GUI;

import DB.Query;
import Entity.Player;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddOrEditPlayer extends JFrame {
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
    private JTextField imagePathField;
    private JButton returnInMainPageButton;
    private JTextField retirementDateField;

    public AddOrEditPlayer(Player player) {
        setContentPane(panel);
        setSize(500, 400);
        boolean isEdit = player != null;
        returnInMainPageButton.setVisible(!isEdit);
        setDefaultCloseOperation((isEdit) ? DISPOSE_ON_CLOSE : EXIT_ON_CLOSE );
        setLocationRelativeTo(null);
        setVisible(true);
        String[] footOpt = {"\0", "Left", "Right", "Ambidextrous"};
        for (String i : footOpt) footComboBox.addItem(i);
        if(!isEdit) {
            setTitle("SIC - Add New Player");
        }
        else  {
            setTitle("SIC - Edit " + player.getName() + " " + player.getLastName());
            insertButton.setText("Edit");
            playerName.setText(player.getName());
            playerLastName.setText(player.getLastName());
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            playerBirthDate_s.setText(sdf.format(player.getBirthDate()));
            var list_positions = player.getPosition().split(",");
            for(var pos : list_positions) {
                switch(pos) {
                    case "G":
                        goalkeeperCheckBox.setSelected(true);
                        break;
                    case "D":
                        defenderCheckBox.setSelected(true);
                        break;
                    case "M":
                        midfielderCheckBox.setSelected(true);
                        break;
                    case "F":
                        forwardCheckBox.setSelected(true);
                        break;
                    default:
                        break;
                }
            }
            String f = "";
            switch(player.getFoot()) {
                case 'R':
                    f = "Right";
                    break;
                case 'L':
                    f = "Left";
                    break;
                case 'A':
                    f = "Ambidextrous";
                    break;
                default:
                    f = "\0";
                    break;
            }
            footComboBox.setSelectedItem(f);
            if(player.getRetirementDate() != null)
                retirementDateField.setText(sdf.format(player.getRetirementDate()));
        }
        goalkeeperCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(goalkeeperCheckBox.isSelected())
                {

                    forwardCheckBox.setEnabled(false);
                    forwardCheckBox.setSelected(false);

                    midfielderCheckBox.setEnabled(false);
                    midfielderCheckBox.setSelected(false);

                    defenderCheckBox.setEnabled(false);
                    defenderCheckBox.setSelected(false);
                }
                else
                {
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
                SimpleDateFormat varDate = new SimpleDateFormat("MM/dd/yyyy");
                Date birthdate = new Date();
                if(playerName.getText().isBlank() || playerLastName.getText().isBlank() || (!forwardCheckBox.isSelected() && !midfielderCheckBox.isSelected() && !defenderCheckBox.isSelected() && !goalkeeperCheckBox.isSelected() ) || playerBirthDate_s.getText().isBlank() || footComboBox.getSelectedIndex() == 0 ) {
                    JOptionPane.showMessageDialog(null, "Error: fields marked with \"*\" are mandatory!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try
                {
                    birthdate = varDate.parse(playerBirthDate_s.getText());
                }
                catch (Exception exception)
                {
                    JOptionPane.showMessageDialog(null, "Error: the birth date must be expressed in the required format!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Date retirementDate = null;
                if(!retirementDateField.getText().isBlank()) {
                    try
                    {
                        retirementDate = new Date();
                        retirementDate = varDate.parse(retirementDateField.getText());
                    }
                    catch (Exception exception)
                    {
                        JOptionPane.showMessageDialog(null, "Error: the retirement date must be expressed in the required format!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                if(!imagePathField.getText().isBlank() && ! imagePathField.getText().substring(imagePathField.getText().lastIndexOf('.')+1).equals("jpg")) {
                    JOptionPane.showMessageDialog(null, "Error: the image is not in jpg format!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                char foot = '\0';
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
                String playerImg = "";
                try {
                    if(!imagePathField.getText().isBlank()) {
                        playerImg = DB.QueryTools.imageToBase64(imagePathField.getText());
                    }
                }
                catch(Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error: invalid path \" + imagePathField.getText() + \"", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Player playerRequest = new Player(playerName.getText(), playerLastName.getText(), positions, birthdate, retirementDate, foot, -1);
                var query = new Query();
                int playerId = -1;
                if(!isEdit) {
                    playerId = query.insertPlayer(playerRequest, playerImg);
                }
                else {
                    playerRequest.setId(player.getId());
                    playerId = query.updatePlayer(playerRequest, playerImg);
                }
                if(playerId == -1) {
                    JOptionPane.showMessageDialog(null, "Error: player not inserted!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(null, "Player inserted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    Player player_i = new Player();
                    player_i.setId(playerId);
                    player_i.setName(playerName.getText());
                    player_i.setLastName(playerLastName.getText());
                    player_i.setPosition(positions);
                    new GUI.AddOrEditCarreer(player_i);
                }
            }
        });
    }
    private void verify_roles() {
        if(defenderCheckBox.isSelected() || midfielderCheckBox.isSelected() || forwardCheckBox.isSelected())
        {
            goalkeeperCheckBox.setEnabled(false);
            goalkeeperCheckBox.setSelected(false);
        }
        else
        {
            goalkeeperCheckBox.setEnabled(true);
        }
    }
}
