package org.cdmckay.android.provider.memoryalpha;

import org.cdmckay.android.provider.AbstractMediaWikiProvider;

import android.net.Uri;

public class MemoryAlphaProvider extends AbstractMediaWikiProvider {

	public static final String VERSION = "1";
	public static final String AUTHORITY = BASE_AUTHORITY + ".memoryalphaprovider";
	private static final String API_URI = "http://memory-alpha.org/api.php";
	
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
