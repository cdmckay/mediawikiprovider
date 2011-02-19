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
