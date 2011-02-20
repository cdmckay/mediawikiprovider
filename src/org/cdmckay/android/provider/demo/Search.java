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
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Search extends Activity {	
	
	private ListView mListView; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);            
        setContentView(R.layout.search);
                
        mListView = (ListView) findViewById(R.id.list);       
        
        final Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
        	final String query = intent.getStringExtra(SearchManager.QUERY);
        	
        	final ContentResolver resolver = getContentResolver();
        	
        	final SharedPreferences settings = getSharedPreferences(Main.SETTINGS, MODE_PRIVATE);        	
        	final Uri providerUri;
        	switch (settings.getInt("provider", R.id.provider_wikipedia)) {        		
        		case R.id.provider_memory_alpha:
        			providerUri = Uri.withAppendedPath(MediaWikiMetaData.CONTENT_URI, 
        					Main.MEMORY_ALPHA_API + "/search");
        			break;
        		case R.id.provider_wikipedia:
        		default:
        			providerUri = Uri.withAppendedPath(MediaWikiMetaData.CONTENT_URI, 
        					Main.WIKIPEDIA_API + "/search");			
        	}
        	
    		final Uri uri = Uri.withAppendedPath(providerUri, query);	
    		final Cursor cursor = resolver.query(uri, null, null, null, null);    		
    		startManagingCursor(cursor);
    		
    		final String[] columns = new String[] { 
    				MediaWikiMetaData.Search.TITLE, 
    				MediaWikiMetaData.Search.DESCRIPTION 
    				};
    		
    		final int[] to = new int[] { R.id.text1, R.id.text2 };
    		
    		mListView.setAdapter(new SimpleCursorAdapter(this, 
    				R.layout.list_item, cursor, columns, to));
    		
    		mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if (cursor.moveToPosition(position)) {
						final Intent articleIntent = new Intent(Search.this, Article.class);			
						final int titleColumn = cursor.getColumnIndex(MediaWikiMetaData.Search.TITLE);
						articleIntent.putExtra("title", cursor.getString(titleColumn));
						startActivity(articleIntent);
					}														
				}				
    			
    		});
        }        
	}
	
}
