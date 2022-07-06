package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class inputWeight extends AppCompatActivity {

    // Create variables for everything on this page
    private Button backButton;
    private EditText weightAmount;
    private WeightDatabase db;
    private TextView warningText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_weight);

        // Set variables to their respective ui elements
        backButton = findViewById(R.id.backButton);
        weightAmount = findViewById(R.id.weightAmount);
        db = new WeightDatabase(getApplicationContext());
        warningText = findViewById(R.id.warningText2);
    }

    // Goes back to weight table screen
    public void Back(View view) {
        Intent intent = new Intent(this, databaseActivity.class);
        startActivity(intent);
    }

    // add weight to the database with today's date
    public void AddWeight(View view) {

        long errorCode;
        if (!weightAmount.getText().toString().equals(""))
            errorCode = db.addWeight(Integer.valueOf(weightAmount.getText().toString()));
        else {
            errorCode = -2;
        }
        if (errorCode == -1) {
            //warningText.setText("Weight already entered today! Input another tomorrow!");
            warningText.setText("Weight " + weightAmount.getText().toString() + " entered successfully!");
        }
        else if (errorCode == -2){
            warningText.setText("No weight has been entered!");
        }
        else {
            warningText.setText("Weight entered successfully!");
        }
    }
}