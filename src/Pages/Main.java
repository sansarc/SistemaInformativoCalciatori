package Pages;

import DB.Query;
import DB.QueryTools;
import Entity.Player;
import Entity.Player_Profile;
import Entity.Team;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

// TODO: profilo militanza del giocatore quando si clicca sull'ID dalla tabella di ricerca
// TODO: sistema login e profili
// TODO: sistema inserimento e modifica dati in base al profilo
// TODO: per identificare il login nel Main, aggiungere "Ciao, utente!"?. Si deve passare come argomento anche il nome utente nella classe

public class Main extends JFrame {
    private JPanel panel;
    private JButton searchButton;
    private JTable resultsTable;
    private JScrollPane tableScrollPane;
    private JTextField nameTextField;
    private JLabel nameLabel;
    private JTextField lastnameTextField;
    private JLabel lastNameLabel;
    private JLabel footLabel;
    private JComboBox<String> footComboBox;
    private JCheckBox retiredCheckBox;
    private JComboBox<Character> ageComboBox;
    private JCheckBox forwardCheckBox;
    private JCheckBox midfielderCheckBox;
    private JCheckBox defenderCheckBox;
    private JCheckBox goalkeeperCheckBox;
    private JButton clearButton;
    private JPanel adminPanel;
    private JLabel userGreetLabel;
    private JButton logoutButton;
    private JComboBox nationComboBox;
    private JComboBox levelComboBox;
    private JComboBox teamComboBox;
    private JPanel TeamPanel;
    private JPanel positionPanel;
    private JSpinner ageSpinner;
    private JComboBox goalsComboBox;
    private JSpinner goalsSpinner;
    private JComboBox featureComboBox;
    private JPanel goalsScoredPanel;
    private JPanel agePanel;
    private JComboBox concededComboBox;
    private JSpinner concededSpinner;
    private JComboBox apparencesComboBox;
    private JSpinner apparencesSpinner;
    private JCheckBox freeagentCheckBox;
    private JPanel filterPanel;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private JButton button7;
    private Query query;
    List<Integer> ids;

