package com.nera.packageinfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.widget.Filter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "packages.db";
	public static final int DATABASE_VERSION = 1;

	public static final String TABLE_NAME = "packages";

	public static final String KEY_ID = "id";
	public static final String KEY_ISP_NAME = "isp_name";
	public static final String KEY_PACKAGE_NAME = "package_name";
	public static final String KEY_PACKAGE_PRICE = "package_price";
	public static final String KEY_VALID_DAYS = "valid_days";
	public static final String KEY_ANYNET_CALLS = "anynet_calls";
	public static final String KEY_THISNET_CALLS = "thisnet_calls";
	public static final String KEY_ANYNET_SMS = "anynet_sms";
	public static final String KEY_THISNET_SMS = "thisnet_sms";
	public static final String KEY_AVAILABLE_APPS = "available_apps";
	public static final String KEY_APP_LIMIT = "app_limit";
	public static final String KEY_ANYTIME_DATA = "anytime_data";
	public static final String KEY_DAYTIME_DATA = "daytime_data";
	public static final String KEY_NIGHTTIME_DATA = "nighttime_data";
	public static final String KEY_DATA_PAY_TYPE = "data_pay_type";
	public static final String KEY_DESCRIPTION = "description";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE_QUERY = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ KEY_ISP_NAME + " TEXT, " + KEY_PACKAGE_NAME + " TEXT, " + KEY_PACKAGE_PRICE + " TEXT, "
				+ KEY_VALID_DAYS + " TEXT, " + KEY_ANYNET_CALLS + " TEXT, " + KEY_THISNET_CALLS + " TEXT, "
				+ KEY_ANYNET_SMS + " TEXT, " + KEY_THISNET_SMS + " TEXT, " + KEY_AVAILABLE_APPS + " TEXT, "
				+ KEY_APP_LIMIT + " TEXT, " + KEY_ANYTIME_DATA + " TEXT, " + KEY_DAYTIME_DATA + " TEXT, "
				+ KEY_NIGHTTIME_DATA + " TEXT, " + KEY_DATA_PAY_TYPE + " TEXT, " + KEY_DESCRIPTION + " TEXT" + ");";

		db.execSQL(CREATE_TABLE_QUERY);	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Handle database upgrades here if needed
	}

	public long addPackage(String ispName, String packageName, String packagePrice, String validDays,
			String anyNetCalls, String thisNetCalls, String anyNetSms, String thisNetSms, String availableAppDataLimit,
			String availableApp, String paymentType, String anyTimeData, String dayTimeData, String nightTimeData,
			String description) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(KEY_ISP_NAME, ispName);
		values.put(KEY_PACKAGE_NAME, packageName);
		values.put(KEY_PACKAGE_PRICE, packagePrice);
		values.put(KEY_VALID_DAYS, validDays);
		values.put(KEY_ANYNET_CALLS, anyNetCalls);
		values.put(KEY_THISNET_CALLS, thisNetCalls);
		values.put(KEY_ANYNET_SMS, anyNetSms);
		values.put(KEY_THISNET_SMS, thisNetSms);
		values.put(KEY_APP_LIMIT, availableAppDataLimit);
		values.put(KEY_AVAILABLE_APPS, availableApp);
		values.put(KEY_DATA_PAY_TYPE, paymentType);
		values.put(KEY_ANYTIME_DATA, anyTimeData);
		values.put(KEY_DAYTIME_DATA, dayTimeData);
		values.put(KEY_NIGHTTIME_DATA, nightTimeData);
		values.put(KEY_DESCRIPTION, description);

		long newRowId = db.insert(TABLE_NAME, null, values);
		db.close();
		return newRowId;
	}

	public int updatePackage(int id, String ispName, String packageName, String packagePrice, String validDays,
			String anyNetCalls, String thisNetCalls, String anyNetSms, String thisNetSms, String availableAppDataLimit,
			String availableApp, String paymentType, String anyTimeData, String dayTimeData, String nightTimeData,
			String description) {

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(KEY_ISP_NAME, ispName);
		values.put(KEY_PACKAGE_NAME, packageName);
		values.put(KEY_PACKAGE_PRICE, packagePrice);
		values.put(KEY_VALID_DAYS, validDays);
		values.put(KEY_ANYNET_CALLS, anyNetCalls);
		values.put(KEY_THISNET_CALLS, thisNetCalls);
		values.put(KEY_ANYNET_SMS, anyNetSms);
		values.put(KEY_THISNET_SMS, thisNetSms);
		values.put(KEY_APP_LIMIT, availableAppDataLimit);
		values.put(KEY_AVAILABLE_APPS, availableApp);
		values.put(KEY_DATA_PAY_TYPE, paymentType);
		values.put(KEY_ANYTIME_DATA, anyTimeData);
		values.put(KEY_DAYTIME_DATA, dayTimeData);
		values.put(KEY_NIGHTTIME_DATA, nightTimeData);
		values.put(KEY_DESCRIPTION, description);

		// Define the WHERE clause to update specific row
		String selection = KEY_ID + " = ?";
		String[] selectionArgs = { String.valueOf(id) };

		int num = db.update(TABLE_NAME, values, selection, selectionArgs);
		db.close();
		return num;
	}

	public void deletePackage(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String DELETE_QUERY = "DELETE FROM " + TABLE_NAME + " WHERE " + KEY_ID + " = " + id;
		db.execSQL(DELETE_QUERY);
		db.close();
	}

	public List<Package> getPackages() {
		List<Package> packages = new ArrayList<>();
		SQLiteDatabase db = this.getReadableDatabase();

		String[] projection = { KEY_ID, KEY_ISP_NAME, KEY_PACKAGE_NAME, KEY_PACKAGE_PRICE, KEY_VALID_DAYS,
				KEY_ANYNET_CALLS, KEY_THISNET_CALLS, KEY_ANYNET_SMS, KEY_THISNET_SMS, KEY_AVAILABLE_APPS, KEY_APP_LIMIT,
				KEY_ANYTIME_DATA, KEY_DAYTIME_DATA, KEY_NIGHTTIME_DATA, KEY_DATA_PAY_TYPE, KEY_DESCRIPTION };

		String orderBy = "CAST(" + KEY_PACKAGE_PRICE + " AS INTEGER) ASC";

		Cursor cursor = db.query(TABLE_NAME, projection, null, null, null, null, orderBy);

		while (cursor.moveToNext()) {
			Package pkg = new Package();
			pkg.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
			pkg.setIspName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ISP_NAME)));
			pkg.setPackageName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PACKAGE_NAME)));
			pkg.setPackagePrice(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PACKAGE_PRICE)));
			pkg.setValidDays(cursor.getString(cursor.getColumnIndexOrThrow(KEY_VALID_DAYS)));
			pkg.setAnyNetCalls(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ANYNET_CALLS)));
			pkg.setThisNetCalls(cursor.getString(cursor.getColumnIndexOrThrow(KEY_THISNET_CALLS)));
			pkg.setAnyNetSms(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ANYNET_SMS)));
			pkg.setThisNetSms(cursor.getString(cursor.getColumnIndexOrThrow(KEY_THISNET_SMS)));
			pkg.setAvailableApps(cursor.getString(cursor.getColumnIndexOrThrow(KEY_AVAILABLE_APPS)));
			pkg.setAvailableAppsLimit(cursor.getString(cursor.getColumnIndexOrThrow(KEY_APP_LIMIT)));
			pkg.setAnyTimeData(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ANYTIME_DATA)));
			pkg.setDayTimeData(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DAYTIME_DATA)));
			pkg.setNightTimeData(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NIGHTTIME_DATA)));
			pkg.setDataPayType(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATA_PAY_TYPE)));
			pkg.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)));

			packages.add(pkg);
		}

		cursor.close();
		db.close();
		return packages;
	}

	public List<Package> getFilteredPackage(FilterData filterData) {
		
		FilterDialog filterDialog = new FilterDialog();
		
		final String SPINNER_DEFAULT_ISP = filterDialog.getSpinDefaultIsp();
		final String SPINNER_DEFAULT_PACKAGE = filterDialog.getSpinDefaultPackageName();
		final String SPINNER_DEFAULT_VALID_TIME = filterDialog.getSpinDefaultValidDays();

		List<Package> packages = new ArrayList<>();
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<String> selectionArgs = new ArrayList<>();

		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT * FROM ").append(TABLE_NAME).append(" WHERE 1=1");

		// ISP Name filter
		if (!SPINNER_DEFAULT_ISP.equals(filterData.getIsp())) {
			queryBuilder.append(" AND ").append(KEY_ISP_NAME).append(" = ?");
			selectionArgs.add(filterData.getIsp());
		}

		// Package Name filter
		if (!SPINNER_DEFAULT_PACKAGE.equals(filterData.getPackageName())) {
			queryBuilder.append(" AND ").append(KEY_PACKAGE_NAME).append(" = ?");
			selectionArgs.add(filterData.getPackageName());
		}

		// Valid Days filter
		if (!SPINNER_DEFAULT_VALID_TIME.equals(filterData.getValidDays())) {
			queryBuilder.append(" AND ").append(KEY_VALID_DAYS).append(" = ?");
			selectionArgs.add(filterData.getValidDays());
		}

		// Price range filter
		if (filterData.getMinPrice() != null && !filterData.getMinPrice().isEmpty()) {
			queryBuilder.append(" AND CAST(").append(KEY_PACKAGE_PRICE).append(" AS INTEGER) >= ?");
			selectionArgs.add(filterData.getMinPrice());
		}

		if (filterData.getMaxPrice() != null && !filterData.getMaxPrice().isEmpty()) {
			queryBuilder.append(" AND CAST(").append(KEY_PACKAGE_PRICE).append(" AS INTEGER) <= ?");
			selectionArgs.add(filterData.getMaxPrice());
		}

		// Calls filter
		if (filterData.isCalls()) {
			queryBuilder.append(" AND (").append(KEY_ANYNET_CALLS).append(" IS NOT NULL AND ").append(KEY_ANYNET_CALLS)
					.append(" != '' OR ").append(KEY_THISNET_CALLS).append(" IS NOT NULL AND ")
					.append(KEY_THISNET_CALLS).append(" != '')");
		}

		// SMS filter
		if (filterData.isSms()) {
			queryBuilder.append(" AND (").append(KEY_ANYNET_SMS).append(" IS NOT NULL AND ").append(KEY_ANYNET_SMS)
					.append(" != '' OR ").append(KEY_THISNET_SMS).append(" IS NOT NULL AND ").append(KEY_THISNET_SMS)
					.append(" != '')");
		}

		// Data filter
		if (filterData.isData()) {
			queryBuilder.append(" AND (").append(KEY_ANYTIME_DATA).append(" IS NOT NULL AND ").append(KEY_ANYTIME_DATA)
					.append(" != '' OR ").append(KEY_DAYTIME_DATA).append(" IS NOT NULL AND ").append(KEY_DAYTIME_DATA)
					.append(" != '' OR ").append(KEY_NIGHTTIME_DATA).append(" IS NOT NULL AND ")
					.append(KEY_NIGHTTIME_DATA).append(" != '')");
		}

		// Included apps filter
		if (filterData.getIncludedApps() != null && !filterData.getIncludedApps().trim().isEmpty()) {
			String[] requiredApps = filterData.getIncludedApps().split(",\\s*");
			for (String app : requiredApps) {
				queryBuilder.append(" AND ").append(KEY_AVAILABLE_APPS).append(" LIKE ?");
				selectionArgs.add("%" + app.trim() + "%");
			}
		}

		// ORDER BY clause for price sorting
		queryBuilder.append(" ORDER BY CAST(").append(KEY_PACKAGE_PRICE).append(" AS INTEGER) ASC");

		Cursor cursor = db.rawQuery(queryBuilder.toString(), selectionArgs.toArray(new String[0]));

		while (cursor.moveToNext()) {
			Package pkg = new Package();
			pkg.setId(cursor.getInt(0));
			pkg.setIspName(cursor.getString(1));
			pkg.setPackageName(cursor.getString(2));
			pkg.setPackagePrice(cursor.getString(3));
			pkg.setValidDays(cursor.getString(4));
			pkg.setAnyNetCalls(cursor.getString(5));
			pkg.setThisNetCalls(cursor.getString(6));
			pkg.setAnyNetSms(cursor.getString(7));
			pkg.setThisNetSms(cursor.getString(8));
			pkg.setAvailableApps(cursor.getString(9));
			pkg.setAvailableAppsLimit(cursor.getString(10));
			pkg.setAnyTimeData(cursor.getString(11));
			pkg.setDayTimeData(cursor.getString(12));
			pkg.setNightTimeData(cursor.getString(13));
			pkg.setDataPayType(cursor.getString(14));
			pkg.setDescription(cursor.getString(15));

			packages.add(pkg);
		}

		cursor.close();
		db.close();
		return packages;
	}
	
	public boolean isDuplicateInsart(String ispName, String packagePrice) {
		SQLiteDatabase db = this.getReadableDatabase();
		String query = "SELECT 1 FROM " + TABLE_NAME + " WHERE " + KEY_ISP_NAME + " = ? AND " + KEY_PACKAGE_PRICE + " = ?";
		Cursor cursor = db.rawQuery(query, new String[]{ispName, packagePrice});
		
		boolean exists = (cursor.getCount() > 0);
		cursor.close();
		db.close();
		return exists;
	}

	public boolean isDuplicateUpdate(int packageId, String ispName, String packagePrice) {
		SQLiteDatabase db = this.getReadableDatabase();
		String query = "SELECT 1 FROM " + TABLE_NAME + " WHERE " + KEY_ISP_NAME + " = ? AND " + KEY_PACKAGE_PRICE + " = ? AND " + KEY_ID + " != ?";
		Cursor cursor = db.rawQuery(query, new String[]{ispName, packagePrice, String.valueOf(packageId)});
		
		boolean exists = (cursor.getCount() > 0);
		cursor.close();
		db.close();
		return exists;
	}
	
	public Package getPackageById(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Package pkg = null;

		String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID + " = " + id;

		Cursor cursor = db.rawQuery(query, null);

		if (cursor.moveToFirst()) {
			pkg = new Package();
			pkg.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
			pkg.setIspName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ISP_NAME)));
			pkg.setPackageName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PACKAGE_NAME)));
			pkg.setPackagePrice(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PACKAGE_PRICE)));
			pkg.setValidDays(cursor.getString(cursor.getColumnIndexOrThrow(KEY_VALID_DAYS)));
			pkg.setAnyNetCalls(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ANYNET_CALLS)));
			pkg.setThisNetCalls(cursor.getString(cursor.getColumnIndexOrThrow(KEY_THISNET_CALLS)));
			pkg.setAnyNetSms(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ANYNET_SMS)));
			pkg.setThisNetSms(cursor.getString(cursor.getColumnIndexOrThrow(KEY_THISNET_SMS)));
			pkg.setAvailableApps(cursor.getString(cursor.getColumnIndexOrThrow(KEY_AVAILABLE_APPS)));
			pkg.setAvailableAppsLimit(cursor.getString(cursor.getColumnIndexOrThrow(KEY_APP_LIMIT)));
			pkg.setAnyTimeData(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ANYTIME_DATA)));
			pkg.setDayTimeData(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DAYTIME_DATA)));
			pkg.setNightTimeData(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NIGHTTIME_DATA)));
			pkg.setDataPayType(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATA_PAY_TYPE)));
			pkg.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)));
		}
		cursor.close();
        db.close();
		return pkg;
	}

	public String[] getColumnValues(String columnName) {
		ArrayList<String> values = new ArrayList<>();
		SQLiteDatabase db = this.getReadableDatabase();

		String query = "SELECT DISTINCT " + columnName + " FROM " + TABLE_NAME + " WHERE " + columnName
				+ " IS NOT NULL AND " + columnName + " != ''" + " ORDER BY " + columnName + " ASC";

		Cursor cursor = db.rawQuery(query, null);

		// Extract values from cursor
		if (cursor.moveToFirst()) {
			do {
				String value = cursor.getString(0);
				values.add(value);
			} while (cursor.moveToNext());
		}

		cursor.close();
        db.close();
		// Convert ArrayList to Array
		return values.toArray(new String[0]);
	}

	public boolean copyDatabaseToUri(Context context, Uri uri) {

		File databaseFile = context.getDatabasePath(DATABASE_NAME);
		boolean result;

		try {
			SQLiteDatabase db = this.getReadableDatabase();

			if (db != null && db.isOpen()) {
				db.close();
			}

			try (FileInputStream fis = new FileInputStream(databaseFile);
					OutputStream os = context.getContentResolver().openOutputStream(uri)) {

				byte[] buffer = new byte[1024];
				int length;
				while ((length = fis.read(buffer)) > 0) {
					os.write(buffer, 0, length);
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			}
			this.getWritableDatabase();
			result = true;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error copying database: " + e.getMessage());
		}
		return result;
	}

	public boolean replaceDatabaseWithUri(Context context, Uri uri) {

		boolean result;
		close();

		File databaseFile = context.getDatabasePath(DATABASE_NAME);

		try (InputStream is = context.getContentResolver().openInputStream(uri);
				FileOutputStream fos = new FileOutputStream(databaseFile)) {
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				fos.write(buffer, 0, length);
			}

			SQLiteDatabase db = this.getWritableDatabase();
			db.close();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error replacing database file", e);
		}
		return result;
	}

	public boolean deleteDatabase(Context context) {
		return context.deleteDatabase(DATABASE_NAME);
	}

}
