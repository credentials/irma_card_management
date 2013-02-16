package org.irmacard.cardmanagement;

import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.JLabel;

import org.irmacard.credentials.info.AttributeDescription;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.Box;
import java.awt.Component;
import java.awt.Dimension;

public class AttributeView extends JPanel {

	private JLabel lblAttributeName;
	private JLabel lblAttributeValue;
	private JLabel lblAttributeDescription;
	private Box horizontalBox;
	private Box horizontalBox_1;
	private Component rigidArea;
	private Component rigidArea_1;

	/**
	 * Create the panel.
	 */
	public AttributeView() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		horizontalBox = Box.createHorizontalBox();
		add(horizontalBox);
		horizontalBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		lblAttributeName = new JLabel("Surfnet ID: ");
		lblAttributeName.setAlignmentX(Component.CENTER_ALIGNMENT);
		horizontalBox.add(lblAttributeName);
		lblAttributeName.setForeground(new Color(0, 66, 137));
		lblAttributeName.setFont(new Font("Ubuntu", Font.BOLD, 11));
		
		lblAttributeValue = new JLabel("s1234567@student.ru.nl");
		horizontalBox.add(lblAttributeValue);
		lblAttributeValue.setForeground(new Color(0, 66, 137));
		lblAttributeValue.setFont(new Font("Ubuntu", Font.BOLD, 11));
		
		horizontalBox_1 = Box.createHorizontalBox();
		add(horizontalBox_1);
		horizontalBox_1.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		rigidArea = Box.createRigidArea(new Dimension(20, 0));
		horizontalBox_1.add(rigidArea);
		
		lblAttributeDescription = new JLabel("The Surfnet ID is used to identify a student");
		horizontalBox_1.add(lblAttributeDescription);
		lblAttributeDescription.setForeground(new Color(0, 66, 137));
		lblAttributeDescription.setFont(new Font("Ubuntu", Font.PLAIN, 11));
		
		rigidArea_1 = Box.createRigidArea(new Dimension(0, 5));
		add(rigidArea_1);
	}
	
	public void setAttribute(AttributeDescription attribute, String value) {
		lblAttributeName.setText(attribute.getName() + ": ");
		lblAttributeValue.setText(value);
		lblAttributeDescription.setText(attribute.getDescription());
		
		setSize(lblAttributeName.getWidth() + lblAttributeValue.getWidth() + 20, lblAttributeName.getHeight() + lblAttributeDescription.getHeight() + 20);
	}
}
