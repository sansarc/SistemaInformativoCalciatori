package Pages;

import javax.swing.*;

import DB.Query;
import Entity.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditUserPassword extends JFrame {
    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField newPasswordField2;
    private JButton changeButton;
    private JPanel panel;

    public EditUserPassword(String email) {
        setContentPane(panel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setSize(850, 500);

        Query query = new Query();
        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(oldPasswordField.getText().isBlank() || newPasswordField.getText().isBlank() || newPasswordField2.getText().isBlank()) {
                    JOptionPane.showMessageDialog(null, "Error: all fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if( ! newPasswordField.getText().equals(newPasswordField2.getText()) ) {
                    JOptionPane.showMessageDialog(null, "Error: the new passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    var ret = query.changePassword(email, oldPasswordField.getText(), newPasswordField.getText());
                    if(ret) {
                        JOptionPane.showMessageDialog(null, "Password updated successfully!", "Password Changed", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Error: the old password is incorrect!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }
}
