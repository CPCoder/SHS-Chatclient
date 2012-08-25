package de.shs.chatclient;

import java.util.Date;
import java.util.StringTokenizer;

/*
 * Project		Chatclient Standalone
 * Filename		Tools.java
 * Author		Steffen Haase
 * Date			26.03.2011
 * License		GPL v3
 */

public class Tools {
	private Client client = null;
	
	public Tools (Client client) {
		this.client = client;
	}
	
	public String buildRequest (String GET) throws Exception {
        Date date = new Date();
		String request = "GET "+GET+" HTTP/1.0\r\n" +
				"Date: "+date+"\r\n" +
				"MIME-Version: 1.0\r\n"+
				"User-Agent: "+Client.userAgent+"\r\n" +
				"Host: "+Client.config.getHost()+":"+Integer.toString(Client.config.getPort())+"\r\n" +
				"Connection: Close\r\n\r\n";
		return request;
	}

	public String replace(String data, String what, String replace){
        String tmp1 = data.toLowerCase();
        String tmp2 = what.toLowerCase();
        int i = what.length();
        StringBuffer stringbuffer = new StringBuffer();
        int j = 0;
        for(int k = tmp1.indexOf(tmp2, j); k > -1; k = tmp1.indexOf(tmp2, j)){
            try{
                stringbuffer.append(data.substring(j, k));
                stringbuffer.append(replace);
                j = k + i;
            }catch(StringIndexOutOfBoundsException stringindexoutofboundsexception){
                j++;
            }
        }
        try{
            stringbuffer.append(data.substring(j));
        }catch(StringIndexOutOfBoundsException stringindexoutofboundsexception1){}
        String result = stringbuffer.toString();
        return result.trim();
    }

	public String replaceHTML (String data) throws Exception
	{
		data = replace(data, "<", "&lt;");
		data = replace(data, ">", "&gt;");
		return data;
	}

	public void getUserData(String data) {
		String begin = "<!--udatab ";
		String end = " udatae-->";
		String _tmp = data;
		StringTokenizer tokenizer;
		client.userdata.clear();
		
		while (_tmp.indexOf(begin) != -1) {
			int i = _tmp.indexOf(begin)+11;
			int k = _tmp.indexOf(end);
			int m = k+10;
			String udata = _tmp.substring(i,k).trim();
			if (Client.debug) {
				System.out.println("udata: "+udata);
			}
			tokenizer = new StringTokenizer(udata, ":");
			try {
				String username = tokenizer.nextToken();
				String userid = tokenizer.nextToken();
				String isaway = tokenizer.nextToken();
				client.userdata.put(username.toLowerCase(), username+":"+userid+":"+isaway);
			} catch (Exception e) {
				e.printStackTrace();
			}
			_tmp = _tmp.substring(m);
			tokenizer = null;
		}
	}
	
	public String getValue(String data, String paraBegin, String paraEnd) throws Exception{
		String value = null;
		if (data.indexOf(paraBegin) != -1) {
			int i = data.indexOf(paraBegin)+paraBegin.length();
			int k = data.indexOf(paraEnd);
			value = data.substring(i, k);
		}
		return value;
	}

}
