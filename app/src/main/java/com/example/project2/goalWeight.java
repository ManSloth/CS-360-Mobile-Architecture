package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class goalWeight extends AppCompatActivity {

    // Create variables for everything on this page
    private EditText goal;
    private TextView current;
    private TextView toGo;
    private Button back;
    private WeightDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_weight);

        // Set variables to their respective ui elements
        goal = findViewById(R.id.goalEditText);
        current = findViewById(R.id.currentWeight);
        toGo = findViewById(R.id.toGo);
        back = findViewById(R.id.backButton3);
        db = new WeightDatabase(getApplicationContext());

        // function to update goal in database when this text is changed
        goalTextListener();

        // Set the rest of the text on screen
        current.setText("Current: " + db.getLastWeight());
        goal.setText(db.getGoal());
        calculateToGo();
    }

    // Listener to change goal weight in database when goal EditText is changed
    public void goalTextListener() {
        goal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                db.updateGoal(goal.getText().toString());
                calculateToGo();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    // Goes back to weight table screen
    public void Back(View view) {
        Intent intent = new Intent(this, databaseActivity.class);
        startActivity(intent);
    }

    // calculate how many pounds to lose or gain
    public void calculateToGo() {
        String last = db.getLastWeight();
        if (!last.equals("???") && !goal.getText().toString().equals("")) {
            int curInt = Integer.parseInt(last);
            int goalInt = Integer.parseInt(goal.getText().toString());
            int difference = Math.abs(curInt - goalInt);

            toGo.setText(difference + " lbs TO GO!");
        }
        else {
            toGo.setText("?? lbs TO GO!");
        }
    }
}