package Pages;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import Entity.Feature;
import DB.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

public class AddOrEditFeature extends JFrame {
    private JTextField nameField;
    private JTextPane descriptionPane;
    private JComboBox typeComboBox;
    private JButton insertButton;
    private JPanel panel;
    private JButton deleteButton;
    private Query query;

    public AddOrEditFeature(Feature feature) {
        setContentPane(panel);
        setSize(500, 400);
        boolean isEdit = feature != null;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        String[] typeOpt = {"\0", "Offensive", "Defensive"};
        for (String i : typeOpt) typeComboBox.addItem(i);
        if (!isEdit) {
            setTitle("SIC - Add New Feature");
            deleteButton.setVisible(false);
        } else {
            setTitle("SIC - Edit " + feature.getName());
            insertButton.setText("Edit");
            nameField.setEnabled(false);
            nameField.setText(feature.getName());
            descriptionPane.setText(feature.getDescription());
            typeComboBox.setSelectedIndex((feature.getType() == 'O') ? 1 : 2);
        }
        query = new Query();
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(nameField.getText().isBlank() || descriptionPane.getText().isBlank() || typeComboBox.getSelectedIndex() < 1) {
                    return;
                }
                var featureRequest = new Feature(nameField.getText(), descriptionPane.getText(), typeComboBox.getSelectedItem().toString().charAt(0));
                if(isEdit) {
                    query.updateFeature(featureRequest);
                }
                else {
                    query.insertFeature(featureRequest);
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                query.DeleteFromId("FEATURES", "FEATURE_NAME", feature.getName());
            }
        });
    }
}
