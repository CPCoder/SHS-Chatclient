package de.shs.chatclient;

import java.awt.Desktop;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

/*
 * Project		Chatclient Standalone
 * Filename		Client.java
 * Author		Steffen Haase
 * Date			26.03.2011
 * License		GPL v3
 */

public class Client extends WindowAdapter {
	
	public static String app_version = "2.1.3";
	public static String app_name = "SHS-Chatclient";
	public static String copyright = "(c) 2011-2012 by Steffen Haase Software";
	public static String userAgent = app_name+" v"+app_version+" Copyright "+copyright;
	public static boolean debug = false;

	public String sessionID = "";
	public String userID = "";
	public String host = "";
	public String windowTitle = app_name+" v"+app_version+" Copyright "+copyright;
	public String windowTitle_ = "";
	public int port = 9090;
	public boolean connected = false;
	public boolean active = true;
	public boolean scrollflag = true;
	public boolean chatting = false;
	public String fSize = "10";
	public String fStyle = "Arial";
	public List<String> users;
	public Hashtable<String, String> userdata = new Hashtable<String, String>();

	private static final long serialVersionUID = 3523084126336936084L;

	JPanel jpanel1 = new JPanel();
	JPanel jpanel2 = new JPanel();
	JPanel buttonPanel = new JPanel();
    JEditorPane output = new JEditorPane();
    JScrollPane jScrollPane1;
	HTMLDocument houtput = null;

    JTextField input;
    JButton scrolling = new JButton("Autoscroll Off");
    JButton clear = new JButton("Clear");
    JButton roomlist = new JButton("Raumliste");
    JButton userlist = new JButton("Userliste");
    JMenu fileMenu;
    JMenu fontMenu;
	JMenu comMenu;
	About about;
	Chatting chat;
	Blink blink = null;
	
	static Config config;
	static Tools tools;
	static Client client1;
	static GUI gui = null;
	

