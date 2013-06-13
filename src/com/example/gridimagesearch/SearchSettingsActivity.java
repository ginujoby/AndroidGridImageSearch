package com.example.gridimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class SearchSettingsActivity extends Activity {

	Spinner sprImageSize, sprColorFilter, sprImageType;
	EditText etSiteFilter;

	ArrayAdapter<CharSequence> imageSizeAdapter, colorFilterAdapter, imageTypeAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_settings);

		SearchSettings settings = (SearchSettings) getIntent().getSerializableExtra("settings");
		setupViews(settings);
	}

	public void setupViews(SearchSettings settings) {
		// Image size
		sprImageSize = (Spinner) findViewById(R.id.sprImageSize);
		imageSizeAdapter = ArrayAdapter
				.createFromResource(this, R.array.image_size_array,
						android.R.layout.simple_spinner_item);
		imageSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sprImageSize.setAdapter(imageSizeAdapter);
		sprImageSize.setSelection(imageSizeAdapter.getPosition(settings.getImageSize()));

		// Color filter
		sprColorFilter = (Spinner) findViewById(R.id.sprColorFilter);
		colorFilterAdapter = ArrayAdapter
				.createFromResource(this, R.array.color_array,
						android.R.layout.simple_spinner_item);
		colorFilterAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sprColorFilter.setAdapter(colorFilterAdapter);
		sprColorFilter.setSelection(colorFilterAdapter.getPosition(settings.getColorFilter()));

		// Image type
		sprImageType = (Spinner) findViewById(R.id.sprImageType);
		imageTypeAdapter = ArrayAdapter
				.createFromResource(this, R.array.image_type_array,
						android.R.layout.simple_spinner_item);
		imageTypeAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sprImageType.setAdapter(imageTypeAdapter);
		sprImageType.setSelection(imageTypeAdapter.getPosition(settings.getImageType()));

		// Site filter
		etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);
		etSiteFilter.setText(settings.getSiteFilter());
	}

	public void onSaveSettings(View v) {
		String imageSize = sprImageSize.getSelectedItem().toString();
		String colorFilter = sprColorFilter.getSelectedItem().toString();
		String imageType = sprImageType.getSelectedItem().toString();
		String siteFilter = etSiteFilter.getText().toString();

		SearchSettings settings = new SearchSettings(imageSize, colorFilter,
				imageType, siteFilter);

		Intent returnIntent = new Intent();
		returnIntent.putExtra("settings", settings);
		setResult(RESULT_OK, returnIntent);
		finish();
	}

	public void onClearSettings(View v) {
		SearchSettings settings = new SearchSettings();

		Intent returnIntent = new Intent();
		returnIntent.putExtra("settings", settings);
		setResult(RESULT_OK, returnIntent);
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_settings, menu);
		return true;
	}

}
