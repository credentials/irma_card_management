package org.irmacard.cardmanagement;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import net.sourceforge.scuba.smartcards.CardEvent;
import net.sourceforge.scuba.smartcards.CardManager;
import net.sourceforge.scuba.smartcards.CardServiceException;
import net.sourceforge.scuba.smartcards.CardTerminalEvent;
import net.sourceforge.scuba.smartcards.CardTerminalListener;
import net.sourceforge.scuba.smartcards.TerminalCardService;
import net.sourceforge.scuba.smartcards.TerminalFactoryListener;

import org.irmacard.chvservice.CardHolderVerificationService;
import org.irmacard.chvservice.IPinVerificationListener;
import org.irmacard.idemix.IdemixService;

public class WelcomeScreen extends JFrame implements CardTerminalListener, TerminalFactoryListener, IPinVerificationListener {
	private static final long serialVersionUID = -1120906824335303913L;

	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("org.irmacard.cardmanagement.messages"); //$NON-NLS-1$

	private JPanel contentPane;

	private CardManager manager;
	private JLabel lblInfo;
	private JButton btnClose;

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
					WelcomeScreen frame = new WelcomeScreen();
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
	public WelcomeScreen() {
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 561, 306);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 66, 137));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblIrmaLogo = new JLabel();
		lblIrmaLogo.setBounds(5, 5, 558, 305);
		/*lblIrmaLogo.addMouseListener(new MouseAdapter() {
			//TODO: Temporary solution for not detecting card insertion
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					CardTerminal terminal = TerminalFactory.getDefault().terminals().list().get(0);
					CardService service = new TerminalCardService(terminal);
					
					cardInserted(new CardEvent(CardEvent.INSERTED, service));
				} catch (CardException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});*/
		contentPane.setLayout(null);
		lblIrmaLogo.setHorizontalAlignment(SwingConstants.CENTER);
		lblIrmaLogo.setIcon(new ImageIcon(WelcomeScreen.class.getResource("/img/PlaceCard.png")));
		contentPane.add(lblIrmaLogo);
		
		lblInfo = new JLabel(BUNDLE.getString("WelcomeScreen.lblInfo.placeCard")); //$NON-NLS-1$
		lblInfo.setBounds(5, 547, 804, 14);
		lblInfo.setVisible(false);
		lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblInfo);
		
		btnClose = new CloseButton();
		btnClose.setLocation(530, 11);
		btnClose.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				WindowEvent wev = new WindowEvent(WelcomeScreen.this, WindowEvent.WINDOW_CLOSING);
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
			}
		});
		contentPane.add(btnClose);
		
		manager = CardManager.getInstance();
		manager.addTerminalFactoryListener(this);
		manager.addCardTerminalListener(this);
		manager.startPolling();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				manager.stopPolling();
			}
		});
	}

	public void cardInserted(CardEvent ce) {
		System.out.println("Card inserted");
		TerminalCardService service = (TerminalCardService)ce.getService();
		
		try {
			IdemixService idemix = new IdemixService(service);
			idemix.open();
			CardHolderVerificationService chv = new CardHolderVerificationService(service);
			chv.addPinVerificationListener(this); 
			int pinResponse;
			do {
				pinResponse = chv.verifyPIN();
			} while(pinResponse > 0 && pinResponse != CardHolderVerificationService.PIN_OK);
			if(pinResponse == CardHolderVerificationService.PIN_OK) {
				//MainWindow mainWindow = new MainWindow(ce.getService());
				MainWindow mainWindow = new MainWindow();
				mainWindow.setVisible(true);
				setVisible(false);
			}
			else {
				//TODO No tries left
			}
		}
		catch (CardServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			service.close();
		}
	}

	public void cardRemoved(CardEvent ce) {
		System.out.println("Card removed");
	}

	public void cardTerminalAdded(CardTerminalEvent cte) {
		System.out.println("Terminal added");
	}

	public void cardTerminalRemoved(CardTerminalEvent cte) {
		System.out.println("Terminal removed");
	}

	@Override
	public String userPinRequest(Integer nr_tries_left) {
		String pinText = "The server requests to authenticate your identity, enter PIN";
		
		if(nr_tries_left != null) {
			pinText += " (" + nr_tries_left + " tries left):";
		} else {
			pinText += ":";
		}
		
        String pinString = "";
        boolean valid = false;
        
		JPasswordField pinField = new JPasswordField(4);
		JLabel lab = new JLabel(pinText);

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		GridBagConstraints cc = new GridBagConstraints();
		cc.anchor = GridBagConstraints.WEST;
		cc.insets = new Insets(10, 10, 10, 10);
		cc.gridx = 0;
		cc.gridy = 0;

		panel.add(lab, cc);
		cc.gridy++;
		panel.add(pinField, cc);
		
		while (!valid) {
			// ask for pin, inform the user
			int result = JOptionPane.showConfirmDialog(null, panel, "PIN",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

			pinString = new String(pinField.getPassword());
			
			if (result != 0) {
				// User pressed cancel;
				lab.setText("<html><font color=\"red\">Please enter a pin</font><br />"
						+ pinText + "</html>");
			} else if (pinString.length() != 4) {
				lab.setText("<html><font color=\"red\">Pin should be 4 digits</font><br />"
						+ pinText + "</html>");
			} else {
				valid = true;
			}
		}

        return pinString;
	}

	@Override
	public void pinPadPinRequired(Integer nr_tries_left) {
		lblInfo.setText(BUNDLE.getString("WelcomeScreen.lblInfo.enterPin"));
	}

	@Override
	public void pinPadPinEntered() {
		lblInfo.setText(BUNDLE.getString("WelcomeScreen.lblInfo.placeCard"));
	}
	
	private class CloseButton extends JButton {
		private static final long serialVersionUID = 2887545853103440609L;
		private ImageIcon defaultIcon = new ImageIcon(WelcomeScreen.class.getResource("/img/closeButton/default.png"));
		private ImageIcon hoverIcon = new ImageIcon(WelcomeScreen.class.getResource("/img/closeButton/hover.png"));
		private ImageIcon pressIcon = new ImageIcon(WelcomeScreen.class.getResource("/img/closeButton/press.png"));
		
		public CloseButton() {
			super();
			setSize(defaultIcon.getIconWidth(), defaultIcon.getIconHeight());
		}
		
		@Override
		public void paint(Graphics g) {
			ButtonModel model = getModel();
			if(model.isArmed() || model.isPressed()) {
				pressIcon.paintIcon(this, g, 0, 0);
			}
			else if(model.isRollover()) {
				hoverIcon.paintIcon(this, g, 0, 0);
			}
			else {
				defaultIcon.paintIcon(this, g, 0, 0);
			}
		}
	}
}
