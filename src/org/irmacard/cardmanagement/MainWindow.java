package org.irmacard.cardmanagement;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JFrame;


import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JToolBar;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.net.URI;


import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;

import javax.swing.ListCellRenderer;

import org.irmacard.credentials.idemix.IdemixCredentials;
import org.irmacard.credentials.info.CredentialDescription;
import org.irmacard.credentials.info.DescriptionStore;
import org.irmacard.credentials.info.InfoException;
import org.irmacard.credentials.util.log.IssueLogEntry;
import org.irmacard.credentials.util.log.LogEntry;
import org.irmacard.credentials.util.log.RemoveLogEntry;
import org.irmacard.credentials.util.log.VerifyLogEntry;
import org.irmacard.credentials.BaseCredentials;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.sourceforge.scuba.smartcards.CardService;
import net.sourceforge.scuba.smartcards.CardServiceException;

import javax.swing.border.BevelBorder;
import javax.swing.ImageIcon;
import java.awt.Toolkit;
import java.awt.Font;
import java.awt.Color;


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
	private void initialize() {
		frmCardManagement = new JFrame();
		frmCardManagement.setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/img/irma.png")));
		frmCardManagement.setTitle(BUNDLE.getString("MainWindow.frmCardManagement.title")); //$NON-NLS-1$
		frmCardManagement.setBounds(100, 100, 690, 456);
		frmCardManagement.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCardManagement.setExtendedState(frmCardManagement.getExtendedState()|JFrame.MAXIMIZED_BOTH);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		frmCardManagement.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		JButton btnChangePin = new JButton("");
		btnChangePin.setToolTipText(BUNDLE.getString("MainWindow.btnChangePin.toolTipText")); //$NON-NLS-1$
		btnChangePin.setIcon(new ImageIcon(MainWindow.class.getResource("/img/changePin.png")));
		toolBar.add(btnChangePin);
		
		splitPaneVert = new JSplitPane();
		splitPaneVert.setBorder(null);
		splitPaneVert.setResizeWeight(0.6);
		splitPaneVert.setOrientation(JSplitPane.VERTICAL_SPLIT);
		frmCardManagement.getContentPane().add(splitPaneVert, BorderLayout.CENTER);
		
		JSplitPane splitPaneHoriz = new JSplitPane();
		splitPaneHoriz.setBorder(null);
		splitPaneHoriz.setResizeWeight(0.5);
		splitPaneVert.setLeftComponent(splitPaneHoriz);
		
		JScrollPane scrollPaneLog = new JScrollPane();
		scrollPaneLog.setBorder(null);
		splitPaneHoriz.setRightComponent(scrollPaneLog);
		
		final JList listLog = new JList();
		listLog.setFont(new Font("Ubuntu", Font.PLAIN, 11));
		listLog.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		listLog.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent evt) {
				if(!evt.getValueIsAdjusting()){
					selectLogIndex(listLog.getSelectedIndex());
				}
			}
		});
		listLog.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent arg0) {
				selectLogIndex(listLog.getSelectedIndex());
			}

			@Override
			public void focusLost(FocusEvent arg0) {}
		});
		listLog.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneLog.setViewportView(listLog);
		logListModel = new DefaultListModel();
		List<LogEntry> logEntries = null;
		try {
			logEntries = baseCredentials.getLog();
		} catch (CardServiceException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InfoException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(LogEntry entry : logEntries) {
			logListModel.addElement(entry);
		}
		listLog.setModel(logListModel);
		listLog.setCellRenderer(new ListCellRenderer() {

			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected, boolean cellHasFocus) {
				LogEntry entry = (LogEntry)value;
				CredentialDescription credential = entry.getCredential();
				String action = "";
				if (entry instanceof IssueLogEntry) {
					action = "Issue";
				} else if (entry instanceof VerifyLogEntry) {
					action = "Verify";
				} else if (entry instanceof RemoveLogEntry) {
					action = "Remove";
				}
				String string = entry.getTimestamp().toString() + ": " + action + credential.getName(); 
				
				return renderCell(string, isSelected, list);
			}
			
		});
		
		JLabel lblLog = new JLabel(BUNDLE.getString("MainWindow.lblLog.text")); //$NON-NLS-1$
		lblLog.setForeground(new Color(0, 66, 137));
		lblLog.setFont(new Font("Ubuntu", Font.BOLD, 12));
		scrollPaneLog.setColumnHeaderView(lblLog);
		
		JScrollPane scrollPaneCredentials = new JScrollPane();
		scrollPaneCredentials.setBorder(null);
		splitPaneHoriz.setLeftComponent(scrollPaneCredentials);
		
		final JList listCredentials = new JList();
		listCredentials.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		listCredentials.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent evt) {
				selectCredentialIndex(listCredentials.getSelectedIndex());
			}
		});
		listCredentials.addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent arg0) {
				selectCredentialIndex(listCredentials.getSelectedIndex());
			}

			@Override
			public void focusLost(FocusEvent arg0) {}
		});
		listCredentials.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneCredentials.setViewportView(listCredentials);
		credListModel = new DefaultListModel();
		
		try {
			List<CredentialDescription> credentials = baseCredentials.getCredentials();
			URI core = new File(System.getProperty("user.dir")).toURI().resolve("irma_configuration/");
			DescriptionStore.setCoreLocation(core);
			for(CredentialDescription cred : credentials) {
				credListModel.addElement(cred);
			}
		} catch (CardServiceException e) {
			e.printStackTrace();
		} catch (InfoException e) {
			e.printStackTrace();
		}
		listCredentials.setModel(credListModel);
		listCredentials.setCellRenderer(new ListCellRenderer(){
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected, boolean cellHasFocus) {
				CredentialDescription credential = (CredentialDescription)value;
				return renderCell(credential.getName(), isSelected, list);
			}
		});
		
		JLabel lblCredentials = new JLabel(BUNDLE.getString("MainWindow.lblCredentials.text")); //$NON-NLS-1$
		lblCredentials.setForeground(new Color(0, 66, 137));
		lblCredentials.setFont(new Font("Ubuntu", Font.BOLD, 12));
		scrollPaneCredentials.setColumnHeaderView(lblCredentials);
		
		JLabel lblNothingSelected = new JLabel(BUNDLE.getString("MainWindow.lblNothinSelected.text"));
		lblNothingSelected.setFont(new Font("Ubuntu", Font.PLAIN, 11));
		splitPaneVert.setRightComponent(lblNothingSelected);
		
		logDetailView = new LogDetailView(baseCredentials, this);
	}

	private JLabel renderCell(String text, boolean isSelected, JList list) {
		JLabel label = new JLabel(text);
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
	
	/**
	 * Shows the log with the given index in the logDetailView
	 * @param index
	 */
	private void selectLogIndex(int index) {
		logDetailView.setLogEntry((LogEntry) logListModel.getElementAt(index));
		splitPaneVert.setRightComponent(logDetailView);
	}
	
	/**
	 * Shows the credential with the given index in the credentialDetailView
	 * @param index
	 */
	private void selectCredentialIndex(int index) {
		CredentialDescription credential = (CredentialDescription)credListModel.getElementAt(index);
		selectCredential(credential);
	}
	
	/**
	 * Shows the credential with the given id in the credentialDetailView
	 * @param id
	 */
	public void selectCredential(CredentialDescription credential) {
		try {
			credentialDetailView.setCredential(credential, baseCredentials.getAttributes(credential));
			splitPaneVert.setRightComponent(credentialDetailView);
		} catch (CardServiceException e) {
			e.printStackTrace();
		}
	}
	
	public void show() {
		frmCardManagement.setVisible(true);
	}

}
