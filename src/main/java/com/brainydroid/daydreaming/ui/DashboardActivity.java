package com.brainydroid.daydreaming.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.brainydroid.daydreaming.R;
import com.brainydroid.daydreaming.background.SchedulerService;
import com.brainydroid.daydreaming.background.StatusManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardActivity extends SherlockActivity {

	private static String TAG = "DashboardActivity";

	public static String EXTRA_COMES_FROM_FIRST_LAUNCH = "comesFromFirstLaunch";

	private StatusManager status;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		// Debug
		if (Config.LOGD) {
			Log.d(TAG, "[fn] onCreate");
		}

		super.onCreate(savedInstanceState);

		status = StatusManager.getInstance(this);

		setContentView(R.layout.activity_dashboard);

        // -------------------------------
        // Transform string to Date object
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");//("MM/dd/yyyy");
        // Get today just in case
        String todayDateString = format.format(new Date());
        // Read Start date string from preferences
        SharedPreferences sharedPrefs = getSharedPreferences("startdatepref", 0);
        String StartDateString = sharedPrefs.getString("startdatestring", todayDateString);

        Date StartDate;
        try {
            StartDate = format.parse(StartDateString);

        // Compute time difference from Date objects:
        // getTime : number of milliseconds since 1 January 1970 00:00:00 UTC
        long dt =  (new Date()).getTime() - StartDate.getTime() ;
        dt /= 1000;
        dt /= 60;
        dt /= 60;
        int hours = (int)(dt % 24);
        dt /= 24;
        int days = (int)(dt);
        String ElapsedTime =  Integer.toString(days) + " days " + Integer.toString(hours) + " hours ";
        TextView textView = (TextView)findViewById(R.id.dashboard_textDaysElapsedNumber);
        textView.setText(ElapsedTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        // -------------------------------

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Debug
		if (Config.LOGD) {
			Log.d(TAG, "[fn] onCreateOptionsMenu");
		}

		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.dashboard, menu);

		// Calling super after populating the menu is necessary here to ensure that the
		// action bar helpers have a chance to handle this event.
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onStart() {

		// Debug
		if (Config.LOGD) {
			Log.d(TAG, "[fn] onStart");
		}

		checkFirstRun();
		super.onStart();
	}

	@Override
	public void onResume() {

		// Debug
		if (Config.LOGD) {
			Log.d(TAG, "[fn] onResume");
		}

		super.onResume();
	}

	@Override
	public void onStop() {

		// Debug
		if (Config.LOGD) {
			Log.d(TAG, "[fn] onStop");
		}

		super.onStop();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Debug
		if (Config.LOGD) {
			Log.d(TAG, "[fn] onOptionsItemSelected");
		}

		switch (item.getItemId()) {
		case android.R.id.home:
			break;

		case R.id.menu_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void checkFirstRun() {

		// Debug
		if (Config.LOGD) {
			Log.d(TAG, "[fn] checkFirstRun");
		}

		if (!status.isFirstLaunchCompleted()) {
			Intent intent;
			if (!status.isFirstLaunchStarted()) {
				intent = new Intent(this, FirstLaunchWelcomeActivity.class);
			} else {
				intent = new Intent(this, ReLaunchWelcomeActivity.class);
			}

			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			startActivity(intent);
			finish();
		}
	}

	public void runPollNow(View view) {

		// Debug
		if (Config.LOGD) {
			Log.d(TAG, "[fn] runPollNow");
		}

		Intent pollIntent = new Intent(this, SchedulerService.class);
		pollIntent.putExtra(SchedulerService.SCHEDULER_DEBUGGING, true);
		startService(pollIntent);

		Toast.makeText(this, "Now wait for 5 secs", Toast.LENGTH_SHORT).show();
	}
}