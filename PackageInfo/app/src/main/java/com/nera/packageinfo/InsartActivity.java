package com.nera.packageinfo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Toast;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class InsartActivity extends AppCompatActivity {

	private AutoCompleteTextView ispNameAct, packageNameAct, packagePriceAct, validDaysAct, anyNetCallsAct,
			thisNetCallsAct, anyNetSmsAct, thisNetSmsAct, availableAppDataLimitAct, paymentTypeAct, anyTimeDataAct,
			dayTimeDataAct, nightTimeDataAct;

	private MultiAutoCompleteTextView availableAppMact, descriptionMact;

	private Button clearAllBtn, addPackageBtn;
	
	DatabaseHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insart);
		
		dbHelper = new DatabaseHelper(this);

		ispNameAct = findViewById(R.id.ispName_act);
		packageNameAct = findViewById(R.id.packageName_act);
		packagePriceAct = findViewById(R.id.packagePrice_act);
		validDaysAct = findViewById(R.id.validDays_act);
		anyNetCallsAct = findViewById(R.id.anyNetCalls_act);
		thisNetCallsAct = findViewById(R.id.thisNetCalls_act);
		anyNetSmsAct = findViewById(R.id.anyNetSms_act);
		thisNetSmsAct = findViewById(R.id.thisNetSms_act);
		availableAppDataLimitAct = findViewById(R.id.availableAppDataLimit_act);
		availableAppMact = findViewById(R.id.availableApp_mact);
		paymentTypeAct = findViewById(R.id.paymentType_act);
		anyTimeDataAct = findViewById(R.id.anyTimeData_act);
		dayTimeDataAct = findViewById(R.id.dayTimeData_act);
		nightTimeDataAct = findViewById(R.id.nightTimeData_act);
		descriptionMact = findViewById(R.id.description_mact);

		clearAllBtn = findViewById(R.id.clearAll_btn);
		addPackageBtn = findViewById(R.id.addPackage_btn);

		AutoCompleteTextAdapters textAdapters = new AutoCompleteTextAdapters(this);
		NewlineTokenizer newlineTokenizer = new NewlineTokenizer();

		int layoutResource = android.R.layout.simple_dropdown_item_1line;
		ispNameAct.setAdapter(textAdapters.ispNameAdapter(layoutResource, null));
		packageNameAct.setAdapter(textAdapters.packageNameAdapter(layoutResource, null));
		packagePriceAct.setAdapter(textAdapters.packagePriceAdapter(layoutResource, null));
		validDaysAct.setAdapter(textAdapters.validDaysAdapter(layoutResource, null));
		anyNetCallsAct.setAdapter(textAdapters.callsAdapter(layoutResource, null));
		thisNetCallsAct.setAdapter(textAdapters.callsAdapter(layoutResource, null));
		anyNetSmsAct.setAdapter(textAdapters.smsAdapter(layoutResource, null));
		thisNetSmsAct.setAdapter(textAdapters.smsAdapter(layoutResource, null));
		availableAppDataLimitAct.setAdapter(textAdapters.appLimitAdapter(layoutResource, null));
		paymentTypeAct.setAdapter(textAdapters.payTypeAdapter(layoutResource, null));
		anyTimeDataAct.setAdapter(textAdapters.dataAdapter(layoutResource, null));
		dayTimeDataAct.setAdapter(textAdapters.dataAdapter(layoutResource, null));
		nightTimeDataAct.setAdapter(textAdapters.dataAdapter(layoutResource, null));
		descriptionMact.setAdapter(textAdapters.descriptionAdapter(layoutResource, null));
		availableAppMact.setAdapter(textAdapters.appsAdapter(layoutResource, null));

		availableAppMact.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		descriptionMact.setTokenizer(newlineTokenizer);

		addPackageBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addNewPackage();
			}
		});

		clearAllBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				clearAllFields();
			}
		});
	}

	private void addNewPackage() {

		if (!validateInputs()) {
			return;
		}

		String ispName = ispNameAct.getText().toString().trim();
		String packageName = packageNameAct.getText().toString().trim();
		String packagePrice = packagePriceAct.getText().toString().trim();
		String validDays = validDaysAct.getText().toString().trim();
		String anyNetCalls = anyNetCallsAct.getText().toString().trim();
		String thisNetCalls = thisNetCallsAct.getText().toString().trim();
		String anyNetSms = anyNetSmsAct.getText().toString().trim();
		String thisNetSms = thisNetSmsAct.getText().toString().trim();
		String availableAppDataLimit = availableAppDataLimitAct.getText().toString().trim();
		String availableApp = availableAppMact.getText().toString().trim();
		String paymentType = paymentTypeAct.getText().toString().trim();
		String anyTimeData = anyTimeDataAct.getText().toString().trim();
		String dayTimeData = dayTimeDataAct.getText().toString().trim();
		String nightTimeData = nightTimeDataAct.getText().toString().trim();
		String description = descriptionMact.getText().toString().trim();
		
        validDays = formatValidDyasValue(validDays);
		anyNetCalls = formatCallsValue(anyNetCalls);
		thisNetCalls = formatCallsValue(thisNetCalls);
		anyTimeData = formatDataValue(anyTimeData);
		dayTimeData = formatDataValue(dayTimeData);
		nightTimeData = formatDataValue(nightTimeData);
		availableApp = formatAvailableApps(availableApp);
		availableAppDataLimit = formatAppDataLimit(availableAppDataLimit, availableApp);

		long newRowId = dbHelper.addPackage(ispName, packageName, packagePrice, validDays, anyNetCalls, thisNetCalls,
				anyNetSms, thisNetSms, availableAppDataLimit, availableApp, paymentType, anyTimeData, dayTimeData,
				nightTimeData, description);

		if (newRowId == -1) {
			Toast.makeText(this, "Something wrong!", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "Package added successfully", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(InsartActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}

	private void clearAllFields() {
		ispNameAct.setText("");
		packageNameAct.setText("");
		packagePriceAct.setText("");
		validDaysAct.setText("");
		anyNetCallsAct.setText("");
		thisNetCallsAct.setText("");
		anyNetSmsAct.setText("");
		thisNetSmsAct.setText("");
		availableAppDataLimitAct.setText("");
		availableAppMact.setText("");
		paymentTypeAct.setText("");
		anyTimeDataAct.setText("");
		dayTimeDataAct.setText("");
		nightTimeDataAct.setText("");
		descriptionMact.setText("");
	}

	private boolean validateInputs() {

		String ispName = ispNameAct.getText().toString().trim();
		if (ispName.isEmpty()) {
			ispNameAct.setError("ISP's name is required");
			ispNameAct.requestFocus();
			return false;
		}

		String packageName = packageNameAct.getText().toString().trim();
		if (packageName.isEmpty()) {
			packageNameAct.setError("Package name is required");
			packageNameAct.requestFocus();
			return false;
		}

		String packagePrice = packagePriceAct.getText().toString().trim();
		if (packagePrice.isEmpty()) {
			packagePriceAct.setError("Package price is required");
			packagePriceAct.requestFocus();
			return false;
		}

		String validDays = validDaysAct.getText().toString().trim();
		if (validDays.isEmpty()) {
			validDaysAct.setError("Valid days is required");
			validDaysAct.requestFocus();
			return false;
		}

		String anyNetCalls = anyNetCallsAct.getText().toString().trim();
		String thisNetCalls = thisNetCallsAct.getText().toString().trim();
		String anyNetSms = anyNetSmsAct.getText().toString().trim();
		String thisNetSms = thisNetSmsAct.getText().toString().trim();
		String anyTimeData = anyTimeDataAct.getText().toString().trim();
		String dayTimeData = dayTimeDataAct.getText().toString().trim();
		String nightTimeData = nightTimeDataAct.getText().toString().trim();
		String availableApp = availableAppMact.getText().toString().trim();

		// Check have content
		if (anyNetCalls.isEmpty() && thisNetCalls.isEmpty() && anyNetSms.isEmpty() && thisNetSms.isEmpty()
				&& anyTimeData.isEmpty() && dayTimeData.isEmpty() && nightTimeData.isEmpty()
				&& availableApp.isEmpty()) {

			final String NO_CONTENT_ERROR = "You must enter the content";
			anyNetCallsAct.setError(NO_CONTENT_ERROR);
			thisNetCallsAct.setError(NO_CONTENT_ERROR);
			anyNetSmsAct.setError(NO_CONTENT_ERROR);
			thisNetSmsAct.setError(NO_CONTENT_ERROR);
			anyTimeDataAct.setError(NO_CONTENT_ERROR);
			dayTimeDataAct.setError(NO_CONTENT_ERROR);
			nightTimeDataAct.setError(NO_CONTENT_ERROR);
			availableAppMact.setError(NO_CONTENT_ERROR);
			return false;
		}
		
		// Check is duplicate
		if (dbHelper.isDuplicateInsart(ispName, packagePrice)) {
			packagePriceAct.setError("The package is already available at this price");
			packagePriceAct.requestFocus();
			return false;
		}

		// Validate data formats
		if (!validateDataFormat(anyTimeData)) {
			anyTimeDataAct.setError("Data must end with MB or GB");
			anyTimeDataAct.requestFocus();
			return false;
		}

		if (!validateDataFormat(dayTimeData)) {
			dayTimeDataAct.setError("Data must end with MB or GB");
			dayTimeDataAct.requestFocus();
			return false;
		}

		if (!validateDataFormat(nightTimeData)) {
			nightTimeDataAct.setError("Data must end with MB or GB");
			nightTimeDataAct.requestFocus();
			return false;
		}

		return true;
	}

	private boolean validateDataFormat(String data) {
		if (data.isEmpty() || data.toUpperCase().equals("UNLIMITED")) {
			return true;
		}
		return data.toUpperCase().endsWith("MB") || data.toUpperCase().endsWith("GB");
	}

	private String formatDataValue(String value) {
		if (value.isEmpty()) {
			return value;
		}

		value = value.toUpperCase();

		if (value.endsWith("MB") || value.endsWith("GB")) {
			String number = value.substring(0, value.length() - 2);
			String unit = value.substring(value.length() - 2);
			return number.trim() + " " + unit;
		}
		return value;
	}
	
	private String formatValidDyasValue(String value) {
		
		// Check if the value contains only numbers
		if (value.matches("\\d+")) {
			value += "Days";
		}
		return value;
	}

	private String formatCallsValue(String value) {
		if (value.isEmpty()) {
			return value;
		}

		// Check if the value contains only numbers
		if (value.matches("\\d+")) {
			value += "Mins";
		}
		return value;
	}

	private String formatAvailableApps(String availableApps) {
		if (availableApps.endsWith(",")) {
			availableApps = availableApps.substring(0, availableApps.length() - 1);
		}
		return availableApps;
	}

	private String formatAppDataLimit(String limitValue, String appsData) {
		if (limitValue.isEmpty() && !appsData.isEmpty()) {
			limitValue = "Unlimited";
		}
		return limitValue;
	}

}