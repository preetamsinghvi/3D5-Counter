package com.example.counter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AddCounter extends Activity {

    Button btn_close;
    EditText counterName;
    EditText counterDesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_counter);

        Button addCounter = findViewById(R.id.counterAdd);
        addCounter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {addNewProject();}
                catch (Exception e){}
            }
        });
        btn_close = findViewById(R.id.btnClose);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8), (int)(height*.7));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 20;
        getWindow().setAttributes(params);
    }

    void addNewProject() throws IOException {
        counterName = findViewById(R.id.counterName);
        counterDesc = findViewById(R.id.counterDesc);
        String nName = counterName.getText().toString();
        String nDesc = counterDesc.getText().toString();
        String add = nName + ";" + nDesc + ";" + Integer.toString(0) + ";\n";
        File file = new File(AddCounter.this.getFilesDir(), "projects");
        FileWriter out = new FileWriter(file, true);
        out.write(add);
        out.close();
        Intent i= new Intent (getBaseContext(),MainActivity.class);
        startActivity(i);
    }
}
