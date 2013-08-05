package com.sample.redditapp;

public class RedditItem {

	public String author;
	public String thumbnail;
	public String title;
	public String url;
	
	@Override
	public String toString() {
		return "RedditItem [author=" + author + ", thumbnail=" + thumbnail
				+ ", title=" + title + "]";
	}
	
}
