package com.example.project;

import java.util.LinkedHashMap;
import java.util.Map;

import com.example.objects.TagContentDataSource;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



public class TagUI extends RelativeLayout {

	private ImageView contentIcon;
	private TextView payload;
	private TextView contentDesc;
	private TextView contentTags;
	private TextView contentId;
	private Context context;
	private TagContentDataSource datasource;
	public Map<String, Integer> PLTI =  new LinkedHashMap<String,Integer>();
	public Map<String, String> DBR =  new LinkedHashMap<String,String>();
	
	public TagUI(final Context context) {
		super(context);

		DBR.put("0",getResources().getString(R.string.link));
		DBR.put("4",getResources().getString(R.string.sms));
		DBR.put("1",getResources().getString(R.string.tel));
		DBR.put("2",getResources().getString(R.string.geoLoc));
		DBR.put("3",getResources().getString(R.string.plainText));

		PLTI.put(getResources().getString(R.string.nA), R.drawable.write_tag64);
		PLTI.put(getResources().getString(R.string.link), R.drawable.link64);
		PLTI.put(getResources().getString(R.string.link), R.drawable.link64);
		PLTI.put(getResources().getString(R.string.tel), R.drawable.default64);
		PLTI.put(getResources().getString(R.string.sms), R.drawable.geo64);
		PLTI.put(getResources().getString(R.string.geoLoc), R.drawable.sms64);
		PLTI.put(getResources().getString(R.string.plainText), R.drawable.tel64);

		
		RelativeLayout rLayout = (RelativeLayout) inflate(context,R.layout.recent_content,this);
		payload = (TextView)findViewById(R.id.contentPayload);
		contentDesc = (TextView)findViewById(R.id.contentDescription);
		contentIcon = (ImageView)findViewById(R.id.contentIcon);
		contentId = (TextView)findViewById(R.id.contentId);
		contentTags = (TextView)findViewById(R.id.contentTags); 
		datasource = new TagContentDataSource(getContext());
	    datasource.open();
	    
		
		rLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getContext(), CreateTagContent.class);
			    String kind = getContentDesc().getText().toString();
			    String payload = getPayload().getText().toString();
			    String activityName = getContext().getClass().getSimpleName();
				Log.d("debug extra",payload);
				intent.putExtra("CONTENT_KIND", kind);
				intent.putExtra("CONTENT_PAYLOAD", payload);
				intent.putExtra("CONTENT_ID", getContentId().getText().toString());
				intent.putExtra("CONTENT_KIND", kind);
				intent.putExtra("CONTENT_EDIT", "EDIT");
				intent.putExtra("CALLING_ACTIVITY", activityName);
				
				
				if(kind.equals("N/A") || payload.contains("nullnull")){
					Toast.makeText(context, context.getResources().getString(R.string.readIntent_Empty), Toast.LENGTH_SHORT).show();
					
				}
				else {
					intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					getContext().startActivity(intent);
					((Activity) context).finish();
				}
				
				
			}
			
		});
		
		rLayout.setOnLongClickListener(new View.OnLongClickListener() {
			
			private ListView optionDialog;
			private CustomDialog dialog;

			@Override
			public boolean onLongClick(View arg0) {
				// TODO Auto-generated method stub
				
				
				optionDialog = new ListView(getContext());
				String[] cOptionsArrayStrings = getResources().getStringArray(R.array.cOptions_array);
				
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, cOptionsArrayStrings);
				
				optionDialog.setAdapter(adapter);
				optionDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		            @Override
		            public void onItemClick(AdapterView<?> parent, final View view,
		                int position, long id) {
		              final String item = (String) parent.getItemAtPosition(position);
		              
		              
		              switch (position) {
					case 0:
						Toast.makeText(getContext(), "Delete!", Toast.LENGTH_LONG).show();
						datasource.deleteContent(Long.valueOf((String) getContentId().getText()));
						
						break;
                    default:
						break;
					}
		              
		              dialog.dismiss();
		            }

		          });
				
				dialog = new CustomDialog(getContext());
				dialog.setTitle("Options");
				dialog.setContentView(optionDialog);
				
				dialog.show();
				
				
				
				return true;
			}
		});
	}

	public TextView getContentId() {
		return contentId;
	}

	public void setContentId(String id) {
		this.contentId.setText(id);
	}

	public ImageView getContentIcon() {
		return contentIcon;
	}

	public void setContentIcon(ImageView contentIcon) {
		this.contentIcon = contentIcon;
	}

	public TextView getPayload() {
		return payload;
	}

	public void setPayload(String text) {
		this.payload.setText(text);
	}

	public TextView getContentDesc() {
		return contentDesc;
	}

	public void setContentDesc(String text) {
		this.contentDesc.setText(text);
	}

	public void setContentIcon(String text) {
		if ( PLTI.containsKey( text ) ) {
			contentIcon.setBackgroundResource(PLTI.get(text));
		}
		
		else {
			Log.d("TagInfo", "It not contains");
			contentIcon.setBackgroundResource(PLTI.get(getResources().getString(R.string.nA)));
		}
	}

	public TextView getContentTags() {
		return contentTags;
	}

	public void setContentTags(TextView contentTags) {
		this.contentTags = contentTags;
	}
}