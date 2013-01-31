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
import org.irmacard.credentials.info.AttributeDescription;
import org.irmacard.credentials.info.CredentialDescription;
import java.util.ResourceBundle;

public class CredentialDetailView extends JPanel {
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("org.irmacard.cardmanagement.messages"); //$NON-NLS-1$
	private static final Object[] COLUMN_NAMES = new Object[]{"Attribute", "Value", "Description"};
	private static final long serialVersionUID = 1625933969087435098L;
	private JTable table;
	private JLabel lblCredName;
	private JLabel lblCredDescription;
	private JLabel lblIssuer;
	private JLabel lblIssuerLabel;
	
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
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, 0, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, 0, SpringLayout.EAST, this);
		add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		lblCredDescription = new JLabel();
		springLayout.putConstraint(SpringLayout.NORTH, lblCredDescription, 0, SpringLayout.SOUTH, lblCredName);
		add(lblCredDescription);
	
		lblIssuerLabel = new JLabel();
		lblIssuerLabel.setText(BUNDLE.getString("CredentialDetailView.lblIssuerLabel.text")); //$NON-NLS-1$
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 0, SpringLayout.SOUTH, lblIssuerLabel);
		springLayout.putConstraint(SpringLayout.NORTH, lblIssuerLabel, 0, SpringLayout.SOUTH, lblCredDescription);
		add(lblIssuerLabel);
		
		lblIssuer = new JLabel();
		springLayout.putConstraint(SpringLayout.BASELINE, lblIssuer, 0, SpringLayout.BASELINE, lblIssuerLabel);
		springLayout.putConstraint(SpringLayout.WEST, lblIssuer, 0, SpringLayout.EAST, lblIssuerLabel);
		add(lblIssuer);
	}

	public void setCredential(CredentialDescription credential, Attributes attributes) {
		lblCredName.setText(credential.getName());
		lblCredDescription.setText(credential.getDescription());
		lblIssuer.setText(credential.getIssuerName());
		DefaultTableModel tableModel = new DefaultTableModel(COLUMN_NAMES, 0);
		table.setModel(tableModel);
		for(AttributeDescription attribute : credential.getAttributes()) {
			tableModel.addRow(new Object[]{attribute.getName(), new String(attributes.get(attribute.getName())), attribute.getDescription()});
		}
	}
}
