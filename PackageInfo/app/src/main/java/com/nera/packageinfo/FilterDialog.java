package com.nera.packageinfo;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.List;

public class FilterDialog {

	private Context context;
	private AlertDialog dialog;
	private AutoCompleteTextAdapters textAdapters;

	private Button btnCancel, btnFilter;
	private Spinner spinIspName, spinPackageName, spinValidTime;
	private EditText edMinPrice, edMaxPrice;
	private CheckBox cbData, cbCalls, cbSms;
	private MultiAutoCompleteTextView mactIncludedApps;

	private ArrayAdapter<String> ispNameSuggestions;
	private ArrayAdapter<String> packageNameSuggestions;
	private ArrayAdapter<String> validDaysSuggestions;

	private FilterData filterData;

	private final String SPINNER_DEFAULT_ISP = "Get All ISPs";
	private final String SPINNER_DEFAULT_PACKAGE = "Get All Packages";
	private final String SPINNER_DEFAULT_VALID_TIME = "Get All Days";
	
	public FilterDialog() {
		
	}

	public FilterDialog(Context context, FilterData filterData) {
		this.context = context;
		this.filterData = filterData;

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		View dialogView = inflater.inflate(R.layout.dialog_filter, null);

		builder.setView(dialogView);
		dialog = builder.create();

		if (dialog.getWindow() != null) {
			dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		}

		btnCancel = dialogView.findViewById(R.id.btnCancel);
		btnFilter = dialogView.findViewById(R.id.btnFliter);
		spinIspName = dialogView.findViewById(R.id.ispName_spinner);
		spinPackageName = dialogView.findViewById(R.id.packageName_spinner);
		spinValidTime = dialogView.findViewById(R.id.validTime_spinner);
		edMinPrice = dialogView.findViewById(R.id.minPrice_et);
		edMaxPrice = dialogView.findViewById(R.id.maxPrice_et);
		cbCalls = dialogView.findViewById(R.id.calls_checkbox);
		cbSms = dialogView.findViewById(R.id.sms_checkbox);
		cbData = dialogView.findViewById(R.id.data_checkbox);
		mactIncludedApps = dialogView.findViewById(R.id.availableApp_mact);

		setAdapters();

		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});

		btnFilter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				applyFilters();
			}
		});
	}

	public void showFilterDialog() {
		reloadFilterData();
		dialog.show();
	}

	private void applyFilters() {

		DatabaseHelper databaseHelper = new DatabaseHelper(context);
		FilterData filterData = new FilterData();

		filterData.setIsp(spinIspName.getSelectedItem().toString());
		filterData.setPackageName(spinPackageName.getSelectedItem().toString());
		filterData.setValidDays(spinValidTime.getSelectedItem().toString());
		filterData.setMinPrice(edMinPrice.getText().toString().trim());
		filterData.setMaxPrice(edMaxPrice.getText().toString().trim());
		filterData.setIncludedApps(mactIncludedApps.getText().toString().trim());
		filterData.setCalls(cbCalls.isChecked());
		filterData.setSms(cbSms.isChecked());
		filterData.setData(cbData.isChecked());

		if (isNotNull(filterData)) {
			((MainActivity) context).setFilterData(filterData);
		}

		dialog.dismiss();

	}

	private void reloadFilterData() {
		if (filterData != null) {
			spinIspName.setSelection(ispNameSuggestions.getPosition(filterData.getIsp()));
			spinPackageName.setSelection(packageNameSuggestions.getPosition(filterData.getPackageName()));
			spinValidTime.setSelection(validDaysSuggestions.getPosition(filterData.getValidDays()));
			edMinPrice.setText(filterData.getMinPrice());
			edMaxPrice.setText(filterData.getMaxPrice());
			mactIncludedApps.setText(filterData.getIncludedApps());
			cbCalls.setChecked(filterData.isCalls());
			cbSms.setChecked(filterData.isSms());
			cbData.setChecked(filterData.isData());
		}
	}

	private void setAdapters() {
		textAdapters = new AutoCompleteTextAdapters(context);
		int layoutResource = android.R.layout.simple_spinner_dropdown_item;

		ispNameSuggestions = textAdapters.ispNameAdapter(layoutResource, SPINNER_DEFAULT_ISP);
		packageNameSuggestions = textAdapters.packageNameAdapter(layoutResource, SPINNER_DEFAULT_PACKAGE);
		validDaysSuggestions = textAdapters.validDaysAdapter(layoutResource, SPINNER_DEFAULT_VALID_TIME);

		spinIspName.setAdapter(ispNameSuggestions);
		spinPackageName.setAdapter(packageNameSuggestions);
		spinValidTime.setAdapter(validDaysSuggestions);

		mactIncludedApps.setAdapter(textAdapters.appsAdapter(android.R.layout.simple_dropdown_item_1line, null));
		mactIncludedApps.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
	}
	
	
	private boolean isNotNull(FilterData filterData) {
		return (!
			(filterData.getIsp() == SPINNER_DEFAULT_ISP &&
			filterData.getPackageName() == SPINNER_DEFAULT_PACKAGE &&
			filterData.getValidDays() == SPINNER_DEFAULT_VALID_TIME &&
			filterData.getMinPrice().toString().isEmpty() &&
			filterData.getMaxPrice().toString().isEmpty() &&
		    filterData.getIncludedApps().toString().isEmpty() &&
			filterData.isCalls() == false &&
			filterData.isSms() == false &&
			filterData.isData() == false)
		);
	}

	public String getSpinDefaultIsp() {
		return SPINNER_DEFAULT_ISP;
	}

	public String getSpinDefaultPackageName() {
		return SPINNER_DEFAULT_PACKAGE;
	}

	public String getSpinDefaultValidDays() {
		return SPINNER_DEFAULT_VALID_TIME;
	}

}