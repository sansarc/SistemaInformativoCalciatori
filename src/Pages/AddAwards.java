package Pages;

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
    public AddAwards() {
        setContentPane(panel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setSize(850, 500);
        Query query = new Query();
        List<String> nations = query.selectAllNationsForTeams();
        List<String> levels = new ArrayList<String>();
        List<Team> teams = new ArrayList<Team>();
        List<Player> players = new ArrayList<Player>();
        for (var n : nations) {
            selectNationBox.addItem(n);
        }
        selectLevelBox.setEnabled(false);
        selectTeamBox.setEnabled(false);
        insertButton.setEnabled(false);
        selectPlayerBox.setEnabled(false);
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
        });
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(dateWinnerField.getText().toString().isBlank() || nameField.getText().toString().isBlank()) {
                    JOptionPane.showMessageDialog(null, "I campi contrassegnati con \"*\" sono obbligatori!", "Errore!", JOptionPane.ERROR_MESSAGE);
                    return;
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
                    JOptionPane.showMessageDialog(null, "NOOOOO!", "Errore!", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(null, "OKKKK!", "Errore!", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
