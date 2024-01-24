package GUI;

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
    private JButton addFeatureButton;
    private JButton changePasswordButton;
    private PlayerTransfer playerTransfer;
    private Player_Profile player;
    private PlayerFeature playerFeature;

    public Profile(int id, boolean editable) {
        setContentPane(panel);
        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        imageLabel.setText("");
        playerNameLabel.setText("");
        Query query = new Query();
        player = query.getPlayerProfileFromId(id);
        playerFeature = query.queryPlayerFeature(id, (Player) player);
        adminPanel.setVisible(editable);
        changePasswordButton.setVisible(Login.user_type == 'P' && editable);
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
                    showOptionsDialog(Profile.this, playerFeature.getFeatureList(), player.getId(), editable);
                }
            });
        }
        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EditUserPassword(Login.user_email);
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the player \"" + player.getName() + " " + player.getLastName() + "\"?", "Delete player", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    var ok_ = query.deleteFromId("PLAYER", "idplayer", player.getId());
                    if(ok_) {
                        JOptionPane.showMessageDialog(null, "Player successfully deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        if(Login.user_type == 'P') {
                            JOptionPane.showMessageDialog(null, "The player associated to this user has been deleted, therefore the user has also been deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            new Login();
                            dispose();
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Error: player was not deleted!", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                }
            }
        });

        editPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GUI.AddOrEditPlayer(player);
            }
        });
        showCarreerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GUI.AddOrEditCarreer(player);
            }
        });
        showPalmaresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GUI.ViewOrDeleteAwardFromPlayer(player);
            }
        });
        addFeatureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GUI.AddPlayerFeature(player);
            }
        });
    }

    private static void showOptionsDialog(JFrame component, List<Feature> features, int idPlayer, boolean editable) {
        String[] options = new String[features.size()];
        for (int i = 0; i < features.size(); i++) {
            options[i] = features.get(i).getName();
        }

        int selectedOption = JOptionPane.showOptionDialog(
                component,
                (editable) ? "What feature do you want to delete?" : "\n" + "Which feature would you like to view the description for?",
                (editable) ? "Delete Feature" : "Feature Description",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (selectedOption != JOptionPane.CLOSED_OPTION) {
            if(editable) {
                Query query = new Query();
                var ok_ = query.deletePlayerFeature(features.get(selectedOption).getName(), idPlayer);
                if(ok_) {
                    JOptionPane.showMessageDialog(null, "Feature successfully deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(null, "Error: feature was not deleted!", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
            else {
                JOptionPane.showMessageDialog(component, features.get(selectedOption).getDescription());
            }
        }
    }
}