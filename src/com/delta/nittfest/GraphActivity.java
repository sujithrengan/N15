package com.delta.nittfest;

import java.util.Calendar;
import java.util.Date;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class GraphActivity extends ActionBarActivity {
	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.graph_activity);
		
		ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
		GraphView graph = (GraphView) findViewById(R.id.graphview);
		graph.getViewport().setXAxisBoundsManual(true);
		graph.getViewport().setMinX(0);
		graph.getViewport().setMaxX(10);

		// set manual Y bounds
		graph.getViewport().setYAxisBoundsManual(true);
		//graph.getViewport().setMinY(new Da);
		graph.getViewport().setMaxY(20);
		graph.getViewport().setScalable(true);
		graph.getViewport().setScrollable(true);
		Calendar calendar = Calendar.getInstance();
		Date d1 = calendar.getTime();
		calendar.add(Calendar.DATE, 1);
		
		LineGraphSeries<DataPoint> series_arch = new LineGraphSeries<DataPoint>(new DataPoint[] {
		          new DataPoint(0, 1),
		          new DataPoint(d1, 5),
		          new DataPoint(2, 3),
		          new DataPoint(3, 2),
		          new DataPoint(4, 6)
		});
		LineGraphSeries<DataPoint> series_chem = new LineGraphSeries<DataPoint>(new DataPoint[] {
		          new DataPoint(0, 3),
		          new DataPoint(d1, 7),
		          new DataPoint(2, 1),
		          new DataPoint(3, 1),
		          new DataPoint(4, 7)
		});
		LineGraphSeries<DataPoint> series_civ = new LineGraphSeries<DataPoint>(new DataPoint[] {
		          new DataPoint(0, 0),
		          new DataPoint(d1, 4),
		          new DataPoint(2, 9),
		          new DataPoint(3, 1),
		          new DataPoint(4, 1)
		});
		LineGraphSeries<DataPoint> series_cse = new LineGraphSeries<DataPoint>(new DataPoint[] {
		          new DataPoint(0, 4),
		          new DataPoint(d1, 7),
		          new DataPoint(2, 5),
		          new DataPoint(3, 5),
		          new DataPoint(4, 5)
		});
		LineGraphSeries<DataPoint> series_ece = new LineGraphSeries<DataPoint>(new DataPoint[] {
		          new DataPoint(0, 6),
		          new DataPoint(d1, 3),
		          new DataPoint(2, 8),
		          new DataPoint(3, 1),
		          new DataPoint(4, 0)
		});
		LineGraphSeries<DataPoint> series_eee = new LineGraphSeries<DataPoint>(new DataPoint[] {
		          new DataPoint(0, 9),
		          new DataPoint(d1, 8),
		          new DataPoint(2, 5),
		          new DataPoint(3, 1),
		          new DataPoint(4, 7)
		});
		LineGraphSeries<DataPoint> series_ice = new LineGraphSeries<DataPoint>(new DataPoint[] {
		          new DataPoint(0, 1),
		          new DataPoint(d1, 9),
		          new DataPoint(2, 7),
		          new DataPoint(3, 9),
		          new DataPoint(4, 3)
		});
		LineGraphSeries<DataPoint> series_mech = new LineGraphSeries<DataPoint>(new DataPoint[] {
		          new DataPoint(0, 8),
		          new DataPoint(d1, 9),
		          new DataPoint(2, 0),
		          new DataPoint(3, 6),
		          new DataPoint(4, 7)
		});
		series_cse.setColor(Color.WHITE);
		series_cse.setTitle("CSE");
		series_cse.setThickness(2);
		series_chem.setColor(Color.BLUE);
		series_chem.setTitle("CHEM");
		series_chem.setThickness(2);
		series_civ.setColor(Color.YELLOW);
		series_civ.setTitle("CIV");
		series_civ.setThickness(2);
		series_arch.setColor(Color.GREEN);
		series_arch.setTitle("CHEM");
		series_arch.setThickness(2);
		series_ece.setColor(Color.RED);
		series_ece.setTitle("ECE");
		series_ece.setThickness(2);
		series_eee.setColor(Color.DKGRAY);
		series_eee.setTitle("EEE");
		series_eee.setThickness(2);
		series_ice.setColor(Color.MAGENTA);
		series_ice.setTitle("ICE");
		series_ice.setThickness(2);
		series_mech.setColor(Color.CYAN);
		series_mech.setTitle("MECH");
		series_mech.setThickness(2);
		graph.addSeries(series_cse);
		graph.addSeries(series_chem);
		graph.addSeries(series_civ);
		graph.addSeries(series_arch);
		graph.addSeries(series_ece);
		graph.addSeries(series_eee);
		graph.addSeries(series_ice);
		graph.addSeries(series_mech);
		graph.getLegendRenderer().setVisible(true);
		
		
		graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.graph, menu);
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