    public Client() { }
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length >= 1 && args[0].equals("debug")){
			debug = true;
		}
		client1 = new Client();
		try {
			//client1.buildGUI();
			gui = new GUI(client1);
			config = new Config();
			String tmp = config.getWindowTitle();
			if (!tmp.equals("")) {
				client1.windowTitle = tmp;
				GUI.frame.setTitle(tmp);  
			}
			tools = new Tools(client1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addToOutput(String message) {
		try {
			Element p = gui.houtput.getParagraphElement(gui.houtput.getLength());
			gui.houtput.insertAfterEnd(p, "<div align=left> "+message+" </div>");
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (scrollflag) {
			gui.output.setCaretPosition(gui.output.getDocument().getLength());
		}
	}
	
	public void getUserID() throws Exception{
		addToOutput("<b>Client:</b> UserID wird angefordert.");
		String request = "http://"+config.getHost()+":"+config.getPort()+config.getComstring()+
						";jsessionid="+sessionID+"?showhtml=getuserid";
		request = tools.buildRequest(request);
		if (debug) {
			System.out.println(
					"DEBUG getUserID(): Server-Request\n====================\n" + request +
					"====================\n\n"
					);
		}
        PrintWriter printwriter;
        BufferedReader bufferedreader;
        Socket socket = new Socket(InetAddress.getByName(config.getHost()), config.getPort());
        printwriter = new PrintWriter(socket.getOutputStream());
        bufferedreader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printwriter.print(request);
        printwriter.flush();
        
        userID = null;
        if(userID == null)
        {
            String line;
            String response = "";
            while((line = bufferedreader.readLine()) != null) {
               	response += line;
            }
            if (!response.equals("")) {
               	if (debug) {
               		System.out.println(
               				"DEBUG getUserID(): Server-Response\n====================\n" + response +
               				"\n====================\n\n"
               				);
               	}
            	String ltag = "<userid>";
    			String rtag = "</userid>";
    			if (response.indexOf("<userid>") != -1 && response.indexOf("</userid>") != -1) {
    				int i = response.indexOf(rtag);
    				userID = response.substring(response.indexOf(ltag) + ltag.length(), i);
    				userID = userID.trim();
    			}
	            if(userID != null) {
            		System.out.println("DEBUG getUserID(): Extrahierte UserID: "+userID+"\n");
	            	addToOutput("<b>Client:</b> OK, habe meine UserID bekommen.");
	            	if (userID.equals("-1")) {
	            		addToOutput("<b>Client:</b> Du bist als Gast eingeloggt.");
	            	}
	            }
            } else {
            	if (debug) {
            		System.out.println("DEBUG getUserID(): Konnte keine UserID auslesen!");
            	}
                addToOutput("<b>Client:</b> Bekomme leider meine UserID nicht vom Server.");
            }
        }
	}
	
	public boolean getSession() throws Exception{
		addToOutput("<b>Client:</b> SessionID wird angefordert.");
		String request = "http://"+config.getHost()+":"+config.getPort()+config.getComstring(); 
		request = tools.buildRequest(request);
		if (debug) {
			System.out.println(
					"DEBUG getSession(): Server-Request\n====================\n" + request +
					"====================\n\n"
					);
		}
        PrintWriter printwriter;
        BufferedReader bufferedreader;
        Socket socket = new Socket(InetAddress.getByName(config.getHost()),config.getPort());
        printwriter = new PrintWriter(socket.getOutputStream());
        bufferedreader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printwriter.print(tools.buildRequest(config.getComstring()));
        printwriter.flush();
        sessionID = null;
        if (sessionID == null) {
            String line;
            String response = "";
            while ((line = bufferedreader.readLine()) != null) {
            	response += line;
            }
            if (!response.equals("")) {
               	if (debug) {
               		System.out.println(
               				"DEBUG getSession(): Server-Response\n====================\n" + response +
               				"\n====================\n\n"
               				);
               	}
               	String lsession = ";jsessionid=";
				String rsession = "showhtml=";
				if (response.indexOf(lsession) != -1 && response.indexOf(rsession) != -1) {
					int i = response.indexOf(rsession);
					int x = i-1;
					sessionID = response.substring(response.indexOf(lsession) + lsession.length(), x);
					sessionID = sessionID.trim();
				}
				if(sessionID != null) {
					if (debug) {
						System.out.println("DEBUG getSession(): Extrahierte SessionID = "+sessionID+"\n\n");
                   	}
					addToOutput("<b>Client:</b> OK, habe eine SessionID bekommen.");
					return true;
				}
            } else {
            	if (debug) {
            		System.out.println("DEBUG getSession(): Konnte keine SessionID auslesen!");
            	}
            	addToOutput(
                    		"<b>Client:</b> Bekomme leider keine Antwort vom Server!" +
            				"Um heraus zu finden was schiefgelaufen ist, wird empfohlen den Chatclient " +
            				"im Debug-Modus zu starten."
                    		);
            	return false;
            }
        }
        return false;
	}

    public boolean doLogin() throws Exception {
   		addToOutput("<b>Client:</b> Login wird durchgeführt ...");
		String request = "http://"+config.getHost()+":"+config.getPort()+config.getComstring()+
						";jsessionid="+sessionID+"?login="+config.getUsername()+
						"&password="+config.getPassword();
		request = tools.buildRequest(request);
		if (debug) {
			System.out.println(
					"DEBUG doLogin(): Server-Request\n====================\n" + request +
					"====================\n\n"
					);
		}
        PrintWriter printwriter;
        BufferedReader bufferedreader;
        Socket socket = new Socket(InetAddress.getByName(config.getHost()), config.getPort());
        printwriter = new PrintWriter(socket.getOutputStream());
        bufferedreader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printwriter.print(request);
        printwriter.flush();
        
        String line = "";
        String response = "";
		do
		{
			if ((line = bufferedreader.readLine()) == null) {
				break;
			}
			response += line;
		} while(true);
        if (!response.equals("")) {
           	if (debug) {
           		System.out.println(
           				"DEBUG doLogin(): Server-Response\n====================\n" + response +
           				"\n====================\n\n"
           				);
           	}
           	response = tools.replaceHTML(response);
           	if (response.indexOf("Passwort ist falsch") != -1) {
           		addToOutput("<font color=\"#FF0000\"><b>Client: Das Passwort ist falsch!</b></font>");
           		return false;
           	}
           	if (response.indexOf("Nick ist zur Zeit gesperrt") != -1) {
           		addToOutput("<font color=\"#FF0000\"><b>Client: Dieser Nick ist zur Zeit gesperrt!</b></font>");
           		return false;
           	}
           	if (response.indexOf("Du wurdest aus dieser Community ausgeschlossen") != -1) {
           		addToOutput("<font color=\"#FF0000\"><b>Client: Du wurdest aus dieser Community ausgeschlossen!</b></font>");
           		return false;
           	}
           	if (response.indexOf("Der Gastzugang ist deaktiviert") != -1) {
           		addToOutput("<font color=\"#FF0000\"><b>Client: Der Gastzugang ist deaktiviert!</b></font>");
           		return false;
           	}
           	if (response.indexOf("Dieser Username ist nicht registriert") != -1) {
           		addToOutput("<font color=\"#FF0000\"><b>Client: Dieser Username ist nicht registriert!</b></font>");
           		return false;
           	}
           	return true;
        } else {
        	if (debug) {
        		System.out.println("DEBUG doLogin(): Es konnte keine Antwort vom Server empfangen werden!");
        	}
        	addToOutput(
        				"<b>Client:</b> Der Loginvorgang konnte nicht durchgef&uuml;hrt werden!<br>" +
        				"Um heraus zu finden was schiefgelaufen ist, wird empfohlen den Chatclient " +
        				"im Debug-Modus zu starten."
        				);
        	return false;
        }
	}
	
	public void clearUserlist() {
		gui.uslist.userlist.setText("Die Userliste wird geladen, sobald eine Verbindung zum Chat besteht.");
	}
	
	public boolean getUserlist() throws Exception{
		if(chatting) {
        	Charset charset = Charset.forName("ISO-8859-1");
        	CharsetDecoder decoder = charset.newDecoder();
			String request = "http://"+config.getHost()+":"+config.getPort()+config.getComstring()+
							";jsessionid="+sessionID+"?showhtml=nickliste&auth=&design=0";
			request = tools.buildRequest(request);
			if (debug) {
				System.out.println(
						"DEBUG getUserlist(): Server-Request\n====================\n" + request +
						"====================\n\n"
						);
			}
	        PrintWriter printwriter;
	        BufferedReader bufferedreader;
	        Socket socket = new Socket(InetAddress.getByName(config.getHost()), config.getPort());
	        printwriter = new PrintWriter(socket.getOutputStream());
	        bufferedreader = new BufferedReader(new InputStreamReader(socket.getInputStream(), decoder));
	        printwriter.print(request);
	        printwriter.flush();
	        
	        String line = "";
	        String response = "";
			do
			{
				if ((line = bufferedreader.readLine()) == null) {
					break;
				}
				response += line;
			} while(true);
	        if (!response.equals("")) {
	           	if (debug) {
	           		System.out.println(
	           				"DEBUG getUserlist(): Server-Response\n====================\n" + response +
	           				"\n====================\n\n"
	           				);
	           	}
	           	response = response.substring(response.indexOf("<html>"));
	    		try {
					tools.getUserData(response);
				} catch (Exception e) {
					e.printStackTrace();
				}
	           	String users = getUsers(response);
	           	String roomname = getRoomName(response);
           		gui.uslist.userlist.setText(roomname+users);
	           	return true;
	        } else {
	        	if (debug) {
	        		System.out.println("DEBUG getUserlist(): Es konnte keine Antwort vom Server empfangen werden!");
	        	}
	        	addToOutput(
	        				"<b>Client:</b> Die Userliste konnte nicht angefordert werden!<br>" +
	        				"Um heraus zu finden was schiefgelaufen ist, wird empfohlen den Chatclient " +
	        				"im Debug-Modus zu starten."
	        				);
	        	return false;
	        }
		}
		return false;
	}
	
	private String getRoomName(String data) throws Exception{
		String roomname = tools.getValue(data, "<!--rnameb-->", "<!--rnamee-->");
		roomname = "<b>"+roomname+"</b><hr>";
		return roomname;
	}
	
	private String getUsers(String data) throws Exception{
		users = null;
		users = new ArrayList<String>();
		String userlist = "";
		String begin = "<!--unameb-->";
		String end = "<!--unamee-->";
		String _tmp = data;
		
		while (_tmp.indexOf(begin) != -1) {
			int i = _tmp.indexOf(begin)+begin.length();
			int k = _tmp.indexOf(end);
			int m = k+end.length();
			users.add(_tmp.substring(i,k).toLowerCase().trim());
			_tmp = _tmp.substring(m);			
		}
		Collections.sort(users);
        ListIterator<String> it = users.listIterator();
        while(it.hasNext()){
        	String tmp = (String)it.next();
        	System.out.println("tmp: "+tmp);
        	StringTokenizer tokenizer = new StringTokenizer((String)userdata.get(tmp), ":");
        	String username = tokenizer.nextToken();
        	String userid = tokenizer.nextToken();
        	String isaway = tokenizer.nextToken();
        	String userline = "<a href=\"http://"+config.getHost()+":"+config.getPort()+config.getComstring()+";jsessionid="+sessionID+
			"?showreg="+userid+"&auth=1&design=0\">"+username+"</a><br>";
    		if (isaway.equals("true")) {
    			userline = "<i>"+userline+"</i>"; 
    		}
    		userlist += userline;
        }
		return userlist;
	}

	public void sendToChat(String message) {
        try {
        	message = URLEncoder.encode(message, "iso-8859-1");
    		String request = "http://"+config.getHost()+":"+config.getPort()+config.getComstring()+
    						";jsessionid="+sessionID+"?auth=1&say="+message;
    		request = tools.buildRequest(request);
        	Socket socket = new Socket(InetAddress.getByName(config.getHost()), config.getPort());
			PrintWriter printwriter = new PrintWriter(socket.getOutputStream());
			printwriter.print(request);
			printwriter.flush();
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doLogout() {
        try {
    		String request = "http://"+config.getHost()+":"+config.getPort()+config.getComstring()+
    						";jsessionid="+sessionID+"?logout=true&auth=";
    		request = tools.buildRequest(request);
        	Socket socket = new Socket(InetAddress.getByName(config.getHost()), config.getPort());
			PrintWriter printwriter = new PrintWriter(socket.getOutputStream());
			printwriter.print(request);
			printwriter.flush();
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void openInBrowser(String url) {
        try {
			Desktop.getDesktop().browse(new URI(url));
		} 
        catch (IOException e) { } 
		catch (URISyntaxException e) { }
	}

	public void openJFChatLink(String url) {
		url = "http://"+config.getHost()+":"+config.getPort()+"/servlet/"+url;
		try {
	        Desktop.getDesktop().browse(new URI(url));
		} catch (Exception e) {
		        e.printStackTrace();
		}
	}
	
	@Override
	public void windowActivated(WindowEvent e) {
		GUI.frame.setTitle(windowTitle);
		active = true;
	}
	
	@Override
	public void windowDeactivated(WindowEvent e) {
		active = false;
	}
	
	@Override
    public void windowClosing(WindowEvent e) {
		if (connected) {
			doLogout();
		}
		GUI.frame.dispose();
		System.exit(1);
	}
}
