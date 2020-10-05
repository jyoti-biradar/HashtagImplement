package com.example.hashtagimplementation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DatabaseHandler databaseHandler;
    recycleViewAdapter recycleViewAdapter;
    RecyclerView recyclerView;
    ArrayList<String> messageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHandler = new DatabaseHandler(this);
        final TextView tvLblSaveTag=(TextView) findViewById(R.id.tvlblSaveTag);
        final EditText etSearch = (EditText) findViewById(R.id.etSearch);
        Button btnSearch=findViewById(R.id.btnSearch);
        recyclerView=findViewById(R.id.recycleView);
        messageList = new ArrayList<>();
        recycleViewAdapter=new recycleViewAdapter(messageList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recycleViewAdapter);
        tvLblSaveTag.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(v.getContext(), SaveTagAndMessageDatabaseActivity.class);
                        startActivity(intent);
                    }

                }
                );
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageData = etSearch.getText().toString();
                if (messageData.length() == 0) {
                    etSearch.requestFocus();
                    etSearch.setError("This Field id Required");
                }
                messageList.clear();
                recycleViewAdapter.setItems(messageList);
                recycleViewAdapter.notifyDataSetChanged();
                String tag = getTag(messageData);
                if(tag.length()>0){
                    filterData(tag);
                }else {
                    Toast.makeText(MainActivity.this,"no data found",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private String  getTag(String text){
        String tag = "";
        ArrayList<String> list = new ArrayList<String>();
        String[] splitData = text.split("#");
        for(int i=0; i<splitData.length; i++){
            if(i!=0){
                String tempTag = splitData[i].split(" ")[0];
                String newTag = tempTag.replaceAll("[^A-Za-z]+", "#").split("#")[0];
                tag = newTag.toLowerCase();
                break;
            }
        }
        return tag;
    }
    public void filterData(String hashTag){
        int tagId = databaseHandler.getTagId(hashTag);
        if(tagId!=0){
            messageList = databaseHandler.fetchMessages(tagId);
            recycleViewAdapter.setItems(messageList);
            recycleViewAdapter.notifyDataSetChanged();
            if(messageList.size()==0){
                Toast.makeText(MainActivity.this,"no data found",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(MainActivity.this,"no data found",Toast.LENGTH_SHORT).show();
        }



    }
}
