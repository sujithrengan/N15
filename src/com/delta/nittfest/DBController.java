package com.delta.nittfest;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBController  extends SQLiteOpenHelper {

	public DBController(Context applicationcontext) {
        super(applicationcontext, "user.db", null, 1);
    }
	//Creates Table
	@Override
	public void onCreate(SQLiteDatabase database) {
		String query;
		query = "CREATE TABLE users ( departmentName TEXT, score INTEGER )";
        database.execSQL(query);
        query = "CREATE TABLE notifs ( notifText TEXT )";
        database.execSQL(query);
	}
	@Override
	public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
		String query;
		query = "DROP TABLE IF EXISTS users";
		database.execSQL(query);
		query = "DROP TABLE IF EXISTS notifs";
		database.execSQL(query);
        onCreate(database);
	}
	
	
	/**
	 * Inserts User into SQLite DB
	 * @param queryValues
	 */
	public void insertUser(HashMap<String, String> queryValues) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("departmentName", queryValues.get("departmentName"));
		values.put("score", queryValues.get("score"));
		database.insert("users", null, values);
		database.close();
	}
	
	public void insertNotif(HashMap<String, String> queryValues) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("notifText", queryValues.get("notifText"));
		//values.put("index", queryValues.get("index"));
		database.insert("notifs", null, values);
		database.close();
	}
	
	public void deleteNotif(String notiftxt) {
		SQLiteDatabase database = this.getWritableDatabase();
		ContentValues values = new ContentValues();
			
		//database.insert("notifs", null, values);
		database.delete("notifs", "notifText"+"= '"+notiftxt+"'",null);
		database.close();
	}
	
	public void updateUser(HashMap<String, String> queryValues) {
		SQLiteDatabase database = this.getWritableDatabase();
		String dn=queryValues.get("departmentName");
		ContentValues values = new ContentValues();
		String where = "departmentName" + "= '" + dn+"'";
		values.put("departmentName", queryValues.get("departmentName"));
		values.put("score", queryValues.get("score"));
		database.update("users", values, where,null);
		
		database.close();
	}
	/**
	 * Get list of Users from SQLite DB as Array List
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getAllUsers() {
		ArrayList<HashMap<String, String>> usersList;
		usersList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  * FROM users";
	    SQLiteDatabase database = this.getWritableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	        do {
	        	HashMap<String, String> map = new HashMap<String, String>();
	        	map.put("departmentName", cursor.getString(0));
	        	map.put("score", cursor.getString(1));
	        	
                usersList.add(map);
	        } while (cursor.moveToNext());
	    }
	    database.close();
	    return usersList;
	}
	
	public ArrayList<HashMap<String, String>> getAllNotifs() {
		ArrayList<HashMap<String, String>> notifsList;
		notifsList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  * FROM notifs";
	    SQLiteDatabase database = this.getWritableDatabase();
	    Cursor cursor = database.rawQuery(selectQuery, null);
	    if (cursor.moveToFirst()) {
	        do {
	        	HashMap<String, String> map = new HashMap<String, String>();
	        	map.put("notifText", cursor.getString(0));
	        	//map.put("index", cursor.getString(1));
	        	
                notifsList.add(map);
	        } while (cursor.moveToNext());
	    }
	    database.close();
	    return notifsList;
	}

}
