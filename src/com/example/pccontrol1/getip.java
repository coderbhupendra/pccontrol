package com.example.pccontrol1;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;



import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import com.mdg.pccontrol1.R;

public class getip extends Activity {
	ServerSocket server;
	Socket fromClient;
	Thread Thread;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		//System.out.print("FunChat  started..........\n");

		
		sendpostions();
		
		////////////////////
		String ip;
		try {
		    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		    while (interfaces.hasMoreElements()) {
		        NetworkInterface iface = interfaces.nextElement();
		        // filters out 127.0.0.1 and inactive interfaces
		        if (iface.isLoopback() || !iface.isUp())
		            continue;

		        Enumeration<InetAddress> addresses = iface.getInetAddresses();
		        while(addresses.hasMoreElements()) {
		            InetAddress addr = addresses.nextElement();
		            ip = addr.getHostAddress();
		          //  System.out.println("WW  "+iface.getDisplayName() + " " + ip);
		        }
		    }
		} catch (SocketException e) {
		    throw new RuntimeException(e);
		}
		
		///////////////////
		
		
		/////////////////////
		
		System.out.println(getLocalIpAddress());
		
		///////////
	}

	public String getLocalIpAddress() {
		int ip=0; String ipadd="";
	    try {
	    	
	        for (Enumeration<NetworkInterface> en = NetworkInterface
	                .getNetworkInterfaces(); en.hasMoreElements();) {
	            NetworkInterface intf = en.nextElement();
	            for (Enumeration<InetAddress> enumIpAddr = intf
	                    .getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = enumIpAddr.nextElement();
	                ip++;
	                ipadd=inetAddress.getHostAddress().toString();
	                System.out.println("ip1--:" + inetAddress);
	                System.out.println("ip2--:" + inetAddress.getHostAddress());

	     
	            }
	        }
	    } catch (Exception ex) {
	       
	    }
	    return ipadd;
	}

	 private void sendpostions() {
			// TODO Auto-generated method stub
	    	
	    	//Log.d(DEBUG_TAG, "onscroll: " + "sendpostion");
	    	new LongOperation().execute();
		}
	
	////////////////
	private class LongOperation extends AsyncTask<String, String, String> {
    protected String doInBackground(String... params) {


	try {
		server = new ServerSocket(8080);
		fromClient = server.accept();
	//	System.out.println("wwgg" + fromClient.toString());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
	
	


return "";
    }	
}//class LongOperation ended
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
