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
package org.cdmckay.android.provider.wikipedia;

import org.cdmckay.android.provider.AbstractMediaWikiProvider;

import android.net.Uri;

public class WikipediaProvider extends AbstractMediaWikiProvider {

	public static final String VERSION = "1";
	public static final String AUTHORITY = BASE_AUTHORITY + ".wikipediaprovider";
	private static final String API_URI = "http://en.wikipedia.org/w/api.php";
	
	public static class Search {
		private Search() {}		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/search");		
	}
	
	public static class Page {
		private Page() {}		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/page");
	}
	
	@Override
	protected String getVersion() {
		return VERSION;
	}	
	
	@Override
	protected String getAuthority() {
		return AUTHORITY;
	}
	
	@Override
	protected String getApiUri() {		
		return API_URI;
	}

}
