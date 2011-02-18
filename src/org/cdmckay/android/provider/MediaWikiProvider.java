/*******************************************************************************
 * Copyright 2010 Cameron McKay
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.cdmckay.android.provider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Xml;

public abstract class MediaWikiProvider extends ContentProvider {   	
		
	protected static String VERSION = "1";
	protected static String BASE_AUTHORITY = "org.cdmckay.android.provider";
	
	private static final int SEARCH = 1;
	private static final int PAGE_BY_TITLE = 2;
	private static final int PAGE_BY_ID = 3;
	
	private static final String SEARCH_FORMAT_URI = 
		"%s?action=opensearch&search=%s&format=xml";
	
	private static final String GET_PAGE_BY_TITLE_FORMAT_URI = 
		"%s?action=query&prop=revisions&titles=%s&rvprop=content&format=xml";
	
	private static final String GET_PAGE_BY_ID_FORMAT_URI = 
		"%s?action=query&prop=revisions&pageids=%s&rvprop=content&format=xml";

	private static final String USER_AGENT = MediaWikiProvider.class.getSimpleName() + "/"
			+ VERSION;

	// The search column names.
	private static final String[] SEARCH_COLUMN_NAMES = new String[] {
			BaseColumns._ID,
			MediaWikiMetaData.Search.TITLE, 
			MediaWikiMetaData.Search.DESCRIPTION,
			MediaWikiMetaData.Search.URL };
	
	// The page column names.
	private static final String[] PAGE_COLUMN_NAMES = new String[] {
			//BaseColumns._ID,
			MediaWikiMetaData.Page.PAGE_ID,
			MediaWikiMetaData.Page.NAMESPACE,
			MediaWikiMetaData.Page.TITLE,
			MediaWikiMetaData.Page.CONTENT
	};

	// A temporary buffer used to hold the response of an HTTP GET request.
	private static byte[] sContentBuffer = new byte[512];	
	
	// The HTTP client.
	private final HttpClient mHttpClient = new DefaultHttpClient();
	
	private final UriMatcher mUriMatcher = buildUriMatcher();
	private UriMatcher buildUriMatcher() {
		UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		matcher.addURI(getAuthority(), "search/*", SEARCH);
		matcher.addURI(getAuthority(), "page/title/*", PAGE_BY_TITLE);
		matcher.addURI(getAuthority(), "page/id/#", PAGE_BY_ID);
		return matcher;
	}	
	
	@Override
	public String getType(Uri uri) {
		switch (mUriMatcher.match(uri)) {
			case SEARCH:
				return MediaWikiMetaData.Search.CONTENT_TYPE;
			case PAGE_BY_TITLE:
			case PAGE_BY_ID:
				return MediaWikiMetaData.Page.CONTENT_ITEM_TYPE;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
	}	

	@Override
	public boolean onCreate() {
		return true;
	}

	// ContentProvider
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		switch (mUriMatcher.match(uri)) {
			case SEARCH:				
				return search(uri.getLastPathSegment());
			case PAGE_BY_TITLE:
				return getPageByTitle(uri.getLastPathSegment());
			case PAGE_BY_ID:
				return getPageById(Long.valueOf(uri.getLastPathSegment()));
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
	}
		
	@Override
	public int delete(Uri uri, String where, String[] selectionArgs) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		throw new UnsupportedOperationException();
	}
	
	// Methods
	
	protected abstract String getVersion();
	protected abstract String getAuthority();
	protected abstract String getApiUri();
	
	protected Cursor search(String query) {		
		final String url = String.format(SEARCH_FORMAT_URI, getApiUri(), URLEncoder.encode(query));
		final String response = getResponse(url);
		final OpenSearchHandler handler = new OpenSearchHandler();

		try {
			Xml.parse(response, handler);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		final List<OpenSearchHandler.Result> results = handler.getResults();
		final MatrixCursor cursor = new MatrixCursor(SEARCH_COLUMN_NAMES);
		for (OpenSearchHandler.Result result : results) {			
			cursor.addRow(new Object[] { result.id, result.title, result.description, result.url });
		}

		return cursor;
	}

	protected Cursor getPageByTitle(String title) {
		final String url = String.format(GET_PAGE_BY_TITLE_FORMAT_URI, getApiUri(), URLEncoder.encode(title));
		final String response = getResponse(url);				
		return parsePage(response);
	}

	protected Cursor getPageById(long id) {
		final String url = String.format(GET_PAGE_BY_ID_FORMAT_URI, getApiUri(), id + "");
		final String response = getResponse(url);				
		return parsePage(response);
	}
	
	protected Cursor parsePage(String page) {
		final PageHandler handler = new PageHandler();
		
		try {
			Xml.parse(page, handler);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		final List<PageHandler.Result> results = handler.getResults();
		final MatrixCursor cursor = new MatrixCursor(PAGE_COLUMN_NAMES);
		for (PageHandler.Result result : results) {
			cursor.addRow(new Object[] { result.pageId, result.namespace, result.title, result.content });
		}
		
		return cursor;
	}

	protected synchronized String getResponse(String url) {		
		HttpGet request = new HttpGet(url);
		request.setHeader("User-Agent", USER_AGENT);

		try {
			HttpResponse response = mHttpClient.execute(request);

			// Check if server response is valid.
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("Invalid response from server: " + status.toString());
			}

			// Pull content stream from response.
			HttpEntity entity = response.getEntity();
			InputStream inputStream = entity.getContent();

			ByteArrayOutputStream content = new ByteArrayOutputStream();

			// Read response into a buffered stream.
			int readBytes = 0;
			while ((readBytes = inputStream.read(sContentBuffer)) != -1) {
				content.write(sContentBuffer, 0, readBytes);
			}

			// Return result from buffered stream.
			return new String(content.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("Problem communicating with API", e);
		}
	}
	
}
