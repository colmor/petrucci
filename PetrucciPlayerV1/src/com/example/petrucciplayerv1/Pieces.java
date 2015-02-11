package com.example.petrucciplayerv1;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Pieces extends ActionBarActivity {
	
	String composer;
	String baseUrl = "http://imslp.org/wiki/Category:";
	ListView listView;
	
	List<String> getPieces() throws IOException {
		List<String> pieces = new LinkedList<String>();
		//build url to fetch pieces for composer
		try {
			//
			String url = baseUrl + URLEncoder.encode(composer, "UTF-8").replace('+', '_');
			//System.out.println(url);
			
			//perform scraping
			Document webPage = Jsoup.connect(url).timeout(5000).get();
			//id mw-pages
			Element pageSect = webPage.getElementById("mw-pages");
			if (pageSect != null){
				Elements pieceLinks = pageSect.getElementsByTag("a");
				for (Element pieceLink : pieceLinks) {
					pieces.add(pieceLink.attr("title"));
				}
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return pieces;
	}
	
	class DownloadPieces extends AsyncTask<Void, String, List<String>> {

		@Override
		protected List<String> doInBackground(Void... params) {
			// download the list of composers
			List<String> result = new LinkedList<String>();
			try {
				result = getPieces();
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
				
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						listView.getContext(),
						android.R.layout.simple_list_item_1, result);

				listView.setAdapter(adapter);
				//listView.setTextFilterEnabled(true);

			}

		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pieces);
		Intent i = getIntent();
		composer = i.getStringExtra("composer");
		//set listview and populate list of pieces
		listView = (ListView) findViewById(R.id.listView1);
		new DownloadPieces().execute();
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//create intent that sends composer name to pieces
				//String composerName = (String) ((TextView) view).getText();
				//Intent i = new Intent(MainActivity.this, Pieces.class);
				//i.putExtra("composer", composerName);
				//startActivity(i);
				String pieceName = (String) ((TextView) view).getText();
				Intent i = new Intent(Pieces.this, Scores.class);
				i.putExtra("composer", composer);
				i.putExtra("piece", pieceName);
				startActivity(i);
				
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pieces, menu);
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
