package com.codepath.apps.mysimpletweets;

import android.content.Context;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     TwitterCl client = RestApplication.getRestClient();
 *     // use client to send requests to API
 *
 */
public class TwitterApp extends com.activeandroid.app.Application {
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		TwitterApp.context = this;
	}

	public static TwitterCl getRestClient() {
		return (TwitterCl) TwitterCl.getInstance(TwitterCl.class, TwitterApp.context);
	}
}