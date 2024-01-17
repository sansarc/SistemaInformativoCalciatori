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

public class AddOrEditCarreer extends JFrame {
    private JPanel panel;
    private JTextField fromDate;
    private JTextField toDate;
    private JButton insertButton;
    private JComboBox selectTeamBox;
    private JComboBox selectLevelBox;
    private JComboBox selectNationBox;
    private JTable carreerTable;
    private JPanel admin_panel;
    private JSpinner scorespinner;
    private JPanel goalkeeperPanel;
    private JSpinner concededspinner;
    private JSpinner apparencesSpinner;

    public AddOrEditCarreer(Player player) {
        setContentPane(panel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setSize(850, 500);
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
        goalkeeperPanel.setVisible(player.getPosition().contains("G"));
        query.select_player_carreer(player.getId(), goalkeeperPanel.isVisible());
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
                insertButton.setEnabled(QueryTools.selectTeamTool(selectTeamBox));
            }
        });
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(     selectTeamBox.getSelectedIndex() != 0
                        && ! fromDate.getText().isBlank())
                {
                    if(Integer.parseInt(scorespinner.getValue().toString()) < 0 || Integer.parseInt(concededspinner.getValue().toString()) < 0 || Integer.parseInt(apparencesSpinner.getValue().toString()) < 0 ) {
                        JOptionPane.showMessageDialog(null, "Errore, i goal e le presenze non possono essere negativi!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    else if( (Integer.parseInt(scorespinner.getValue().toString()) > 0 || Integer.parseInt(concededspinner.getValue().toString()) > 0) && Integer.parseInt(apparencesSpinner.getValue().toString()) == 0 ) {
                        JOptionPane.showMessageDialog(null, "Errore, se le presenze sono 0 non possono esserci goal subiti o segnati!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
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
                    int transferId = -1;
                    transferId = query.insert_player_team(player.getId(), idTeam, from_date, to_date, Integer.parseInt(scorespinner.getValue().toString()), Integer.parseInt(concededspinner.getValue().toString()), goalkeeperPanel.isVisible(), Integer.parseInt(apparencesSpinner.getValue().toString()));
                    if(transferId == -1) {
                        JOptionPane.showMessageDialog(null, "Errore, non è stato possibile inserire il record!", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Record inserito con successo!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        query.select_player_carreer(player.getId(), goalkeeperPanel.isVisible());
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "Errore, i campi contrassegnati con \"*\" sono obbligatori!", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

}