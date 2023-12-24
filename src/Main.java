import DB.Query;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

// TODO: profilo militanza del giocatore quando si clicca sull'ID dalla tabella di ricerca
// TODO: sistema login e profili
// TODO: sistema inserimento e modifica dati in base al profilo

public class Main extends JFrame {
    private JPanel panel;
    private JLabel positionLabel;
    private JButton searchButton;
    private JTable resultsTable;
    private JScrollPane tableScrollPane;
    private JTextField nameTextField;
    private JLabel nameLabel;
    private JTextField lastnameTextField;
    private JLabel lastNameLabel;
    private JLabel footLabel;
    private JComboBox<String> footComboBox;
    private JLabel ageLabel;
    private JTextField ageTextField;
    private JCheckBox retiredCheckBox;
    private JLabel teamLabel;
    private JTextField teamTextField;
    private JComboBox<Character> ageComboBox;
    private JCheckBox forwardCheckBox;
    private JCheckBox midfielderCheckBox;
    private JCheckBox defenderCheckBox;
    private JCheckBox goalkeeperCheckBox;
    private JButton clearButton;
    private Query query;
    List<Integer> ids;

    public Main() {
        setContentPane(panel);
        setTitle("Sistema Informativo Calciatori");
        setSize(1100, 600);
        setDefaultCloseOperation(HIDE_ON_CLOSE);     // TODO: cambia a EXIT_ON_CLOSE
        setLocationRelativeTo(null);
        setVisible(true);

        String[] footOpt = {"\0", "Left", "Right", "Ambidextrous"};
        for (String i : footOpt) footComboBox.addItem(i);
        char[] ageOpt = {'=', '>', '<'};
        for (char i : ageOpt) ageComboBox.addItem(i);

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
                String age = ageTextField.getText();
                boolean isRetired = retiredCheckBox.isSelected();
                String team = teamTextField.getText();

                if (showMessagePanel(Main.this, isFormOk(positions, name, lastname, foot, age, isRetired, team))) {
                    query = new Query(resultsTable);
                    ids = query.queryPlayers(name, lastname, ageMath, age, positions, foot, isRetired, team);
                }
            }
        });

        resultsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = resultsTable.rowAtPoint(e.getPoint());
                int col = resultsTable.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 3) new Profile(ids.get(row));
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nameTextField.setText("");
                lastnameTextField.setText("");
                teamTextField.setText("");
                forwardCheckBox.setSelected(false);
                midfielderCheckBox.setSelected(false);
                defenderCheckBox.setSelected(false);
                goalkeeperCheckBox.setSelected(false);
                footComboBox.setSelectedIndex(0);
                ageTextField.setText("");
                retiredCheckBox.setSelected(false);
                ageComboBox.setSelectedIndex(0);
            }
        });
    }

    private List<String> getCheckedPositions() {
        List<String> positions = new ArrayList<>();
        if (forwardCheckBox.isSelected()) positions.add("Forward");
        if (midfielderCheckBox.isSelected()) positions.add("Midfielder");
        if (defenderCheckBox.isSelected()) positions.add("Defender");
        if (goalkeeperCheckBox.isSelected()) positions.add("Goalkeeper");
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

    public static void main(String[] args) {
        new Main();
    }
}
