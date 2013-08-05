package com.sample.redditapp;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class RedditMainList extends Activity  implements OnItemClickListener{

	private RedditItemAdapter  adapter;
	private String subredit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reddit_main_list);
		setTitle(getString(R.string.reddit_title, "funny"));
		
		subredit = getIntent().getStringExtra("selected_search");

		if(subredit == null)
			subredit = "funny";
		
		adapter = new RedditItemAdapter(this);
		ListView list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(this);
		
		startNetworkOperation();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == RESULT_OK) {
			Log.d("Search", "Search resulted ok");
			subredit = data.getStringExtra("selected_search");

			if(subredit == null) {
				subredit = "funny";
				startNetworkOperation();
			}

		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> adpater, View view, int position, long id) {
		RedditItem item = adapter.getItem(position);
		
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(android.content.Intent.EXTRA_SUBJECT, R.string.subject);
		intent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.content, item.author, item.title, item.url));
		startActivity(Intent.createChooser(intent, getString(R.string.share)));
		
	}
	
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.reddit_main_list, menu);

	    // Get the SearchView and set the searchable configuration
	    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
	    // Assumes current activity is the searchable activity
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

	    return true;
	}
	
	
	
	//All initial network calls , this should definetly should go in a service or at least in a different class
	// but I only have 6 hours
	public void startNetworkOperation() {
		RequestQueue queue = Volley.newRequestQueue(this);

		String url = String.format("http://www.reddit.com/r/%s/.json", subredit);
		
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				try {
					adapter.setItems(Processor.process(response));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(RedditMainList.this, "Error happened", Toast.LENGTH_LONG).show();
			}
		});
		
		queue.add(request);
	}

}
