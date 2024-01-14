package Pages;
import Entity.*;
import DB.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class AddOrModifyCarreer extends JFrame {
    private JPanel panel;
    private JTextField fromDate;
    private JTextField toDate;
    private JButton insertButton;
    private JComboBox selectTeamBox;
    private JComboBox selectLevelBox;
    private JComboBox selectNationBox;
    private JTable carreerTable;
    private JPanel admin_panel;

    /*public static void main(String[] args)
    {
        boolean internal_action = false;
        Player test = new Player();
        test.setName("Victor");
        test.setLastName("Osimhen");
        test.setId(1);
        new AddOrModifyCarreer(test, 'A');
    }*/

    public AddOrModifyCarreer(Player player) {
        setContentPane(panel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setSize(850, 850);
        if(Login.user_type == 'A') {
            admin_panel.setVisible(true);
            setTitle("SIC - Edit Carreer of " + player.getName() + " " + player.getLastName());
        }
        else {
            admin_panel.setVisible(false);
            setTitle("SIC - View Carreer of " + player.getName() + " " + player.getLastName());
        }

        Query query = new Query(carreerTable);
        List<String> nations = query.SelectAllNationsForTeams();
        List<String> levels = new ArrayList<String>();
        List<Team> teams = new ArrayList<Team>();
        for (var n : nations) {
            selectNationBox.addItem(n);
        }
        selectLevelBox.setEnabled(false);
        selectTeamBox.setEnabled(false);
        insertButton.setEnabled(false);

        query.select_player_career(player.getId());
        selectNationBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(selectNationBox.getSelectedIndex() != 0) {
                        if(selectLevelBox.getItemCount() > 0)
                            selectLevelBox.removeAllItems();
                        levels.clear();
                        levels.addAll(query.SelectLevelsFromNation(selectNationBox.getSelectedItem().toString()));
                        for (var l : levels) {
                            selectLevelBox.addItem(l);
                            selectLevelBox.setSelectedIndex(0);
                            selectLevelBox.setEnabled(true);
                        }
                    }
                    else {
                        selectLevelBox.setEnabled(false);
                        selectLevelBox.setSelectedIndex(0);
                    }
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        selectLevelBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(selectLevelBox.getSelectedIndex() != 0) {
                        if (selectTeamBox.getItemCount() > 0)
                            selectTeamBox.removeAllItems();
                        teams.clear();
                        teams.addAll(query.TeamsFromNationAndLevel(selectNationBox.getSelectedItem().toString(), Integer.parseInt(selectLevelBox.getSelectedItem().toString())));
                        for (var t : teams) {
                            selectTeamBox.addItem(t.getName());
                            selectTeamBox.setEnabled(true);
                        }
                    }
                    else {
                        selectTeamBox.setEnabled(false);
                        selectTeamBox.setSelectedIndex(0);
                    }
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        selectTeamBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(selectTeamBox.getSelectedIndex() != 0) {
                        insertButton.setEnabled(true);
                    }
                    else {
                        insertButton.setEnabled(false);
                    }
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(     selectTeamBox.getSelectedIndex() != 0
                        && ! fromDate.getText().isBlank() )
                {
                    Date from_date = new Date();
                    Date to_date = null;
                    SimpleDateFormat varDate = new SimpleDateFormat("MM/dd/yyyy");
                    try {
                        from_date = varDate.parse(fromDate.getText());
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                        return;
                    }
                    try {
                        if(! toDate.getText().isBlank()) {
                            to_date = new Date();
                            to_date = varDate.parse(toDate.getText());
                        }
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                        return;
                    }
                    int idTeam = -1;
                    for(var t : teams) {
                        if(t.getName().equals(selectTeamBox.getSelectedItem().toString()) ) {
                            idTeam = t.getId();
                            break;
                        }
                    }
                    var idPlayerRet = query.insert_player_team(player.getId(), idTeam, from_date, to_date);
                    if(idPlayerRet == -1) {
                        JOptionPane.showMessageDialog(null, "Errore, non è stato possibile inserire il record!", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Record inserito con successo!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        query.select_player_career(player.getId());
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "Errore, i campi contrassegnati con \"*\" sono obbligatori!", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

}