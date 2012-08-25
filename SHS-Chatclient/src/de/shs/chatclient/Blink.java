package de.shs.chatclient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.Timer;

/*
 * Project		Chatclient Standalone
 * Filename		Blink.java
 * Author		Steffen Haase
 * Date			26.03.2011
 * License		GPL v3
 */
public class Blink extends Thread {
	 
    private JFrame frame = null;
    private Client client = null;
    private String windowTitle = "New Message(s)";
    
    public Blink(Client client, JFrame frame) {
        super();
        this.frame = frame;
        this.client = client;
    }
    
    public void stopTimer() {
    }
    
    public void run() {
        super.run();
		new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!client.active) {
					String tmp = frame.getTitle();
					frame.setTitle(windowTitle);
					windowTitle = tmp;
				} else {
					((Timer)e.getSource()).stop();
				}
			}
		}).start();
    }
}