package Pages;

import DB.Query;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

// TODO: profilo militanza del giocatore quando si clicca sull'ID dalla tabella di ricerca
// TODO: sistema login e profili
// TODO: sistema inserimento e modifica dati in base al profilo

public class Login extends JFrame {
    private JPanel panel;
    private JPasswordField password;
    private JTextField username;
    private JButton CreateButton;
    private JButton LoginButton;
    private Query query;

    public Login() {
        setContentPane(panel);
        setTitle("Sistema Informativo Calciatori");
        setSize(1100, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);     // HIDE_ON_CLOSE
        setLocationRelativeTo(null);
        setVisible(true);
        LoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String u_name = username.getText();
                String pwd = password.getText();
                if(u_name.isBlank() || pwd.isBlank())
                {
                    JOptionPane.showMessageDialog(null, "Errore, tutti i campi sono obbligatori!", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                query = new Query();
                var user_type = query.Login(u_name, pwd);
                if(user_type == '0')
                {
                    JOptionPane.showMessageDialog(null, "Errore interno!", "Errore", JOptionPane.ERROR_MESSAGE);
                }
                else if(user_type == '1')
                {
                    JOptionPane.showMessageDialog(null, "Credenziali errate!", "Errore", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    new Main(user_type);
                    dispose();
                }
            }
        });
        CreateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String u_name = username.getText();
                String pwd = password.getText();
                query = new Query();
                var resultCreateUser = query.CreateUser(u_name, pwd);
                if(resultCreateUser)
                    JOptionPane.showMessageDialog(null, "User created successfully!", "User creation", JOptionPane.INFORMATION_MESSAGE);
                else
                    JOptionPane.showMessageDialog(null, "Email already associated with a user!", "Creazione Utente", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args)
    {
        new Login();
    }
}
