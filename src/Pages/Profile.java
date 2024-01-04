package Pages;

import Entity.*;
import DB.Query;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class Profile extends JFrame {
    private JPanel panel;
    private JLabel playerNameLabel;
    private JLabel imageLabel;
    private JLabel birthDateLabel;
    private JLabel positionLabel;
    private JLabel goalsLabel;
    private JScrollPane tableScrollPane;
    private JTable transferTable;
    private JLabel goalsConcededLabel;
    private JLabel footLabel;
    private JLabel featureLabel;
    private PlayerTransfer playerTransfer;
    private Player player;
    private PlayerFeature playerFeature;

    public Profile(int id) {
        setContentPane(panel);
        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        imageLabel.setText("");
        playerNameLabel.setText("");
        Query query = new Query();
        playerTransfer = query.queryPlayerProfile(id);
        player = playerTransfer.getPlayer();
        playerFeature = query.queryPlayerFeature(id);

        setTitle(player.getName() + " " + player.getLastName());
        imageLabel.setIcon(new ImageIcon(player.getImage()));
        playerNameLabel.setText(playerNameLabel.getText() + " " + player.getName() + " " + player.getLastName());
        birthDateLabel.setText(birthDateLabel.getText() + " " + player.getDateOfBirth().toString().substring(5, 7) + "/" + player.getDateOfBirth().toString().substring(8, 10)  + "/" + player.getDateOfBirth().toString().substring(0, 4) + " (" + player.getAge(true) + ")");
        var positions = player.getPosition().split(",");
        for(var pst : positions)
        {
            switch(pst)
            {
                case "G":
                    positionLabel.setText(positionLabel.getText() + " Goalkeeper");
                    break;
                case "D":
                    positionLabel.setText(positionLabel.getText() + " Defender");
                    break;
                case "M":
                    positionLabel.setText(positionLabel.getText() + " Midfielder");
                    break;
                case "F":
                    positionLabel.setText(positionLabel.getText() + " Forward");
                    break;
            }
        }
        goalsLabel.setText(goalsLabel.getText() + " " + player.getGoals());
        if (player.getPosition().contains("Goalkeeper")) {
            goalsConcededLabel.setText(goalsConcededLabel.getText() + " " + player.getGoalsConceded());
            goalsConcededLabel.setVisible(true);
        }
        footLabel.setText(footLabel.getText() + " " + player.getFootString());
        featureLabel.setText("<html><u>" + featureLabel.getText() + "</u>" + ": " + playerFeature.getFeature(0).getName());
        if (playerFeature.getFeatureList().size() > 1) {
            for (int i = 1; i < playerFeature.getFeatureList().size(); i++) {
                featureLabel.setText(featureLabel.getText() + ", " + playerFeature.getFeature(i).getName());
            }
        }
        featureLabel.setText(featureLabel.getText() + "</html>");

        featureLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showOptionsDialog(Profile.this, playerFeature.getFeatureList());
            }
        });

        DefaultTableModel transferTableModel = new DefaultTableModel();
        transferTableModel.addColumn("Date");
        transferTableModel.addColumn("Left");
        transferTableModel.addColumn("Joined");

        for (int i = 0; i < playerTransfer.getStartList().size(); i++) {
            String left = "";
            if (i > 0) {
                left = playerTransfer.getTeam(i - 1).getName();
                if(!left.isBlank())
                    left = left.substring(5, 7) + "/" + left.substring(8, 10) + "/" + left.substring(0, 4);
            }
            Object[] rowData = {
                    playerTransfer.getStart(i).toString().substring(5, 7) + "/" + playerTransfer.getStart(i).toString().substring(8, 10) + "/" + playerTransfer.getStart(i).toString().substring(0, 4),
                    left,
                    playerTransfer.getTeam(i).getName()
            };
            transferTableModel.addRow(rowData);
        }
        transferTable.setModel(transferTableModel);
        transferTable.setEnabled(false);
    }

    private static void showOptionsDialog(JFrame component, List<Feature> features) {
        String[] options = new String[features.size()];
        for (int i = 0; i < features.size(); i++) {
            options[i] = features.get(i).getName();
        }

        int selectedOption = JOptionPane.showOptionDialog(
                component,
                "What feature do you want to be described?",
                "Feature Description",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (selectedOption != JOptionPane.CLOSED_OPTION) {
            JOptionPane.showMessageDialog(component, features.get(selectedOption).getDescription());
        }
    }
}