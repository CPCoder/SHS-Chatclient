package de.shs.chatclient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/*
 * Project		Chatclient Standalone
 * Filename		Config.java
 * Author		Steffen Haase
 * Date			26.03.2011
 * License		GPL v3
 */

public class Config 
{
	public static String host = "";
	public static String username = "";
	public static String password = "";
	public static String room = "";
	public static String forumurl = "";
	public static String mailurl = "";
	public static String comurl = "";
	public static String comstring = "";
	public static String windowTitle = "";
	public static String roomlist = "/wc";
	public static String userlist = "/w";
	public static String emodb	= "../EMOS";
	public static String emourl = "http://{hostname}:{port}/EMOS";
	public static int port = 9090;
	private static String inputfile_config = "config.txt";
	private static Properties einstellungen = new Properties();

	public Config () throws Exception {
		readProperties();
	}
	
	public String getEmoDB() {
		return emodb;
	}
	
	public String getEmoURL() {
		return emourl;
	}
	
	public String getHost() {
		return host;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getRoom() {
		return room;
	}
	
	public String getForumURL() {
		return forumurl;
	}
	
	public String getComURL() {
		return comurl;
	}
	
	public String getMailURL() {
		return mailurl;
	}
	
	public String getComstring() {
		return comstring;
	}
	
	public int getPort() {
		return port;
	}
	
	public String getWindowTitle() {
		return windowTitle;
	}
	
	public String getRoomlist() {
		return roomlist;
	}

	public String getUserlist() {
		return userlist;
	}
	
	public void setRoom(String room) {
		Config.room = room;
	}

	private void readProperties () throws Exception {
		if (Client.debug) {
			System.out.println("DEBUG readProperties(): Lese Konfigurationsdatei (config.txt) ein ...");
		}
        try
        {
        	FileInputStream fileinputstream;
        	ClassLoader loader = this.getClass().getClassLoader();
        	if (loader == null) {
        		loader = ClassLoader.getSystemClassLoader();
        	}
            fileinputstream = new FileInputStream(inputfile_config);
            einstellungen.load(fileinputstream);
        }
        catch(FileNotFoundException filenotfoundexception)
        {
        	Client.gui.addToOutput("<font color=\"#FF0000\"><b>FEHLER: Kann die Datei >" + inputfile_config + "< nicht finden :(</font>");
        }
        catch(IOException ioexception)
        {
        	Client.gui.addToOutput("<font color=\"#FF0000\"><b>FEHLER: I/O Fehler beim lesen der Datei >" + inputfile_config + "<.</font>");
        }
        String errors = "";
        boolean errflag = false;
        host = einstellungen.getProperty("hostname");
        if (host == null || host.equals("")) {
        	errors += "Bitte \"hostname=\" in der config.txt definieren!<br>";
        	errflag = true;
        }
        if (einstellungen.getProperty("port") == null) {
        	errors += "Bitte \"port=\" in der config.txt definieren!<br>";
        	errflag = true;
        } else {
        	port = Integer.valueOf(einstellungen.getProperty("port"));
        }
        room = einstellungen.getProperty("startroom");
        if (room == null || room.equals("")) {
        	errors += "Bitte \"startroom=\" in der config.txt definieren!<br>";
        	errflag = true;
        }
        username = einstellungen.getProperty("username");
        if (username == null || username.equals("")) {
        	errors += "Bitte \"username=\" in der config.txt definieren!<br>";
        	errflag = true;
        }
        password = einstellungen.getProperty("password");
        if (password == null || password.equals("")) {
        	errors += "Bitte \"password=\" in der config.txt definieren!<br>";
        	errflag = true;
        }
        comurl = einstellungen.getProperty("comurl");
        if (comurl == null || comurl.equals("")) {
        	errors += "Bitte \"comurl=\" in der config.txt definieren!<br>";
        	errflag = true;
        }
        forumurl = einstellungen.getProperty("forumurl");
        if (forumurl == null || forumurl.equals("")) {
        	errors += "Bitte \"forumurl=\" in der config.txt definieren!<br>";
        	errflag = true;
        }
        mailurl = einstellungen.getProperty("mailurl");
        if (mailurl == null || mailurl.equals("")) {
        	errors += "Bitte \"mailurl=\" in der config.txt definieren!<br>";
        	errflag = true;
        }
        comstring = einstellungen.getProperty("comstring");
        if (comstring == null || comstring.equals("")) {
        	errors += "Bitte \"comstring=\" in der config.txt definieren!<br>";
        	errflag = true;
        }
        emodb = einstellungen.getProperty("emodb");
        if (emodb == null || emodb.equals("")) {
        	errors += "Bitte \"emodb=\" in der config.txt definieren!<br>";
        	errflag = true;
        }
        emourl = einstellungen.getProperty("emourl");
        if (emourl == null || emourl.equals("")) {
        	errors += "Bitte \"emourl=\" in der config.txt definieren!<br>";
        	errflag = true;
        }
        if (einstellungen.getProperty("windowtitle") != null) {
        	windowTitle = einstellungen.getProperty("windowtitle");
        }
        if (einstellungen.getProperty("com_wc") != null) {
        	roomlist = einstellungen.getProperty("com_wc");
        }
        if (einstellungen.getProperty("com_w") != null) {
        	userlist = einstellungen.getProperty("com_w");
        }
        if (errflag) {
        	Client.gui.addToOutput(
        			"<font color=\"#FF0000\"><b>FEHLER:</b> Beim Einlesen der Konfigurations-Parameter " +
        			"fehlten Einstellungsparameter:<br>"+errors+"</font><br><br>"
        			);
        	Client.gui.fileMenu.getItem(0).setEnabled(false);
        } else {
            if (Client.debug) {
            	System.out.println("DEBUG readProperties(): OK\n====================\n" +
            			"Hostname : "+host+"\n" +
            			"Port     : "+port+"\n" +
            			"Comstring: "+comstring+"\n" +
            			"Startroom: "+room+"\n" +
            			"Username : "+username+"\n" +
            			"Password : "+password+"\n" +
            			"Forum-URL: "+forumurl+"\n" +
            			"Com-URL  : "+comurl+"\n" +
            			"Mail-URL : "+mailurl+"\n" +
            			"Emo-DB   : "+emodb+"\n" +
            			"Emo-URL  : "+emourl+"\n" +
            			"====================\n\n");
            }
        }
	}
}
