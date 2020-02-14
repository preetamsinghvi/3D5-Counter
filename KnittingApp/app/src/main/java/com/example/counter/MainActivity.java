package com.example.counter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button addbutton;
    ArrayList<Project> projects;
    int rowCounter = 0;
    String projectN;
    String projectD;
    SharedPreferences counterpref;
    TextView counter;
    TextView counterName;
    TextView counterDesc;
    boolean list = true;
    int currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addbutton = findViewById(R.id.addcounterbutton);

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddCounter.class);
                startActivity(i);

            }
        });

        counterpref = getSharedPreferences("counterpref",
                MODE_PRIVATE);

        File file = new File(MainActivity.this.getFilesDir(), "projects");
        Log.e("myTag", file.getAbsolutePath());
        if (!file.exists()) {
            try {
                Log.e("File Error", "File does not exist");
                file.createNewFile();
            } catch (Exception e) {Log.e("File Error", "File does not exist");
            }
        } else {Log.e("File Error", "File exists");}

        projects = new ArrayList<Project>();
        try {
            FileReader read = new FileReader(file);
            BufferedReader inBuffer = new BufferedReader(read);
            String line = inBuffer.readLine();
            while(line != null){
                Log.e("File Log", line);
                String[] filter = line.split("(;)");
                Log.e("File Log", filter[1]);
                if(filter.length == 3){
                    projects.add(new Project(filter[0], filter[1], Integer.parseInt(filter[2])));
                }
                line = inBuffer.readLine();
            }
        } catch (Exception e) { Log.e("File Log", e.getMessage());}

        if(projects.size()!=0){
            projectN=projects.get(0).getName();

        } else projectN = "NAME";

        Log.e("File Log", Integer.toString(projects.size()));
        if(list) {
            refreshProjectList();
        }
        else {
            setContentView(R.layout.counter_details);
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
                counterName.setText(projectN);
                projects.get(currentId).setName(projectN);
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
    public void enterProjectDesc() {
        projectD="";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Input Project Description");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                projectD = input.getText().toString();
                counterDesc.setText(projectD);
                projects.get(currentId).setDescription(projectD);
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
        Button addNew = findViewById(R.id.addcounterbutton);
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getBaseContext(), AddCounter.class);
                startActivity(myIntent);
            }
        });
        LinearLayout lay = findViewById(R.id.list);
        if(projects.size()==0){
            TextView tv=new TextView(getApplicationContext());
            tv.setText("No Counters Available");
            lay.addView(tv);
            
        } else {
            for(int r=0; r<projects.size(); r++){
                TextView tv=new TextView(getApplicationContext());
                String m = projects.get(r).getName();
                String n = "--> ";
                String s = n.concat(m);
                int len = s.length();
                SpannableString ss1=  new SpannableString(s);
                ss1.setSpan(new RelativeSizeSpan(1.5f), 0,len, 0);
                ss1.setSpan(new ForegroundColorSpan(Color.BLACK), 0, len, 0);
                ss1.setSpan(new QuoteSpan(), 0, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv.setText(ss1);
                final int current = r;
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        currentId = current;
                        Log.e("File Log", Integer.toString(currentId));
                        list = false;
                        rowCounter = projects.get(currentId).getCounter();
                        projectN =  projects.get(currentId).getName();
                        projectD = projects.get(currentId).getDescription();

                        setContentView(R.layout.counter_details);
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
        projectD = projects.get(currentId).getDescription();
        counter = findViewById(R.id.textView);
        counterName = findViewById(R.id.textView3);
        counterDesc = findViewById(R.id.descr);

        Button editName = findViewById(R.id.button3);
        editName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                enterProjectName();
            }
        });

        Button editDesc = findViewById(R.id.button7);
        editDesc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                enterProjectDesc();
            }
        });

        Button backB = findViewById(R.id.button5);
        backB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveChanges();
                list = true;
                setContentView(R.layout.activity_main);
                refreshProjectList();
            }
        });

        Button plusB = findViewById(R.id.button);
        Button minusB = findViewById(R.id.button2);

        counter.setText(Integer.toString(rowCounter));
        counterName.setText(projectN);
        counterDesc.setText(projectD);

        plusB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rowCounter++;
                projects.get(currentId).setCounter(rowCounter);
                counter.setText(Integer.toString(rowCounter));
                saveChanges();
            }

        });

        minusB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (rowCounter > 0) {
                    rowCounter--;
                    projects.get(currentId).setCounter(rowCounter);
                    counter.setText(Integer.toString(rowCounter));
                    saveChanges();
                } else {
                    Toast.makeText(getApplicationContext(), "Can't reduce the counter below 0", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    void saveChanges(){
        Log.e("Save File", "Got called");
        try {
            File file = new File(MainActivity.this.getFilesDir(), "projects");
            FileWriter out = new FileWriter(file, false);
            int coun=0;
            for (Project p : projects) {
                coun++;
                String add = p.getName() + ";" + p.getDescription() + ";" + Integer.toString(p.getCounter()) + ";\n";
                out.write(add);
            }
            out.close();
            Log.e("Saved Items:", Integer.toString(coun));
        }
        catch (Exception e) {}
    }



    }
