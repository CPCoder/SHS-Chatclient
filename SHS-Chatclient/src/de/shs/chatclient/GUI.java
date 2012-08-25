package de.shs.chatclient;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.ListIterator;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/*
 * Project		Chatclient Standalone
 * Filename		GUI.java
 * Author		Steffen Haase
 * Date			26.03.2011
 * License		GPL v3
 */
public class GUI implements HyperlinkListener{
	static JFrame frame;
	String[] inputcache = new String[10];
	int cachefield = -1;
	JPanel jpanel1 = new JPanel();
	JPanel jpanel2 = new JPanel();
	JPanel buttonPanel = new JPanel();
    JEditorPane output = new JEditorPane();
    JScrollPane jScrollPane1;
	HTMLDocument houtput = null;
    JTextField input = new JTextField();
    JButton scrolling = new JButton("Autoscroll Off");
    JButton clear = new JButton("Clear");
    JButton roomlist = new JButton("Raumliste");
    JButton userlist = new JButton("Userliste");
    JButton quit = new JButton("Client beenden");
    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("Client");
	JMenu comMenu = new JMenu("Community");
    JMenu fontMenu = new JMenu("Font");
	JMenu sizeMenu = new JMenu("Schriftgrösse");
	JMenu styleMenu = new JMenu("Schriftart");
	JMenu helpMenu = new JMenu("Info");
	
	Client client;
	Tools tools;
	Userlist uslist;
	
