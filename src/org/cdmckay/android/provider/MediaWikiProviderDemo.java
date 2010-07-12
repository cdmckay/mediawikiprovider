package org.cdmckay.android.provider;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * An Android activity that can be used to demonstrate and test the capabilities of the
 * MediaWikiProvider content provider.
 * 
 * @author cdmckay
 *
 */
public class MediaWikiProviderDemo extends Activity {

	private EditText mSearchText;
	private ListView mListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.main);
		
		mSearchText = (EditText) findViewById(R.id.search);
		mListView = (ListView) findViewById(R.id.list);
		
		TextView headerView = new TextView(this);
		headerView.setText("Results");
		
		mListView.addHeaderView(headerView);
		mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[] { "Test 1", "Test 2" }));
	}

}
