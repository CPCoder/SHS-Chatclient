package de.shs.chatclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/*
 * Project		SHS-Chatclient
 * Filename		Client.java
 * Author		Steffen Haase
 * Date			26.03.2011
 * License		GPL v3
 */

public class Chatting implements Runnable
{
	private Client client = null;
	private Tools tools = null;
	Thread thread;
	
	public Chatting(Client client) {
		this.client = client;
		tools = new Tools(client);
	}
	
	public synchronized void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public synchronized void stop() {
		if (thread != null) {
			thread = null;
		}
	}

	@Override
	public void run() {
        try {
        	Charset charset = Charset.forName("ISO-8859-1");
        	CharsetDecoder decoder = charset.newDecoder();

    		if (!Client.debug) {
    			System.out.println("DEBUG: Betrete den Chat (" + Client.config.getRoom() + ") ...");
    		}
    		String request = "http://"+Client.config.getHost()+":"+Client.config.getPort()+Client.config.getComstring()+
    						";jsessionid="+client.sessionID+"?chatten=true&auth=&room="+Client.config.getRoom();
    		request = tools.buildRequest(request);
    		if (Client.debug) {
    			System.out.println(
    					"DEBUG run(): Server-Request\n====================\n" + request +
    					"====================\n\n"
    					);
    		}
            PrintWriter printwriter;
            BufferedReader bufferedreader;
            Socket socket = new Socket(InetAddress.getByName(Client.config.getHost()), Client.config.getPort());
            printwriter = new PrintWriter(socket.getOutputStream());
            bufferedreader = new BufferedReader(new InputStreamReader(socket.getInputStream(), decoder));
            printwriter.print(request);
            printwriter.flush();

			client.connected = true;
			Client.gui.roomlist.setEnabled(true);
			Client.gui.userlist.setEnabled(true);
			Client.gui.clear.setEnabled(true);
			Client.gui.input.setEnabled(true);
			Client.gui.input.requestFocus();
			Client.gui.input.setCaretPosition(0);
			boolean flag = false;
			while(true){
				try{
					if (!client.chatting) {
						client.chatting = true;
						if (!client.getUserlist()) {
							Client.gui.uslist.userlist.setText("Leider konnte die Userliste nicht angefordert werden!");
						}
					}
					String line = bufferedreader.readLine();
					String tmp = "";
					if(line == null){
						break;
					}
					if (Client.debug) {
						System.out.println("RAW-DATA FROM CHATSERVER: "+line);
					}
					if(line.indexOf("<body onFocus")!=-1) flag = true;
					if(flag){
						if(!line.equals("<!-- -->") && !line.equals("")){
							if (line.toLowerCase().indexOf("<script>parent.nickliste.location.reload()</script>") != -1) {
								client.getUserlist();
							}
							if(line.toLowerCase().indexOf("<script")!=-1){
								tmp = line.substring(0, line.toLowerCase().indexOf("<script"));
								line = tmp + line.substring(line.toLowerCase().indexOf("</script>")+"</script>".length(), line.length());
							}
							String emoURL = Client.config.getEmoURL();
							emoURL = tools.replace(emoURL, "{hostname}", Client.config.getHost());
							emoURL = tools.replace(emoURL, "{port}", String.valueOf(Client.config.getPort()));
							line = tools.replace(line, Client.config.getEmoDB(), emoURL);
							String gfxURL = "http://"+Client.config.getHost()+":"+Client.config.getPort()+"/gfx";
							line = tools.replace(line, "../gfx", gfxURL);
							System.out.println("line: "+line);
							client.addToOutput(line);
							if (!client.active && client.blink == null) {
								client.blink = new Blink(client, GUI.frame);
								client.blink.run();
							} else {
								if (client.blink != null) {
									client.blink = null;
								}
							}
						}
					}
				}catch(Exception ex){
					ex.printStackTrace();
					break;
				}
			}
			client.chatting = false;
			client.clearUserlist();
			Client.gui.roomlist.setEnabled(false);
			Client.gui.userlist.setEnabled(false);
			Client.gui.clear.setEnabled(false);
			client.addToOutput("<font color=\"#FF0000\"><b>Client: Die Verbindung zum Chatserver wurde beendet!</b></font>");
			try{
				if(thread != null){
					thread = null;
				}
			}catch(Exception ee){
				ee.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}