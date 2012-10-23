package org.irmacard.cardmanagement;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import credentials.Attributes;

public class CredentialDetailView extends JPanel {
	private static final Object[] COLUMN_NAMES = new Object[]{"Attribute", "Value"};
	private static final long serialVersionUID = 1625933969087435098L;
	private JTable table;
	private JLabel lblCredName;

	/**
	 * Create the panel.
	 */
	public CredentialDetailView() {
		setLayout(new BorderLayout(0, 0));
		
		lblCredName = new JLabel();
		lblCredName.setFont(new Font("Tahoma", Font.BOLD, 14));
		add(lblCredName, BorderLayout.NORTH);
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
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