    public Main(String username) {
        setContentPane(panel);
        setTitle("Sistema Informativo Calciatori");
        setSize(1100, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        //adminPanel.setVisible(Login.user_type == 'A');
        userGreetLabel.setText("<html> Hi, <b>" + username + "</b>! </html>");
        concededSpinner.setEnabled(false);
        String[] footOpt = {"\0", "Left", "Right", "Ambidextrous"};
        for (String i : footOpt) footComboBox.addItem(i);
        char[] comboBoxOperator = {'\0','=', '>', '<'};
        for (char i : comboBoxOperator) {
            ageComboBox.addItem(i);
            goalsComboBox.addItem(i);
            concededComboBox.addItem(i);
            apparencesComboBox.addItem(i);
        }
        query = new Query(resultsTable);
        List<String> nations = query.SelectAllNationsForTeams();
        List<String> levels = new ArrayList<String>();
        List<Team> teams = new ArrayList<Team>();
        for (var n : nations) {
            nationComboBox.addItem(n);
        }
        levelComboBox.setEnabled(false);
        teamComboBox.setEnabled(false);
        List<String> features = new ArrayList<String>();
        features.add("\0");
        features.addAll(query.selectAllFeatures());
        for(var f : features) {
            featureComboBox.addItem(f);
        }
        concededComboBox.setEnabled(false);
        concededSpinner.setEnabled(false);
        ageSpinner.setEnabled(false);
        apparencesSpinner.setEnabled(false);
        goalsSpinner.setEnabled(false);
        goalsComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(goalsComboBox.getSelectedIndex() > 0) {
                    goalsSpinner.setEnabled(true);
                }
                else {
                    goalsSpinner.setValue(0);
                    goalsSpinner.setEnabled(false);
                }
            }
        });
        concededComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(concededComboBox.getSelectedIndex() > 0) {
                    concededSpinner.setEnabled(true);
                }
                else {
                    concededSpinner.setValue(0);
                    concededSpinner.setEnabled(false);
                }
            }
        });
        apparencesComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(apparencesComboBox.getSelectedIndex() > 0) {
                    apparencesSpinner.setEnabled(true);
                }
                else {
                    apparencesSpinner.setValue(0);
                    apparencesSpinner.setEnabled(false);
                }
            }
        });
        ageComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(ageComboBox.getSelectedIndex() > 0) {
                    ageSpinner.setEnabled(true);
                }
                else {
                    ageSpinner.setValue(0);
                    ageSpinner.setEnabled(false);
                }
            }
        });
        freeagentCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(freeagentCheckBox.isSelected()) {
                    retiredCheckBox.setEnabled(false);
                    nationComboBox.setEnabled(false);
                }
                else {
                    retiredCheckBox.setEnabled(true);
                    nationComboBox.setEnabled(true);
                }
            }
        });
        retiredCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(retiredCheckBox.isSelected()) {
                    freeagentCheckBox.setEnabled(false);
                    nationComboBox.setEnabled(false);
                }
                else {
                    freeagentCheckBox.setEnabled(true);
                    nationComboBox.setEnabled(true);
                }
            }
        });
        nationComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                QueryTools.selectNationTool(nationComboBox, levelComboBox, levels);
                retiredCheckBox.setEnabled(!levelComboBox.isEnabled());
                freeagentCheckBox.setEnabled(!levelComboBox.isEnabled());
            }
        });
        levelComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                QueryTools.selectLevelTool(nationComboBox, levelComboBox, teamComboBox, teams);
            }
        });
        teamComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                QueryTools.selectTeamTool(teamComboBox);
            }
        });
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

                    concededComboBox.setEnabled(true);
                }
                else
                {
                    forwardCheckBox.setEnabled(true);
                    midfielderCheckBox.setEnabled(true);
                    defenderCheckBox.setEnabled(true);
                    concededComboBox.setEnabled(false);
                    concededSpinner.setValue(0);
                    concededSpinner.setEnabled(false);
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

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> positions = getCheckedPositions();
                String name = nameTextField.getText();
                String lastname = lastnameTextField.getText();
                char foot;
                if (footComboBox.getSelectedItem().equals("Right")) foot = 'R';
                else if (footComboBox.getSelectedItem().equals("Left")) foot = 'L';
                else if (footComboBox.getSelectedItem().equals("Ambidextrous")) foot = 'A';
                else foot = '\0';
                char ageMath = (char) ageComboBox.getSelectedItem();
                String age = ageSpinner.getValue().toString();
                boolean isRetired = retiredCheckBox.isSelected();
                String team = "";//teamTextField.getText();


                Player_Profile player = new Player_Profile(name, lastname, null, null, null, foot, Integer.parseInt(goalsSpinner.getValue().toString()), Integer.parseInt(concededSpinner.getValue().toString()), Integer.parseInt(apparencesSpinner.getValue().toString()));
                //player.setGoals();
                //player.setGoalsConceded();
                //player.setApparences();
                ids = query.queryPlayers(player,
                    new Team(
                    (teamComboBox.getSelectedIndex() > 0) ? teamComboBox.getSelectedItem().toString() : "",
                    (nationComboBox.getSelectedIndex() > 0) ? nationComboBox.getSelectedItem().toString() : "",
                    (levelComboBox.getSelectedIndex() > 0) ? Integer.parseInt(levelComboBox.getSelectedItem().toString()) : -1, -1),
                    freeagentCheckBox.isSelected(), retiredCheckBox.isSelected(), positions, goalsComboBox.getSelectedItem().toString().charAt(0),
                    concededComboBox.getSelectedItem().toString().charAt(0), apparencesComboBox.getSelectedItem().toString().charAt(0), ageComboBox.getSelectedItem().toString().charAt(0),
                    Integer.parseInt(ageSpinner.getValue().toString()), (featureComboBox.getSelectedIndex() > 0) ? featureComboBox.getSelectedItem().toString() : "", true);
                    //ids = query.queryPlayers(name, lastname, ageMath, age, positions, foot, isRetired, team, true);
            }
        });
        //select pc.idPlayer, pc.name, pc.lastname from player_carreer pc where
        //if name -> pc.player_name LIKE '%?%'
        //if lastname -> pc.lastname LIKE '%?%'
        //if free agent ->
        //else if(retired) retireddate is not null
        //else if(team) -> join teams t -- t.name = '?', t.nation = '?' t.enddate is null
        //if leveOfTeam -> join teams t -- t.nation = ?, t.level = ? t.enddate is null
        //if(positions) ->
        //if goalscsored/concedd/apparences -> sum(...) > ?
        //if(foot) substring 0,1 foot = ?
        //if age //già fatto
        //if features -> join player_features f on f.idplayer = pc.idplayer -- f.idFeatures = ?
        resultsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = resultsTable.rowAtPoint(e.getPoint());
                int col = resultsTable.columnAtPoint(e.getPoint());
                new Profile(ids.get(row));
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nameTextField.setText("");
                lastnameTextField.setText("");
                //teamTextField.setText("");
                forwardCheckBox.setSelected(false);
                midfielderCheckBox.setSelected(false);
                defenderCheckBox.setSelected(false);
                goalkeeperCheckBox.setSelected(false);
                footComboBox.setSelectedIndex(0);
                //ageTextField.setText("");
                retiredCheckBox.setSelected(false);
                ageComboBox.setSelectedIndex(0);
            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Login();
                dispose();
            }
        });
    }

    private List<String> getCheckedPositions() {
        List<String> positions = new ArrayList<>();
        if (forwardCheckBox.isSelected()) positions.add("F");
        if (midfielderCheckBox.isSelected()) positions.add("M");
        if (defenderCheckBox.isSelected()) positions.add("D");
        if (goalkeeperCheckBox.isSelected()) positions.add("G");
        return positions;
    }

    private int isFormOk(List<String> position, String name, String lastname, char foot, String age, Boolean isRetired, String team) {
        if (position.isEmpty() && name.isBlank() && lastname.isBlank() && age.isEmpty() && foot == '\0' && !isRetired && team.isBlank()) return 1;
        else if (!age.isEmpty() && Character.isLetter(age.charAt(0))) return 2;
        return 0;
    }

    public static boolean showMessagePanel(JFrame component, int code) {
        if (code == 1) JOptionPane.showMessageDialog(component, "Invalid search, form can't be empty", "Invalid Search", JOptionPane.WARNING_MESSAGE);
        else if (code == 2) JOptionPane.showMessageDialog(component, "Age field has to be a number", "Invalid Search", JOptionPane.WARNING_MESSAGE);
        return code == 0;
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
