package com.example.knittingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Project> projects;
    int rowCounter = 0;
    String projectN;
    SharedPreferences knitprefs;
    TextView counter;
    TextView projectName;
    boolean list = true;
    int currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        knitprefs = getSharedPreferences("knitprefs",
                MODE_PRIVATE);


        File file = new File(MainActivity.this.getFilesDir(), "projects");
        Log.e("myTag", file.getAbsolutePath());
            if (!file.exists()) {
                try {
                    Log.e("File Fuckery", "File does not exist");
                    file.createNewFile();
                } catch (Exception e) {Log.e("File Fuckery", "File does not exist");
                }
            } else {Log.e("File Fuckery", "File exists");}

            projects = new ArrayList<Project>();
            try {
                FileReader read = new FileReader(file);
                BufferedReader inBuffer = new BufferedReader(read);
                String line = inBuffer.readLine();
                while(line != null){
                    Log.e("File Fuckery", line);
                    String[] filter = line.split("(;)");
                    Log.e("File Fuckery", filter[1]);
                    if(filter.length == 3){
                        projects.add(new Project(filter[0], filter[1], Integer.parseInt(filter[2])));
                    }
                    line = inBuffer.readLine();
                }
            } catch (Exception e) { Log.e("File Fuckery", e.getMessage());}

            if(projects.size()!=0){
               projectN=projects.get(0).getName();
            } else projectN = "NAME";

        Log.e("File Fuckery", Integer.toString(projects.size()));
         if(list) {
             refreshProjectList();
         }
         else {
             setContentView(R.layout.activity_main);
             refreshProjectPage();

         }
        }


    public void enterProjectName() {
        projectN="";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Input Project Name");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                projectN = input.getText().toString();
                projectName.setText(projectN);
                saveChanges();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


    void refreshProjectList(){
        Button addNew = findViewById(R.id.button6);
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getBaseContext(), AddProject.class);
                startActivity(myIntent);
            }
        });
        LinearLayout lay = findViewById(R.id.list);
        if(projects.size()==0){
            TextView tv=new TextView(getApplicationContext());
            tv.setText("No projects yet :(");
            lay.addView(tv);
        } else {
            for(int r=0; r<projects.size(); r++){
                TextView tv=new TextView(getApplicationContext());
                tv.setText(projects.get(r).getName());
                final int current = r;
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        currentId = current;
                        Log.e("File Fuckery", Integer.toString(currentId));
                        list = false;
                        rowCounter = projects.get(currentId).getCounter();
                        projectN =  projects.get(currentId).getName();

                        setContentView(R.layout.activity_main);
                        refreshProjectPage();

                    }
                });
                lay.addView(tv);
            }
        }

    }

    void refreshProjectPage(){
        rowCounter = projects.get(currentId).getCounter();
        projectN =  projects.get(currentId).getName();
        counter = findViewById(R.id.textView);
        projectName = findViewById(R.id.textView3);

        Button editName = findViewById(R.id.button3);
        editName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                enterProjectName();
            }
        });

        Button backB = findViewById(R.id.button5);
        backB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveChanges();
                list = true;
                setContentView(R.layout.activity_main_list);
                refreshProjectList();
            }
        });





        Button plusB = findViewById(R.id.button);
        Button minusB = findViewById(R.id.button2);


        counter.setText(Integer.toString(rowCounter));
        projectName.setText(projectN);

        plusB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rowCounter++;
                counter.setText(Integer.toString(rowCounter));
            }

        });

        minusB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (rowCounter > 0) {
                    rowCounter--;
                    counter.setText(Integer.toString(rowCounter));
                } else {
                    Toast.makeText(getApplicationContext(), "Can't reduce the counter below 0", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    void saveChanges(){
        projects.get(currentId).setCounter(rowCounter);
        projects.get(currentId).setName(projectN);
        try {
            File file = new File(MainActivity.this.getFilesDir(), "projects");
            FileWriter out = new FileWriter(file, false);
            for (Project p : projects) {
                String add = p.getName() + ";" + p.getDescription() + ";" + Integer.toString(p.getCounter()) + ";\n";
                out.write(add);
                out.close();
            }
        }
        catch (Exception e) {}
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(!list){
        projectN = knitprefs.getString("projectName", "");
        projectName.setText(projectN);
        rowCounter = knitprefs.getInt("rowCounter", 0);
        counter.setText(Integer.toString(rowCounter));}

    }

    protected void onPause() {
        super.onPause();
        saveChanges();
        SharedPreferences.Editor ed = knitprefs.edit();
        ed.putInt("rowCounter", rowCounter);
        ed.putString("projectName", projectN);
        ed.commit();
    }


}
