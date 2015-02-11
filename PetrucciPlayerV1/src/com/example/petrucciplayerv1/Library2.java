package com.example.petrucciplayerv1;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

//import android.support.v7.app.ActionBarActivity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Library2 extends ListActivity {
	
	

	List<String> fileList;
	ArrayAdapter<String> adapter;
	String baseDir;
	String selectedDir;
	File myRoot;
	boolean inSubDir = false;
	
	void getFiles(File[] files) {
		//File[] files = file.listFiles();
		fileList.clear();
		if (inSubDir){
			fileList.add("Back to Composers");
		}
		for (File f : files) {
			fileList.add(f.getName());
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_library2);
		baseDir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/petrucci";
		fileList = new LinkedList<String>();
		myRoot = new File(baseDir);
		getFiles(myRoot.listFiles());
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fileList);
		setListAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		int selectedRow = (int) id;
		if (!inSubDir) {
			String fName = fileList.get(selectedRow);//this is the directory extension from root
			selectedDir = baseDir + "/" + fName;
			inSubDir = true;
			getFiles(new File(selectedDir).listFiles());
			adapter.notifyDataSetChanged();
			
		} else if (inSubDir && selectedRow==0) {
			//rerender composer list
			inSubDir = false;
			getFiles(myRoot.listFiles());
			adapter.notifyDataSetChanged();
			
		} else {
			String chosenFilePath = selectedDir + "/" +fileList.get(selectedRow);
			File chosenFile = new File(chosenFilePath);
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setDataAndType(Uri.fromFile(chosenFile), "application/pdf");
			startActivity(i);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.library2, menu);
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
