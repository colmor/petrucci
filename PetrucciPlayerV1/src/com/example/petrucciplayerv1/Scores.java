package com.example.petrucciplayerv1;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.example.petrucciplayerv1.Scores.myAdapter;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Scores extends ActionBarActivity {
	
	String composer;
	String piece;
	String baseUrl = "http://imslp.org/wiki/";
	ListView listView;
	
	class Score implements Serializable {
		private static final long serialVersionUID = 1234L;
		private String id;
		private String composer;
		private String piece;
		private String title;
		private String sizeInfo;
		
		public Score(String id, String composer, String piece, String title, String sizeInfo) {
			this.id = id.substring(23);
			this.composer = composer;
			this.piece = piece;
			this.title = title;
			this.sizeInfo = sizeInfo;
		}
		
		public String getId(){
			return id;
		}
		public String getComposer() {
			return composer;
		}
		public String getPiece() {
			return  piece;
		}
		public String getTitle() {
			return title;
		}
		public String getSizeInfo() {
			return sizeInfo;
		}
	}
	
	public class myAdapter extends ArrayAdapter<Score> {

		public myAdapter(Context context, List<Score> objects) {
			super(context, 0, objects);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			//return super.getView(position, convertView, parent);
			Score score = getItem(position);
			//check if existing view is being reused, otherwise inflate view
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.score_item, parent, false);
			}
			//get views for data population
			TextView title = (TextView) convertView.findViewById(R.id.scoreTitle);
			TextView id = (TextView) convertView.findViewById(R.id.scoreId);
			TextView size = (TextView) convertView.findViewById(R.id.scoreSize);
			TextView piece = (TextView) convertView.findViewById(R.id.scorePiece);
			
			title.setText(score.getTitle());
			id.setText(score.getId());
			size.setText(score.getSizeInfo());
			piece.setText(score.getPiece());
			return convertView;
		}
		
		
		
	}
	
	List<Score> getScores() throws IOException {
		List<Score> scores = new LinkedList<Score>();
		//build url
		try {
			String url = baseUrl + URLEncoder.encode(piece, "UTF-8").replace('+', '_');
			Document webPage = Jsoup.connect(url).get();
			
			
			//get tabs class items (each tab for full scores, parts etc.)
			Elements tabs = webPage.getElementsByClass("tabs");
			Elements children = null;
			Elements items = null;
			if (tabs.size()>0){
				Element tab = null;
				int tabNo=1;
				//scorefound = false 
				//at least one tab = false
				//stop = false
				//iterate through tabs
				for (Element currentTab : tabs){
					tab = currentTab.getElementById("tab"+tabNo);
					if(tab != null){
						children = tab.children();
						for (Element c : children) {
							if (c.hasClass("we")){
								items = c.getElementsByClass("we");
								for (Element item : items) {
									//process the scores... we_file and we_file_first identical
									Elements weFileFirst = item.getElementsByClass("we_file_first");
									processScore(composer, piece, weFileFirst, scores);
									Elements weFile = item.getElementsByClass("we_file");
									processScore(composer, piece, weFile, scores);
								}
							}
						}
					}
					tabNo++;
				}
			} else {
				items = webPage.getElementsByClass("we");
				if (items != null) {
					for (Element item : items) {
						Elements weFileFirst = item
								.getElementsByClass("we_file_first");
						processScore(composer, piece, weFileFirst, scores);
						Elements weFile = item
								.getElementsByClass("we_file");
						processScore(composer, piece, weFile, scores);
					}
					//url = null;					
				}
			}			
			//if (tabs.size()==1) {
//				Elements we = webPage.getElementsByClass("we");
//				for (Element w : we) {
//					//Elements wefilefirst...
//					Elements weFileFirst = w.getElementsByClass("we_file_first");
//					//deal with score info
//					for (Element element : weFileFirst) {
//						String linkExt = element.getElementsByTag("a").get(0).attr("title").toString();
//						String sizeInfo = element.getElementsByClass("we_file_info2").get(0).text();
//						System.out.println(linkExt);
//						System.out.println(sizeInfo);
//					}
//				}
			//}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return scores;
	}
	
	private void processScore(String composer2, String piece2, Elements weFileFirst, List<Score> scores) {
		// TODO Auto-generated method stub
		for (Element weItem : weFileFirst) {
			String id = weItem.getElementsByTag("a").get(0).attr(
					"title").toString();
			String sizeInfo = weItem.getElementsByClass(
					"we_file_info2").get(0).text();
			String title =  weItem.getElementsByClass(
					"we_file_dlarrwrap").parents().get(0).text();
			Score score = new Score(id, composer, piece, title, sizeInfo);
			scores.add(score);
		}
		
	}

	class DownloadScores extends AsyncTask<Void, String, List<Score>> {

		@Override
		protected List<Score> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			List<Score> result = new LinkedList<Score>();
			try {
				result = getScores();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return result;
		}
		
		protected void onPostExecute(List<Score> result) {
			// deal with downloaded list here...
			super.onPostExecute(result);
			// try to populate listview using array adapter
			//simple version for testing
			if (result != null) {
				//have to use custom adapter
//				List<String> simple = new LinkedList<String>();
//				for(Score res : result){
//					simple.add(res.getTitle());
//					Log.d("simple score", res.getTitle());
//				}
				
				myAdapter adapter = new myAdapter(listView.getContext(), result);
				listView.setAdapter(adapter);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scores);
		Intent i = getIntent();
		composer = i.getStringExtra("composer");
		piece = i.getStringExtra("piece");
		//set listview and populate list of pieces
		listView = (ListView) findViewById(R.id.listView1);
		new DownloadScores().execute();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Score scoreInList = (Score) listView.getAdapter().getItem(position);
				Intent intent = new Intent(Scores.this, Downloader.class);
				//intent.putExtra("score", scoreInList);
				//intent.put
				intent.putExtra("id", scoreInList.getId());
				intent.putExtra("composer", scoreInList.getComposer());
				intent.putExtra("piece", scoreInList.getPiece());
				startActivity(intent);
			}
			
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scores, menu);
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
