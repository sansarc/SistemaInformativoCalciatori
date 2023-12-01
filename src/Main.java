import Calciatore.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Main extends JFrame {
    private JPanel panel;
    private JLabel ruoloLabel;
    private JComboBox<String> ruoliComboBox;
    private JLabel titleLabel;
    private JButton searchButton;
    private JTable resultsTable;
    private JScrollPane tableScrollPane;

    public Main() {
        setContentPane(panel);
        setTitle("Sistema Informativo Calciatori");
        pack();
        setDefaultCloseOperation(HIDE_ON_CLOSE);     // TODO: change it to EXIT_ON_CLOSE
        setLocationRelativeTo(null);
        setVisible(true);

        String[] ruoli = new String[] {"Seleziona un ruolo", "Forward", "Midfielder", "Defender", "Goalkeeper"};
        for (String i : ruoli) ruoliComboBox.addItem(i);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ruolo = (String) ruoliComboBox.getSelectedItem();
                Query query = new Query(resultsTable);
                boolean state = query.getCalciatoriByRuolo(ruolo);
            }
        });

        ruoliComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ruolo = (String) ruoliComboBox.getSelectedItem();
                searchButton.setEnabled(!ruolo.equals("Seleziona un ruolo"));
            }
        });
    }


    public static void main(String[] args) {
        new Main();
    }
}
