package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;


public class databaseActivity extends AppCompatActivity {

    // Create variables for everything on this page
    private ImageButton addButton;
    private ImageButton goalButton;
    private ImageButton settingsButton;
    private WeightDatabase db;
    private TableLayout scrollTable;

    private EditText date;
    private TextView numPounds;
    private int i;
    private ArrayList <Integer> prevDateLenList = new ArrayList<Integer>();
    private ArrayList <String> idList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        // Set variables to their respective ui elements
        addButton = findViewById(R.id.addButton);
        goalButton = findViewById(R.id.goalButton);
        settingsButton = findViewById(R.id.settingsButton);
        date = findViewById(R.id.editTextDate);
        db = new WeightDatabase(getApplicationContext());
        scrollTable = findViewById(R.id.scrollTable);
        numPounds = findViewById(R.id.numberOfPounds);

        // calculate how many pounds need to be gained or lost
        calculateToGo();
        // Create the table of weights and dates from database
        refreshTable();

    }

    // Loads the input weight screen
    public void AddWeight(View view) {
        Intent intent = new Intent(this, inputWeight.class);
        startActivity(intent);
    }

    // Loads the Settings page screen
    public void Settings(View view) {
        Intent intent = new Intent(this, settings.class);
        startActivity(intent);
    }

    // Loads the goal weight input screen
    public void Goal(View view) {
        Intent intent = new Intent(this, goalWeight.class);
        startActivity(intent);
    }

    // deletes a weight from the database
    public void DeleteRow(String index) {
        db.deleteRow(index);
    }

    // Creates the table that is displayed on screen from the database
    private void refreshTable() {
        // Create scrollable table from current entries of weights in the database table, WeightTable.TABLE
        String cursorRow = "";
        i = 0;
        // Loop through the weight table to grab all weights saved to the database
        while (!cursorRow.equals("Invalid")) {
            cursorRow = db.getWeight(i);
            if (!cursorRow.equals("Invalid")) {
                idList.add(db.getID(i));

                // create a row to add ui elements
                TableRow row = new TableRow(this);
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
                row.setLayoutParams(lp);

                // create editable weight entry text
                EditText et = new EditText(this);
                et.setInputType(InputType.TYPE_CLASS_NUMBER);
                et.setHint("Weight amount");
                et.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                et.setText(cursorRow);

                et.addTextChangedListener(new TextWatcher() {
                    int index = i;

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // update weight in database when number is changed
                        db.saveWeight(idList.get(index), et.getText().toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                // created lsb text after weight
                TextView tv = new TextView(this);
                tv.setText("lbs");

                cursorRow = db.getDate(i);

                // created editable date text
                EditText etd = new EditText(this);
                etd.setInputType(InputType.TYPE_CLASS_NUMBER);
                etd.setHint("MM/DD/YYYY");
                etd.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                int maxLength = 10;
                etd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

                etd.setText(cursorRow);

                // keep track of date value on previous frame for formatting "/' in between month, day, and year
                prevDateLenList.add(0);

                etd.addTextChangedListener(new TextWatcher() {
                    int index = i;

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // if numbers are added
                        if (etd.length() > prevDateLenList.get(index)) {
                            if (etd.length() == 2 || etd.length() == 5) {
                                etd.setText((etd.getText() + "/"));
                                etd.setSelection(etd.length());
                                prevDateLenList.set(index, etd.length());
                            }
                        } // else if numbers are deleted
                        else {
                            if (etd.length() == 2 || etd.length() == 5) {
                                String text = etd.getText().toString();
                                etd.setText(text.substring(0, text.length() - 1));
                                etd.setSelection(etd.length());
                                prevDateLenList.set(index, etd.length());
                            }
                        }
                        db.saveDate(idList.get(index), etd.getText().toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                // create 'x' button to delete weights from database.
                // Was making app crash because each delete was messing up the indexes so,
                // it currently reloads the whole page to avoid that crash for now
                ImageButton ib = new ImageButton(this);
                ib.setImageResource(R.drawable.delete_icon32);

                ib.setOnClickListener(new View.OnClickListener() {
                    int index = i;

                    @Override
                    public void onClick(View view) {

                        DeleteRow(idList.get(index));

                        // reload page when row is deleted to avoid crashing
                        finish();
                        startActivity(getIntent());
                    }
                });

                // add all ui elements to the current row
                row.addView(et);
                row.addView(tv);
                row.addView(etd);
                row.addView(ib);

                // add the row to the table
                scrollTable.addView(row, 1 + i);

                // check to see if we've reached the end of the table
                i += 1;
                cursorRow = db.getWeight(i);
            }
        }
    }

    // calculates how many pounds to lose or gain
    public void calculateToGo() {
        String last = db.getLastWeight();
        String goal = db.getGoal();
        if (!last.equals("???") && !goal.equals("")) {
            int curInt = Integer.parseInt(last);
            int goalInt = Integer.parseInt(goal);
            int difference = Math.abs(curInt - goalInt);

            numPounds.setText(String.valueOf(difference));
        }
        else {
            numPounds.setText("??");
        }
    }
}