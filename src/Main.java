import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    public Main() {
        setContentPane(panel);
        setTitle("Sistema Informativo Calciatori");
        setSize(900, 600);
        setDefaultCloseOperation(HIDE_ON_CLOSE);     // TODO: cambia a EXIT_ON_CLOSE
        setLocationRelativeTo(null);
        setVisible(true);

        String[] positions = new String[] {"", "Forward", "Midfielder", "Defender", "Goalkeeper"};
        for (String i : positions) positionComboBox.addItem(i);
        footComboBox.addItem("");
        footComboBox.addItem("Dx");
        footComboBox.addItem("Sx");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String position = (String) positionComboBox.getSelectedItem();
                String name = nameTextField.getText();
                String lastname = lastnameTextField.getText();
                char foot = footComboBox.getSelectedItem().equals("Dx") ? 'D' : 'S';
                String age = ageTextField.getText();
                boolean isRetired = retiredCheckBox.isSelected();

                if (position.isBlank() && name.isBlank() && lastname.isBlank() && foot == '\0' && !isRetired) JOptionPane.showMessageDialog(Main.this, "Ricerca non valida, riprova.");
                else {
                    Query query = new Query(resultsTable);
                    query.getSearch(name, lastname, age, position, foot, isRetired);        // TODO: aggiungere campo Squadra
                }
            }
        });
    }

    public static void main(String[] args) {
        new Main();
    }
}
