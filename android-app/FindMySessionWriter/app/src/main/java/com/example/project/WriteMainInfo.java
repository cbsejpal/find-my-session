package com.example.project;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.objects.CustomAdapater;
import com.example.objects.TagContent;
import com.example.objects.TagContentDataSource;

public class WriteMainInfo extends Activity {

	private NfcAdapter myNfcAdapter;
	private TextView status;
	private ListView contentList;
	private String[][] techListsArray;
	private IntentFilter[] intentFiltersArray;
	private PendingIntent pendingIntent;
	private TagContentDataSource datasource;
	private TextView emptyDB;
	private Button moreButton;
	private LinearLayout writeFooter;
	private CustomAdapater adapterAdapater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_write_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

		myNfcAdapter = NfcAdapter.getDefaultAdapter(this);

		contentList = (ListView) findViewById(R.id.recentList);
		moreButton = (Button) findViewById(R.id.moreButton);
		writeFooter = (LinearLayout) findViewById(R.id.writeFooter);

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

		datasource = new TagContentDataSource(this);
		datasource.open();

		List<TagContent> test = datasource.getAllComments();
		for (int i = test.size() - 1; i >= 0; i--) {
			Log.d("List element", "tag_content: " + test.get(i));
		}

		List<TagUIContent> tagUIContents = datasource.getTagUIContents();
		Log.d("debug list write", "" + tagUIContents.size() + "");
		Collections.reverse(tagUIContents);

		if (tagUIContents.size() >= 5) {
			writeFooter.setVisibility(View.VISIBLE);
		}

		if (tagUIContents.size() >= 10) {
			tagUIContents = tagUIContents.subList(0, 10);
		}

		adapterAdapater = new CustomAdapater(this, tagUIContents);
		if (adapterAdapater == null) {
			Log.d("debug list write", "adapter is null");
		}

		contentList.setAdapter(adapterAdapater);


		datasource.close();
	}

	public void onPause() {
		super.onPause();
		myNfcAdapter.disableForegroundDispatch(this);
	}

	public void onResume() {
		super.onResume();
		Log.d("debug resumed", "activity resumed.");
		myNfcAdapter.enableForegroundDispatch(this, pendingIntent,
				intentFiltersArray, techListsArray);
		datasource.open();
		int size = datasource.getTagUIContents().size();
		if (size < 5) {
			Log.d("debug resumed", "activity resumed. Size: " + size);
			writeFooter.setVisibility(View.GONE);
		}
		datasource.close();
	}

	public void showButton() {
		datasource.open();
		int size = datasource.getTagUIContents().size();
		if (size < 5) {
			Log.d("debug resumed", "activity resumed. Size: " + size);
			writeFooter.setVisibility(View.GONE);
		}
		datasource.close();
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
					Toast.makeText(this, "Tag is read-only", Toast.LENGTH_SHORT)
							.show();
					return false;
				}
				if (ndef.getMaxSize() < size) {
					Toast.makeText(
							this,
							"Tag data can't written to tag, Tag capacity is "
									+ ndef.getMaxSize() + "bytes, message is"
									+ size + " bytes.", Toast.LENGTH_SHORT)
							.show();
					return false;
				}
				ndef.writeNdefMessage(message);
				ndef.close();
				Toast.makeText(this, "Message is written tag",
						Toast.LENGTH_SHORT).show();
				return true;
			} else {
				NdefFormatable ndefFormat = NdefFormatable.get(detectedTag);
				if (ndefFormat != null) {
					try {
						ndefFormat.connect();
						ndefFormat.format(message);
						ndefFormat.close();
						Toast.makeText(this, "The data is written to teh tag",
								Toast.LENGTH_SHORT).show();
						return true;
					} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(this, "Failed to format tag",
								Toast.LENGTH_SHORT).show();
						return false;
					}

				} else {
					Toast.makeText(this, "NDEF is not supported",
							Toast.LENGTH_SHORT).show();
					return false;
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("debug", "Exception: " + e.toString());
			Toast.makeText(this, "Write operation is failed",
					Toast.LENGTH_SHORT).show();
			return false;
		}

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

	public void onNewIntent(Intent intent) {


	}

	public void onClick(View view) {
		Intent intent;
		switch (view.getId()) {
		case R.id.newContentButton:
			intent = new Intent(this, CreateTagContent.class);
			startActivity(intent);
			break;
		case R.id.moreButton:
			intent = new Intent(this, TagsMain.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent);
			break;
		default:
			break;
		}

	}

	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_write_main,
					container, false);
			return rootView;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
}
