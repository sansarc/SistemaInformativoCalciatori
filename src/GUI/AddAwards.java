package GUI;

import DB.Query;
import DB.QueryTools;
import Entity.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class AddAwards extends JFrame {
    private JTextField nameField;
    private JTextField dateWinnerField;
    private JComboBox selectNationBox;
    private JComboBox selectLevelBox;
    private JComboBox selectTeamBox;
    private JPanel WinnerPanel;
    private JComboBox selectPlayerBox;
    private JButton insertButton;
    private JPanel panel;
    public Date WinnerDate;

    public AddAwards() {
        setContentPane(panel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setSize(850, 500);
        setTitle("SIC - Add a new Award");
        Query query = new Query();
        List<String> nations = query.selectAllNationsForTeams();
        List<String> levels = new ArrayList<String>();
        List<Team> teams = new ArrayList<Team>();
        List<Player> players = new ArrayList<Player>();
        for (var n : nations) {
            selectNationBox.addItem(n);
        }
        WinnerDate = new Date();
        selectNationBox.setEnabled(false);
        selectLevelBox.setEnabled(false);
        selectTeamBox.setEnabled(false);
        insertButton.setEnabled(false);
        selectPlayerBox.setEnabled(false);
        dateWinnerField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!dateWinnerField.getText().isBlank()) {
                    try {
                        SimpleDateFormat varDate = new SimpleDateFormat("MM/dd/yyyy");
                            WinnerDate = varDate.parse(dateWinnerField.getText());
                            selectNationBox.setEnabled(true);
                    }
                    catch(Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error: bad date format!", "Error!", JOptionPane.ERROR_MESSAGE);
                        selectNationBox.setEnabled(false);
                    }
                }
                else {
                    selectNationBox.setEnabled(false);
                }
                selectNationBox.setSelectedIndex(0);
            }
        });

        selectNationBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                QueryTools.selectNationTool(selectNationBox, selectLevelBox, levels);
            }
        });
        selectLevelBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                QueryTools.selectLevelTool(selectNationBox, selectLevelBox, selectTeamBox, teams);
            }
        });
        selectTeamBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectTeamBox.getSelectedIndex() > 0) {
                    int idTeam = -1;
                    for(var t : teams) {
                        if(t.getName().equals(selectTeamBox.getSelectedItem().toString()) ) {
                            idTeam = t.getId();
                            break;
                        }
                    }
                    SimpleDateFormat varDate = new SimpleDateFormat("MM/dd/yyyy");
                    Date dt = new Date();
                    try {
                        dt = varDate.parse(dateWinnerField.getText());
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                        return;
                    }
                    boolean v = QueryTools.selectTeamTool(selectTeamBox, selectPlayerBox, players, idTeam, dt);
                    insertButton.setEnabled(v);
                    selectPlayerBox.setEnabled(v);
                }
                else {
                    boolean v = QueryTools.selectTeamTool(selectTeamBox);
                    insertButton.setEnabled(v);
                    selectPlayerBox.setEnabled(v);
                }
            }
        });
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(dateWinnerField.getText().toString().isBlank() || nameField.getText().toString().isBlank()) {
                    JOptionPane.showMessageDialog(null, "Error: fields marked with \"*\" are mandatory", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                SimpleDateFormat varDate = new SimpleDateFormat("MM/dd/yyyy");
                Date dt = new Date();
                try {
                    dt = varDate.parse(dateWinnerField.getText());
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error: bad date format!", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int idTeam = -1;
                int idPlayer = -1;
                if(selectPlayerBox.getSelectedIndex() > 0) {
                    var selected_element = selectPlayerBox.getSelectedItem().toString();
                    idPlayer = Integer.parseInt(selected_element.substring(selected_element.indexOf("$") + 1));;
                }
                else {
                    for(var t : teams) {
                        if(t.getName().equals(selectTeamBox.getSelectedItem().toString()) ) {
                            idTeam = t.getId();
                            break;
                        }
                    }
                }
                Award awardRequest = new Award(nameField.getText().toString(), dt, idTeam);
                awardRequest.setIdPlayer(idPlayer);
                awardRequest.setIdTeam(idTeam);
                int awardId = query.insertAward(awardRequest);
                if(awardId == -1) {
                    JOptionPane.showMessageDialog(null, "Error: insertion failed!", "Error!", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(null, "Insertion successful!", "Success!", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }
}
