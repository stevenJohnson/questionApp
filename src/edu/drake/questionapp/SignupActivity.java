package edu.drake.questionapp;

import utilities.ThisApplication;
import database.dbmethods;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends Activity {

	private EditText userName;
	private EditText firstName;
	private EditText pass1;
	private EditText pass2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);

		userName = (EditText) findViewById(R.id.signupemail);
		firstName = (EditText) findViewById(R.id.signupfirstname);
		pass1 = (EditText) findViewById(R.id.signuppass1);
		pass2 = (EditText) findViewById(R.id.signuppass2);


		findViewById(R.id.signup_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if(userName.getText().toString().length() > 0 && firstName.getText().toString().length() > 0 &&
								pass1.getText().toString().length() > 0 && pass2.getText().toString().length() > 0)
						{
							if(pass1.getText().toString().length() > 3)
							{
								if(userName.getText().toString().contains("@"))
								{
									if(pass1.getText().toString().equals(pass2.getText().toString()))
									{
										attemptSignup();
									}
									else
									{
										// password and confirm pass don't match..
										Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_LONG).show();
									}
								}
								else
								{
									// no @ sign in email
									Toast.makeText(getApplicationContext(), "Invalid email address.", Toast.LENGTH_LONG).show();
								}
							}
							else
							{
								// da password ain't long nuff
								Toast.makeText(getApplicationContext(), "Password must be at least 4 characters long.", Toast.LENGTH_LONG).show();
							}
						}
						else
						{
							// missing a field
							Toast.makeText(getApplicationContext(), "One or more required fields are missing!", Toast.LENGTH_LONG).show();
						}
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.signup, menu);
		return true;
	}

	private void attemptSignup()
	{
		UserSignupTask t = new UserSignupTask();
		t.execute();
	}

	public class UserSignupTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params)
		{
			return dbmethods.signupPls(userName.getText().toString(), firstName.getText().toString(), pass1.getText().toString(), getApplicationContext());
		}

		@Override
		protected void onPostExecute(final Boolean success)
		{
			if (success)
			{
				// successful signup... so tell user and finish

				// TODO ::: create a signup confirmed dialog fragment and display here...
				new AlertDialog.Builder(SignupActivity.this)
				.setTitle("Account Created")
				.setMessage("Congratulations! Your account has been created.")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setPositiveButton("Return to login screen", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton)
					{
						finish();
					}}).show();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Invalid signup. An account with that email already exists.", Toast.LENGTH_LONG).show();
			}
		}
	}
}
