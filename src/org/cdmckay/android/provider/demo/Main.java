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
import org.cdmckay.android.provider.wikipedia.WikipediaProvider;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RadioGroup;

/**
 * An Android activity that can be used to demonstrate and test the capabilities of the
 * MediaWikiProvider content provider.
 * 
 * @author cdmckay
 *
 */
public class Main extends Activity {	
	
	public static final String SETTINGS = "preferences";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.main);
				
		final SharedPreferences settings = getSharedPreferences(SETTINGS, MODE_PRIVATE);
		
		final RadioGroup providerModule = (RadioGroup) findViewById(R.id.provider_choice);
		providerModule.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {				
				final SharedPreferences.Editor editor = settings.edit();
				
				switch (checkedId) {
					case R.id.provider_memory_alpha:
						editor.putInt("provider", R.id.provider_memory_alpha);
						break;
					case R.id.provider_wikipedia:						
					    editor.putInt("provider", R.id.provider_wikipedia);											
						break;    			
				}
				
				editor.commit();
			}
		});
		providerModule.check(settings.getInt("provider", R.id.provider_wikipedia));		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.options, menu);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		    case R.id.search:
		    	onSearchRequested();
		        return true;
		    default:
		        return super.onOptionsItemSelected(item);
		}
	}	

}
