package org.irmacard.cardmanagement;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

import org.irmacard.chvservice.CardHolderVerificationService;
import org.irmacard.chvservice.IPinVerificationListener;

import service.IdemixService;

import net.sourceforge.scuba.smartcards.CardEvent;
import net.sourceforge.scuba.smartcards.CardManager;
import net.sourceforge.scuba.smartcards.CardService;
import net.sourceforge.scuba.smartcards.CardServiceException;
import net.sourceforge.scuba.smartcards.CardTerminalEvent;
import net.sourceforge.scuba.smartcards.CardTerminalListener;
import net.sourceforge.scuba.smartcards.TerminalCardService;
import net.sourceforge.scuba.smartcards.TerminalFactoryListener;

import java.util.ResourceBundle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WelcomeScreen extends JFrame implements CardTerminalListener, TerminalFactoryListener, IPinVerificationListener {
	private static final long serialVersionUID = -1120906824335303913L;

	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("org.irmacard.cardmanagement.messages"); //$NON-NLS-1$

	private JPanel contentPane;
	private JLabel pinLabel;

	private CardManager manager;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JLabel lblIrmaLogo = new JLabel();
		lblIrmaLogo.addMouseListener(new MouseAdapter() {
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
		});
		lblIrmaLogo.setHorizontalAlignment(SwingConstants.CENTER);
		lblIrmaLogo.setIcon(new ImageIcon(WelcomeScreen.class.getResource("/img/irma.png")));
		contentPane.add(lblIrmaLogo, BorderLayout.CENTER);
		
		JLabel lblPlaceCard = new JLabel(BUNDLE.getString("WelcomeScreen.lblPlaceCard.text")); //$NON-NLS-1$
		lblPlaceCard.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblPlaceCard, BorderLayout.SOUTH);
		
		pinLabel = new JLabel(BUNDLE.getString("WelcomeScreen.pinLabel.text")); //$NON-NLS-1$
		pinLabel.setHorizontalAlignment(SwingConstants.CENTER);
		pinLabel.setVisible(false);
		contentPane.add(pinLabel, BorderLayout.SOUTH);
		
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
				MainWindow mainWindow = new MainWindow(ce.getService());
				mainWindow.show();
				setVisible(false);
			}
			else {
				//TODO No tries left
			}
		} catch (CardServiceException e) {
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
		pinLabel.setVisible(true);
	}

	@Override
	public void pinPadPinEntered() {
		pinLabel.setVisible(false);
	}
}
