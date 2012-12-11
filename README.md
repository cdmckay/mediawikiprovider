MediaWikiProvider
=================

MediaWikiProvider is an [Android Content Provider](https://developer.android.com/guide/topics/providers/content-providers.html) for searching and querying [MediaWiki](https://www.mediawiki.org/wiki/MediaWiki) wikis.

The authority for MediaWikiProvider is `org.cdmckay.android.provider.MediaWikiProvider`.

When retrieving articles, only the latest revision is fetched.

Here are the possible URIs:

```
*/search/*
*/page/title/*
*/page/id/#
*/page/title/*/sections
*/page/title/*/section/#
```

Here are some examples of each:

```
content://org.cdmckay.android.provider.mediawikiprovider/en.wikipedia.org+w/search/Tex
content://org.cdmckay.android.provider.mediawikiprovider/en.wikipedia.org+w/page/title/Texas
content://org.cdmckay.android.provider.mediawikiprovider/en.wikipedia.org+w/page/id/432
content://org.cdmckay.android.provider.mediawikiprovider/en.wikipedia.org+w/page/title/Texas/sections
content://org.cdmckay.android.provider.mediawikiprovider/en.wikipedia.org+w/page/title/Texas/section/1
```

