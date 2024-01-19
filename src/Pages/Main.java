package Pages;

import DB.Query;
import DB.QueryTools;
import Entity.Player_Profile;
import Entity.Team;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

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
    private JLabel userGreetLabel;
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
    private JButton logoutButton;
    private JButton editProfileButton;
    private JPanel adminPanel;
    private JButton addPlayerButton;
    private JButton addEditFeaturesButton;
    private JButton addTeamButton;
    private JButton editTeamButton;
    private JButton addAwardsButton;
    private Query query;
    List<Integer> ids;

    public Main(String username) {
        setContentPane(panel);
        setTitle("Sistema Informativo Calciatori");
        setSize(1100, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        adminPanel.setVisible(Login.user_type == 'A');
        userGreetLabel.setText("<html> Hi, <b>" + username + "</b>! </html>");
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
        List<String> features = new ArrayList<String>();
        features.add("\0");
        features.addAll(query.selectAllFeatures());
        for(var f : features) {
            featureComboBox.addItem(f);
        }
        InitFilter();
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
                if(isFormOk()) {
                    List<String> positions = getCheckedPositions();
                    String name = nameTextField.getText();
                    String lastname = lastnameTextField.getText();
                    char foot = (footComboBox.getSelectedIndex() > 0) ? footComboBox.getSelectedItem().toString().charAt(0) : '\0';
                    Player_Profile player = new Player_Profile(name, lastname, null, null, null, foot, Integer.parseInt(goalsSpinner.getValue().toString()), Integer.parseInt(concededSpinner.getValue().toString()), Integer.parseInt(apparencesSpinner.getValue().toString()));
                    ids = query.queryPlayers(player,
                            (nationComboBox.getSelectedIndex() > 0) ? new Team(
                                    (teamComboBox.getSelectedIndex() > 0) ? teamComboBox.getSelectedItem().toString() : "",
                                    nationComboBox.getSelectedItem().toString(),
                                    (levelComboBox.getSelectedIndex() > 0) ? Integer.parseInt(levelComboBox.getSelectedItem().toString()) : -1, -1) : null,
                            freeagentCheckBox.isSelected(), retiredCheckBox.isSelected(), positions, goalsComboBox.getSelectedItem().toString().charAt(0),
                            concededComboBox.getSelectedItem().toString().charAt(0), apparencesComboBox.getSelectedItem().toString().charAt(0), ageComboBox.getSelectedItem().toString().charAt(0),
                            Integer.parseInt(ageSpinner.getValue().toString()), (featureComboBox.getSelectedIndex() > 0) ? featureComboBox.getSelectedItem().toString() : "", true);
                }
            }
        });
        addPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddOrEditPlayer(null);
            }
        });
        addTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddTeam();
            }
        });
        editTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EditTeam();
            }
        });
        addAwardsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddAwards();
            }
        });
        addEditFeaturesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
            }
        });
        resultsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = resultsTable.rowAtPoint(e.getPoint());
                //int col = resultsTable.columnAtPoint(e.getPoint());
                new Profile(ids.get(row));
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InitFilter();
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

    private boolean isFormOk() {
        int ret;
        if (nameTextField.getText().isBlank() && lastNameLabel.getText().isBlank()
                && teamComboBox.getSelectedIndex() < 1 && levelComboBox.getSelectedIndex() < 1 && nationComboBox.getSelectedIndex() < 1
                && !retiredCheckBox.isSelected()  && !freeagentCheckBox.isSelected()
                && getCheckedPositions().isEmpty()
                && !goalsSpinner.isEnabled() && !concededSpinner.isEnabled() && !apparencesSpinner.isEnabled()
                && footComboBox.getSelectedIndex() < 1 && !ageSpinner.isEnabled() && featureComboBox.getSelectedIndex() < 1
        ) ret = 1;
        else if (Integer.parseInt(ageSpinner.getValue().toString()) < 0) ret = 2;
        else if (Integer.parseInt(goalsSpinner.getValue().toString()) < 0) ret = 3;
        else if (Integer.parseInt(concededSpinner.getValue().toString()) < 0) ret = 4;
        else if (Integer.parseInt(apparencesSpinner.getValue().toString()) < 0) ret = 5;
        else ret = 0;
        return showMessagePanel(ret);
    }

    private boolean showMessagePanel(int code) {
        if (code == 1) JOptionPane.showMessageDialog(null, "Invalid search, form can't be empty", "Invalid Search", JOptionPane.WARNING_MESSAGE);
        else if (code == 2) JOptionPane.showMessageDialog(null, "Age spinner has to be a not negative number", "Invalid Search", JOptionPane.WARNING_MESSAGE);
        else if (code == 3) JOptionPane.showMessageDialog(null, "Goals scored spinner has to a not negative number", "Invalid Search", JOptionPane.WARNING_MESSAGE);
        else if (code == 4) JOptionPane.showMessageDialog(null, "Goals conceded field has to a not negative number", "Invalid Search", JOptionPane.WARNING_MESSAGE);
        else if (code == 5) JOptionPane.showMessageDialog(null, "Apparences has to be a not negative number", "Invalid Search", JOptionPane.WARNING_MESSAGE);
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
    private void InitFilter() {
        nameTextField.setText("");
        lastNameLabel.setText("");
        retiredCheckBox.setSelected(false);
        freeagentCheckBox.setSelected(false);
        forwardCheckBox.setSelected(false);
        midfielderCheckBox.setSelected(false);
        defenderCheckBox.setSelected(false);
        goalkeeperCheckBox.setSelected(false);
        goalsComboBox.setSelectedIndex(0);
        concededComboBox.setSelectedIndex(0);
        apparencesComboBox.setSelectedIndex(0);
        footComboBox.setSelectedIndex(0);
        ageComboBox.setSelectedIndex(0);
        featureComboBox.setSelectedIndex(0);
        nationComboBox.setSelectedIndex(0);
        freeagentCheckBox.setEnabled(true);
        retiredCheckBox.setEnabled(true);
        concededSpinner.setEnabled(false);
        levelComboBox.setEnabled(false);
        teamComboBox.setEnabled(false);
        concededComboBox.setEnabled(false);
        concededSpinner.setEnabled(false);
        ageSpinner.setEnabled(false);
        apparencesSpinner.setEnabled(false);
        goalsSpinner.setEnabled(false);
        goalkeeperCheckBox.setEnabled(true);
        defenderCheckBox.setEnabled(true);
        midfielderCheckBox.setEnabled(true);
        forwardCheckBox.setEnabled(true);

    }

}