package com.example.mp3_player;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    Button btn1, btn2;
    TextView textView;
    ProgressBar pgMP3;

    ArrayList<String> mp3List;
    String selectedMP3;

    String path =  "/sdcard/Music/";
    MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("간단 MP3 플레이어");

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MODE_PRIVATE);

        mp3List = new ArrayList<String>();

        File[] listFiles = new File("/sdcard/Music/").listFiles();
        String fileName, extName;

        for(int i=0; i<listFiles.length; i++){
            fileName = listFiles[i].getName();
            extName = fileName.substring(fileName.length() - 3);
            if(extName.equals((String)"mp3")){
                mp3List.add(fileName);
            }
        }

        listView = (ListView)findViewById(R.id.listView);
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        textView = (TextView)findViewById(R.id.textView);
        pgMP3 = (ProgressBar)findViewById(R.id.pbMP3);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, mp3List);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);
        listView.setItemChecked(0, true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedMP3 = mp3List.get(position);
            }
        });

        selectedMP3 = mp3List.get(0);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    mPlayer = new MediaPlayer();
                    mPlayer.setDataSource(path + selectedMP3);
                    mPlayer.prepare();
                    mPlayer.start();
                    btn1.setClickable(false);
                    btn2.setClickable(true);
                    textView.setText("실행중인 음악 : " + selectedMP3);
                    pgMP3.setVisibility(View.VISIBLE);
                } catch(IOException e){

                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.stop();
                mPlayer.reset();
                btn1.setClickable(true);
                btn2.setClickable(false);
                textView.setText("실행중인 음악 : ");
                pgMP3.setVisibility(View.INVISIBLE);
            }
        });
        btn2.setClickable(false);

    }
}