package com.example.objects;

import java.util.LinkedHashMap;
import java.util.Map;

import com.example.project.R;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TagDetail extends RelativeLayout{

	private Map<String, Integer> featureIcons =  new LinkedHashMap<String,Integer>();
	
	private TextView featureName;
	private TextView featureValue;
	private ImageView featureIcon;
		
	public TagDetail(Context context) {
		super(context);
		
		/*Initialize associative array of Feature icons id*/
		featureIcons.put("N/A", R.drawable.linkb48);
		featureIcons.put("ID", R.drawable.id64);
		featureIcons.put("Class", R.drawable.class64);
		featureIcons.put("CBMRO", R.drawable.readonly64);
		featureIcons.put("Size", R.drawable.storage64);
		featureIcons.put("WRTBL", R.drawable.wrtbl64);
		featureIcons.put("TechList", R.drawable.techlist64);
		
		
		RelativeLayout rLayout = (RelativeLayout) inflate(context,R.layout.tag_feature,this);
		featureName = (TextView)findViewById(R.id.firstLine);
		featureValue = (TextView)findViewById(R.id.secondLine);
		featureIcon = (ImageView)findViewById(R.id.icon);
	}

	public TextView getFeatureName() {
		return featureName;
	}

	public void setFeatureName(Integer id) {
		this.featureName.setText(id);
		
	}

	public TextView getFeatureValue() {
		return featureValue;
	}

	public void setFeatureValue(String text) {
		this.featureValue.setText(text);
	}

	public ImageView getFeatureIcon() {
		return featureIcon;
	}

	public void setFeatureIcon(String text) {
		if ( featureIcons.containsKey( text ) ) {
			featureIcon.setBackgroundResource(featureIcons.get(text));
		}
		
		else {
			Log.d("TagInfo", "It not contains");
			featureIcon.setBackgroundResource(featureIcons.get("N/A"));
		}
	}

}
