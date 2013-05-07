package org.irmacard.cardmanagement;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JTabbedPane;
import javax.swing.JSplitPane;

import org.irmacard.credentials.Attributes;
import org.irmacard.credentials.info.CredentialDescription;
import org.irmacard.credentials.info.InfoException;
import java.awt.Font;
import java.awt.Color;
import java.awt.Toolkit;
import java.util.ResourceBundle;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import java.awt.SystemColor;
import java.awt.Frame;

public class MainWindow extends JFrame implements ListSelectionListener {

	private static final long serialVersionUID = 3280620387236869052L;

	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("org.irmacard.cardmanagement.messages"); //$NON-NLS-1$

	private JPanel contentPane;

	private DefaultListModel credentialListModel;

	private CredentialDetailView credentialDetailView;

	private JList list;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setTitle(BUNDLE.getString("MainWindow.frmCardManagement.title")); //$NON-NLS-1$
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/img/irma.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 673, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setForeground(new Color(0, 66, 137));
		tabbedPane.setFont(new Font("Ubuntu", Font.PLAIN, 11));
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JSplitPane credentialsTab = new JSplitPane();
		tabbedPane.addTab("Credentials", null, credentialsTab, null);
		
		credentialDetailView = new CredentialDetailView();
		
		
		Attributes attributes = new Attributes();
		attributes.add("University", "Radboud University Nijmegen".getBytes());
		attributes.add("Student ID", "u921154".getBytes());
		attributes.add("Student Card", "2300921154".getBytes());
		try {
			credentialDetailView.setCredential(new CredentialDescription((short)0), attributes);
		} catch (InfoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		credentialsTab.setRightComponent(credentialDetailView);
		
		list = new JList();
		list.setBackground(SystemColor.control);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		credentialListModel = new DefaultListModel();
		try {
			credentialListModel.addElement(new CredentialDescription((short)0));
			credentialListModel.addElement(new CredentialDescription((short)1));
			credentialListModel.addElement(new CredentialDescription((short)2));
		} catch (InfoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		list.setCellRenderer(new CredentialRenderer());
		list.setModel(credentialListModel);
		list.addListSelectionListener(this);
		credentialsTab.setLeftComponent(list);
		
		JSplitPane logTab = new JSplitPane();
		tabbedPane.addTab("Log", null, logTab, null);
	}

	@Override
	public void valueChanged(ListSelectionEvent event) {
		Attributes attributes = new Attributes();
		if(list.getSelectedIndex() == 0) {
			attributes.add("University", "Radboud University Nijmegen".getBytes());
			attributes.add("Student ID", "u921154".getBytes());
			attributes.add("Student Card", "2300921154".getBytes());
		}
		else if(list.getSelectedIndex() == 1) {
			attributes.add("Over 16", "True".getBytes());
			attributes.add("Over 18", "True".getBytes());
			attributes.add("Over 21", "False".getBytes());
		}
		else if(list.getSelectedIndex() == 2) {
			attributes.add("Nationality", "Nederlands".getBytes());
		}
		credentialDetailView.setCredential((CredentialDescription)list.getSelectedValue(), attributes);
	}
}
