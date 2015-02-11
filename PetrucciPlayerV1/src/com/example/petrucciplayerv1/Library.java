package com.example.petrucciplayerv1;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Library extends ListActivity {
	
	List<String> fileList;
	ListView listView;
	File baseDir;
	String base;
	String composer;
	ArrayAdapter<String> adapter; 
//	boolean home=true;
	
	void getFiles(File[] files) {
		//File[] files = file.listFiles();
		fileList.clear();
		for (File f : files) {
			fileList.add(f.getName());
		}
		
		//adapter.setNotifyOnChange(true);
		//listView.setAdapter(adapter);
		//setListAdapter(adapter);
	}
		
//	class rerenderList extends AsyncTask<Params, Progress, Result>	{
//		
//	}
//	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_library);
		baseDir = new File(Environment.getExternalStorageDirectory()+"/petrucci");
		base = Environment.getExternalStorageDirectory().toString()+"/petrucci";
		fileList = new LinkedList<String>();
		listView = (ListView) findViewById(android.R.id.list);
		getFiles(baseDir.listFiles());
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fileList);
		listView.setAdapter(adapter);
		Button b = (Button) findViewById(R.id.button1);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Library.this, MainActivity.class);
				startActivity(intent);
			}
		});
		
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		int selectedRow = (int) id;
//		if (composer!=null) {
//			base = Environment.getExternalStorageDirectory().toString()+"/petrucci/"+"\""+composer +"\"";
//		} else
//			base = Environment.getExternalStorageDirectory().toString()+"/petrucci/";
		base = Environment.getExternalStorageDirectory().toString()+"/petrucci/";
		Log.v("base",base);
		File dir = new File(baseDir+"/"+composer);
		File tmpDir = dir;
		Log.v("dir",dir.toString());
		if (tmpDir.isDirectory()){
			getFiles(tmpDir.listFiles());
			composer = fileList.get(selectedRow);
			Log.v("composer",composer);
			
			tmpDir = new File(tmpDir, composer);
			Log.v("tmpdir",tmpDir.getAbsolutePath());
			//new AdapterHelper().update()
			adapter.notifyDataSetChanged();
//			home=false;			
		} else {
			File pdf = new File(base+"/"+"\""+fileList.get(selectedRow)+"\"");
			Log.v("pdf",pdf.toString());
//			File pdf = new File(baseDir+"/\""+composer+"\"/"+fileList.get(selectedRow));
//			Intent i = new Intent(Intent.ACTION_VIEW);
//			i.setDataAndType(Uri.fromFile(pdf), "application/pdf");
			
		}
		
	}
	
//	@Override
//	public void onBackPressed() {
//		// TODO Auto-generated method stub
//		super.onBackPressed();
//		if(home==false){
//			getFiles(baseDir.listFiles());
//			adapter.notifyDataSetChanged();
//		}
//	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.library, menu);
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
