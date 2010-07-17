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
package org.cdmckay.android.provider.demo;

import org.cdmckay.android.provider.R;
import org.cdmckay.android.provider.R.id;
import org.cdmckay.android.provider.R.layout;

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
