package Pages;

import Entity.*;
import DB.Query;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JLabel goalsConcededLabel;
    private JLabel footLabel;
    private JLabel featureLabel;
    private JButton showCarreerButton;
    private JButton editPlayerButton;
    private JButton deleteButton;
    private JPanel adminPanel;
    private JButton showPalmaresButton;
    private PlayerTransfer playerTransfer;
    private Player_Profile player;
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
        player = query.GetPlayerProfileFromId(id);
        playerFeature = query.queryPlayerFeature(id, (Player) player);
        adminPanel.setVisible(Login.user_type == 'A');
        setTitle(player.getName() + " " + player.getLastName());
        try {
            imageLabel.setIcon(new ImageIcon(player.getImage()));
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        playerNameLabel.setText(playerNameLabel.getText() + " " + player.getName() + " " + player.getLastName());
        birthDateLabel.setText(birthDateLabel.getText() + " " + player.getBirthDate().toString().substring(5, 7) + "/" + player.getBirthDate().toString().substring(8, 10)  + "/" + player.getBirthDate().toString().substring(0, 4) + " (" + player.getAge(true) + ")");
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
        if (player.getPosition().contains("G")) {
            goalsConcededLabel.setText(goalsConcededLabel.getText() + " " + player.getGoalsConceded());
            goalsConcededLabel.setVisible(true);
        }
        footLabel.setText(footLabel.getText() + " " + player.getFootString());
        if(!playerFeature.getFeatureList().isEmpty()) {
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
        }
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "Esiste già un calciatore corrispondente ai dati inseriti, si vuole proseguire comunque?", "Calciatore già presente", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    /*boolean r =*/ query.DeleteFromId("PLAYERS", "idplayer", player.getId());
                    /*if(r) {
                        JOptionPane.showMessageDialog(null, "Calciatore eliminato", "Invalid Search", JOptionPane.WARNING_MESSAGE);
                        dispose();
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Calciatore non eliminato", "Invalid Search", JOptionPane.WARNING_MESSAGE);
                        dispose();
                    }*/
                }

            }
        });

        editPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Pages.AddOrEditPlayer(player);
            }
        });
        showCarreerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Pages.AddOrEditCarreer(player);
            }
        });
        showPalmaresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Pages.ViewOrDeleteAwardFromPlayer(player);
            }
        });
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