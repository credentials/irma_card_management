package org.irmacard.cardmanagement;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.irmacard.credentials.info.CredentialDescription;
import org.irmacard.credentials.info.InfoException;

public class CredentialList extends JPanel {
	private static final long serialVersionUID = -1213410785197808016L;
	CredentialButton selected = null;
	
	
	/**
	 * Create the panel.
	 */
	public CredentialList() {
		setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		//setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setLayout(new FlowLayout());
		
		// FIXME: fill the list with buttons
//		CredentialDescription credential = null;
//		try {
//			credential = new CredentialDescription((short) 0);
//		} catch (InfoException e) {
//			e.printStackTrace();
//		}
//		CredentialButton btnCredential = new CredentialButton(credential, this);
//		add(btnCredential);
//		try {
//			credential = new CredentialDescription((short) 1);
//		} catch (InfoException e) {
//			e.printStackTrace();
//		}
//		CredentialButton btnCredential2 = new CredentialButton(credential, this);
//		add(btnCredential2);

	}
	
	public void setSelected(CredentialButton button) {
		if(selected != null) {
			selected.setSelected(false);
		}
		selected = button;
	}
	
	public CredentialDescription getCredential() {
		if(selected != null) {
			return selected.getCredential();
		}
		else {
			return null;
		}
	}

	private class CredentialButton extends JButton implements MouseListener {
		private static final long serialVersionUID = -2426794449213059099L;
		private CredentialDescription credential;
		private CredentialList parent;
		private Color selectedColor = new Color(0, 66, 137);
		
		
		public CredentialButton(CredentialDescription credential, CredentialList parent) {
			//super(credential.getName());
			this.credential = credential;
			this.parent = parent;
			
			this.setBorderPainted(false);
			this.setOpaque(false);
			this.setBackground(new Color(0, true));
			this.addMouseListener(this);
			
			setText(credential.getName());
		}
		
		@Override
		public void paint(Graphics g) {
			
			if(isSelected()) {
				g.setColor(selectedColor);
			}
			else {
				g.setColor(getBackground());
			}
			g.fillRect(0, 0, getWidth(), getHeight());
			
			g.setColor(Color.WHITE);
			g.fillRoundRect(15, 15, getWidth() - 20, getHeight() - 20, 2, 2);
			
			g.setColor(getForeground());
		    g.drawChars(getText().toCharArray(), 0, getText().length(), 20, 20);
		    
	    }
		
		@Override
		public void setText(String text) {
			super.setText(text);
			
			FontMetrics fontMetrics = getFontMetrics(getFont());
			int width = fontMetrics.stringWidth(getText()) + 25;
			int height = fontMetrics.getHeight() + 40;
			
			setSize(width, height);
		}

		public CredentialDescription getCredential() {
			return credential;
		}
		

		@Override
		public void mouseClicked(MouseEvent e) {
			parent.setSelected(this);
			this.setSelected(true);
			repaint();
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
}
