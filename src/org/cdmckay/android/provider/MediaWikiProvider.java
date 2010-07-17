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

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class MediaWikiProvider extends ContentProvider {   	
		
	private static final int SEARCH = 1;
	private static final int PAGE_BY_TITLE = 2;
	private static final int PAGE_BY_ID = 3;
	
	private static final UriMatcher sUriMatcher = buildUriMatcher();
	private static UriMatcher buildUriMatcher() {
		UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		matcher.addURI(MediaWikiMetaData.AUTHORITY, "search/*", SEARCH);
		matcher.addURI(MediaWikiMetaData.AUTHORITY, "page/title/*", PAGE_BY_TITLE);
		matcher.addURI(MediaWikiMetaData.AUTHORITY, "page/id/#", PAGE_BY_ID);
		return matcher;
	}	
	
	private static final Module sModule = new WikipediaModule();
	
	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
			case SEARCH:
				return MediaWikiMetaData.Search.CONTENT_TYPE;
			case PAGE_BY_TITLE:
			case PAGE_BY_ID:
				return MediaWikiMetaData.Page.CONTENT_ITEM_TYPE;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
	}	

	@Override
	public boolean onCreate() {
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		switch (sUriMatcher.match(uri)) {
			case SEARCH:				
				return sModule.search(uri.getLastPathSegment());
			case PAGE_BY_TITLE:
				return sModule.getPageByTitle(uri.getLastPathSegment());
			case PAGE_BY_ID:
				return sModule.getPageById(Long.valueOf(uri.getLastPathSegment()));
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
	}
		
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		throw new UnsupportedOperationException();
	}
}
