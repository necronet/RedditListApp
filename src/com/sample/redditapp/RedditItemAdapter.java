package com.sample.redditapp;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

public class RedditItemAdapter extends BaseAdapter {

	private Context context;
	private List<RedditItem> items;
	private RequestQueue queue;
	private ImageLoader loader;

	public RedditItemAdapter(Context context) {
		this.context = context;
		items = new ArrayList<RedditItem>();
		queue = Volley.newRequestQueue(context);

		loader = new ImageLoader(queue, new ImageCache() {

			@Override
			public void putBitmap(String url, Bitmap bitmap) {
			}

			@Override
			public Bitmap getBitmap(String url) {
				return null;
			}
		});
	}

	@Override
	public int getCount() {
		return items.size();
	}

	public RedditItemAdapter setItems(List<RedditItem> items) {
		if (items != null) {
			this.items = items;
			notifyDataSetChanged();
		}
		return this;
	}

	@Override
	public RedditItem getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.reddit_item, null);
		}

		RedditItem item = getItem(position);
		TextView textAuthor = (TextView) convertView
				.findViewById(R.id.textAuthor);
		TextView textDescription = (TextView) convertView
				.findViewById(R.id.textDescription);
		textAuthor.setText(item.author);
		textDescription.setText(item.title);

		NetworkImageView thumbnail = (NetworkImageView) convertView
				.findViewById(R.id.thumbnail);
		thumbnail.setDefaultImageResId(R.drawable.ic_launcher);
		thumbnail.setImageUrl(item.thumbnail, loader);

		return convertView;
	}

}
