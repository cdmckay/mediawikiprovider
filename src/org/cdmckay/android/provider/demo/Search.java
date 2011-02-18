package org.cdmckay.android.provider.demo;

import org.cdmckay.android.provider.MediaWikiMetaData;
import org.cdmckay.android.provider.R;
import org.cdmckay.android.provider.memoryalpha.MemoryAlphaProvider;
import org.cdmckay.android.provider.wikipedia.WikipediaProvider;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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
        			providerUri = MemoryAlphaProvider.Search.CONTENT_URI;
        			break;
        		case R.id.provider_wikipedia:
        		default:
        			providerUri = WikipediaProvider.Search.CONTENT_URI;        			
        	}
        	
    		final Uri uri = Uri.withAppendedPath(providerUri, query);	
    		final Cursor cursor = resolver.query(uri, null, null, null, null);    		    		
    		
    		final String[] columns = new String[] { 
    				MediaWikiMetaData.Search.TITLE, 
    				MediaWikiMetaData.Search.DESCRIPTION 
    				};
    		
    		final int[] to = new int[] { R.id.text1, R.id.text2 };
    		
    		mListView.setAdapter(new SimpleCursorAdapter(this, 
    				R.layout.list_item, cursor, columns, to));
        }        
	}
	
}
