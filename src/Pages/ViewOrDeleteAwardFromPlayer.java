package Pages;
import Entity.*;
import DB.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class ViewOrDeleteAwardFromPlayer extends JFrame {
    private JPanel panel;
    private JTable awardsTable;

    public ViewOrDeleteAwardFromPlayer(Player player) {
        setContentPane(panel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setSize(850, 500);
        setTitle("SIC - Award of " + player.getName() + " " + player.getLastName());
        Query query = new Query(awardsTable);
        List<Award> awards = query.select_palmares_from_player(player.getId());
        awardsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = awardsTable.rowAtPoint(e.getPoint());
                int col = awardsTable.columnAtPoint(e.getPoint());
                if(Login.user_type == 'A') {
                    int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the selected award?", "Delete award", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        var ok_ = query.deleteFromId("AWARD", "idaward", awards.get(row).getId());
                        if(ok_) {
                            JOptionPane.showMessageDialog(null, "Award successfully deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Error: award was not deleted!", "Error", JOptionPane.ERROR_MESSAGE);
                        }

                    }
                }
            }
        });
    }
}
