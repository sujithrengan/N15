package com.delta.nittfest;

import static com.delta.nittfest.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.delta.nittfest.CommonUtilities.SENDER_ID;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.delta.nittfest.Myreceiver;
import com.delta.nittfest.ServerUtilities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MainActivity extends ActionBarActivity {
	// DB Class to perform DB related operations
	DBController controller = new DBController(this);
	// Progress Dialog Object
	ProgressDialog prgDialog;
	public GoogleCloudMessaging gcm;
	AsyncTask<Void, Void, Void> mRegisterTask;
	HashMap<String, String> queryValues;
	ArrayList<HashMap<String, String>> userList;
	ArrayList<HashMap<String,Integer>> deptList;
	String score[];
	ListView listView;
	int[] colors = {
            R.color.saffron,
            R.color.eggplant,
            R.color.sienna,
            R.color.greensea,
            R.color.belizehole,
            //R.color.saffron,
            //R.color.eggplant,
            R.color.sienna,
            R.color.saffron,
            R.color.eggplant,
            R.color.greensea,
            R.color.belizehole,
            //R.color.sienna,
            //R.color.saffron,
            R.color.eggplant,
            R.color.sienna};
	int depts[] = {
            R.string.archi,
            R.string.ca,
           R.string.chem,
            R.string.civil,
            R.string.comsci,
            R.string.doms,
            R.string.ece,
            R.string.eee,
            R.string.ice,
            R.string.mech,
            R.string.mme,
            R.string.prod,
        };

	float sc[];
	String name[];
	int maxscore,maxindex;
	public RelativeLayout rv;
	String regId = "";
	Myreceiver mr;
	static String filename = "abc";
	public static final int REFRESH_DELAY = 2000;

    private PullToRefreshView mPullToRefreshView;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);
		 setContentView(R.layout.activity_pull_to_refresh);
		 ActionBar actionBar = getActionBar();
	        actionBar.setDisplayShowTitleEnabled(false);
		 
		 rv=(RelativeLayout) findViewById(R.id.ol);	
		 prgDialog = new ProgressDialog(this);
			prgDialog.setMessage("Keep Calm and support your Department. Please wait...");
			prgDialog.setCancelable(false);	  
			listView = (ListView) findViewById(R.id.list_view);
			View footerView =  ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer, null, false);
	        listView.addFooterView(footerView);
	        
	        
		       if (isFirstTime()) {
		    	   		        	rv.setVisibility(View.INVISIBLE);
		    	   		        	//Toast.makeText(this, "Drag down to refresh", Toast.LENGTH_LONG).show();;
		        	
		        }
			Button gt=(Button)findViewById(R.id.gi);
			gt.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					rv.setVisibility(View.INVISIBLE);
					syncSQLiteMySQLDB();
										
				}
			});
			userList = controller.getAllUsers();
			updatelist();
			syncSQLiteMySQLDB();
			/*HashMap<String,Integer> map1 =new HashMap<String,Integer>();
			map1.put("ARCHI",R.string.archi);
			deptList.add(map1);
			map1.put("CA",R.string.ca);
			deptList.add(map1);
			map1.put("CHEM",R.string.chem);
			deptList.add(map1);
			map1.put("CIVIL",R.string.civil);
			deptList.add(map1);
			map1.put("COMSCI",R.string.comsci);
			deptList.add(map1);
			map1.put("DOMS",R.string.doms);
			deptList.add(map1);
			map1.put("ECE",R.string.ece);
			deptList.add(map1);
			map1.put("EEE",R.string.eee);
			deptList.add(map1);
			map1.put("ICE",R.string.ice);
			deptList.add(map1);
			map1.put("MECH",R.string.mech);
			deptList.add(map1);
			map1.put("MME",R.string.mme);
			deptList.add(map1);
			map1.put("PROD",R.string.prod);
			deptList.add(map1);*/
			
						
			//int[] depts;
		//	depts=new int[12];
		 
			//syncSQLiteMySQLDB();
			
			
		 
		 
		 
		 	
			
			
			gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
			mr = new Myreceiver();
			registerReceiver(mr, new IntentFilter(DISPLAY_MESSAGE_ACTION));

			mRegisterTask = new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {
					if (regId.equals("")) {
						try {
							regId = gcm.register(SENDER_ID);

							storeId(regId);

						} catch (Exception e) {
						}

					}
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					try {
						ServerUtilities.register(getApplicationContext(), regId);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mRegisterTask = null;
				}

			};
			mRegisterTask.execute(null, null, null);
			
			
			
	        

	        

	        
	        
	    
	        

	        
	        
	        //listView.addHeaderView(footerView);
	        

	        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
	        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
	            @Override
	            public void onRefresh() {
	                mPullToRefreshView.postDelayed(new Runnable() {
	                    @Override
	                    public void run() {
	                    	syncSQLiteMySQLDB();
	                    	
	                        mPullToRefreshView.setRefreshing(false);
	                    }
	                }, REFRESH_DELAY);
	            }
	        });
	        
	        
	 /*       
	        
	        
		// Get User records from SQLite DB
		userList = controller.getAllUsers();
		TableLayout table = (TableLayout) findViewById(R.id.tb);
		name=new String[100];
		score=new String[100];
		for(int i=0;i<100;i++)
		{
		name[i]=score[i]="";	
		}
		Log.e("db", String.valueOf(userList.size()));
		for(int i=0;i<userList.size();i++)
		{
		name[i] = userList.get(i).get("departmentName");
	    score[i] = userList.get(i).get("score");
		}
		
		for(int i=0;i<userList.size();i++)
		{
		    TableRow row=new TableRow(this);
		    TextView n=new TextView(this);
		    n.setText(""+name[i]);
		    row.addView(n);
		    TextView s=new TextView(this);
		    s.setText("  --  "+score[i]);
		    row.addView(s);
		    
		    table.addView(row);
		}
		
		// If users exists in SQLite DB
		if (userList.size() != 0) {
			
			
			// Set the User Array list in ListView
			ListAdapter adapter = new SimpleAdapter(MainActivity.this, userList, R.layout.view_user_entry, new String[] {
							"departmentName","score" }, new int[] {R.id.userName, R.id.userId });
			ListView myList = (ListView) findViewById(android.R.id.list);
			myList.setAdapter(adapter);
		}
		
		
		*/
		
		// Initialize Progress Dialog properties
		
		// BroadCase Receiver Intent Object
		//Intent alarmIntent = new Intent(getApplicationContext(), SampleBC.class);
		// Pending Intent Object
		//PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		// Alarm Manager Object
		//AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
		// Alarm Manager calls BroadCast for every Ten seconds (10 * 1000), BroadCase further calls service to check if new records are inserted in 
		// Remote MySQL DB
		//alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 5000, 100 * 1000, pendingIntent);
	}
	class MapComparator implements Comparator<Map<String, String>>
	 {
	     private final String key;

	     public MapComparator(String key)
	     {
	         this.key = key;
	     }

	     public int compare(Map<String, String> first,
	                        Map<String, String> second)
	     {
	         // TODO: Null checking, both for maps and values
	         float firstValue =  Float.parseFloat(first.get(key));
	         float secondValue = Float.parseFloat(second.get(key));
	         
	         if(firstValue>secondValue) return -1;
	         else if(firstValue<secondValue) return 1;
	         else return 0;
	         
	         //return -1*firstValue.compareTo(secondValue);
	     }
	 }
	// Options Menu (ActionBar Menu)
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// When Options Menu is selected
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. 
		int id = item.getItemId();
		// When Sync action button is clicked
		/*if (id == R.id.refresh) {
			// Transfer data from remote MySQL DB to SQLite on Android and perform Sync
			syncSQLiteMySQLDB();
			return true;
		}*/
		
		/*if (id == R.id.graph) {
			// Transfer data from remote MySQL DB to SQLite on Android and perform Sync
			Intent objIntent = new Intent(getApplicationContext(), GraphActivity.class);
			
			startActivity(objIntent);
			overridePendingTransition(R.anim.splash_in, R.anim.splash_out);
			return true;
		}
		*/
		
		if (id == R.id.notif) {
			// Transfer data from remote MySQL DB to SQLite on Android and perform Sync
			Intent objIntent = new Intent(getApplicationContext(), Notify.class);
			
			startActivity(objIntent);
			overridePendingTransition(R.anim.splash_in, R.anim.splash_out);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	// Method to Sync MySQL to SQLite DB
	public void syncSQLiteMySQLDB() {
		// Create AsycHttpClient object
		AsyncHttpClient client = new AsyncHttpClient();
		// Http Request Params Object
		RequestParams params = new RequestParams();
		// Show ProgressBar
		prgDialog.show();
		// Make Http call to getusers.php
		client.post("http://nittfest.in/15/home/sujith/+getscore", params, new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(String response) {
					// Hide ProgressBar
					prgDialog.hide();
					// Update SQLite DB with response sent by getusers.php
					updateSQLite(response);
				}
				// When error occured
				@Override
				public void onFailure(int statusCode, Throwable error, String content) {
					// TODO Auto-generated method stub
					// Hide ProgressBar
					prgDialog.hide();
					if (statusCode == 404) {
						Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
					} else if (statusCode == 500) {
						Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
								Toast.LENGTH_LONG).show();
					}
				}
		});
	}
	
	public void updateSQLite(String response){
		ArrayList<HashMap<String, String>> usersynclist;
		usersynclist = new ArrayList<HashMap<String, String>>();
		// Create GSON object
		Gson gson = new GsonBuilder().create();
		try {
			// Extract JSON array from the response
			JSONArray arr = new JSONArray(response);
			
			System.out.println(arr.length());
			// If no of array elements is not zero
			if((arr.length() != 0)){
				// Loop through each array element, get JSON object which has userid and username
				for (int i = 0; i < arr.length(); i++) {
					// Get JSON object
					JSONObject obj = (JSONObject) arr.get(i);
					System.out.println(obj.get("departmentName"));
					System.out.println(obj.get("score"));
					
					// DB QueryValues Object to insert into SQLite
					queryValues = new HashMap<String, String>();
					
					// Add departmentName extracted from Object
					queryValues.put("departmentName", obj.get("departmentName").toString());
					// Add userID extracted from Object
					queryValues.put("score", obj.get("score").toString());
					// Insert User into SQLite DB

					// Insert User into SQLite DB
					if((userList.size()==0))
					controller.insertUser(queryValues);
					else
						controller.updateUser(queryValues);
					/*HashMap<String, String> map = new HashMap<String, String>();
					// Add status for each User in Hashmap
					map.put("Id", obj.get("userId").toString());
					map.put("status", "1");
					usersynclist.add(map);*/
				}
				// Inform Remote MySQL DB about the completion of Sync activity by passing Sync status of Users
				//updateMySQLSyncSts(gson.toJson(usersynclist));
				// Reload the Main Activity
				updatelist();
				//reloadActivity();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	
	public void updatelist()
	{
		userList = controller.getAllUsers();
		name=new String[12];
				score=new String[12];
				sc=new float[12];
				for(int i=0;i<12;i++)
				{
				name[i]="";	
				score[i]="0";
				sc[i]=0f;
				}
				maxindex=0;
				maxscore=0;
				
				Collections.sort(userList, new MapComparator("score"));
				Log.d("don",String.valueOf(userList.size()));
				for(int i=0;i<userList.size();i++)
				{
				name[i] = userList.get(i).get("departmentName");
				score[i] = userList.get(i).get("score");
				if(name[i].equals("CIVIL"))
				{
					depts[i]=R.string.civil;
					
					
					//Log.d("sort","Here civ"+String.valueOf(depts[i]));
				}
				if(name[i].equals("ARCHI"))
					
					depts[i]=R.string.archi;
				if(name[i].equals("CA"))
					depts[i]=R.string.ca;
				if(name[i].equals("CHEM"))
					depts[i]=R.string.chem;
				
				if(name[i].equals("COMSCI"))
					depts[i]=R.string.comsci;
				if(name[i].equals("DOMS"))
					depts[i]=R.string.doms;
				if(name[i].equals("ECE"))
					depts[i]=R.string.ece;
				if(name[i].equals("EEE"))
					depts[i]=R.string.eee;
				if(name[i].equals("ICE"))
					depts[i]=R.string.ice;
				if(name[i].equals("MME"))
					depts[i]=R.string.mme;
				if(name[i].equals("MECH"))
					depts[i]=R.string.mech;
				if(name[i].equals("PROD"))
					depts[i]=R.string.prod;
				
				//depts[i]=deptList.get(i)
				//Log.d("sort","Psort"+name[i]+ String.valueOf(depts[i]));
			    
			    
				}

				Map<String, Integer> map;
		        List<Map<String, Integer>> sampleList = new ArrayList<Map<String, Integer>>();

		         for (int i = 0; i < depts.length; i++) {
		            map = new HashMap<String, Integer>();
		            map.put(SampleAdapter.KEY_DEPT, depts[i]);
		            map.put(SampleAdapter.KEY_COLOR, colors[i]);
		           // map.put(SampleAdapter.KEY_SCORE, Integer.parseInt(score[i]));
		            sc[i]=Float.parseFloat(score[i]);
		            //Log.d("sort","Dsort__"+depts[i]+"__"+score[i]);
		            sampleList.add(map);
		        }

		        ListAdapter listad=(ListAdapter) new SampleAdapter(this, R.layout.list_item, sampleList,sc);
		        listView.setAdapter(listad);
		        

		        //code to set adapter to populate list
		        
	}
	
	
	
	
	
	
	// Method to inform remote MySQL DB about completion of Sync activity
	public void updateMySQLSyncSts(String json) {
		System.out.println(json);
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("syncsts", json);
		// Make Http call to updatesyncsts.php with JSON parameter which has Sync statuses of Users
		/*client.post("http://thehyperionproject.comoj.com/mysqlsqlitesync/updatesyncsts.php", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				Toast.makeText(getApplicationContext(),	"MySQL DB has been informed about Sync activity", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onFailure(int statusCode, Throwable error, String content) {
					Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();
			}
		});*/
	}
	private void storeId(String regId) {
		final SharedPreferences prefs = getSharedPreferences(filename, 0);

		
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("registration_id", regId);
		editor.commit();
	}
	// Reload MainActivity
	public void reloadActivity() {
		Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
		
		startActivity(objIntent);
		this.finish();
	}
	 class SampleAdapter extends ArrayAdapter<Map<String, Integer>> {

	        public static final String KEY_ICON = "icon";
	        public static final String KEY_COLOR = "color";
	        public static final String KEY_DEPT = "dept";
	        public static final String KEY_SCORE = "score";

	        private final LayoutInflater mInflater;
	        private final List<Map<String, Integer>> mData;
	        private final float[] scdata;

	        public SampleAdapter(Context context, int layoutResourceId, List<Map<String, Integer>> data,float[] sc) {
	            super(context, layoutResourceId, data);
	            mData = data;
	            scdata =sc;
	            mInflater = LayoutInflater.from(context);
	        }

	        @Override
	        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
	            final ViewHolder viewHolder;
	            if (convertView == null) {
	                viewHolder = new ViewHolder();
	                convertView = mInflater.inflate(R.layout.list_item, parent, false);
	                //viewHolder.imageViewIcon = (ImageView) convertView.findViewById(R.id.image_view_icon);
	                viewHolder.dept=(TextView)convertView.findViewById(R.id.departmentName);
	                viewHolder.score=(TextView)convertView.findViewById(R.id.score);
	                viewHolder.top=(LinearLayout) findViewById(R.id.top);
	                convertView.setTag(viewHolder);
	            } else {
	                viewHolder = (ViewHolder) convertView.getTag();
	            }
	          
	           

	            //viewHolder.imageViewIcon.setImageResource(mData.get(position).get(KEY_ICON));
	            if(position!=12){
	            viewHolder.dept.setText(mData.get(position).get(KEY_DEPT));
	            //viewHolder.score.setText(String.valueOf(mData.get(position).get(KEY_SCORE)));
	            viewHolder.score.setText(String.valueOf(scdata[position]));
	            convertView.setBackgroundResource(mData.get(position).get(KEY_COLOR));
	            }
	           /*TextView t= (TextView)convertView.findViewById(R.id.departmentName);
	           TextView t2= (TextView)convertView.findViewById(R.id.score);
	           
	           t.setText(mData.get(position).get(KEY_DEPT));
	           t2.setText(mData.get(position).get(KEY_SCORE));*/
	            //Log.e("ps",String.valueOf(position)+" "+String.valueOf(maxindex)+" "+String.valueOf(maxscore));
	            
	            
	            //viewHolder.top.setBackgroundColor(mData.get(position).get(KEY_COLOR));
	            /*if(viewHolder.score.getText()==String.valueOf(maxscore)){
	            	Log.e("ps",String.valueOf(position)+" "+String.valueOf(maxindex)+" "+String.valueOf(maxscore));
	            float[] hsv = new float[3];
	            int color = mData.get(position).get(KEY_COLOR);
	            Color.colorToHSV(color, hsv);
	            hsv[2] *= 0.8f; // value component
	            color = Color.HSVToColor(hsv);
	            viewHolder.top.setBackgroundColor(color);
	            }*/
	            return convertView;
	        }

	        class ViewHolder {
	            ImageView imageViewIcon;
	            LinearLayout top;
	            TextView dept;
	            TextView score;
	        }
}
	
	 
	 private boolean isFirstTime()
	 {
	 SharedPreferences preferences = getPreferences(MODE_PRIVATE);
	 boolean ranBefore = preferences.getBoolean("RanBefore", false);
	 if (!ranBefore) {
	 	
	     SharedPreferences.Editor editor = preferences.edit();
	     editor.putBoolean("RanBefore", true);
	     editor.commit();
	     RelativeLayout rv=(RelativeLayout) findViewById(R.id.ol);
	    
	     
	     rv.setVisibility(View.VISIBLE);
	    
	     }
	 return ranBefore;
	     
	 }
	 @Override
		protected void onDestroy() {
			if (mRegisterTask != null) {
				mRegisterTask.cancel(true);
			}
			try {
				unregisterReceiver(mr);
			} catch (Exception e) {
				Log.e("UnRegister Receiver Error", "> " + e.getMessage());
			}
			super.onDestroy();
		}
	 @Override
	protected void onStop() {
		// TODO Auto-generated method stub
		 if (mRegisterTask != null) {
				mRegisterTask.cancel(true);
			}
			try {
				unregisterReceiver(mr);
			} catch (Exception e) {
				Log.e("UnRegister Receiver Error", "> " + e.getMessage());
			}
		super.onStop();
	}
}
