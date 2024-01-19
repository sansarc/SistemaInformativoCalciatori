package Pages;

import DB.Query;
import Entity.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class EditUser extends JFrame {
    private JComboBox emailBox;
    private JButton editUserTypeButton;
    private JButton editMyPasswordButton;
    private JButton deleteUserButton;
    private JPanel panel;

    public EditUser() {
        setContentPane(panel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setSize(850, 500);

        Query query = new Query();
        List<User> users = query.SelectUser("","",true);
        emailBox.addItem("");
        for (var u : users) {
            emailBox.addItem(u.getEmail());
        }
        editUserTypeButton.setEnabled(false);
        deleteUserButton.setEnabled(false);
        emailBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(emailBox.getSelectedIndex() > 0) {
                    editUserTypeButton.setEnabled(true);
                    deleteUserButton.setEnabled(true);
                }
                else {
                    editUserTypeButton.setEnabled(false);
                    deleteUserButton.setEnabled(false);
                }
            }
        });
        editUserTypeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                query.updateUserType(emailBox.getSelectedItem().toString());
            }
        });
        deleteUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                query.DeleteFromId("SIC_USER", "EMAIL", emailBox.getSelectedItem().toString());
            }
        });
    }
}