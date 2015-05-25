package com.delta.nittfest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;







import com.delta.nittfest.MainActivity.SampleAdapter;
import com.delta.nittfest.MainActivity.SampleAdapter.ViewHolder;

import de.timroes.swipetodismiss.SwipeDismissList;
import de.timroes.swipetodismiss.SwipeDismissList.SwipeDirection;
import de.timroes.swipetodismiss.SwipeDismissList.Undoable;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Path.Direction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Notify extends ActionBarActivity {
	
	DBController controller = new DBController(this);
	List<Map<String, String>> sampleList;
	SampleAdapter sa;
	// Progress Dialog Object
	ProgressDialog prgDialog;
	HashMap<String, String> queryValues;
	ArrayList<HashMap<String, String>> notifList;
	String notifs[];
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		 setContentView(R.layout.notif_layout);
		 ActionBar actionBar = getActionBar();
	        actionBar.setDisplayShowTitleEnabled(false);
	        Toast.makeText(this, "Swipe left to delete", Toast.LENGTH_LONG).show();
		 //queryValues = new HashMap<String, String>();
			
			// Add departmentName extracted from Object
			//queryValues.put("notifText", "Hey there, Welcome to Nittfest 2015. The theme this year is Carnival.");
			// Add userID extracted from Object
			
			// Insert User into SQLite DB
			
			//controller.insertNotif(queryValues);
			final TextView em=(TextView) findViewById(R.id.empty);
			
			notifList = controller.getAllNotifs();
			if(notifList.size()>0)
			{
			em.setVisibility(View.INVISIBLE);
			}
			else
				em.setVisibility(View.VISIBLE);
			notifs=new String[100];
			for(int i=0;i<100;i++)
			{
			notifs[i]="null";	
			}
			for(int i=0;i<notifList.size();i++)
			{
			notifs[i] = notifList.get(i).get("notifText");
		    
			}
			
			/*
			for(int i=0;i<notifList.size();i++)
			{
			    TableRow row=new TableRow(this);
			    TextView n=new TextView(this);
			    n.setText(""+notifs[i]);
			    row.addView(n);
			    
			    
			    table.addView(row);
			}*/
			
			
			Map<String, String> map;
	        sampleList = new ArrayList<Map<String, String>>();
	        sa=new SampleAdapter(this, R.layout.notif_item, sampleList);

	        for (int i = notifList.size()-1; i >=0 ; i--) {
	            map = new HashMap<String, String>();
	            map.put(SampleAdapter.KEY_DEPT, notifs[i]);
	            
	            sampleList.add(map);
	        }
	        final ListAdapter listad=(ListAdapter) new SampleAdapter(this, R.layout.notif_item, sampleList);
	        ListView listView = (ListView) findViewById(R.id.list_notif);
	        listView.setAdapter(listad);
	        
	        SwipeDismissList.OnDismissCallback callback = new SwipeDismissList.OnDismissCallback() {
	            public SwipeDismissList.Undoable onDismiss(ListView listView, final int position) {
	                // Delete the item from your adapter (sample code):
	                final Map<String, String> itemToDelete = sa.getItem(position);
	               sa.remove(itemToDelete);Log.e("ll", "Here"+String.valueOf(itemToDelete));
	               
	               return new SwipeDismissList.Undoable() {

	                   // Method is called when user undoes this deletion
	                   

	                   // Return an undo message for that item
	                   

	                   // Called when user cannot undo the action anymore
	            	  
	            	   public void discard() {
	                       // Use this place to e.g. delete the item from database
	                       finallyDeleteFromSomeStorage(itemToDelete);
	                   }

					private void finallyDeleteFromSomeStorage(
							Map<String, String> itemToDelete) {
						Log.e("ll", "Here finally");
						//sampleList.remove(itemToDelete);
						
					}

					@Override
					public void undo() {
						// TODO Auto-generated method stub
						Log.e("ll", "Here undo");
						
					}
	               };
	                //return null;
	            }

	    		@Override
	    		public Undoable onDismiss(AbsListView listView, int position) {
	    			// TODO Auto-generated method stub
	    			Log.e("ll", "ondismiss");
	    			final Map<String, String> itemToDelete = sa.getItem(position);
		               sa.remove(itemToDelete);Log.e("ll", "Here"+String.valueOf(itemToDelete));
		               //sampleList.remove(itemToDelete);
		               
		   	        controller.deleteNotif(itemToDelete.get(SampleAdapter.KEY_DEPT));
		   	        listView.setAdapter((ListAdapter) sa);
		   	        if(sa.getCount()>0)
		   	        {
		   	        	em.setVisibility(View.INVISIBLE);
		   	        }
		   	        else
		   	        	em.setVisibility(View.VISIBLE);
	    			return null;
	    		}
	        };
	        SwipeDismissList swipeList = new SwipeDismissList(listView, callback);
	        swipeList.setSwipeDirection(SwipeDirection.START);
	        
	        
	        
	        
	       
	}
	
	
	/*
	class SampleAdapter extends ArrayAdapter<String[]> {

        public static final String KEY_ICON = "icon";
        public static final String KEY_COLOR = "color";
        public static final String KEY_NOTIF = "notif";
        public static final String KEY_INDEX = "index";

        private final LayoutInflater mInflater;
        private final String[] mData;

        public SampleAdapter(Context context, int layoutResourceId, String[] data) {
            super(context, layoutResourceId);
            mData = data;
            Log.e("tt","o"+mData[0]);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            final ViewHolder viewHolder;
            Log.e("tt","i"+mData[position]);
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.notif_item, parent, false);
                //viewHolder.imageViewIcon = (ImageView) convertView.findViewById(R.id.image_view_icon);
                viewHolder.notif=(TextView)convertView.findViewById(R.id.notifText);
                
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //viewHolder.imageViewIcon.setImageResource(mData.get(position).get(KEY_ICON));
            viewHolder.notif.setText(mData[position]);
           
            int[] colors = {
	                R.color.saffron,
	                R.color.eggplant,
	                R.color.sienna,
	                };
            convertView.setBackgroundResource(colors[position%3]);
            

            return convertView;
        }

        class ViewHolder {
            ImageView imageViewIcon;
            TextView notif;
            TextView index;
        }
}*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notif_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. 
		int id = item.getItemId();
		// When Sync action button is clicked
		
		if (id == R.id.home) {
			// Transfer data from remote MySQL DB to SQLite on Android and perform Sync
			Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
			
			objIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            
			startActivity(objIntent);
			//overridePendingTransition(R.anim.splash_out, R.anim.splash_in);
			this.finish();
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	class SampleAdapter extends ArrayAdapter<Map<String, String>> {

        public static final String KEY_ICON = "icon";
        public static final String KEY_COLOR = "color";
        public static final String KEY_DEPT = "dept";
        public static final String KEY_SCORE = "score";

        private final LayoutInflater mInflater;
        private final List<Map<String, String>> mData;

        public SampleAdapter(Context context, int layoutResourceId, List<Map<String, String>> data) {
            super(context, layoutResourceId, data);
            mData = data;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.notif_item, parent, false);
                //viewHolder.imageViewIcon = (ImageView) convertView.findViewById(R.id.image_view_icon);
                viewHolder.dept=(TextView)convertView.findViewById(R.id.notifText);
                //viewHolder.score=(TextView)convertView.findViewById(R.id.score);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //viewHolder.imageViewIcon.setImageResource(mData.get(position).get(KEY_ICON));
            viewHolder.dept.setText(mData.get(position).get(KEY_DEPT));
            //viewHolder.score.setText(String.valueOf(mData.get(position).get(KEY_SCORE)));
           /*TextView t= (TextView)convertView.findViewById(R.id.departmentName);
           TextView t2= (TextView)convertView.findViewById(R.id.score);
           
           t.setText(mData.get(position).get(KEY_DEPT));
           t2.setText(mData.get(position).get(KEY_SCORE));*/
            int[] colors = {
	                R.color.saffron,
	                R.color.eggplant,
	                R.color.sienna,
	               };
            convertView.setBackgroundResource(colors[position%3]);
            

            return convertView;
        }

        class ViewHolder {
            ImageView imageViewIcon;
            TextView dept;
            TextView score;
        }
}

}
