package com.example.petrucciplayerv1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.example.petrucciplayerv1.Scores.Score;

import android.support.v7.app.ActionBarActivity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
//import android.preference.PreferenceActivity.Header;
import android.view.Menu;
import android.view.MenuItem;
import java.io.Serializable;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class Downloader extends ActionBarActivity {
	
	Score score;
	String piece;
	String composer;
	String id;
	
	public void fileDownload(String url, String piece, String composer) throws ClientProtocolException, IOException {
		
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();
		Header type = entity.getContentType();
		for (HeaderElement elem : type.getElements()) {
			String elemName = elem.getName();
			if (elemName.equals("application/pdf")) {
				InputStream is = response.getEntity().getContent();
				File baseDir = new File(Environment.getExternalStorageDirectory()+"/petrucci");
				if (!baseDir.exists()){
					baseDir.mkdir();
				}
				File subDir = new File(baseDir+"/"+composer);
				if (!subDir.exists()) {
					subDir.mkdir();
				}
				File file = new File(subDir, piece+".pdf");				
				FileOutputStream os = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int bufferLength = 0;
				while ((bufferLength = is.read(buffer))>0) {
					os.write(buffer, 0, bufferLength);
				}
				os.close();
				
			}
		}
		
		//put files in /petrucci directory
//		File directory = new File(Environment.getExternalStorageDirectory()+"/petrucci");
//		if (!directory.exists()){
//			directory.mkdir();
//		}
//		File subdir = new File(Environment.getExternalStorageDirectory()+"/petrucci/"+composer);
//		if (!subdir.exists()) {
//			subdir.mkdir();
//		}
//		DownloadManager manager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
//		Uri downloadUri = Uri.parse(url);
//		DownloadManager.Request request = new DownloadManager.Request(downloadUri);
//		request.setDescription("Downloading score");
//		request.setDestinationInExternalPublicDir("/petrucci/"+composer, piece+".pdf");
//		manager.enqueue(request);
	}
	
	class ScoreDownload extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			//build url
			String downloadUrl = "http://imslp.org/wiki/Special:IMSLPDisclaimerAccept/"+id.trim();
			try {
				fileDownload(downloadUrl, piece, composer);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				System.out.println("file download error clientprotocol");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("file dl error ioexc");
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Intent i = new Intent(Downloader.this, Library.class);
			startActivity(i);
			
			
		}
		
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_downloader);
		Intent i = getIntent();
		composer = i.getStringExtra("composer");
		Log.v("composer", composer);
		piece = i.getStringExtra("piece");
		id = i.getStringExtra("id");
		
		new ScoreDownload().execute();
		//do the downloading.... implement async etc
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.downloader, menu);
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
