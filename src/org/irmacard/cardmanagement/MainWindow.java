package org.irmacard.cardmanagement;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;

import java.util.Date;
import java.util.ResourceBundle;
import javax.swing.JToolBar;
import java.awt.BorderLayout;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JSplitPane;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ListCellRenderer;

import org.irmacard.credentials.Attributes;
import org.irmacard.credentials.util.LogEntry;
import org.irmacard.credentials.util.LogEntry.Action;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import net.sourceforge.scuba.smartcards.CardService;

public class MainWindow {
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("org.irmacard.cardmanagement.messages"); //$NON-NLS-1$

	private JFrame frmCardManagement;
	/**
	 * @wbp.nonvisual location=742,219
	 */
	private final CredentialDetailView credentialDetailView = new CredentialDetailView();
	/**
	 * @wbp.nonvisual location=749,309
	 */
	private final LogDetailView logDetailView = new LogDetailView();

	private CardService cardService;

	/**
	 * Create the application.
	 * @param cardService 
	 */
	public MainWindow(CardService cardService) {
		this.cardService = cardService;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmCardManagement = new JFrame();
		frmCardManagement.setTitle(BUNDLE.getString("MainWindow.frmCardManagement.title")); //$NON-NLS-1$
		frmCardManagement.setBounds(100, 100, 690, 456);
		frmCardManagement.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		frmCardManagement.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		JButton btnChangePin = new JButton(BUNDLE.getString("MainWindow.btnChangePin.text")); //$NON-NLS-1$
		toolBar.add(btnChangePin);
		
		final JSplitPane splitPaneVert = new JSplitPane();
		splitPaneVert.setResizeWeight(0.6);
		splitPaneVert.setOrientation(JSplitPane.VERTICAL_SPLIT);
		frmCardManagement.getContentPane().add(splitPaneVert, BorderLayout.CENTER);
		
		JSplitPane splitPaneHoriz = new JSplitPane();
		splitPaneHoriz.setResizeWeight(0.5);
		splitPaneVert.setLeftComponent(splitPaneHoriz);
		
		JScrollPane scrollPaneLog = new JScrollPane();
		splitPaneHoriz.setRightComponent(scrollPaneLog);
		
		JList listLog = new JList();
		listLog.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneLog.setViewportView(listLog);
		DefaultListModel logListModel = new DefaultListModel();
		listLog.setModel(logListModel);
		listLog.setCellRenderer(new ListCellRenderer() {

			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected, boolean cellHasFocus) {
				LogEntry entry = (LogEntry)value;
				JLabel label = new JLabel(entry.getTimestamp().toString() + ": " + (entry.getAction() == Action.ISSUE ? "Issue" : "Verify") + " credential " + entry.getCredential());
		        if (isSelected) {
		            label.setBackground(list.getSelectionBackground());
		            label.setForeground(list.getSelectionForeground());
		        } else {
		        	label.setBackground(list.getBackground());
		        	label.setForeground(list.getForeground());
		        }
		        label.setEnabled(list.isEnabled());
		        label.setFont(list.getFont());
		        label.setOpaque(true);
		        return label;
			}
			
		});
		
		JScrollPane scrollPaneCredentials = new JScrollPane();
		splitPaneHoriz.setLeftComponent(scrollPaneCredentials);
		
		final JList listCredentials = new JList();
		final Attributes attr = new Attributes();
		attr.add("Attr1", new byte[]{0, 1, 2, 3});
		attr.add("Attr2", new byte[]{4, 2, 3, 9, 7});
		listCredentials.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent evt) {
				credentialDetailView.setCredential((Short) listCredentials.getModel().getElementAt(evt.getFirstIndex()), attr);
				splitPaneVert.setRightComponent(credentialDetailView);
			}
		});
		listCredentials.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneCredentials.setViewportView(listCredentials);
		DefaultListModel credListModel = new DefaultListModel();
		for(short i = 0; i < 10; i++)
			credListModel.addElement(i);
		listCredentials.setModel(credListModel);
		
		JLabel lblNothingSelected = new JLabel(BUNDLE.getString("MainWindow.lblNothinSelected.text"));
		//splitPaneVert.setRightComponent(lblNothingSelected);
		logDetailView.setLogEntry(new LogEntry(new Date(), Action.ISSUE, (short)5, (short)5));
		splitPaneVert.setRightComponent(logDetailView);
	}

	public void show() {
		frmCardManagement.setVisible(true);
	}

}
