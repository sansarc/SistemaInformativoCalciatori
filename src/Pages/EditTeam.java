package Pages;

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

    public EditTeam() {
        setContentPane(panel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setSize(850, 500);

        Query query = new Query();
        List<String> nations = query.SelectAllNationsForTeams();
        List<String> levels = new ArrayList<String>();
        List<Team> teams = new ArrayList<Team>();
        for (var n : nations) {
            selectNationBox.addItem(n);
        }
        selectLevelBox.setEnabled(false);
        selectTeamBox.setEnabled(false);
        deleteButton.setEnabled(false);
        editButton.setEnabled(false);

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
                    JOptionPane.showMessageDialog(null, "Livello non può essere < 0", "Invalid Search", JOptionPane.WARNING_MESSAGE);
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
                query.update_team(team);
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
                var r = query.DeleteTeam(team.getId());
                if(r) {
                    JOptionPane.showMessageDialog(null, "Team eliminato", "Invalid Search", JOptionPane.WARNING_MESSAGE);
                    dispose();
                }
                else {
                    JOptionPane.showMessageDialog(null, "Team non eliminato", "Invalid Search", JOptionPane.WARNING_MESSAGE);
                    dispose();
                }

            }
        });
    }
}
