package org.cdmckay.android.provider.demo;

import org.cdmckay.android.provider.MediaWikiMetaData;
import org.cdmckay.android.provider.R;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class Search extends Activity {

	private ListView mListView; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);            
        setContentView(R.layout.search);
                
        mListView = (ListView) findViewById(R.id.list);
        
//        final TextView headerView = new TextView(this);
//        headerView.setText(R.string.results);        
//        mListView.addHeaderView(headerView);
        
        final Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
        	final String query = intent.getStringExtra(SearchManager.QUERY);
        	
        	final ContentResolver resolver = getContentResolver();
    		final Uri uri = Uri.withAppendedPath(MediaWikiMetaData.Search.CONTENT_URI, query);	
    		final Cursor cursor = resolver.query(uri, null, null, null, null);    	
    		
    		final String[] columns = new String[] { 
    				MediaWikiMetaData.Search.TITLE, 
    				MediaWikiMetaData.Search.DESCRIPTION 
    				};
    		
    		final int[] to = new int[] { android.R.id.text1, android.R.id.text2 };
    		
    		mListView.setAdapter(new SimpleCursorAdapter(this, 
    				R.layout.list_item, cursor, columns, to));
        }        
	}
	
}
