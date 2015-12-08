package com.example.project;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.objects.TagContentDataSource;
import com.example.objects.TagInfo;

public class TransferInfo extends Activity {

	private NfcAdapter myNfcAdapter;
	private PendingIntent pendingIntent;
	private IntentFilter[] intentFiltersArray;
	private String[][] techListsArray;
	private CustomDialog dialog;
	private TextView writeResult;
	private NdefMessage content;
	private TagInfo tInfo;
	private RelativeLayout pContent;
	private RelativeLayout cContent;
	private RelativeLayout rContainer;
	private RelativeLayout container;
	private TagContentDataSource datasource;
	private String writeMessage = "";
	public String payloadForBack;
	public String typeforBack;
	public String idForBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transfer_content);

		myNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		
		writeResult = (TextView) findViewById(R.id.writeResult);
		pContent = (RelativeLayout) findViewById(R.id.pContent_container);
		cContent = (RelativeLayout) findViewById(R.id.cContent_container);
		rContainer = (RelativeLayout) findViewById(R.id.resultContainer);
		container = (RelativeLayout) findViewById(R.id.main_container);

		content = getIntent().getParcelableExtra("TAG_CONTENT");
		datasource = new TagContentDataSource(this);
		
		datasource.open();
		List<TagUIContent> tagUIContents = datasource.getTagUIContents();
		if (getIntent().getStringExtra("CONTENT_EDIT").equals("NEW")) {
			TagUIContent tagUI = tagUIContents.get(datasource.getTagUIContents().size() - 1);
			idForBack = tagUI.getContentId().getText().toString();
			payloadForBack = tagUI.getPayload().getText().toString();
			typeforBack = tagUI.getContentDesc().getText().toString();
		} else {
			for (TagUIContent tagUIContent : tagUIContents) {
				Log.d("debug edit tranfer", tagUIContent.getContentId().getText().toString()+ " - " + getIntent().getStringExtra("CONTENT_ID"));
				if (tagUIContent.getContentId().getText().toString().equals(getIntent().getStringExtra("CONTENT_ID"))) {
					idForBack = tagUIContent.getContentId().getText().toString();
					payloadForBack = tagUIContent.getPayload().getText().toString();
					typeforBack = tagUIContent.getContentDesc().getText().toString();
				}
			}
		}
		datasource.close();
		
		dialog = new CustomDialog(this);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.write_tag_dialog);

		checkNFCConnection();

		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);

		try {
			ndef.addDataType("*/*");
		}

		catch (MalformedMimeTypeException e) {
			throw new RuntimeException("fail", e);
		}
		intentFiltersArray = new IntentFilter[] { ndef, };

		techListsArray = new String[][] {
				new String[] { NfcA.class.getName(), Ndef.class.getName() },
				{ MifareUltralight.class.getName() } };

		
		//datasource.open();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		myNfcAdapter.disableForegroundDispatch(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		myNfcAdapter.enableForegroundDispatch(this, pendingIntent,
				intentFiltersArray, techListsArray);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		checkNFCConnection();
	}
	
	public void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		datasource.open();
		if (dialog.isShowing()) {
			dialog.dismiss();
		}

		Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		Ndef ndef = Ndef.get(detectedTag);

		tInfo = new TagInfo(detectedTag, intent, this);
		NdefMessage[] messages = getNdefMessages(intent);

		container.setVisibility(View.VISIBLE);

		Log.d("debug", "Starting writing process");
		boolean result = writeNdefMessageToTag(content, detectedTag);
		if (result) {
			writeResult.setText(writeMessage);
			List<TagUIContent> tagUIContents = datasource.getTagUIContents();

			if (getIntent().getStringExtra("CONTENT_EDIT").equals("NEW")) {
				TagUIContent tagUI = tagUIContents.get(datasource.getTagUIContents().size() - 1);
				cContent.addView(tagUI);
				idForBack = tagUI.getContentId().getText().toString();
				payloadForBack = tagUI.getPayload().getText().toString();
				typeforBack = tagUI.getContentDesc().getText().toString();
			} else {

				for (TagUIContent tagUIContent : tagUIContents) {
					Log.d("debug edit tranfer", tagUIContent.getContentId()
							.getText().toString()
							+ " - " + getIntent().getStringExtra("CONTENT_ID"));
					if (tagUIContent.getContentId().getText().toString()
							.equals(getIntent().getStringExtra("CONTENT_ID"))) {
						Log.d("debug info", "dentro del if");
						cContent.addView(tagUIContent);
						idForBack = tagUIContent.getContentId().getText().toString();
						payloadForBack = tagUIContent.getPayload().getText().toString();
						typeforBack = tagUIContent.getContentDesc().getText().toString();

					}
				}
			}

			if (messages != null) {
				TagUIContent nTagUIContent = new TagUIContent(this);
				String ploadString = "";
				if (tInfo.getTagRecords().get(0).isWOP()) {
					if (tInfo.getTagRecords().get(0).getRecordType()
							.equalsIgnoreCase("Text")) {
						ploadString = " ("
								+ tInfo.getTagRecords().get(0)
										.getRecordLanguageCode().toUpperCase()
								+ ")";
					}
				} else {
					ploadString = tInfo.getTagRecords().get(0)
							.getRecordPayloadHeaderDesc();
				}

				nTagUIContent.setPayload(ploadString
						+ tInfo.getTagRecords().get(0).getRecordPayload());
				nTagUIContent.setContentDesc(tInfo.getTagRecords().get(0)
						.getRecordPayloadTypeDesc());
				nTagUIContent.setContentIcon(tInfo.getTagRecords().get(0)
						.getRecordPayloadTypeDesc());
				nTagUIContent.setContentId(String.valueOf(-1));
				pContent.addView(nTagUIContent);
			}
			rContainer.setVisibility(View.VISIBLE);
		} else {
			writeResult.setText(writeMessage);
		}
		datasource.close();
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.doneButton:
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			this.finish();
			break;

		default:
			break;
		}
	}

	private boolean writeNdefMessageToTag(NdefMessage message, Tag detectedTag) {
		// TODO Auto-generated method stub

		int size = message.toByteArray().length;

		Log.d("debug", "Before TRY");
		try {
			Ndef ndef = Ndef.get(detectedTag);
			if (ndef != null) {
				ndef.connect();
				Log.d("debug", "After Connect");
				if (!ndef.isWritable()) {
					writeMessage = getResources().getString(R.string.tc_ro);
					return false;
				}
				if (ndef.getMaxSize() < size) {
					Log.d("debug erase", "Tag data can't written to tag, Tag capacity is "
									+ ndef.getMaxSize() + "bytes, message is"
									+ size + " bytes.");
					
					String msg = getResources().getString(R.string.tc_oom);
					writeMessage = String.format(msg, ndef.getMaxSize(), size);
					return false;
				}
				ndef.writeNdefMessage(message);
				ndef.close();
				writeMessage = getResources().getString(R.string.tc_ok);
				return true;
			} else {
				NdefFormatable ndefFormat = NdefFormatable.get(detectedTag);
				if (ndefFormat != null) {
					try {
						ndefFormat.connect();
						ndefFormat.format(message);
						ndefFormat.close();
						writeMessage = getResources().getString(R.string.tc_ok);
						return true;
					} catch (Exception e) {
						// TODO: handle exception
						/*Toast.makeText(this, getResources().getString(R.string.tc_ff),
								Toast.LENGTH_SHORT).show();*/
						writeMessage = getResources().getString(R.string.tc_ff);
						return false;
					}

				} else {
					writeMessage = getResources().getString(R.string.tc_ndef);
					return false;
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("debug", "Exception: " + e.toString());
			writeMessage = getResources().getString(R.string.tc_wrong) + writeMessage;
			return false;
		}

	}
	
	private NdefMessage[] getNdefMessages(Intent intent) {
		// TODO Auto-generated method stub
		NdefMessage[] message = null;
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
			Log.d("debug", "I found some shit.");
			Parcelable[] rawMessages = intent
					.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMessages != null) {
				message = new NdefMessage[rawMessages.length];
				for (int i = 0; i < rawMessages.length; i++) {
					message[i] = (NdefMessage) rawMessages[i];
				}
			} else {
				Log.d("debug", "0 Ndef Messages.");
				byte[] empty = new byte[] {};
				NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN,
						empty, empty, empty);
				NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
				message = new NdefMessage[] { msg };
			}
		} else if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
			Log.d("debug", "NDEF intent.");
			Log.d("debug", "I found some shit.");
			Parcelable[] rawMessages = intent
					.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMessages != null) {
				message = new NdefMessage[rawMessages.length];
				for (int i = 0; i < rawMessages.length; i++) {
					message[i] = (NdefMessage) rawMessages[i];
				}
			} else {
				byte[] empty = new byte[] {};
				NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN,
						empty, empty, empty);
				NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
				message = new NdefMessage[] { msg };
			}
		} else {
			Log.d("debug", "Unknow intent.");
			finish();
		}
		return message;
	}

	private void checkNFCConnection(){
		if (myNfcAdapter != null) {
			Log.d("debug NFC Connection", "NFC is available for the device");
			if (myNfcAdapter.isEnabled()) {
				Log.d("debug NFC Connection", "Connected");
				dialog.show();
			} else {
				Log.d("debug NFC Connection", "Disonnected");
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
				
				alertDialog.setTitle(getResources().getString(R.string.dialogNTitle));
				
				alertDialog.setMessage(getResources().getString(R.string.dialogNMessage));
				
				alertDialog.setPositiveButton(getResources().getString(R.string.dialogOkButton), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
						startActivity(intent);
					}
				});
				
				alertDialog.setNegativeButton(getResources().getString(R.string.dialogCancelButton), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				
				alertDialog.show();

			}
			
		} else {
			Log.d("debug NFC Connection", "NFC is not available for the device");
			
		}

	}

	@Override
	public void onBackPressed() {
		Intent intent;
		if (dialog.isShowing()) {
			Log.d("debug", "dialog on");
			intent = new Intent(this, CreateTagContent.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			intent.putExtra("CONTENT_KIND", typeforBack);
			intent.putExtra("CONTENT_PAYLOAD", payloadForBack);
			intent.putExtra("CONTENT_ID", idForBack);
			intent.putExtra("CONTENT_EDIT", "EDIT");
			startActivity(intent);
			this.finish();
		}
		else{
			Log.d("debug", "Starting off");
			intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent);
			this.finish();
		}
	}
}
