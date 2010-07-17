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

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PageHandler extends DefaultHandler {

	private static final String EMPTY_STRING = "";
	
	private boolean inPage = false;
	private boolean inRevisions = false;
	private boolean inRevision = false; 
	
	private long pageId = 0;
	private long namespace = 0;
	private String title = EMPTY_STRING;
	private StringBuilder content = new StringBuilder();
	
	public static class Result {
		public final long pageId;
		public final long namespace;
		public final String title;
		public final String content;
		
		public Result(long pageId, long namespace, String title, String content) {		
			this.pageId = pageId;
			this.namespace = namespace;
			this.title = title;
			this.content = content;
		}				
	}
	
	private List<Result> results = new ArrayList<Result>();
	
	public List<Result> getResults() {		
		return results;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException {
		final String name = localName.trim().toLowerCase();
		if (name.equals("page")) {
			inPage = true;
			pageId = Long.valueOf(attributes.getValue("pageid"));
			namespace = Long.valueOf(attributes.getValue("ns"));
			title = attributes.getValue("title");
		} else if (name.equals("revisions")) {
			inRevisions = true;
		} else if (name.equals("rev")) {
			inRevision = true;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		final String name = localName.trim().toLowerCase();
		if (name.equals("page")) {			
			pageId = 0;
			namespace = 0;
			title = EMPTY_STRING;			
			inPage = false;
		} else if (name.equals("revisions")) {
			inRevisions = false;
		} else if (name.equals("rev")) {
			results.add(new Result(pageId, namespace, title, content.toString()));
			content.setLength(0);
			inRevision = false;
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		final String str = new String(ch).substring(start, start + length);
		if (inPage && inRevisions && inRevision) {
			content.append(str);
		}
	}
	
}
