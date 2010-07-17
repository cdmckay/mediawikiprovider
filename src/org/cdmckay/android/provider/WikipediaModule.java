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

import android.database.Cursor;
import android.database.MatrixCursor;
import android.util.Xml;

public class WikipediaModule implements Module {

	private static final String API_URI = "http://en.wikipedia.org/w/api.php";
	private static final String USER_AGENT = MediaWikiProvider.class.getSimpleName() + "/"
			+ MediaWikiMetaData.VERSION;
	
	// The search column names.
	private static final String[] SEARCH_COLUMN_NAMES = new String[] {
		MediaWikiMetaData.Search.TITLE,
		MediaWikiMetaData.Search.DESCRIPTION,
		MediaWikiMetaData.Search.URL
	};
	
	// A temporary buffer used to hold the response of an HTTP GET request.
	private static byte[] sContentBuffer = new byte[512];
	
	

	public Cursor search(String query) {
		// Format must be XML in order to get description text.
		// Using JSON just gives you a list of strings.
		final String url = String.format("%s?action=opensearch&search=%s&format=xml", API_URI,
				URLEncoder.encode(query));
		final String response = getResponse(url);
		final OpenSearchHandler handler = new OpenSearchHandler();
		
		try {			
			Xml.parse(response, handler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        final List<OpenSearchHandler.Result> results = handler.getResults();
        final MatrixCursor cursor = new MatrixCursor(SEARCH_COLUMN_NAMES);
        for (OpenSearchHandler.Result result: results) {
        	cursor.addRow(new Object[] { result.title, result.description, result.url });
        }

		return cursor;
	}

	public Cursor getPageByTitle(String title) {
		return null;
	}

	public Cursor getPageById(long id) {
		return null;
	}

	private synchronized String getResponse(String url) {
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		request.setHeader("User-Agent", USER_AGENT);

		try {
			HttpResponse response = client.execute(request);

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
