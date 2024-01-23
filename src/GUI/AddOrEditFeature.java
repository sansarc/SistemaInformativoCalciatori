package GUI;

import javax.swing.*;

import Entity.Feature;
import DB.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddOrEditFeature extends JFrame {
    private JTextField nameField;
    private JTextPane descriptionPane;
    private JComboBox typeComboBox;
    private JButton insertButton;
    private JPanel panel;
    private JButton deleteButton;
    private JButton returnInMainPageButton;
    private Query query;

    public AddOrEditFeature(Feature feature) {
        setContentPane(panel);
        setSize(500, 400);
        boolean isEdit = feature != null;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        String[] typeOpt = {"\0", "Offensive", "Defensive"};
        for (String i : typeOpt) typeComboBox.addItem(i);
        if (!isEdit) {
            setTitle("SIC - Add New Feature");
            deleteButton.setVisible(false);
        } else {
            setTitle("SIC - Edit Feature \"" + feature.getName() + "\"");
            insertButton.setText("Edit");
            nameField.setEnabled(false);
            nameField.setText(feature.getName());
            descriptionPane.setText(feature.getDescription());
            typeComboBox.setSelectedIndex((feature.getType() == 'O') ? 1 : 2);
        }
        query = new Query();
        returnInMainPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Main();
                dispose();
            }
        });
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(nameField.getText().isBlank() || descriptionPane.getText().isBlank() || typeComboBox.getSelectedIndex() < 1) {
                    JOptionPane.showMessageDialog(null, "Error: all fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                else if(descriptionPane.getText().length() > 128) {
                    JOptionPane.showMessageDialog(null, "Error: description can contain a maximum of 128 characters!", "Error", JOptionPane.ERROR_MESSAGE);

                }
                var featureRequest = new Feature(nameField.getText(), descriptionPane.getText(), typeComboBox.getSelectedItem().toString().charAt(0));
                boolean _ok = false;
                if(isEdit) {
                    _ok = query.updateFeature(featureRequest);
                }
                else {
                    _ok = query.insertFeature(featureRequest);
                }
                if(_ok)
                    JOptionPane.showMessageDialog(null, "Operation completed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                else
                    JOptionPane.showMessageDialog(null, "Error during the operation!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the selected feature?", "Delete feature", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    var ok_ = query.deleteFromId("FEATURE", "FEATURE_NAME", feature.getName());
                    if(ok_) {
                        JOptionPane.showMessageDialog(null, "Feature successfully deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Error: feature was not deleted!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }
}
