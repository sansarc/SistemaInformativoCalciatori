package GUI;

import DB.Query;
import DB.QueryTools;
import Entity.Team;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class EditTeam extends JFrame {
    private JPanel searchTeam_panel;
    private JComboBox selectTeamBox;
    private JComboBox selectLevelBox;
    private JComboBox selectNationBox;
    private JButton editButton;
    private JSpinner levelSpinner;
    private JButton deleteButton;
    private JPanel panel;
    private JButton returnInMainPageButton;

    public EditTeam() {
        setContentPane(panel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setSize(850, 500);
        setTitle("SIC - Edit Team");
        Query query = new Query();
        List<String> nations = query.selectAllNationsForTeams();
        List<String> levels = new ArrayList<String>();
        List<Team> teams = new ArrayList<Team>();
        for (var n : nations) {
            selectNationBox.addItem(n);
        }
        selectLevelBox.setEnabled(false);
        selectTeamBox.setEnabled(false);
        deleteButton.setEnabled(false);
        editButton.setEnabled(false);
        returnInMainPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Main();
                dispose();
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
                var b_enabled = QueryTools.selectTeamTool(selectTeamBox);
                editButton.setEnabled(b_enabled);
                deleteButton.setEnabled(b_enabled);
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var new_level = Integer.parseInt(levelSpinner.getValue().toString());
                if(new_level <0 ) {
                    JOptionPane.showMessageDialog(null, "Level cannot be a negative number", "Invalid Search", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Team team = new Team();
                for(var t : teams) {
                    if(t.getName().equals(selectTeamBox.getSelectedItem().toString()) ) {
                        team = t;
                        break;
                    }
                }
                team.setCategory(new_level);
                int idTeam = query.update_team(team);
                if(idTeam != -1) {
                    JOptionPane.showMessageDialog(null, "New team successfully updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(null, "Error while updating!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Team team = new Team();
                for(var t : teams) {
                    if(t.getName().equals(selectTeamBox.getSelectedItem().toString()) ) {
                        team = t;
                        break;
                    }
                }
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the selected team?", "Delete team", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    var ok_ = query.deleteFromId("TEAM", "idteam", team.getId());
                    if(ok_) {
                        JOptionPane.showMessageDialog(null, "Team successfully deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Team: feature was not deleted!", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                }
            }
        });
    }
}
