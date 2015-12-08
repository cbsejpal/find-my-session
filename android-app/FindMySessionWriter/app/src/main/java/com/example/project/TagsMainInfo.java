package com.example.project;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.objects.ContentTag;
import com.example.objects.CustomAdapater;
import com.example.objects.FilterAdapter;
import com.example.objects.FilterKind;
import com.example.objects.FilterLayout;
import com.example.objects.TagContent;
import com.example.objects.TagContentDataSource;

public class TagsMainInfo extends ListActivity {

	private TagContentDataSource datasource;
	private TextView emptyDB;
	private ListView contentList;
	private TagUIContent[] arrayContents;
	private CustomAdapater adapterAdapater;
	private List<TagUIContent> tagUIContents;
	private CustomDialog dialog;
	private FilterAdapter filterListAdapter;
	private FilterLayout filterLayout;
	private String[] selectedFilters = new String[6];
	private ArrayList<String> selectedTagFilters;
	private int selectedCount = 0;
	private String fromSearch;
	private Menu actionsMenu;
	private boolean filtered = false; // Filtered by type
	private FilterLayout filterTagLayout;
	private FilterAdapter filterTagListAdapter;
	private int FilteredBy; // 1 - Type | 2 - Tag
	private boolean tagfiltered = false; // Filtered by tag
	private String dialogTitle;
	private int TypeFilter; // Tag Filter On

