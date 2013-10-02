package com.mobidict;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class VoiceRecognitionActivity extends Activity implements Runnable {
 private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;
 private static final String ACTION_USB_PERMISSION =
		    "com.android.example.USB_PERMISSION";
 private EditText metTextSpeech;
 //private ListView mlvTextMatches;
 //private Spinner msTextMatches;
 private Button mbtSpeak;
 private Button mbtSend;
 private UsbManager mManager;
 private UsbDevice mDevice;
 private UsbDeviceConnection mDeviceConnection;
 private UsbInterface mInterface;
 private UsbEndpoint  mEndpoint;

 @Override
 public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_voice_recognition);
  metTextSpeech = (EditText) findViewById(R.id.etTextSpeech);
  //mlvTextMatches = (ListView) findViewById(R.id.lvTextMatches);
  //msTextMatches = (Spinner) findViewById(R.id.sNoOfMatches);
  mbtSpeak = (Button) findViewById(R.id.btSpeak);
  mbtSend = (Button) findViewById(R.id.btSend);
  checkVoiceRecognition();
  
  // listen for new devices
  // listen for new devices
  /*
  IntentFilter filter = new IntentFilter();
  filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
  filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
  registerReceiver(mUsbReceiver, filter);
  */
  /*
  mManager = (UsbManager) getSystemService(Context.USB_SERVICE);
  PendingIntent mPermissionIntent;
  HashMap<String, UsbDevice> deviceList = mManager.getDeviceList();
  Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
  
	 while(deviceIterator.hasNext()){ 
		 //Intent intent2 = new Intent(UsbManager.ACTION_USB_DEVICE_ATTACHED);
		 mDevice = deviceIterator.next();
	     
	     //if ( Integer.valueOf(mDevice.getVendorId()) == 1478 && 
	    //	  Integer.valueOf(mDevice.getProductId()) == 36940 ){
	    	   
		     System.out.println(mDevice.getVendorId() + ":" + mDevice.getProductId());
		     System.out.println(mDevice.getClass() + " : " + mDevice.getDeviceSubclass() + " : " + mDevice.getDeviceProtocol()); 
		     System.out.println(mDevice.getDeviceName());
		     //System.out.println(metTextSpeech.getText());
		  //   break;
	     //}
	     //your code
	 }
  
  //if(DEBUG) Log.i(TAG, "Setting PermissionIntent -> MainMenu");
  mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
  //if(DEBUG) Log.i(TAG, "Setting IntentFilter -> MainMenu");
  IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
  //if(DEBUG) Log.i(TAG, "Setting registerReceiver -> MainMenu");
  registerReceiver(mUsbReceiver, filter);
  //if(DEBUG) Log.i(TAG, "Setting requestPermission -> MainMenu");
  mManager.requestPermission(mDevice, mPermissionIntent);
  */
 }
 private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
     @Override
     public void onReceive(Context context, Intent intent) {
         String action = intent.getAction();
         if (ACTION_USB_PERMISSION.equals(action)) {
             synchronized (this) {
                 //UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            	 mDevice = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                 if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                     if(mDevice != null){
                         //call method to set up device communication
                     }
                 } 
                 else {
                     //Log.d(TAG, "permission denied for device " + device);
                	 System.out.println("permission denied for device " + mDevice);
                 }
                 
             }
         }
     }
 };
 public void checkVoiceRecognition() {
  // Check if voice recognition is present
  PackageManager pm = getPackageManager();
  List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
    RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
  if (activities.size() == 0) {
   mbtSpeak.setEnabled(false);
   mbtSend.setEnabled(true);
   mbtSpeak.setText("Voice recognizer not present");
   Toast.makeText(this, "Voice recognizer not present",
     Toast.LENGTH_SHORT).show();
  }
 }

 public void speak(View view) {
  Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

  // Specify the calling package to identify your application
  intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass()
    .getPackage().getName());

  // Display an hint to the user about what he should say.
  //intent.putExtra(RecognizerIntent.EXTRA_PROMPT, metTextHint.getText()
  //  .toString());

  // Given an hint to the recognizer about what the user is going to say
  //There are two form of language model available
  //1.LANGUAGE_MODEL_WEB_SEARCH : For short phrases
  //2.LANGUAGE_MODEL_FREE_FORM  : If not sure about the words or phrases and its domain.
  intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

  // If number of Matches is not selected then return show toast message
