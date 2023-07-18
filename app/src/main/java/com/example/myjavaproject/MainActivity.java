package com.example.myjavaproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        Button button2=findViewById(R.id.button2);

button2.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intents=new Intent(MainActivity.this, ThirdActivity.class);
        startActivity(intents);
    }
});
    }

    private void setSupportActionBar(Toolbar toolbar) {
    }
}