package edu.drake.questionapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ChangeEmail extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_email);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.change_email, menu);
		return true;
	}

}
