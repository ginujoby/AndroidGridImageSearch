package com.example.gridimagesearch;

import java.io.Serializable;

import android.net.Uri;


public class SearchSettings implements Serializable {
	private static final long serialVersionUID = 7880327858259066395L;
	private String imageSize;
	private String colorFilter;
	private String imageType;
	private String siteFilter;
	
	public SearchSettings(String imageSize, String colorFilter, String imageType, String siteFilter) {
		this.imageSize = imageSize;
		this.colorFilter = colorFilter;
		this.imageType = imageType;
		this.siteFilter = siteFilter;		
	}
	
	public SearchSettings() {
		this.imageSize = "";
		this.colorFilter = "";
		this.imageType = "";
		this.siteFilter = "";
	}

	public String getImageSize() {
		return imageSize;
	}
	
	public String getColorFilter() {
		return colorFilter;
	}
	
	public String getImageType() {
		return imageType;
	}
	
	public String getSiteFilter() {
		return siteFilter;
	}

	public String getSettingsQuery() {
		String query = "";
		query += (getImageSize().isEmpty()) ? "" : "&imgsz=" + getImageSize();
		query += (getColorFilter().isEmpty()) ? "" : "&imgcolor=" + getColorFilter();
		query += (getImageType().isEmpty()) ? "" : "&imgtype=" + getImageType();
		query += (getSiteFilter().trim().isEmpty()) ? "" : "&as_sitesearch=" + Uri.encode(getSiteFilter());
		
		return query;
	}
}
