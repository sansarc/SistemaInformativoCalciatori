import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.MathContext;

public class Main extends JFrame {
    private JPanel panel;
    private JLabel positionLabel;
    private JComboBox<String> positionComboBox;
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

    public Main() {
        setContentPane(panel);
        setTitle("Sistema Informativo Calciatori");
        setSize(900, 600);
        setDefaultCloseOperation(HIDE_ON_CLOSE);     // TODO: cambia a EXIT_ON_CLOSE
        setLocationRelativeTo(null);
        setVisible(true);

        String[] positions = new String[] {"", "Forward", "Midfielder", "Defender", "Goalkeeper"};
        for (String i : positions) positionComboBox.addItem(i);
        footComboBox.addItem("\0");
        footComboBox.addItem("Dx");
        footComboBox.addItem("Sx");
        char[] ageMath = {'=', '>', '<'};
        for (char i : ageMath) ageComboBox.addItem(i);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String position = (String) positionComboBox.getSelectedItem();
                String name = nameTextField.getText();
                String lastname = lastnameTextField.getText();
                char foot;
                if (footComboBox.getSelectedItem().equals("Dx")) foot = 'D';
                else if (footComboBox.getSelectedItem().equals("Sx")) foot = 'S';
                else foot = '\0';
                char ageMath = (char) ageComboBox.getSelectedItem();
                String age = ageTextField.getText();
                boolean isRetired = retiredCheckBox.isSelected();
                System.out.println(isRetired);
                String team = teamTextField.getText();

                if (isFormEmpty(position, name, lastname, foot, age, isRetired, team)) JOptionPane.showMessageDialog(Main.this, "Ricerca non valida, riprova.");
                else {
                    Query query = new Query(resultsTable);
                    query.getSearch(name, lastname, ageMath, age, position, foot, isRetired, team);
                }
            }
        });

        resultsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = resultsTable.rowAtPoint(e.getPoint());
                int col = resultsTable.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 1) JOptionPane.showMessageDialog(Main.this, "Valore cliccato");
            }
        });
    }

    private boolean isFormEmpty(String position, String name, String lastname, char foot, String age, Boolean isRetired, String team) {
        return (position.isBlank() && name.isBlank() && lastname.isBlank() && age.isBlank() && foot == '\0' && !isRetired && team.isBlank());
    }

    public static void main(String[] args) {
        new Main();
    }
}
