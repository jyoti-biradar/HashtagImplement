package com.example.hashtagimplementation;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

public class DatabaseHandler {

    SQLiteDatabase sqLiteDatabaseObj;
    String  SQLiteDataBaseQueryHolders;
    private Activity activity;

    public DatabaseHandler(Activity activity){
        this.activity = activity;
        SQLiteDataBaseBuild();
        createTables();
    }

    //Database Creation
    public void SQLiteDataBaseBuild() {
        sqLiteDatabaseObj = activity.openOrCreateDatabase("HashtagDatabase", Context.MODE_PRIVATE, null);
    }
    //Create Three Table HashtagTagTable,MessageTable and TagMessageRelation
    public void createTables() {
        sqLiteDatabaseObj.execSQL("CREATE TABLE IF NOT EXISTS MessageTable(messageId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, content VARCHAR);");
        sqLiteDatabaseObj.execSQL("CREATE TABLE IF NOT EXISTS HashtagTable(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, tagName VARCHAR);");
        sqLiteDatabaseObj.execSQL("CREATE TABLE IF NOT EXISTS TagMessageRelationTable(messageId INTEGER,tagId INTEGER);");
    }
    //inserting data in message table
    public int insertDataIntoMessageTable(String message) {
        if (message.length()>0) {
            ContentValues cv = new  ContentValues();
            cv.put("content",    message);
            long id = sqLiteDatabaseObj.insert("MessageTable",null,cv);
            Toast.makeText(activity, "Data Inserted in Message Table Successfully", Toast.LENGTH_LONG).show();
            return (int) id;
        }
        return 0;
    }
    //Inserting Data in hashtagTag Table
    public int insertDataIntoTagTable(String tagname) {
        if (tagname.length()>0) {
            SQLiteDataBaseQueryHolders="SELECT * FROM HashtagTable where tagName = '"+tagname+"'";
            Cursor s = sqLiteDatabaseObj.rawQuery(SQLiteDataBaseQueryHolders,null);
            //checking hashtag already exist in table or not.
            if (s!= null && s.moveToFirst()){
                 int id = s.getInt(0);
                Toast.makeText(activity, "This Hashtag Already Exist in table"+id, Toast.LENGTH_LONG).show();
                return id;
            }
            else {
                ContentValues cv = new  ContentValues();
                cv.put("tagName", tagname);
                long id = sqLiteDatabaseObj.insert("HashtagTable",null,cv);
                return (int) id;
            }
        }
        return 0;
    }

    public void insertDataIntoTagMessageRelationTable(int messageId, int tagId){
        if (messageId!=0 && tagId!=0) {
            ContentValues cv = new  ContentValues();
            cv.put("messageId",    messageId);
            cv.put("tagId",    tagId);
            sqLiteDatabaseObj.insert("TagMessageRelationTable",null,cv);
            Toast.makeText(activity, "Data Inserted in TagMessageRelation Table Successfully", Toast.LENGTH_LONG).show();
        }

    }

    public int getTagId(String tagname) {
        SQLiteDataBaseQueryHolders="SELECT * FROM HashtagTable where tagName = '"+tagname+"'";
        Cursor s = sqLiteDatabaseObj.rawQuery(SQLiteDataBaseQueryHolders,null);
        if (s!= null && s.moveToFirst()){
            int id = s.getInt(0);
            return id;
        }
        return 0;
    }

    public ArrayList<String> fetchMessages(int tagId) {
        ArrayList<String> messageList = new ArrayList<>();
        SQLiteDataBaseQueryHolders="SELECT content FROM MessageTable where messageId IN (select messageId " +
                "from TagMessageRelationTable where tagId="+tagId+")";
        //select This is #testing #hashtag message. from message table where
        Cursor s = sqLiteDatabaseObj.rawQuery(SQLiteDataBaseQueryHolders,null);
        if (s!= null){
            while (s.moveToNext()){
                String msg = s.getString(0);
                messageList.add(msg);
            }
        }
        return messageList;
    }
}
