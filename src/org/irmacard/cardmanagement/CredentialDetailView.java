package org.irmacard.cardmanagement;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableModel;

import org.irmacard.credentials.Attributes;

public class CredentialDetailView extends JPanel {
	private static final Object[] COLUMN_NAMES = new Object[]{"Attribute", "Value"};
	private static final long serialVersionUID = 1625933969087435098L;
	private JTable table;
	private JLabel lblCredName;

	/**
	 * Create the panel.
	 */
	public CredentialDetailView() {
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		
		lblCredName = new JLabel();
		lblCredName.setFont(new Font("Tahoma", Font.BOLD, 14));
		springLayout.putConstraint(SpringLayout.NORTH, lblCredName, 0, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblCredName, 0, SpringLayout.WEST, this);
		add(lblCredName);
		
		JScrollPane scrollPane = new JScrollPane();
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 0, SpringLayout.SOUTH, lblCredName);
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, 0, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, 0, SpringLayout.EAST, this);
		add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);

	}

	public void setCredential(short credID, Attributes attributes) {
		lblCredName.setText("Credential " + credID);
		DefaultTableModel tableModel = new DefaultTableModel(COLUMN_NAMES, 0);
		table.setModel(tableModel);
		for(String attribute : attributes.getIdentifiers()) {
			tableModel.addRow(new Object[]{attribute, attributes.get(attribute)});
		}
	}
}
