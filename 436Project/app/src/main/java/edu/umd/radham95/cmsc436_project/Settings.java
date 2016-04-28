package edu.umd.radham95.cmsc436_project;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

public class Settings extends Activity {

	
	private static final String RESTART_KEY = "restart";
	private static final String RESUME_KEY = "resume";
	private static final String START_KEY = "start";
	private static final String CREATE_KEY = "create";

	// String for LogCat documentation
	private final static String TAG = "Lab-ActivityOne";



	SeekBar mGender;
	EditText mWeight ;
	EditText mLoss;
	EditText mHeight;

	EditText mAge;
	TextView total_cal;
	String gen, wei, los, hei, act, ag;
SeekBar sb;
	int sb_val = 0;
	int sb2_val = 0;
	double cal_cons = 0;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		sb = (SeekBar) findViewById(R.id.seekBar);
		mGender = (SeekBar) findViewById(R.id.seekBar2);
		mWeight = (EditText) findViewById(R.id.editText4);
		mLoss = (EditText) findViewById(R.id.editText5);
		mHeight = (EditText) findViewById(R.id.editText6);

		mAge = (EditText) findViewById(R.id.editText8);
		total_cal = (TextView) findViewById(R.id.textView3);



		sb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				sb_val = progress;
			}
		});

		mGender.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				sb2_val = progress;
			}
		});


		final Button launchActivityTwoButton = (Button) findViewById(R.id.bLaunchActivityTwo);
		launchActivityTwoButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				wei = mWeight.getText().toString();
				los = mLoss.getText().toString();
				hei = mHeight.getText().toString();

				ag = mAge.getText().toString();


				if (sb2_val == 1) {
					Double wei_int = Double.parseDouble(wei);
					cal_cons = 655 + (4.35 * (wei_int));
					cal_cons = cal_cons + (4.7 * Double.parseDouble(hei));
					cal_cons = cal_cons - (4.7 * Double.parseDouble(ag));
					cal_cons = cal_cons * ((((float) sb_val) / 100.0) + 1);
					cal_cons = cal_cons - 500 * (Double.parseDouble(los));


				} else {
					Double wei_int = Double.parseDouble(wei);
					cal_cons = 66 + (6.23 * (wei_int));
					cal_cons = cal_cons + (12.7 * Double.parseDouble(hei));
					cal_cons = cal_cons - (6.8 * Double.parseDouble(ag));
					cal_cons = cal_cons * ((((float) sb_val) / 100.0) + 1);
					cal_cons = cal_cons - 500 * (Double.parseDouble(los));
				}

				String disp = Integer.toString((int) cal_cons);
				total_cal.setText(disp);

				Toast t = Toast.makeText(getApplicationContext(), "Calories Allowed Per Day: " + disp, Toast.LENGTH_LONG);
t.show();


				Intent dat = new Intent();
				dat.putExtra("calsPerDay", cal_cons);
				setResult(RESULT_OK, dat);
				finish();




			}
		});


		// Emit LogCat message using the Log.i method
		displayCounts();


/*
		final Button backButton = (Button) findViewById(R.id.button);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent dat = new Intent();
				dat.putExtra("calsPerDay", cal_cons);
				setResult(1, dat);
				finish();

			}
		});
*/
	}



	@Override
	public void onStart() {
		super.onStart();

		displayCounts();
		// Emit LogCat message using the Log.i method
	}

	@Override
	public void onResume() {
        super.onResume();

		displayCounts();
		// Emit LogCat message using the Log.i method
	}

	@Override
	public void onPause() {
        super.onPause();
		// Emit LogCat message using the Log.i method
	}

	@Override
	public void onStop() {
        super.onStop();
		// Emit LogCat message using the Log.i method
	}

	@Override
	public void onRestart() {
        super.onRestart();

		displayCounts();
		// Emit LogCat message using the Log.i method

	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		// Emit LogCat message using the Log.i method
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);



	}


	public void displayCounts() {

	}
}
