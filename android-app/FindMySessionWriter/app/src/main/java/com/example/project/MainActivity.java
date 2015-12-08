                                                                                                                                                                                                       package com.example.project;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends Activity {

	CustomDialog aboutDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_about:
			aboutDialog = new CustomDialog(this);
			aboutDialog.setTitle(getResources().getString(R.string.action_about));
			aboutDialog.setContentView(R.layout.about_dialog);
			aboutDialog.setCancelable(true);
			aboutDialog.setCanceledOnTouchOutside(true);
			aboutDialog.show();
			return true;
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	public void onClick(View view){
		Intent intent;
		switch (view.getId()) {
		case R.id.read:
			intent = new Intent(MainActivity.this, ReadMain.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent);
			this.finish();
			break;
		case R.id.write:
			intent = new Intent(MainActivity.this, WriteMain.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent);
			this.finish();
			break;
		case R.id.my_tag:
			intent = new Intent(MainActivity.this, TagsMain.class);
			intent.setComponent(new ComponentName("com.example.wlanhelper", "com.example.wlanhelper.MainActivity"));
			intent.setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
			startActivity(intent);
			//this.finish();
			break;
		case R.id.erase:
			intent = new Intent(MainActivity.this, EraseTag.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			intent.putExtra("CALLING_ACTIVITY", this.getClass().getSimpleName());
			startActivity(intent);
			this.finish();
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.finish();
	}


 	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
