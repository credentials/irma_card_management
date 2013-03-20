package org.irmacard.cardmanagement;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.table.DefaultTableModel;

import net.sourceforge.scuba.smartcards.CardServiceException;

import org.irmacard.credentials.Attributes;
import org.irmacard.credentials.BaseCredentials;
import org.irmacard.credentials.info.AttributeDescription;
import org.irmacard.credentials.info.CredentialDescription;
import org.irmacard.credentials.util.log.IssueLogEntry;
import org.irmacard.credentials.util.log.LogEntry;
import org.irmacard.credentials.util.log.RemoveLogEntry;
import org.irmacard.credentials.util.log.VerifyLogEntry;

public class LogDetailView extends JPanel {
	private static final long serialVersionUID = 8260302052925451249L;
	private static final Object[] COLUMN_NAMES = new Object[]{"Attribute", "Value", "Description"};
	private JTable table;
	private JEditorPane lblTitle;
	private JLabel lblTimestamp;
	private BaseCredentials credentials;
	private CredentialSelector credentialSelector;
	private CredentialDescription credential;

	/**
	 * Create the panel.
	 */
	public LogDetailView(BaseCredentials credentials, CredentialSelector credentialSelector) {
		this.credentials = credentials;
		this.credentialSelector = credentialSelector;
		
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		
		lblTitle = new JEditorPane();
		lblTitle.setOpaque(false);
		springLayout.putConstraint(SpringLayout.NORTH, lblTitle, 0, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblTitle, 0, SpringLayout.WEST, this);
		lblTitle.setContentType("text/html");
		lblTitle.setEditable(false);
		lblTitle.addHyperlinkListener(new HyperlinkListener() {
			
			@Override
			public void hyperlinkUpdate(HyperlinkEvent evt) {
				if(evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					LogDetailView.this.credentialSelector.selectCredential(credential);
				}
			}
		});
		add(lblTitle);
		
		lblTimestamp = new JLabel();
		springLayout.putConstraint(SpringLayout.NORTH, lblTimestamp, 6, SpringLayout.SOUTH, lblTitle);
		springLayout.putConstraint(SpringLayout.WEST, lblTimestamp, 0, SpringLayout.WEST, this);
		add(lblTimestamp);
		
		table = new JTable();
		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(table);
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, 0, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, 0, SpringLayout.EAST, this);
		add(scrollPane);
		
		JLabel lblAttributes = new JLabel("Attributes:");
		springLayout.putConstraint(SpringLayout.NORTH, lblAttributes, 10, SpringLayout.SOUTH, lblTimestamp);
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 0, SpringLayout.SOUTH, lblAttributes);
		springLayout.putConstraint(SpringLayout.NORTH, table, 0, SpringLayout.SOUTH, lblAttributes);
		springLayout.putConstraint(SpringLayout.WEST, lblAttributes, 0, SpringLayout.WEST, this);
		add(lblAttributes);
	}
	
	public void setLogEntry(LogEntry log) {
		try {
			credential = log.getCredential();
			String action = "";
			if (log instanceof IssueLogEntry) {
				action = "Issue";
			} else if (log instanceof VerifyLogEntry) {
				action = "Verify";
			} else if (log instanceof RemoveLogEntry) {
				action = "Remove";
			}
			lblTitle.setText(String.format("<html>%s <a href=\"%d\">%s</a></html>", action, credential.getId(), credential.getName()));
			
			lblTimestamp.setText(log.getTimestamp().toString());
			DefaultTableModel tableModel = new DefaultTableModel(COLUMN_NAMES, 0);
			table.setModel(tableModel);
			Attributes attributes = credentials.getAttributes(credential);
			for(AttributeDescription attribute : credential.getAttributes()) {
				tableModel.addRow(new Object[]{attribute.getName(), new String(attributes.get(attribute.getName())), attribute.getDescription()});
			}
//		} catch (InfoException e) {
//			e.printStackTrace();
		} catch (CardServiceException e) {
			e.printStackTrace();
		}
	}
}
