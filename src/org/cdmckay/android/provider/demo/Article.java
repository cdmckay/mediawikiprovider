/*******************************************************************************
 * Copyright 2011 Cameron McKay
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
package org.cdmckay.android.provider.demo;

import org.cdmckay.android.provider.MediaWikiMetaData;
import org.cdmckay.android.provider.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class Article extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article);
		
		final Intent intent = getIntent();
		
		// Set the title.
		final String title = intent.getStringExtra("title");
		final TextView titleView = (TextView) findViewById(R.id.article_title);			
		titleView.setText(title);	
		
		// Set the content.
		final ContentResolver resolver = getContentResolver();
    	
		// Get the proper provider URI.		
    	final SharedPreferences settings = getSharedPreferences(Main.SETTINGS, MODE_PRIVATE);        	
    	final Uri providerUri;
    	switch (settings.getInt("provider", R.id.provider_wikipedia)) {        		
    		case R.id.provider_memory_alpha:
    			providerUri = Uri.withAppendedPath(MediaWikiMetaData.CONTENT_URI, 
    					Main.MEMORY_ALPHA_API + "/page");
    			break;
    		case R.id.provider_wikipedia:
    		default:
    			providerUri = Uri.withAppendedPath(MediaWikiMetaData.CONTENT_URI, 
    					Main.WIKIPEDIA_API + "/page");        			
    	}
    	
    	// Append the title string and query the provider.
    	final Uri uri = Uri.withAppendedPath(providerUri, "title/" + title);	
		final Cursor cursor = resolver.query(uri, null, null, null, null);     		
		startManagingCursor(cursor);    	
		
    	final int contentColumn = cursor.getColumnIndex(MediaWikiMetaData.Page.CONTENT);
    	final TextView contentView = (TextView) findViewById(R.id.article_content);
    	if (cursor.moveToFirst()) {
    		contentView.setText(cursor.getString(contentColumn));
    	}
	}		
	
}
