package org.cdmckay.android.provider.demo.tasks;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

public abstract class QueryTask extends AsyncTask<Uri, Void, Cursor> {
		
	private final ContentResolver resolver;	
	
	public QueryTask(ContentResolver resolver) {
		this.resolver = resolver;
	}
	
	@Override
	protected Cursor doInBackground(Uri... uris) {		
		final Cursor cursor = resolver.query(uris[0], null, null, null, null);
		return cursor;
	}
	
	protected abstract void onPostExecute(Cursor cursor);
	
}
