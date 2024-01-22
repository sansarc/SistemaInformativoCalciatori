package Pages;

import DB.Query;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class Login extends JFrame {
    private JPanel panel;
    private JPasswordField password;
    private JTextField username;
    private JButton signupButton;
    private JButton loginButton;
    private Query query;
    static char user_type;
    static String user_email;

    public Login() {
        setContentPane(panel);
        setTitle("Log In SIC");
        setSize(220, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String u_name = username.getText();
                String pwd = password.getText();
                if (blankCredentials(u_name, pwd)) return;
                query = new Query();
                var ut = query.login(u_name, pwd);
                if (ut == '0')
                {
                    JOptionPane.showMessageDialog(null, "Internal Error!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if (ut == '1')
                {
                    JOptionPane.showMessageDialog(null, "Wrong Credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    user_email = u_name;
                    user_type = ut;
                    new Main();
                    dispose();
                }
            }
        });
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String u_name = username.getText();
                String pwd = password.getText();
                if (blankCredentials(u_name, pwd)) return;
                query = new Query();
                var resultCreateUser = query.createUser(u_name, pwd);
                if(resultCreateUser)
                    JOptionPane.showMessageDialog(null, "User created successfully.", "Sign up", JOptionPane.INFORMATION_MESSAGE);
                else
                    JOptionPane.showMessageDialog(null, "This email is already in use. Please choose a different one.", "Sign up", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private boolean blankCredentials(String u_name, String pwd) {
        if (u_name.isBlank() || pwd.isBlank()) {
            String message = "";
            if (u_name.isBlank() && pwd.isBlank()) message = "Please enter your email address and password.";
            else if (u_name.isBlank()) message = "Please enter your email address.";
            else message = "Please enter your password.";
            JOptionPane.showMessageDialog(null, message, "Sign up", JOptionPane.WARNING_MESSAGE);
            return true;
        }

        return false;
    }

    public static void main(String[] args)
    {
        new Login();
    }
}

