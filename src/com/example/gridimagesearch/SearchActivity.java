package com.example.gridimagesearch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {
	EditText etQuery;
	GridView gvResults;
	Button btnSearch, btnLoadMore;

	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultArrayAdapter imageAdapter;
	SearchSettings settings = new SearchSettings();
	int start = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		setupViews();
		readSettings();

		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvResults.setAdapter(imageAdapter);
		gvResults.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> adapter, View parent,
					int position, long rowId) {
				Intent i = new Intent(getApplicationContext(),
						ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				i.putExtra("result", imageResult);
				startActivity(i);
			}
		});
		
		etQuery.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				resetSearch();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.btnSettings:
			Intent i = new Intent(getApplicationContext(),
					SearchSettingsActivity.class);
			i.putExtra("settings", this.settings);
			startActivityForResult(i, 1);
			break;
		default:
			break;
		}

		return true;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent settings) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				this.settings = (SearchSettings) settings.getSerializableExtra("settings");
				writeSettings();
				Toast.makeText(this, "Settings Saved!", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void writeSettings() {
		File filesDir = getFilesDir();
		
		try {
			File settingsFile = new File(filesDir, "settings.ser");
			FileOutputStream fos = new FileOutputStream(settingsFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(settings);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readSettings() {
		File filesDir = getFilesDir();
		
		try {
			File settingsFile = new File(filesDir, "settings.ser");
			FileInputStream fis = new FileInputStream(settingsFile);
			ObjectInputStream ois = new ObjectInputStream(fis);
			this.settings = (SearchSettings) ois.readObject();
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void setupViews() {
		etQuery = (EditText) findViewById(R.id.etQuery);
		gvResults = (GridView) findViewById(R.id.gvResults);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnLoadMore = (Button) findViewById(R.id.btnLoadMore);
	}

	public void onImageSearch(View v) {
		String query = etQuery.getText().toString();
		String tag = v.getTag().toString();
		
		if(tag.equals("search")) {
			resetSearch();
		}
		
		//Toast.makeText(this, "Searching for " + query + " start=" + start + " " + this.settings.getSettingsQuery(), Toast.LENGTH_LONG).show();
		
		AsyncHttpClient client = new AsyncHttpClient();
		// https://ajax.googleapis.com/ajax/services/search/images?q=Android&v=1.0
		client.get(
				"https://ajax.googleapis.com/ajax/services/search/images?rsz=8&"
						+ "start=" + start + "&v=1.0&q=" + Uri.encode(query) + this.settings.getSettingsQuery(),
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {
						JSONArray imageJsonResults = null;
						try {
							imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
							imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
							Log.d("DEBUG", imageAdapter.toString());
							
							if(imageJsonResults.length() > 0) {
								btnLoadMore.setVisibility(View.VISIBLE);
								start += 8;
							} else {
								btnLoadMore.setVisibility(View.GONE);
								Toast.makeText(getApplicationContext(), "Your search did not match any documents.", Toast.LENGTH_SHORT).show();
							}
							
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	private void resetSearch() {
		imageResults.clear();
		btnLoadMore.setVisibility(View.GONE);
		this.start = 0;
	}
}
