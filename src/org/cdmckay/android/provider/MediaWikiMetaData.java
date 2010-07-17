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

import android.net.Uri;

public class MediaWikiMetaData {

	public static final String VERSION = "1";
	public static final String AUTHORITY = "org.cdmckay.android.provider.MediaWikiProvider";
			
	public static class Search {
		private Search() {}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/search");		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cdmckay.mediawiki.search";		
		
		// String
		public static final String TITLE = "title";
		// String
		public static final String DESCRIPTION = "description";
		// String
		public static final String URL = "url"; 
	}
	
	public static class Page {
		private Page() {}
		
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/page");		
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.cdmckay.mediawiki.page";		
		
		// long
		public static final String PAGE_ID = "pageid";
		// long		
		public static final String NAMESPACE = "ns";
		// String
		public static final String TITLE = "title";
		// String
		public static final String CONTENT = "content";
	}
	
}
