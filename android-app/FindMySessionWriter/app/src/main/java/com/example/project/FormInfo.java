package com.example.project;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class FormInfo extends RelativeLayout {

	private Button contactButton;
	private EditText fieldPhone;
		 
	private final static int PICK_CONTACT = 1;
	
	
	public FormInfo(Context context, int LAYOUT_ID) {
		super(context);
		// TODO Auto-generated constructor stub
		RelativeLayout rLayout = (RelativeLayout) inflate(context,LAYOUT_ID,this);
		setContactButton((Button)findViewById(R.id.contactButton));
		fieldPhone  = (EditText)findViewById(R.id.fieldPhone);
	}
	public Button getContactButton() {
		return contactButton;
	}
	
	public void setContactButton(Button contactButton) {
		this.contactButton = contactButton;
	}
	public EditText getFieldPhone() {
		return fieldPhone;
	}
	public void setFieldPhone(EditText fieldPhone) {
		this.fieldPhone = fieldPhone;
	}
}
