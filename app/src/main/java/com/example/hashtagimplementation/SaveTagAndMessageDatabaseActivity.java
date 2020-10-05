package com.example.hashtagimplementation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class SaveTagAndMessageDatabaseActivity extends AppCompatActivity {

    DatabaseHandler databaseHandler;
    EditText etAddMessageANdTag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_tag_and_message_database);
        etAddMessageANdTag = findViewById(R.id.etAddMessageANdTag);
        Button btnSaveData = findViewById(R.id.btnAddData);
        databaseHandler = new DatabaseHandler(this);

        btnSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageData = etAddMessageANdTag.getText().toString();
                //validate Edittext
                if (messageData.length() == 0) {
                    etAddMessageANdTag.requestFocus();
                    etAddMessageANdTag.setError("This Field id Required");
                }

                ArrayList<String> tagList = extractTags(messageData);
                ArrayList<Integer> tagIdList = getTagIds(tagList);
                int messageID = databaseHandler.insertDataIntoMessageTable(messageData);
                Log.d("testidmessage", String.valueOf(messageID));
                if(messageID!=0){
                    insetTagMessageRelation(tagIdList, messageID);
                }
                emptyEditTextAfterDataInsert();
            }
        });
    }
    //Extract tag using String Spit Method
    private ArrayList<String> extractTags(String message){
        ArrayList<String> list = new ArrayList<String>();
        String[] splitData = message.split("#");
        for(int i=0; i<splitData.length; i++){
            Log.d("testingtag 1",splitData[0].split(" ")[0]);
            if(i!=0){
                String tag = splitData[i].split(" ")[0];
                //remove Special Character in Hashtag eg-> . , : ; ' etc
                String newTag = tag.replaceAll("[^A-Za-z]+", "#").split("#")[0];
                if(newTag.length()>0){
                    list.add(newTag.toLowerCase());
                }
            }
        }
        return list;
    }
    //
    private ArrayList<Integer> getTagIds(ArrayList<String> tagList){
        ArrayList<Integer> tagIds = new ArrayList<>();
        for (String tag:tagList) {
            int id = databaseHandler.insertDataIntoTagTable(tag);
            Log.d("testidtag", String.valueOf(id));
            if(id!=0){
                tagIds.add(id);
            }
        }
        return  tagIds;
    }
    //Relation between Tag and Message table and inserting tagID and MessageId
    private void insetTagMessageRelation(ArrayList<Integer> tagIds,int messageId){
        for (int tag:tagIds) {
            databaseHandler.insertDataIntoTagMessageRelationTable(messageId,tag);
        }
    }

    //set Empty Edittext After Inserting Data in Table.
    public void emptyEditTextAfterDataInsert() {
        etAddMessageANdTag.getText().clear();
    }
}
