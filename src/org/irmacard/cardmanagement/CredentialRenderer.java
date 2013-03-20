package org.irmacard.cardmanagement;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.irmacard.credentials.info.CredentialDescription;

public class CredentialRenderer implements ListCellRenderer {

	private static final ImageIcon badge = new ImageIcon(MainWindow2.class.getResource("/img/badge.png"));
	private static final ImageIcon shadeLeft = new ImageIcon(MainWindow2.class.getResource("/img/shade_left.png"));
	private static final ImageIcon shadeMiddle = new ImageIcon(MainWindow2.class.getResource("/img/shade_middle.png"));
	private static final ImageIcon shadeRight = new ImageIcon(MainWindow2.class.getResource("/img/shade_right.png"));
	private static final ImageIcon lock = new ImageIcon(MainWindow2.class.getResource("/img/lock.png"));
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index,
    boolean isSelected, boolean cellHasFocus) {
		CredentialLabel label = new CredentialLabel((CredentialDescription)value, isSelected);
		
		return label;
	}

	
	private class CredentialLabel extends JLabel{

		private static final long serialVersionUID = -5932447452784481666L;
		
		private boolean drawLock;
		private boolean isSelected;
		
		
		public CredentialLabel(CredentialDescription value, boolean isSelected) {
			
			setText(value.getName());
			this.isSelected = isSelected;
			if(isSelected) {
				setBackground(new Color(0, 66, 137));
			}
			else {
				setBackground(new Color(0, 0, 0, 0));
			}
			
			drawLock = value.getName() == "Student";
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
			
			if(!isSelected) {
				shadeLeft.paintIcon(this, g, 0, 0);
				g.drawImage(shadeMiddle.getImage(), shadeLeft.getIconWidth(), 0, getWidth() - shadeLeft.getIconWidth() - shadeRight.getIconWidth(), shadeMiddle.getIconHeight(), null);
				shadeRight.paintIcon(this, g, getWidth() - shadeRight.getIconWidth(), 0);
			}
			g.setColor(Color.white);
			g.fillRoundRect(18, 16, getWidth() - 20, getHeight() - 19, 10, 10);
			badge.paintIcon(this, g, 1, 1);
			
			try {
				Font ubuntuBold = Font.createFont(Font.TRUETYPE_FONT, MainWindow2.class.getResourceAsStream("/fonts/Ubuntu-B.ttf"));
				ubuntuBold = ubuntuBold.deriveFont(14.0f);
				g.setFont(ubuntuBold);
				g.setColor(new Color(0, 66, 137));
				g.drawString(getText(), 40, 50);
			} catch (FontFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(drawLock) {
				lock.paintIcon(this, g, getWidth() - 25, 20);
			}
		}
		
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(200, 65);
		}
		
		@Override
		public Dimension getMinimumSize() {
			return getPreferredSize();
		}
	}
}
