package org.irmacard.cardmanagement;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JFrame;


import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JToolBar;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;


import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;

import javax.swing.ListCellRenderer;

import org.irmacard.credentials.util.LogEntry;
import org.irmacard.credentials.util.LogEntry.Action;
import org.irmacard.credentials.idemix.IdemixCredentials;
import org.irmacard.credentials.BaseCredentials;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.sourceforge.scuba.smartcards.CardService;


public class MainWindow implements CredentialSelector {
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("org.irmacard.cardmanagement.messages"); //$NON-NLS-1$

	private JFrame frmCardManagement;
	/**
	 * @wbp.nonvisual location=742,219
	 */
	private final CredentialDetailView credentialDetailView = new CredentialDetailView();
	/**
	 * @wbp.nonvisual location=749,309
	 */
	private LogDetailView logDetailView;

	private BaseCredentials baseCredentials;

	private DefaultListModel credListModel;

	private JSplitPane splitPaneVert;

	private DefaultListModel logListModel;

	/**
	 * Create the application.
	 * @param cardService 
	 */
	public MainWindow(CardService cardService) {
		this.baseCredentials = new IdemixCredentials(cardService);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initialize() {
		frmCardManagement = new JFrame();
		frmCardManagement.setTitle(BUNDLE.getString("MainWindow.frmCardManagement.title")); //$NON-NLS-1$
		frmCardManagement.setBounds(100, 100, 690, 456);
		frmCardManagement.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCardManagement.setExtendedState(frmCardManagement.getExtendedState()|JFrame.MAXIMIZED_BOTH);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		frmCardManagement.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		JButton btnChangePin = new JButton(BUNDLE.getString("MainWindow.btnChangePin.text")); //$NON-NLS-1$
		toolBar.add(btnChangePin);
		
		splitPaneVert = new JSplitPane();
		splitPaneVert.setResizeWeight(0.6);
		splitPaneVert.setOrientation(JSplitPane.VERTICAL_SPLIT);
		frmCardManagement.getContentPane().add(splitPaneVert, BorderLayout.CENTER);
		
		JSplitPane splitPaneHoriz = new JSplitPane();
		splitPaneHoriz.setResizeWeight(0.5);
		splitPaneVert.setLeftComponent(splitPaneHoriz);
		
		JScrollPane scrollPaneLog = new JScrollPane();
		splitPaneHoriz.setRightComponent(scrollPaneLog);
		
		final JList listLog = new JList();
		listLog.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent evt) {
				if(!evt.getValueIsAdjusting()){
					selectLog(listLog.getSelectedIndex());
				}
			}
		});
		listLog.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent arg0) {
				selectLog(listLog.getSelectedIndex());
			}

			@Override
			public void focusLost(FocusEvent arg0) {}
		});
		listLog.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneLog.setViewportView(listLog);
		logListModel = new DefaultListModel();
		List<LogEntry> logEntries = baseCredentials.getLog();
		for(LogEntry entry : logEntries) {
			logListModel.addElement(entry);
		}
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
		listCredentials.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent evt) {
				selectCredential(listCredentials.getSelectedIndex());
			}
		});
		listCredentials.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent arg0) {
				selectCredential(listCredentials.getSelectedIndex());
			}

			@Override
			public void focusLost(FocusEvent arg0) {}
		});
		listCredentials.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneCredentials.setViewportView(listCredentials);
		credListModel = new DefaultListModel();
		List<Integer> credentials = baseCredentials.getCredentials();
		for(Integer cred : credentials) {
			credListModel.addElement(cred);
		}
		listCredentials.setModel(credListModel);
		
		JLabel lblNothingSelected = new JLabel(BUNDLE.getString("MainWindow.lblNothinSelected.text"));
		splitPaneVert.setRightComponent(lblNothingSelected);
		
		logDetailView = new LogDetailView(baseCredentials, this);
	}

	/**
	 * Shows the log with the given index in the logDetailView
	 * @param index
	 */
	private void selectLog(int index) {
		logDetailView.setLogEntry((LogEntry) logListModel.getElementAt(index));
		splitPaneVert.setRightComponent(logDetailView);
	}
	
	/**
	 * Shows the credential with the given index in the credentialDetailView
	 * @param index
	 */
	private void selectCredential(int index) {
		short id = ((Integer)credListModel.getElementAt(index)).shortValue();
		selectCredentialID(id);
	}
	
	/**
	 * Shows the credential with the given id in the credentialDetailView
	 * @param id
	 */
	public void selectCredentialID(short id) {
		credentialDetailView.setCredential(id, baseCredentials.getAttributes(id));
		splitPaneVert.setRightComponent(credentialDetailView);
	}
	
	public void show() {
		frmCardManagement.setVisible(true);
	}

}