	private int TagFilter;
	private static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tags_main);

		context = this;
		selectedTagFilters = new ArrayList<String>();
		datasource = new TagContentDataSource(this);
		datasource.open();

		List<TagContent> test = datasource.getAllComments();
		for (int i = test.size() - 1; i >= 0; i--) {
			Log.d("List element", "tag_content: " + test.get(i));
		}

		if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
			String query = getIntent().getStringExtra(SearchManager.QUERY);
			Log.d("debug search", "OC called by search widget . Query: "
					+ query);

			tagUIContents = datasource.getContentFiltered(String.valueOf("'"
					+ query + "'"));
		} else {
			tagUIContents = datasource.getTagUIContents();

		}

		arrayContents = new TagUIContent[tagUIContents.size()];
		for (int i = 0; i < tagUIContents.size(); i++) {
			arrayContents[i] = tagUIContents.get(i);
		}

		adapterAdapater = new CustomAdapater(this, tagUIContents);
		setListAdapter(adapterAdapater);

		// adapterAdapater.notifyDataSetChanged();

		Log.d("debug", "Content length " + tagUIContents.size());


		List<FilterKind> filterList = getContentFilter();
		filterLayout = new FilterLayout(this);
		// ListView filterListView = (ListView)findViewById(R.id.filterList);
		filterListAdapter = new FilterAdapter(this, filterList);
		filterLayout.getFilterList().setAdapter(filterListAdapter);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		datasource.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tags_main, menu);
		actionsMenu = menu;

		if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
			actionsMenu.getItem(0).setVisible(false);
		}
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.action_filter) {

			FilteredBy = 1;
			TypeFilter = 1;
			String filterText = "";
			TextView filterTextView = (TextView) findViewById(R.id.action_filter);
			List<FilterKind> filterList = getContentFilter();
			Log.d("debug filters list size", filterList.size() + "");
			Log.d("debug filterAdapter size", filterListAdapter.getCount() + "");
			filterLayout = new FilterLayout(this);
			dialogTitle = getResources()
					.getString(R.string.title_filterTypeOff);
			int selectedC = selectedCount;
			Log.d("debug filter button", filterTextView.getText().toString());

			// filterTextView.getText().toString() == "ON"
			if (filtered) {
				filterLayout.getFilterList().setVisibility(View.INVISIBLE);
				filterLayout.getFilterImageView().setVisibility(View.VISIBLE);
				for (String filterKind : selectedFilters) {
					if (filterKind != "" && filterKind != null) {
						filterText += filterKind + ",";
					}

				}
				filterLayout.getFilterTextView().setText(
						getResources().getString(R.string.filterByT)
								+ filterText.substring(0,
										filterText.length() - 1));
				filterLayout.getFilterTextView().setVisibility(View.VISIBLE);
				filterLayout.getFilterButton().setText(
						getResources().getString(R.string.removeFilterButton));
				dialogTitle = getResources().getString(
						R.string.title_filterTypeOn);
			}

			filterListAdapter = new FilterAdapter(this, filterList);
			filterLayout.getFilterList().setAdapter(filterListAdapter);
			filterListAdapter.notifyDataSetChanged();

			dialog = new CustomDialog(this);
			dialog.setTitle(dialogTitle);
			dialog.setContentView(filterLayout);
			dialog.show();

			return true;
		}

		if (id == R.id.action_search) {
			onSearchRequested();
			
		}

		if (id == R.id.action_filterTag) {

			FilteredBy = 2;
			TagFilter = 1;
			datasource.open();
			String filterText = "";
			TextView filterTextView = (TextView) findViewById(R.id.action_filterTag);
			List<FilterKind> filterTagList = getContentTagFilter();
			Log.d("debug filters list size", filterTagList.size() + "");
			filterTagLayout = new FilterLayout(this);
			dialogTitle = getResources().getString(
					R.string.title_filterLabelOff);

			if (filterTagList.size() == 0) {
				
				filterTagLayout.getFilterTextView().setText("No existen etiquetas");
				filterTagLayout.getFilterImageView().setVisibility(View.GONE);
				filterTagLayout.getFilterTextView().setVisibility(View.VISIBLE);
				filterTagLayout.getFilterButton().setVisibility(View.GONE);
			}

			if (tagfiltered) {
				filterTagLayout.getFilterList().setVisibility(View.INVISIBLE);
				filterTagLayout.getFilterImageView().setBackgroundResource(
						R.drawable.filter_v3);
				filterTagLayout.getFilterImageView()
						.setVisibility(View.VISIBLE);
				for (String filterKind : selectedTagFilters) {
					if (filterKind != "" && filterKind != null) {
						filterText += filterKind + ",";
					}
					dialogTitle = getResources().getString(
							R.string.title_filterLabelOn);
				}

				filterTagLayout.getFilterTextView().setText(
						getResources().getString(R.string.filterByL)
								+ filterText.substring(0,
										filterText.length() - 1));
				filterTagLayout.getFilterTextView().setVisibility(View.VISIBLE);
				filterTagLayout.getFilterButton().setText(
						getResources().getString(R.string.removeFilterButton));
			}

			filterTagListAdapter = new FilterAdapter(this, filterTagList);
			filterTagLayout.getFilterList().setAdapter(filterTagListAdapter);
			filterTagListAdapter.notifyDataSetChanged();

			
			dialog = new CustomDialog(this);
			dialog.setTitle(dialogTitle);
			dialog.setContentView(filterTagLayout);
			dialog.show();
			datasource.close();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		datasource.open();
		if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
			String query = getIntent().getStringExtra(SearchManager.QUERY);
			Log.d("debug search", "OR called by search widget. Query: " + query);
			fromSearch = query;
			tagUIContents = datasource.getContentbySearch(query);
			
		} else {
			tagUIContents = datasource.getTagUIContents();
		}
		Log.d("debug resumed", "Content length " + tagUIContents.size());

		adapterAdapater.clear();
		adapterAdapater.addAll(tagUIContents);
		adapterAdapater.notifyDataSetChanged();
		datasource.close();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		datasource.open();

		tagUIContents = datasource.getTagUIContents();
		adapterAdapater.notifyDataSetChanged();
		datasource.close();
	}

	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.filterButton:
			datasource.open();
			switch (FilteredBy) {
			case 1:
				selectedCount = 0;
				boolean removeFilter = false;
				TextView filterTextView = (TextView) findViewById(R.id.action_filter);

				String filters = "";
				for (int i = 0; i < filterListAdapter.getCount(); i++) {
					if (filterListAdapter.getItem(i).getContentCheck()
							.isChecked()) {
						selectedCount++;
					}
				}

				int c = 0;
				for (int i = 0; i < filterListAdapter.getCount(); i++) {
					if (filterListAdapter.getItem(i).getContentCheck()
							.isChecked()) {
						filters += "'"
								+ filterListAdapter.getItem(i).getContentDesc()
										.getText() + "'";
						selectedFilters[c] = filterListAdapter.getItem(i)
								.getContentDesc().getText().toString();
						Log.d("debug filters list ", selectedFilters[c]);

						if (c < selectedCount - 1) {
							filters += ",";
							c++;
						}
					}

				}


				if (filterLayout.getFilterButton().getText().toString()
						.toString() == getResources().getString(
						R.string.removeFilterButton)) {
					filters = "";
					for (int i = 0; i < selectedFilters.length; i++) {
						selectedFilters[i] = "";
					}
					for (int j = 0; j < filterListAdapter.getCount(); j++) {
						filterListAdapter.getItem(j).getContentCheck()
								.setChecked(false);
					}
					removeFilter = true;
				}
				if (filters == "") {
					removeFilter = true;
				}
				TypeFilter = 0;
				adapterAdapater.addAll(getFilteredList(removeFilter));
				adapterAdapater.notifyDataSetChanged();

				if (filters != "") {

					if (actionsMenu != null) {
						Log.d("debug filters button ", "Menu found");
						actionsMenu.findItem(R.id.action_filter).setTitle("ON");
						actionsMenu.findItem(R.id.action_filter).setIcon(
								R.drawable.ic_action_filter_on);
						filtered = true;
						TypeFilter = 1;
					}
				}

				dialog.dismiss();

				if (filterLayout.getFilterButton().getText().toString()
						.toString() == getResources().getString(
						R.string.removeFilterButton)) {
					// filterTextView.setText("Filter");
					filterLayout.getFilterButton().setText("Filter");
					selectedCount = 0;
					filtered = false;

					actionsMenu.findItem(R.id.action_filter).setIcon(
							R.drawable.ic_action_filter_off);
				}

				break; // END CASE FILTERED BY = 1
			case 2:
				selectedCount = 0;
				boolean removeTagFilter = false;

				String tagfilters = "";
				for (int i = 0; i < filterTagListAdapter.getCount(); i++) {
					if (filterTagListAdapter.getItem(i).getContentCheck()
							.isChecked()) {
						selectedCount++;
					}
				}

				int count = 0;
				for (int i = 0; i < filterTagListAdapter.getCount(); i++) {
					if (filterTagListAdapter.getItem(i).getContentCheck()
							.isChecked()) {
						tagfilters += "'"
								+ filterTagListAdapter.getItem(i)
										.getContentDesc().getText() + "'";
						selectedTagFilters.add(filterTagListAdapter.getItem(i)
								.getContentDesc().getText().toString());

						Log.d("debug filters list ",
								selectedTagFilters.get(count).toString());

						if (count < selectedCount - 1) {
							tagfilters += ",";
							count++;
						}
					}

				}


				if (filterTagLayout.getFilterButton().getText().toString()
						.toString() == getResources().getString(
						R.string.removeFilterButton)) {
					tagfilters = "";
					selectedTagFilters.clear();
					for (int j = 0; j < filterTagListAdapter.getCount(); j++) {
						filterTagListAdapter.getItem(j).getContentCheck()
								.setChecked(false);
					}
					removeTagFilter = true;
				}
				if (tagfilters == "") {
					removeTagFilter = true;
				}
				TagFilter = 0;
				adapterAdapater.addAll(getFilteredList(removeTagFilter));
				adapterAdapater.notifyDataSetChanged();

				if (tagfilters != "") {

					if (actionsMenu != null) {
						actionsMenu.findItem(R.id.action_filterTag).setTitle(
								"ON");
						actionsMenu.findItem(R.id.action_filterTag).setIcon(
								R.drawable.ic_action_filter_on);
						tagfiltered = true;
						TagFilter = 1;
					}
				}

				dialog.dismiss();

				if (filterTagLayout.getFilterButton().getText().toString()
						.toString() == getResources().getString(
						R.string.removeFilterButton)) {
					filterTagLayout.getFilterButton().setText("Filter");
					selectedCount = 0;
					tagfiltered = false;

					actionsMenu.findItem(R.id.action_filterTag).setIcon(
							R.drawable.ic_action_filter_tag);
				}

				break; // END CASE FILTERED BY = 2
			default:
				break;
			} // END SWITCH FILTERED BY
			datasource.close();
			break;

		default:
			break;
		} // END SWITCH VIEW ID
	}

	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_tags_main,
					container, false);
			return rootView;
		}
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {

		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, MeasureSpec.UNSPECIFIED);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	class ShortenUrlTask extends AsyncTask<String, Void, String> {
		private final String GOOGLE_URL = "https://www.googleapis.com/urlshortener/v1/url";
		private String mLongUrl = null;

		@Override
		protected String doInBackground(String... arg) {
			mLongUrl = arg[0];
			try {
				HttpParams httpParameters = new BasicHttpParams();
				int timeoutConnection = 5000;
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);
				int timeoutSocket = 10000;
				HttpConnectionParams
						.setSoTimeout(httpParameters, timeoutSocket);
				HttpClient hc = new DefaultHttpClient(httpParameters);
				HttpPost request = new HttpPost(GOOGLE_URL);
				request.setHeader("Content-type", "application/json");
				request.setHeader("Accept", "application/json");
				JSONObject obj = new JSONObject();
				obj.put("longUrl", mLongUrl);
				request.setEntity(new StringEntity(obj.toString(), "UTF-8"));
				HttpResponse response = hc.execute(request);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					return out.toString();
				} else {
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result == null)
				return;
			try {
				final JSONObject json = new JSONObject(result);
				final String id = json.getString("id");
				if (json.has("id")) {
					Log.d("debug shortened url?", id);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	public List<FilterKind> getContentFilter() {

		String[] kind = getResources().getStringArray(R.array.kinds_array);
		List<FilterKind> contentFilters = new ArrayList<FilterKind>();
		int id = 1;
		for (String kindString : kind) {
			FilterKind nContentFilter = new FilterKind(this);
			nContentFilter.getContentDesc().setText(kindString);
			nContentFilter.getContentId().setText(String.valueOf(id));
			nContentFilter.getContentCheck().setChecked(false);
			nContentFilter.setKindIcon(kindString);
			contentFilters.add(nContentFilter);
			id++;
		}

		return contentFilters;
	}

	public List<FilterKind> getContentTagFilter() {

		List<ContentTag> kind = datasource.getAllTags();
		List<FilterKind> contentFilters = new ArrayList<FilterKind>();
		for (ContentTag contentTag : kind) {
			FilterKind nContentFilter = new FilterKind(this);
			nContentFilter.getContentDesc().setText(contentTag.getName());
			nContentFilter.getContentId().setText(
					String.valueOf(contentTag.getId()));
			nContentFilter.getContentCheck().setChecked(false);
			nContentFilter.setKindIcon(getResources().getString(R.string.tag));
			contentFilters.add(nContentFilter);
		}
		return contentFilters;
	}

	public List<TagUIContent> getFilteredList(Boolean rFilters) {

		datasource.open();
		List<TagUIContent> tagUIContents = new ArrayList<TagUIContent>();
		int j;

		if (rFilters) {
			if (fromSearch != null) {
				tagUIContents = datasource.getContentbySearch(fromSearch);
			} else {
				adapterAdapater.clear();
				adapterAdapater.addAll(datasource.getTagUIContents());

				if (TagFilter == 1) {
					for (int i = 0; i < selectedTagFilters.size(); i++) {
						j = 0;
						int aas = adapterAdapater.getCount();
						while (j < aas) {
							if (adapterAdapater.getItem(j).getContentTags()
									.getText().toString()
									.contains(selectedTagFilters.get(i))) {
								if (!tagUIContents.contains(adapterAdapater
										.getItem(j))) {
									tagUIContents.add(adapterAdapater
											.getItem(j));
								}
							}
							j++;
						}
					}
				} else if (TypeFilter == 1) {
					for (int i = 0; i < selectedFilters.length; i++) {
						j = 0;
						while (j < adapterAdapater.getCount()) {
							if (adapterAdapater.getItem(j).getContentDesc()
									.getText().toString()
									.equals(selectedFilters[i])) {
								tagUIContents.add(adapterAdapater.getItem(j));
							}
							j++;
						}
					}
				} else {
					tagUIContents = datasource.getTagUIContents();
				}

			}

		} else {
			if (FilteredBy == 1) { // Kind
				for (int i = 0; i < selectedFilters.length; i++) {
					j = 0;
					while (j < adapterAdapater.getCount()) {
						if (adapterAdapater.getItem(j).getContentDesc()
								.getText().toString()
								.equals(selectedFilters[i])) {
							tagUIContents.add(adapterAdapater.getItem(j));
						}
						j++;
					}
				}
			}
			if (FilteredBy == 2) {// Tags

				for (int i = 0; i < selectedTagFilters.size(); i++) {
					j = 0;
					int aas = adapterAdapater.getCount();
					while (j < aas) {
						if (adapterAdapater.getItem(j).getContentTags()
								.getText().toString()
								.contains(selectedTagFilters.get(i))) {
							if (!tagUIContents.contains(adapterAdapater
									.getItem(j))) {
								tagUIContents.add(adapterAdapater.getItem(j));
								// adapterAdapater.remove(adapterAdapater.getItem(j));
							}

						}
						j++;
					}
				}
			}

		}

		// Log.d("debug filters list NF ", "Size: " + tagUIContents.size());
		adapterAdapater.clear();
		datasource.close();
		return tagUIContents;
	}

	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		Log.d("debug back from search", "pressed ");
		super.onBackPressed();
		if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
			Log.d("debug back from search", "pressed if");
			Intent intent = new Intent(this, TagsMainInfo.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent);
			this.finish();
		}
		else{
			Intent intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(intent);
			this.finish();
		}
	}
}