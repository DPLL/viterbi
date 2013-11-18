package com.example.voicerecognition2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/* Speech recognition example from 
 * http://androidbite.blogspot.com/2013/04/android-voice-recognition-example.html
 */
public class VoiceActivity extends Activity {

	private static final int REQUEST_CODE = 1234;
	private ListView resultList;
	Button speakButton;

	String SERVER_IP = "172.16.180.159"; //TODO: update IP address here
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.voice_demo);

		speakButton = (Button) findViewById(R.id.speakButton); 

		resultList = (ListView) findViewById(R.id.list);

		// Disable button if no recognition service is present
		PackageManager pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (activities.size() == 0) {
			speakButton.setEnabled(false);
			Toast.makeText(getApplicationContext(), "Recognizer Not Found", Toast.LENGTH_LONG).show();
		}
		
		speakButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startVoiceRecognitionActivity();
			}
		});
		
	}

	private void startVoiceRecognitionActivity() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
				"AndroidBite Voice Recognition...");
		startActivityForResult(intent, REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			android.util.Log.i("MATCH", matches.toString());

			resultList.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, matches));
			
			String [] s = new String[matches.size()];
			new UDPClientAsyncTask().execute(s);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public class UDPClientAsyncTask extends AsyncTask <String, String, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			UDPClient client;
			try {
				client = new UDPClient("");
				client.send(params);
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
	}
 
	public class UDPClient {
		
		protected DatagramSocket sock;
		protected InetAddress otherAddr;
		int port = 9999;
	 
		public UDPClient(String serverAddr) throws SocketException, UnknownHostException {
			sock = new DatagramSocket();
			otherAddr = InetAddress.getByName(serverAddr);
		}
	 
		protected void send (String[] params) throws IOException {
			if (params.length > 0){
				byte [] data = params.toString().getBytes(); //send all items in the list
				DatagramPacket packet = new DatagramPacket(data, data.length, otherAddr, port);
				sock.send(packet);
			}
		}
	}
}