/*  if (msTextMatches.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
   Toast.makeText(this, "Please select No. of Matches from spinner",
     Toast.LENGTH_SHORT).show();
   return;
  }

  int noOfMatches = Integer.parseInt(msTextMatches.getSelectedItem()
    .toString());
  // Specify how many results you want to receive. The results will be
  // sorted where the first result is the one with higher confidence.
  intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, noOfMatches);
  //Start the Voice recognizer activity for the result.

   */
  startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
 }

 public void send(View view) {
	 	 
	 UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
	 UsbDevice device = null;
	 HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
	 Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
	 
	 byte[] bytes = null;
	 int TIMEOUT = 0;
	 boolean forceClaim = true;
	 
	 while(deviceIterator.hasNext()){ 
		 //Intent intent2 = new Intent(UsbManager.ACTION_USB_DEVICE_ATTACHED);
	     device = deviceIterator.next();
	     
	     if ( Integer.valueOf(device.getVendorId()) == 1478 && 
	    	  Integer.valueOf(device.getProductId()) == 36940 ){
	    	   
		     System.out.println(device.getVendorId() + ":" + device.getProductId());
		     System.out.println(device.getClass() + " : " + device.getDeviceSubclass() + " : " + device.getDeviceProtocol()); 
		     System.out.println(device.getDeviceName());
		     System.out.println(metTextSpeech.getText());
		     break;
	     }
	     //your code
	 }
	
     StringBuffer sb = new StringBuffer(metTextSpeech.getText());
     bytes = sb.toString().getBytes();
     mInterface = mDevice.getInterface(0);
     //mInterface = mDevice.getInterface(0);
     mEndpoint = mInterface.getEndpoint(0);
     Log.d("jnewman", Integer.toString(mEndpoint.getType()));
     mDeviceConnection = mManager.openDevice(mDevice); 
     mDeviceConnection.claimInterface(mInterface, forceClaim);
     
     if (mDevice != null) {
         UsbDeviceConnection connection = mManager.openDevice(mDevice);
         if (connection != null && connection.claimInterface(mInterface, true)) {
             Log.d("newmanj", "open SUCCESS");
             mDeviceConnection = connection;
             Thread thread = new Thread(this);
             thread.start();
             metTextSpeech.setText("");

         } else {
             Log.d("newmanj", "open FAIL");
             mDeviceConnection = null;
         }
      }
     //conn.bulkTransfer(endpoint, bytes, bytes.length, TIMEOUT); //do in another thread
	 
 }
 @Override
 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  if (requestCode == VOICE_RECOGNITION_REQUEST_CODE)

   //If Voice recognition is successful then it returns RESULT_OK
   if(resultCode == RESULT_OK) {
	   
    ArrayList<String> textMatchList = data
    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

    if (!textMatchList.isEmpty()) {
     // If first Match contains the 'search' word
     // Then start web search.
     if (textMatchList.get(0).contains("search")) {

        String searchQuery = textMatchList.get(0);
                                           searchQuery = searchQuery.replace("search","");
        Intent search = new Intent(Intent.ACTION_WEB_SEARCH);
        search.putExtra(SearchManager.QUERY, searchQuery);
        startActivity(search);
     } else {
         // populate the Matches
    	 //metTextSpeech.
    	 Iterator<String> iter = textMatchList.iterator();
    	 while ( iter.hasNext()){
    		 metTextSpeech.append(iter.next());
    	 }
         //mlvTextMatches.setAdapter(new ArrayAdapter<String>(this,
    	 //android.R.layout.simple_list_item_1,
        //textMatchList));
     }

    }
   //Result code for various error.
   }else if(resultCode == RecognizerIntent.RESULT_AUDIO_ERROR){
    showToastMessage("Audio Error");
   }else if(resultCode == RecognizerIntent.RESULT_CLIENT_ERROR){
    showToastMessage("Client Error");
   }else if(resultCode == RecognizerIntent.RESULT_NETWORK_ERROR){
    showToastMessage("Network Error");
   }else if(resultCode == RecognizerIntent.RESULT_NO_MATCH){
    showToastMessage("No Match");
   }else if(resultCode == RecognizerIntent.RESULT_SERVER_ERROR){
    showToastMessage("Server Error");
   }
  super.onActivityResult(requestCode, resultCode, data);
 }
 /**
 * Helper method to show the toast message
 **/
 void showToastMessage(String message){
  Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
 }

 public void run() {
    /*
	 ByteBuffer buffer = ByteBuffer.allocate(1);
     UsbRequest request = new UsbRequest();
     request.initialize(mDeviceConnection, mEndpoint);
     byte status = -1;
     while (true) {
         // queue a request on the interrupt endpoint
         request.queue(buffer, 1);
         // send poll status command
         //sendCommand(COMMAND_STATUS);
         // wait for status event
         if (mDeviceConnection.requestWait() == request) {
             byte newStatus = buffer.get(0);
             if (newStatus != status) {
                 Log.d("newmanj", "got status " + newStatus);
                 status = newStatus;
                 
                 if ((status & COMMAND_FIRE) != 0) {
                     // stop firing
                     sendCommand(COMMAND_STOP);
                 }
                 
             }
             try {
                 Thread.sleep(100);
             } catch (InterruptedException e) {
             }
         } else {
             Log.e("newmanj", "requestWait failed, exiting");
             break;
         }
     }
     */
	 while(true){
		 Log.e("newmanj", "requestWait failed, exiting");
		 System.out.println("spanky");
		 
		 break;
	 }
 }
 
}
 