	public GUI(Client client) {
		this.client = client;
		try {
			tools = new Tools(client);
			uslist = new Userlist(client);
			buildGUI();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setStyle() {
		StyleSheet css = ((HTMLEditorKit)output.getEditorKit()).getStyleSheet(); 
		css.addRule(""+
				"BODY { background-color:#FFFFFF; "+
				"font-family:"+client.fStyle+"; font-size:"+client.fSize+"px;}"
				);
		if (client.connected) {
			client.addToOutput("<b>Client:</b> Die Schriftart ist nun \""+client.fStyle+"\" bei einer Schriftgr&ouml;sse von \""+client.fSize+"\" Pixel.");
		}
	}
	
	@SuppressWarnings("unchecked")
	private void buildGUI() throws Exception {
		frame = new JFrame(client.windowTitle);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(client);
		frame.setLayout(new BorderLayout());
		menuBar.add(fileMenu);
		menuBar.add(comMenu);
		menuBar.add(fontMenu);
		menuBar.add(helpMenu);
		frame.setJMenuBar(menuBar);
		fontMenu.add(styleMenu);
		fontMenu.add(sizeMenu);
        input.setPreferredSize(new Dimension(794, 30));
        input.setMinimumSize(new Dimension(794, 30));
        output.setEditable(false);
        output.addHyperlinkListener(this);
        output.setContentType("text/html; charset=iso-8859-1");
		jScrollPane1 = new JScrollPane(output, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
	            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setAutoscrolls(false);
        jScrollPane1.setPreferredSize(new Dimension(644, 486));
        jpanel1.setLayout(new GridBagLayout());
        jpanel2.setLayout(new GridBagLayout());
        frame.add(jpanel2, java.awt.BorderLayout.CENTER);
        jpanel2.add(jScrollPane1, new GridBagConstraints(0, 0, 2, 2, 0.1, 0.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        jpanel2.add(uslist.scrollpane, new GridBagConstraints(2, 0, 1, 2, 0.0, 0.1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        frame.add(jpanel1, java.awt.BorderLayout.SOUTH);
        jpanel1.add(scrolling, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jpanel1.add(roomlist, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jpanel1.add(userlist, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jpanel1.add(clear, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jpanel1.add(quit, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        jpanel1.add(input, new GridBagConstraints(1, 1, 5, 1, 0.1, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));//        jpanel1.add(jScrollPane1);
        setStyle();
    	houtput = (HTMLDocument)output.getDocument();
		styleMenu.add(new AbstractAction() {
			private static final long serialVersionUID = 2949527942791043067L;

			{
				putValue(Action.NAME, "Arial");
				putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, 0);
			}

			@Override
			public void actionPerformed(ActionEvent ae) {
				client.fStyle = "Arial";
				setStyle();
			}
		});
		styleMenu.add(new AbstractAction() {
			private static final long serialVersionUID = 2949527942791043067L;

			{
				putValue(Action.NAME, "Helvetia");
				putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, 1);
			}

			@Override
			public void actionPerformed(ActionEvent ae) {
				client.fStyle = "Helvetia";
				setStyle();
			}

		});
		styleMenu.add(new AbstractAction() {
			private static final long serialVersionUID = 2949527942791043067L;

			{
				putValue(Action.NAME, "Tahoma");
				putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, 2);
			}

			@Override
			public void actionPerformed(ActionEvent ae) {
				client.fStyle = "Tahoma";
				setStyle();
			}

		});
		sizeMenu.add(new AbstractAction() {
			private static final long serialVersionUID = 2949527942791043067L;

			{
				putValue(Action.NAME, "10px");
				putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, 0);
			}

			@Override
			public void actionPerformed(ActionEvent ae) {
				client.fSize = "10";
				setStyle();
			}

		});
		sizeMenu.add(new AbstractAction() {
			private static final long serialVersionUID = 2949527942791043067L;

			{
				putValue(Action.NAME, "11px");
				putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, 1);
			}

			@Override
			public void actionPerformed(ActionEvent ae) {
				client.fSize = "11";
				setStyle();
			}

		});
		sizeMenu.add(new AbstractAction() {
			private static final long serialVersionUID = 2949527942791043067L;

			{
				putValue(Action.NAME, "12px");
				putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, 2);
			}

			@Override
			public void actionPerformed(ActionEvent ae) {
				client.fSize = "12";
				setStyle();
			}

		});
		sizeMenu.add(new AbstractAction() {
			private static final long serialVersionUID = 2949527942791043067L;

			{
				putValue(Action.NAME, "14px");
				putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, 3);
			}

			@Override
			public void actionPerformed(ActionEvent ae) {
				client.fSize = "14";
				setStyle();
			}

		});
		sizeMenu.add(new AbstractAction() {
			private static final long serialVersionUID = 2949527942791043067L;

			{
				putValue(Action.NAME, "16px");
				putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, 4);
			}

			@Override
			public void actionPerformed(ActionEvent ae) {
				client.fSize = "16";
				setStyle();
			}

		});
		helpMenu.add(new AbstractAction() {
			private static final long serialVersionUID = 2949527942791043067L;

			{
				putValue(Action.NAME, "About");
				putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, 0);
			}

			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					client.about = new About();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}

		});

		fileMenu.add(new AbstractAction() {
			private static final long serialVersionUID = 2949527942791043067L;

			{
				putValue(Action.NAME, "Connect");
				putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, 2);
			}

			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					if (client.getSession()){
						if (client.doLogin()) {
							client.getUserID();
							fileMenu.getItem(0).setEnabled(false);
							fileMenu.getItem(1).setEnabled(true);
							input.setText("");
							input.requestFocus();
							input.setCaretPosition(0);
							input.setEnabled(true);
							if (client.userID.equals("-1")) {
								comMenu.getItem(2).setEnabled(false);
							} else {
								comMenu.getItem(2).setEnabled(true);
							}
							comMenu.setEnabled(true);
							fontMenu.setEnabled(true);
	        				if (client.chat == null) {
	        					client.chat = new Chatting(client);
	        					client.chat.start();
	        				}
						}
					}
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		});
		
		fileMenu.add(new AbstractAction() {
			private static final long serialVersionUID = 2949527942791043067L;

			{
				putValue(Action.NAME, "Disconnect");
				putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, 1);
			}

			@Override
			public void actionPerformed(ActionEvent ae) {
				doLogout();
				fileMenu.getItem(0).setEnabled(true);
				fileMenu.getItem(1).setEnabled(false);
				input.setText("Sobald eine Verbindung hergestellt wurde, wird dieses Feld freigegeben.");
				input.setEnabled(false);
				comMenu.setEnabled(false);
				fontMenu.setEnabled(false);
				if (client.chat != null) {
					client.chat.stop();
					client.chat = null;
				}
			}
		});
		
		fileMenu.add(new AbstractAction() {
			private static final long serialVersionUID = 2949527942791043067L;

			{
				putValue(Action.NAME, "Exit");
				putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, 0);
			}

			@Override
			public void actionPerformed(ActionEvent ae) {
				if (client.connected) {
					client.sendToChat("/logout");
					if (client.chat != null) {
						client.chat.stop();
						client.chat = null;
					}
				}
				System.exit(0);
			}
		});
		comMenu.add(new AbstractAction() {
			private static final long serialVersionUID = 2949527942791043067L;

			{
				putValue(Action.NAME, "Startseite");
				putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, 2);
			}

			@Override
			public void actionPerformed(ActionEvent ae) {
				String url = tools.replace(Client.config.getComURL(),"{hostname}",Client.config.getHost());
				url = tools.replace(url, "{comstring}", Client.config.getComstring());
				url = tools.replace(url,"{port}",Integer.toString(Client.config.getPort()));
				url = tools.replace(url,"{sessionid}",client.sessionID);
				try {
			        Desktop.getDesktop().browse(new URI(url));
				} catch (Exception e) {
				        e.printStackTrace();
				}
			}
		});
		comMenu.add(new AbstractAction() {
			private static final long serialVersionUID = 2949527942791043067L;

			{
				putValue(Action.NAME, "Forum");
				putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, 1);
			}

			@Override
			public void actionPerformed(ActionEvent ae) {
				String url = tools.replace(Client.config.getForumURL(),"{hostname}",Client.config.getHost());
				url = tools.replace(url, "{comstring}", Client.config.getComstring());
				url = tools.replace(url,"{port}",Integer.toString(Client.config.getPort()));
				url = tools.replace(url,"{sessionid}",client.sessionID);
				try {
			        Desktop.getDesktop().browse(new URI(url));
				} catch (Exception e) {
				        e.printStackTrace();
				}
			}
		});
		comMenu.add(new AbstractAction() {
			private static final long serialVersionUID = 2949527942791043067L;

			{
				putValue(Action.NAME, "Mailbox");
				putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, 0);
			}

			@Override
			public void actionPerformed(ActionEvent ae) {
				String url = tools.replace(Client.config.getMailURL(),"{hostname}",Client.config.getHost());
				url = tools.replace(url, "{comstring}", Client.config.getComstring());
				url = tools.replace(url,"{port}",Integer.toString(Client.config.getPort()));
				url = tools.replace(url,"{sessionid}",client.sessionID);
				url = tools.replace(url,"{uid}",client.userID);
				try {
			        Desktop.getDesktop().browse(new URI(url));
				} catch (Exception ea) {
				        ea.printStackTrace();
				}
			}
		});
        fileMenu.getItem(1).setEnabled(false);
        comMenu.setEnabled(false);
        fontMenu.setEnabled(false);
        roomlist.setEnabled(false);
        userlist.setEnabled(false);
        clear.setEnabled(false);
        addToOutput("<b>"+Client.userAgent+"</b><br><br>");
        if (Client.debug) {
        	addToOutput("<b>Hinweis:</b> Der Client l&auml;uft im Debug-Modus!<br><br>");
        }
        roomlist.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent ae) {
        		client.sendToChat(Client.config.getRoomlist());
        	}
        });
        userlist.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent ae) {
        		client.sendToChat(Client.config.getUserlist());
        	}
        });
        clear.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent ae) {
        		output.setText("");
        	}
        });
        quit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent ae) {
				if (client.connected) {
					doLogout();
					if (client.chat != null) {
						client.chat.stop();
						client.chat = null;
					}
				}
				System.exit(0);
        	}
        });
        scrolling.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent ae) {
        		if (client.scrollflag) {
        			client.scrollflag = false;
        			scrolling.setText("Autoscroll On");
        		} else {
        			output.setCaretPosition(output.getDocument().getLength());
        			client.scrollflag = true;
        			scrolling.setText("Autoscroll Off");
        		}
        	}
        });
		input.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	String text = ((JTextField)e.getSource()).getText().trim();
            	if (text.toLowerCase().equals("/logout") || text.toLowerCase().equals("7logout")
            			|| text.toLowerCase().equals("/quit") || text.toLowerCase().equals("7quit")) {
            		doLogout();
            	} else {
                   	client.sendToChat(text);
                	((JTextField)e.getSource()).setText("");
                	addInput(text);
                	cachefield = -1;
            	}
            }
        });
		KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		kfm.setDefaultFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
		kfm.setDefaultFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
		input.addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e){
				try{
			        if(e.getKeyCode() == KeyEvent.VK_UP){
			        	cachefield++;
		        		String tmp = inputcache[cachefield];
		        		((JTextField)e.getSource()).setText(tmp);
			        }
			        if(e.getKeyCode() == KeyEvent.VK_DOWN){
			        	if(cachefield>=0){
			        		cachefield--;
			        		String tmp = inputcache[cachefield];
			        		((JTextField)e.getSource()).setText(tmp);
			        	}else{
			        		((JTextField)e.getSource()).setText("");
			        	}
			        }
			        if(e.getKeyCode() == KeyEvent.VK_TAB){
			        	String text = ((JTextField)e.getSource()).getText().trim();
			        	autoFillName(text, e);
			        }
				}catch(Exception ee){}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			}
		});
		input.setText("Sobald eine Verbindung hergestellt wurde, wird dieses Feld freigegeben.");
		input.requestFocus();
		input.setCaretPosition(0);
		input.setEnabled(false);
		frame.setSize(800, 600);
		frame.setMinimumSize(new Dimension(600, 480));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private void doLogout() {
		client.doLogout();
		fileMenu.getItem(0).setEnabled(true);
		fileMenu.getItem(1).setEnabled(false);
		input.setEnabled(false);
		input.setText("Sobald eine Verbindung hergestellt wurde, wird dieses Feld freigegeben.");
		comMenu.setEnabled(false);
		fontMenu.setEnabled(false);
		if (client.chat != null) {
			client.chat.stop();
			client.chat = null;
		}
	}
    public void addInput(String input){
    	try{
    		inputcache[9] = inputcache[8];
    		inputcache[8] = inputcache[7];
    		inputcache[7] = inputcache[6];
    		inputcache[6] = inputcache[5];
    		inputcache[5] = inputcache[4];
    		inputcache[4] = inputcache[3];
    		inputcache[3] = inputcache[2];
    		inputcache[2] = inputcache[1];
    		inputcache[1] = inputcache[0];
    		inputcache[0] = input;
    	}catch(Exception e){}
    }
    
	void autoFillName(String input, KeyEvent e){
		try{
        	int pos = ((JTextField)e.getSource()).getCaretPosition();
        	if(pos > 0){
        		boolean whiteflag = false;
        		String crumb = "";
        		String left = input.substring(0,pos);
        		if(left.indexOf(" ")!=-1){
        			crumb = input.substring(left.lastIndexOf(" "), pos).trim();
        			left = input.substring(0,left.lastIndexOf(" ")+1);
        			whiteflag = true;
        		}else{
        			crumb = input.substring(0,pos);
        		}

                ListIterator<String> it = client.users.listIterator();
                while(it.hasNext()){
                	String name = (String)it.next();
                	if(name.toLowerCase().startsWith(crumb.toLowerCase().trim())){
                		String line = "";
                		if(whiteflag){
                			line = left+name;
                		}else{
                			line = name;
                		}
                		((JTextField)e.getSource()).setText(line+" ");
                		break;
                	}
                }
        	}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public void addToOutput(String message) {
		try {
			Element p = houtput.getParagraphElement(houtput.getLength());
			houtput.insertAfterEnd(p, "<div align=left> "+message+" </div>");
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (client.scrollflag) {
			output.setCaretPosition(output.getDocument().getLength());
		}
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent event) {
	    if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
	        try {
	        	String url = event.getDescription();
	        	if (url != null) {
	        		if (url.indexOf("jfchat;jsessionid=") != -1) {
	        			if (url.indexOf("say=") != -1) {
	        				String tmp = url.substring(url.indexOf("say=")+"say=".length());
	        				client.sendToChat(tmp);
	        			} else {
		        			client.openJFChatLink(url);
	        			}
	        		} else if (url.indexOf("jfchat?refout=") != -1) {
	        			String tmp = url.substring(url.indexOf("refout=")+"refout=".length());
	        			client.openInBrowser(tmp);
	        		} else {
	        			client.openInBrowser(url);
	        		}
	        	}
	        } catch(Exception e) {
	        	e.printStackTrace();
	        }
	    }
	}
}
