package com.example.project;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class CustomMessage extends Dialog {

	private String activityName;
	private Button positive, negative;

	public CustomMessage(Context context) {
		super(context);
		if (context instanceof Activity) {
			setOwnerActivity((Activity) context);
		}
		activityName = this.getOwnerActivity().getLocalClassName();
		Log.d("debug", "activity name?: "
				+ this.getOwnerActivity().getLocalClassName());
		setCanceledOnTouchOutside(false);
	}

	@Override
	public void onBackPressed() {
		Intent intent;
		if(activityName=="ReadMain") {
			intent = new Intent(getContext(), MainActivity.class);
			getContext().startActivity(intent);
		}
		else if(activityName== "TransferContent") {
			TransferContent tContent = (TransferContent) getOwnerActivity();
			if (tContent != null) {
				tContent.onBackPressed();
			}
		}
		else if (activityName== "EraseTag") {
			EraseTag eTag = (EraseTag) getOwnerActivity();
			if (eTag != null) {
				eTag.onBackPressed();
			}
		}
		else{}
		this.dismiss();

	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
		if (activityName.equals("WriteMain")) {
			WriteMain wMain = (WriteMain) getOwnerActivity();
			if (wMain != null) {
				Log.d("debug resumed", "updating UI");
				wMain.showButton();

			} else {
				Log.d("debug resumed", " HA HA");
			}
		}
	}

	public void setPositiveButton(String buttonText,
			View.OnClickListener listener, int buttonId) {
		positive = (Button) findViewById(buttonId);
		positive.setText(buttonText);
		positive.setOnClickListener(listener);
		positive.setVisibility(View.VISIBLE);
	}

	public void setNegativeButton(String buttonText,
			View.OnClickListener listener) {
		negative.setText(buttonText);
		negative.setOnClickListener(listener);
		negative.setVisibility(View.VISIBLE);
	}

	
}
