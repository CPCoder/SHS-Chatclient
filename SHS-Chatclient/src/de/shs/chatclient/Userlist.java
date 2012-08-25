package de.shs.chatclient;

import java.awt.Dimension;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/*
 * Project		Chatclient Standalone
 * Filename		Userlist.java
 * Author		Steffen Haase
 * Date			29.03.2011
 * License		GPL v3
 */
public class Userlist implements HyperlinkListener {
	public boolean showmenu = false;
	JPanel panel = new JPanel();
	JScrollPane scrollpane = new JScrollPane();
	JEditorPane userlist = new JEditorPane();
	HTMLDocument huserlist = null;
	Client client;
	
	public Userlist(Client client) throws Exception{
		this.client = client;
		userlist.setEditable(false);
		userlist.addHyperlinkListener(this);
		userlist.setContentType("text/html; charset=iso-8859-1");
		scrollpane = new JScrollPane(userlist, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollpane.setAutoscrolls(false);
		scrollpane.setPreferredSize(new Dimension(150, 486));
		scrollpane.setMinimumSize(new Dimension(150, 486));
        setStyle();
    	huserlist = (HTMLDocument)userlist.getDocument();
    	userlist.setText("Die Userliste wird geladen, sobald eine Verbindung zum Chat besteht.");
		
	}
	
	private void setStyle() {
		StyleSheet css = ((HTMLEditorKit)userlist.getEditorKit()).getStyleSheet(); 
		css.addRule(""+
				"body { background-color:#FFFFFF; color:#000000"+
				"font-family:arial; font-size:10px;}"+
				"a { text-decoration:none; }"
				);
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent event) {
	    if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
	        try {
	        	String url = event.getDescription();
	        	if (url != null) {
	        		client.openInBrowser(url);
	        	}
	        } catch(Exception e) {
	        	e.printStackTrace();
	        }
	    }
	}

}
