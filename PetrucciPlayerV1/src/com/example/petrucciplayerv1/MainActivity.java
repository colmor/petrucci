package com.example.petrucciplayerv1;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.support.v7.app.ActionBarActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;

public class MainActivity extends ActionBarActivity {

	private static String baseUrl = "http://imslp.org/wiki/Category:Composers";
	ListView listView;

	List<String> getComposers() throws IOException {
		String url = baseUrl;
		List<String> composers = new LinkedList<String>();

		// do for one page first....
		Document webPage = Jsoup.connect(url).timeout(5000).get();

		// get page section with composers (id=mw-subcategories)
		Element compSection = webPage.getElementById("mw-subcategories");
		if (compSection != null) {
			// get and loop through composer links
			Elements compLinks = compSection.getElementsByTag("a");
			for (Element compLink : compLinks) {
				// get composer name, strip "Category:" and append to list
				composers.add(compLink.attr("title").substring(9));
//				String clean = compLink.attr("title").substring(9);
//				String quotes = "\""+clean+"\"";
//				composers.add(quotes);
				
			}
		}

		return composers;
	}

	class DownloadComposers extends AsyncTask<Void, String, List<String>> {

		@Override
		protected List<String> doInBackground(Void... params) {
			// download the list of composers
			List<String> result = new LinkedList<String>();
			try {
				result = getComposers();
				//System.out.println(result.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//Log.e("doInBackground", e.toString());
				//return null;
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<String> result) {
			// deal with downloaded list here...
			super.onPostExecute(result);
			// try to populate listview using array adapter
			if (result != null) {
				//List<String> array = new ArrayList<String>(result);
				//System.out.println(array.toString());
				//maybe define a better text view
//				ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						listView.getContext(),
						android.R.layout.simple_list_item_1, result);
				//ListAdapter listAd = adapter;
				//System.out.println(adapter.areAllItemsEnabled());
				//adapter.addAll(result);
				listView.setAdapter(adapter);
				//listView.setTextFilterEnabled(true);
				//setListAdapter();
			}

		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView = (ListView) findViewById(R.id.listView1);
		new DownloadComposers().execute();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//create intent that sends composer name to pieces
				String composerName = (String) ((TextView) view).getText();
				Intent i = new Intent(MainActivity.this, Pieces.class);
				i.putExtra("composer", composerName);
				startActivity(i);
			}
		});

	}
	
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}			
}
