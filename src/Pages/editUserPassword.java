package Pages;

import javax.swing.*;

import DB.Query;
import Entity.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class editUserPassword extends JFrame {
    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField newPasswordField2;
    private JButton changeButton;
    private JPanel panel;

    public editUserPassword(User user) {
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
                    //tutti i campi sono obbligatori
                }
                else if( ! newPasswordField.getText().equals(newPasswordField2.getText()) ) {
                    //new password non combaciano
                }
                else {
                    var ret = query.ChangePassword(user.getEmail(), oldPasswordField.getText(), newPasswordField.getText());
                    if(ret) {
                        //ok!
                    }
                    else {
                        //password errata!
                    }
                }
            }
        });
    }
}
