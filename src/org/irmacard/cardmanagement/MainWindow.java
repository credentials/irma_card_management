package org.irmacard.cardmanagement;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;

import java.util.Date;
import java.util.ResourceBundle;
import javax.swing.JToolBar;
import java.awt.BorderLayout;

import javax.swing.AbstractListModel;
import javax.swing.JSplitPane;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;

import credentials.util.LogEntry;
import credentials.util.LogEntry.Action;

public class MainWindow {
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("org.irmacard.cardmanagement.messages"); //$NON-NLS-1$

	private JFrame frmCardManagement;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmCardManagement.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
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
		
		JSplitPane splitPaneVert = new JSplitPane();
		splitPaneVert.setResizeWeight(0.6);
		splitPaneVert.setOrientation(JSplitPane.VERTICAL_SPLIT);
		frmCardManagement.getContentPane().add(splitPaneVert, BorderLayout.CENTER);
		
		JSplitPane splitPaneHoriz = new JSplitPane();
		splitPaneHoriz.setResizeWeight(0.5);
		splitPaneVert.setLeftComponent(splitPaneHoriz);
		
		JList<Integer> listCredentials = new JList<Integer>();
		splitPaneHoriz.setLeftComponent(listCredentials);
		
		JList<LogEntry> listLog = new JList<LogEntry>();
		splitPaneHoriz.setRightComponent(listLog);
		listLog.setModel(new AbstractListModel<LogEntry>() {

			@Override
			public LogEntry getElementAt(int index) {
				// TODO Auto-generated method stub
				return new LogEntry(new Date(10000*index), Action.ISSUE, (short)index, (short) 5);
			}

			@Override
			public int getSize() {
				// TODO Auto-generated method stub
				return 10;
			}
		});
		listLog.setCellRenderer(new ListCellRenderer<LogEntry>() {

			@Override
			public Component getListCellRendererComponent(
					JList<? extends LogEntry> list, LogEntry value, int index,
					boolean isSelected, boolean cellHasFocus) {
				
				JLabel label = new JLabel(value.getTimestamp().toString() + ": " + (value.getAction() == Action.ISSUE ? "Issue" : "Verify") + " credential " + value.getCredential());
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
		
		JLabel lblNothinSelected = new JLabel(BUNDLE.getString("MainWindow.lblNothinSelected.text"));
		splitPaneVert.setRightComponent(lblNothinSelected);
	}

}
