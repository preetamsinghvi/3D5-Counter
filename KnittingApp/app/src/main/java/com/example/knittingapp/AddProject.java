package com.example.knittingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class AddProject extends AppCompatActivity {

    EditText inName;
    EditText inDesc;
    Project newP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        Button addP = findViewById(R.id.button4);
        addP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {addNewProject();}
                catch (Exception e){}
            }
        });
    }

    void addNewProject() throws IOException {
        inName = findViewById(R.id.nameEdit);
        inDesc = findViewById(R.id.descEdit);
        String nName = inName.getText().toString();
        String nDesc = inDesc.getText().toString();
        String add = nName + ";" + nDesc + ";" + Integer.toString(0) + ";\n";
        File file = new File(AddProject.this.getFilesDir(), "projects");
        FileWriter out = new FileWriter(file, true);
        out.write(add);
        out.close();
        Intent i= new Intent (getBaseContext(),MainActivity.class);
        startActivity(i);
    }
}
