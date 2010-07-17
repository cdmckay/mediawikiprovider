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
