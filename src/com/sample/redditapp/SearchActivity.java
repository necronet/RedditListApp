package com.sample.redditapp;

import java.io.ObjectOutputStream.PutField;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class SearchActivity extends Activity {

	private List<String> subredits;
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		ListView list = (ListView)findViewById(R.id.list);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
		list.setAdapter(adapter);
		// Show the Up button in the action bar.
		setupActionBar();

		// Get the intent, verify the action and get the query
		Intent intent = getIntent();
		Log.d("Search", "Search activity");
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			
			final String query = intent.getStringExtra(SearchManager.QUERY);
			Log.d("Search", "Search activity intent query: " + query);
			String url = "http://www.reddit.com/subreddits.json";

			RequestQueue queue = Volley.newRequestQueue(this);
			JsonObjectRequest request = new JsonObjectRequest(
					Request.Method.GET, url, null,
					new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							// get all subredits wish I would have time to save
							try {
								subredits = Processor
										.processSubredits(response);
								Log.d("Search", "list " + subredits);
								search(query);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

						
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							Toast.makeText(SearchActivity.this,
									"Error happened", Toast.LENGTH_LONG).show();
						}
					});
			queue.add(request);
			
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapter, View view,
						int position, long id) {
					Intent intent = new Intent();
					intent.putExtra("selected_search", SearchActivity.this.adapter.getItem(position));
					setResult(RESULT_OK, intent);
					finish();
				}
			});
		}
	}
	
	private void search(String query) {
		
		adapter.clear();
		for(String subredit: subredits) {
			if(subredit.toLowerCase().contains(query.toLowerCase())) {
				adapter.add(subredit);
			}
		}
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
