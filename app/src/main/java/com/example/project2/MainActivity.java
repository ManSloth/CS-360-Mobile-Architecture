package com.example.project2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Variables to be used on login screen
    private EditText userName;
    private EditText password;
    private Button signIn;
    private Button createAccount;
    private WeightDatabase db;
    private TextView warningText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assign variables to objects in the layout
        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        signIn = findViewById(R.id.signInButton);
        createAccount = findViewById(R.id.createAccountButton);
        warningText = findViewById(R.id.warningText);
        db = new WeightDatabase(getApplicationContext());

    }

    // sign in to app with valid login info
    public void SignIn(View view) {
        // don't allow empty strings
        if (userName.getText().toString().equals("")) {
            warningText.setText("Username has to be longer than 0 characters! :(");
        }
        else { // if username contains any characters
            boolean nameExists = db.getUsername(userName.getText().toString());
            if (nameExists) { // check if password matches that username
                boolean passwordMatch = db.checkPassword(userName.getText().toString(), password.getText().toString());
                if (passwordMatch) {
                    warningText.setText("Login successfull! :)");
                    Log.d("Exist", "Login successfull!");

                    // load the table page with all of the weights
                    Intent intent = new Intent(this, databaseActivity.class);
                    startActivity(intent);
                } else { // wrong password
                    warningText.setText("Wrong password! Try again. :(");
                    Log.d("Exist", "Wrong password!");
                }
            } else { // username doesn't exist
                warningText.setText("Username " + userName.getText().toString() + " does not exist. :(");
                Log.d("Exist", "Username " + userName.getText().toString() + " does not exist.");
            }
        }
    }

    // creates account with the input on screen
    public void CreateAccount(View view) {
        // try to create account
        long errorCode = db.addLogin(userName.getText().toString(), password.getText().toString());

        // password has to have at least 1 character
        if (errorCode == -1) {
            warningText.setText("Password has to be longer than 0 characters! :(");
        } // don't allow duplicate usernames
        else if (errorCode == -2) {
            warningText.setText("Username " + userName.getText().toString() + " already exists! :(");
        }// username has to be at least 1 character
        else if (errorCode == -3) {
            warningText.setText("Username has to be longer than 0 characters! :(");
        }
        else { // Account is created
            warningText.setText("Account " + userName.getText().toString() + " created! :)");
        }
    }
}