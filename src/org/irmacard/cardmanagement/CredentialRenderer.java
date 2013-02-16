package org.irmacard.cardmanagement;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.irmacard.credentials.info.CredentialDescription;

public class CredentialRenderer implements ListCellRenderer {

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index,
    boolean isSelected, boolean cellHasFocus) {
		CredentialLabel label = new CredentialLabel((CredentialDescription)value, isSelected);
		
		return label;
	}

	
	private class CredentialLabel extends JLabel{
		private boolean drawLock;
		private ImageIcon lock = new ImageIcon(MainWindow2.class.getResource("/img/lock.png"));
		
		public CredentialLabel(CredentialDescription value, boolean isSelected) {
			String shade = "";
			if(isSelected) {
				shade = "_noshade";
			}
			setIcon(new ImageIcon(MainWindow2.class.getResource("/img/badge" + shade + ".png")));
			setText(value.getName());
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
			
			getIcon().paintIcon(this, g, 0, 0);
			
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
				lock.paintIcon(this, g, 175, 20);
			}
		}
		
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(getIcon().getIconWidth(), getIcon().getIconHeight());
		}
		
		@Override
		public Dimension getMinimumSize() {
			return getPreferredSize();
		}
	}
}
