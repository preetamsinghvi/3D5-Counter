package com.example.knittingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Project> projects;
    int rowCounter = 0;
    String projectN;
    SharedPreferences knitprefs;
    TextView counter;
    TextView projectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        knitprefs = getSharedPreferences("knitprefs",
                MODE_PRIVATE);

        File file = new File(MainActivity.this.getFilesDir(), "text");
        if (!file.exists()) {
            file.mkdir();
        }

        projects = new ArrayList<Project>();


        counter = findViewById(R.id.textView);
        projectName = findViewById(R.id.textView3);
        Button editName = findViewById(R.id.button3);
        editName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                enterProjectName();
            }
        });


        Button plusB = findViewById(R.id.button);
        Button minusB = findViewById(R.id.button2);


        counter.setText(Integer.toString(rowCounter));

        plusB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rowCounter++;
                counter.setText(Integer.toString(rowCounter));
            }
        });

        minusB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(rowCounter>0){rowCounter--;
                counter.setText(Integer.toString(rowCounter));}
                else {
                    Toast.makeText(getApplicationContext(), "Can't reduce the counter below 0", 1).show();}
            }
        });


    }

    //TODO: Exception Handling
    public void addNewProject(String pName, String pDesc){
        Project nProject = new Project(pName, pDesc);
        projects.add(nProject);
    }

    public void deleteProject(Project dProj){
        projects.remove(dProj);
    }

    //Writes Data for projects into projects.txt file in folder text in csv format
    //Creates File if it not already exists
    public void writeProjectsInFile(){
        File file = new File(MainActivity.this.getFilesDir(), "text");
        try {
            File gpxfile = new File(file, "projects");
            FileWriter writer = new FileWriter(gpxfile);
            for(Project p : projects){
                writer.append("\"");
                writer.append(p.getName());
                writer.append("\",\"");
                writer.append(p.getDescription());
                writer.append("\",\"");
                writer.append(Integer.toString(p.getCounter()));
                writer.append("\"\n");}
            writer.flush();
            writer.close();
        } catch (Exception e) { }
    }

    //TODO: This is a base version, could use some more work (e.g. check for Numberformatexception
    //when parsing int, also still needs testing
    //Reads data for all projects from projects.txt into the projects array when called
    public void readProjectsFromFile() throws IOException{
        String filePath = "/data/data/com.example.knittingapp/files/text/projects";
        File tempFile = new File(filePath);
        if (! tempFile.exists () || !tempFile.canRead() ||!tempFile.canExecute ())
            { throw new IOException("Nein");}
        FileReader reader = new FileReader(filePath);
        BufferedReader inBuffer = new BufferedReader(reader);
        projects = new ArrayList<Project>();
        String line = inBuffer.readLine();
        while(line != null){
            String[] filter = line.split ("(,)");
            if(filter.length == 3){
               Project pTemp = new Project(filter[0], filter[1], Integer.parseInt(filter[2]));
               projects.add(pTemp);
            }

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


    @Override
    protected void onResume()
    {
        super.onResume();

        projectN = knitprefs.getString("projectName", "");
        projectName.setText(projectN);
        rowCounter = knitprefs.getInt("rowCounter", 0);
        counter.setText(Integer.toString(rowCounter));

    }

    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor ed = knitprefs.edit();
        ed.putInt("rowCounter", rowCounter);
        ed.putString("projectName", projectN);
        ed.commit();
        writeProjectsInFile();
    }


}
