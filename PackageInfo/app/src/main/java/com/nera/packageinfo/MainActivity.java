package com.nera.packageinfo;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	private TextView packageCount;
	private RecyclerView packageRecyclerView;
	private FloatingActionButton addNewButton;
	private ImageButton filterButton, menuButton, removeFilterButton;
	private LinearLayout contentLayout, emptyContentLayout;
	private PackageRecyclerAdapter recyclerAdapter;
	private DatabaseHelper databaseHelper;
	private FilterData filterData;
	private FilterDialog filterDialog;

	private static final int REQUEST_CODE_PICK_FILE = 1;
	private static final int REQUEST_CODE_IMPORT_FILE = 2;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && data != null) {
			Uri uri = data.getData();
			if (uri != null) {
				if (requestCode == REQUEST_CODE_PICK_FILE) {
					backupDatabase(uri);
				} else if (requestCode == REQUEST_CODE_IMPORT_FILE) {
					restorDatabase(uri);
				}
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		packageCount = findViewById(R.id.packageCount_txt);
		packageRecyclerView = findViewById(R.id.package_recyclerView);
		addNewButton = findViewById(R.id.addNew_fab);
		filterButton = findViewById(R.id.filter_ibtn);
		removeFilterButton = findViewById(R.id.RemoveFilter_btn);
		menuButton = findViewById(R.id.menu_ibtn);
		contentLayout = findViewById(R.id.content_layout);
		emptyContentLayout = findViewById(R.id.emptyContent_layout);

		databaseHelper = new DatabaseHelper(this);
		filterDialog = new FilterDialog(MainActivity.this, filterData);
		packageRecyclerView.setLayoutManager(new LinearLayoutManager(this));

		addNewButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, InsartActivity.class);
				startActivity(intent);
			}
		});

		filterButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				filterDialog.showFilterDialog();
			}
		});

		removeFilterButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				filterData = null;
				filterDialog = new FilterDialog(MainActivity.this, filterData);
				loadPackages();
				Toast.makeText(MainActivity.this, "Remove Filter", Toast.LENGTH_SHORT).show();
			}
		});

		menuButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showPopupMenu(view);
			}
		});

		loadPackages();
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadPackages();
	}

	public void setFilterData(FilterData filterData) {
		this.filterData = filterData;
		loadPackages();
	}

	public void loadPackages() {

		List<Package> packages = getPackage();

		if (recyclerAdapter == null) {
			recyclerAdapter = new PackageRecyclerAdapter(this, packages);
			packageRecyclerView.setAdapter(recyclerAdapter);
		} else {
			recyclerAdapter.updatePackages(packages);
		}

		setFilterIndicator();
		setPackageCount();
	}

	private List<Package> getPackage() {

		List<Package> packages;

		if (filterData == null) {
			packages = databaseHelper.getPackages();
		} else {
			packages = databaseHelper.getFilteredPackage(filterData);
		}
		return packages;
	}

	private void setFilterIndicator() {
		if (filterData != null) {
			removeFilterButton.setVisibility(View.VISIBLE);
		} else {
			removeFilterButton.setVisibility(View.GONE);
		}
	}

	private void setPackageCount() {
		int count = recyclerAdapter.getItemCount();

		if (count != 0) {
			packageCount.setText(String.valueOf(count));

			contentLayout.setVisibility(View.VISIBLE);
			emptyContentLayout.setVisibility(View.GONE);
		} else {
			contentLayout.setVisibility(View.INVISIBLE);
			emptyContentLayout.setVisibility(View.VISIBLE);
		}
	}

	private void showPopupMenu(View view) {

		PopupMenu popup = new PopupMenu(this, view);
		popup.getMenuInflater().inflate(R.menu.app_bar_menu, popup.getMenu());
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				int itemId = item.getItemId();
				if (itemId == R.id.menu_backup) {
					openFilePicker();
					return true;
				} else if (itemId == R.id.menu_restore) {
					openImportFilePicker();
					return true;
				} else if (itemId == R.id.menu_delete_all) {
					deleteDatabase();
					return true;
				}
				return false;
			}
		});
		popup.show();
	}

	private void deleteDatabase() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Are you sure ?").setMessage("You want to delete all this package information?")
				.setPositiveButton("Yes", (dialog, which) -> {

					boolean isDeleted = databaseHelper.deleteDatabase(MainActivity.this);
					if (isDeleted) {
						databaseHelper = new DatabaseHelper(MainActivity.this);
						loadPackages();
						Toast.makeText(MainActivity.this, "All packages deleted successfully", Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(MainActivity.this, "Error deleting packages", Toast.LENGTH_SHORT).show();
					}

				}).setNegativeButton("No", (dialog, which) -> {
					dialog.dismiss();
				});

		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private void restorDatabase(Uri uri) {

		if (recyclerAdapter.getItemCount() == 0) {

			boolean isRestored = databaseHelper.replaceDatabaseWithUri(this, uri);

			if (isRestored) {
				Toast.makeText(this, "Database restored successfully", Toast.LENGTH_SHORT).show();
				loadPackages();
			} else {
				Toast.makeText(this, "Error restoring database", Toast.LENGTH_LONG).show();
			}

		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Are you sure ?").setMessage(
					"Your current package information will be lost in this process. Do you want to continue this?")
					.setPositiveButton("Yes", (dialog, which) -> {

						boolean isRestored = databaseHelper.replaceDatabaseWithUri(this, uri);

						if (isRestored) {
							Toast.makeText(this, "Database restored successfully", Toast.LENGTH_SHORT).show();
							loadPackages();
						} else {
							Toast.makeText(this, "Error restoring database", Toast.LENGTH_LONG).show();
						}

					}).setNegativeButton("No", (dialog, which) -> {
						dialog.dismiss();
					});

			AlertDialog dialog = builder.create();
			dialog.show();
		}

	}

	private void backupDatabase(Uri uri) {
		boolean isBackedUp = databaseHelper.copyDatabaseToUri(this, uri);

		if (isBackedUp) {
			Toast.makeText(this, "Database backup successful", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "Failed to backup database", Toast.LENGTH_LONG).show();
		}
	}

	// Method to open the file picker
	private void openFilePicker() {
		Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("application/octet-stream"); // Use appropriate MIME type
		intent.putExtra(Intent.EXTRA_TITLE, getBackupFileName()); // Default file name
		startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
	}

	// Method to open the file picker for importing a database file
	private void openImportFilePicker() {
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("application/octet-stream"); // Use appropriate MIME type
		startActivityForResult(intent, REQUEST_CODE_IMPORT_FILE);
	}

	private String getBackupFileName() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		Date date = new Date();
		String formattedDateTime = formatter.format(date);
		String appName = getApplicationContext().getString(R.string.app_name);
		String fileName = String.format("%s_backup_%s.db", appName, formattedDateTime);
		return fileName;
	}

}