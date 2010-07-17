package org.cdmckay.android.provider;

import android.database.Cursor;

public interface Module {

	public abstract Cursor search(String query);

	public abstract Cursor getPageByTitle(String title);

	public abstract Cursor getPageById(long id);

}