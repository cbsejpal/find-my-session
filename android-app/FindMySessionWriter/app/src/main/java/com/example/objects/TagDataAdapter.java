package com.example.objects;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.project.R;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TagDataAdapter extends ArrayAdapter<TagContent> {

	private Activity context;
	private List<TagContent> objects;
	private TagContentDataSource datasource;
	private TagDataAdapter tAdapater;
	public Map<String, String> DBR = new LinkedHashMap<String, String>(); // DataBaseResource
	public Map<String, Integer> PLTI = new LinkedHashMap<String, Integer>();


	
	public TagDataAdapter(Activity context, List<TagContent> objects) {
		super(context,com.example.project.R.layout.recent_content, objects);
		// TODO Auto-generated constructor stub
		DBR.put("0", context.getResources().getString(R.string.link));
		DBR.put("1", context.getResources().getString(R.string.tel));
		DBR.put("2", context.getResources().getString(R.string.geoLoc));
		DBR.put("3", context.getResources().getString(R.string.plainText));
		DBR.put("4", context.getResources().getString(R.string.sms));

		PLTI.put(context.getResources().getString(R.string.nA), R.drawable.default64);
		PLTI.put(context.getResources().getString(R.string.link), R.drawable.link64);
		PLTI.put(context.getResources().getString(R.string.link), R.drawable.link64);
		PLTI.put(context.getResources().getString(R.string.tel), R.drawable.default64);
		PLTI.put(context.getResources().getString(R.string.geoLoc), R.drawable.sms64);
		PLTI.put(context.getResources().getString(R.string.plainText), R.drawable.tel64);
		PLTI.put(context.getResources().getString(R.string.sms), R.drawable.geo64);
		this.context = context;
		this.objects = objects;
		datasource = new TagContentDataSource(getContext());
		tAdapater = this;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(
					com.example.project.R.layout.recent_content, null);

			ViewHolder viewHolder = new ViewHolder();
			viewHolder.payloadContent = (TextView) rowView
					.findViewById(com.example.project.R.id.contentPayload);
			viewHolder.payloadDescContent = (TextView) rowView
					.findViewById(com.example.project.R.id.contentDescription);
			viewHolder.payloadContentId = (TextView) rowView
					.findViewById(com.example.project.R.id.contentId);
			viewHolder.payloadIconContent = (ImageView) rowView
					.findViewById(com.example.project.R.id.contentIcon);
			rowView.setTag(viewHolder);
		}

		final ViewHolder holder = (ViewHolder) rowView.getTag();
		
		
		holder.payloadIconContent.setBackgroundResource(PLTI.get(DBR.get(objects.get(position).getPayloadType())));
		holder.payloadContent.setText(objects.get(position).getPayload());
		holder.payloadDescContent.setText(DBR.get(objects.get(position).getPayloadType()));
		holder.payloadContentId.setText(String.valueOf(objects.get(position).getId()));

		datasource.open();
		List<ContentTag> tagList = datasource
				.getTagsOfContent(holder.payloadContentId.getText().toString());
		for (ContentTag contentTag : tagList) {
			Log.d("debug tags", contentTag.toString());
		}

		datasource.close();
		return rowView;
	}
	
	
	
	static class ViewHolder {
		public TextView payloadContent;
		public TextView payloadDescContent;
		public ImageView payloadIconContent;
		public TextView payloadContentId;
		
	}

}
