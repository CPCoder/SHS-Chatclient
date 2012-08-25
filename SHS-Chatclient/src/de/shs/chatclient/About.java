package de.shs.chatclient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * Project		Chatclient Standalone
 * Filename		About.java
 * Author		Steffen Haase
 * Date			26.03.2011
 * License		GPL v3
 */

public class About
{
	private JLabel appname = new JLabel();
	private JLabel appversion = new JLabel();
	private JLabel copyright = new JLabel();
	private JLabel contact = new JLabel("not available");
	private JLabel website = new JLabel("not available");
	
	Client client;
	JFrame frame;
	JPanel jpanel;
	
	public About() {
		showAbout();
	}
	
	private void showAbout() {
		appname.setText(Client.app_name);
		appname.setFont(new Font("Arial", 0, 12));
		appversion.setText(Client.app_version);
		appversion.setFont(new Font("Arial", 0, 12));
		copyright.setText(Client.copyright);
		copyright.setFont(new Font("Arial", 0, 12));
		contact.setFont(new Font("Arial", 0, 12));
		website.setFont(new Font("Arial", 0, 12));
		frame = new JFrame("About");
		frame.setLayout(new BorderLayout());
		jpanel = new JPanel();
		jpanel.setPreferredSize(new Dimension(400,150));
		jpanel.setLayout(new GridBagLayout());
		frame.add(jpanel, java.awt.BorderLayout.CENTER);
        jpanel.add(
        		new JLabel("Software-Name: "), 
        		new GridBagConstraints(
        				0, 0, 1, 1, 0.1, 0.0, 
        				GridBagConstraints.EAST, 
        				GridBagConstraints.NONE, 
        				new Insets(0, 0, 0, 0)
        				, 0, 0)
        		);
        jpanel.add(
        		appname, 
        		new GridBagConstraints(
        				1, 0, 1, 1, 0.1, 0.0, 
        				GridBagConstraints.WEST, 
        				GridBagConstraints.NONE, 
        				new Insets(0, 0, 0, 0)
        				, 0, 0)
        		);
        jpanel.add(
        		new JLabel("Software-Version: "), 
        		new GridBagConstraints(
        				0, 1, 1, 1, 0.1, 0.0, 
        				GridBagConstraints.EAST, 
        				GridBagConstraints.NONE, 
        				new Insets(0, 0, 0, 0)
        				, 0, 0)
        		);
        jpanel.add(
        		appversion, 
        		new GridBagConstraints(
        				1, 1, 1, 1, 0.1, 0.0, 
        				GridBagConstraints.WEST, 
        				GridBagConstraints.NONE, 
        				new Insets(0, 0, 0, 0)
        				, 0, 0)
        		);
        jpanel.add(
        		new JLabel("Copyright: "), 
        		new GridBagConstraints(
        				0, 2, 1, 1, 0.1, 0.0, 
        				GridBagConstraints.EAST, 
        				GridBagConstraints.NONE, 
        				new Insets(0, 0, 0, 0)
        				, 0, 0)
        		);
        jpanel.add(
        		copyright, 
        		new GridBagConstraints(
        				1, 2, 1, 1, 0.1, 0.0, 
        				GridBagConstraints.WEST, 
        				GridBagConstraints.NONE, 
        				new Insets(0, 0, 0, 0)
        				, 0, 0)
        		);
        jpanel.add(
        		new JLabel("Kontakt: "), 
        		new GridBagConstraints(
        				0, 3, 1, 1, 0.1, 0.0, 
        				GridBagConstraints.EAST, 
        				GridBagConstraints.NONE, 
        				new Insets(0, 0, 0, 0)
        				, 0, 0)
        		);
        jpanel.add(
        		contact, 
        		new GridBagConstraints(
        				1, 3, 1, 1, 0.1, 0.0, 
        				GridBagConstraints.WEST, 
        				GridBagConstraints.NONE, 
        				new Insets(0, 0, 0, 0)
        				, 0, 0)
        		);
        jpanel.add(
        		new JLabel("Webseite: "), 
        		new GridBagConstraints(
        				0, 4, 1, 1, 0.1, 0.0, 
        				GridBagConstraints.EAST, 
        				GridBagConstraints.NONE, 
        				new Insets(0, 0, 0, 0)
        				, 0, 0)
        		);
        jpanel.add(
        		website, 
        		new GridBagConstraints(
        				1, 4, 1, 1, 0.1, 0.0, 
        				GridBagConstraints.WEST, 
        				GridBagConstraints.NONE, 
        				new Insets(0, 0, 0, 0)
        				, 0, 0)
        		);
        frame.setSize(400, 150);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}

}
