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
        List<User> users = query.selectUser("","",true);
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
                int choice = JOptionPane.showConfirmDialog(null, "Do yo want to make the " +  emailBox.getSelectedItem().toString() + " user administrator?", "Update user", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    boolean ok_ = query.updateUserType(emailBox.getSelectedItem().toString());
                    if(ok_) {
                        JOptionPane.showMessageDialog(null, "User type updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "error updating user type!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        deleteUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the selected user (" + emailBox.getSelectedItem().toString()  + ")?", "Delete user", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    var ok_ = query.deleteFromId("SIC_USER", "EMAIL", emailBox.getSelectedItem().toString());
                    if(ok_) {
                        JOptionPane.showMessageDialog(null, "User successfully deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Error: user was not deleted!", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                }
            }
        });
        editMyPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EditUserPassword(Login.user_email);
                dispose();
            }
        });
    }
}
