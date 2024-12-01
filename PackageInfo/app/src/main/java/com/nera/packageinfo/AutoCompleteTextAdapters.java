package com.nera.packageinfo;

import android.content.Context;
import android.widget.ArrayAdapter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AutoCompleteTextAdapters {

    Context context;

    private String[] ispSuggestions;
    private String[] packageNameSuggestions;
    private String[] packagePiceSuggestions;
    private String[] validDaysSuggestions;
    private String[] anyNetCallsSuggestions;
    private String[] thisNetCallsSuggestions;
    private String[] anyNetSmsSuggestions;
    private String[] thisNetSmsSuggestions;
    private String[] appDataLimitSuggestions;
    private String[] appsSuggestions;
    private String[] payTypeSuggestions;
    private String[] anyTimeDataSuggestions;
    private String[] dayTimeDataSuggestions;
    private String[] nightTimeDataSuggestions;
    private String[] descriptionSuggestions;

    private String[] allCallsSuggestions;
    private String[] allSmsSuggestions;
    private String[] allDataSuggestions;

    public AutoCompleteTextAdapters(Context context) {
        this.context = context;
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        this.ispSuggestions = dbHelper.getColumnValues(DatabaseHelper.KEY_ISP_NAME);
        this.packageNameSuggestions = dbHelper.getColumnValues(DatabaseHelper.KEY_PACKAGE_NAME);
        this.packagePiceSuggestions = dbHelper.getColumnValues(DatabaseHelper.KEY_PACKAGE_PRICE);
        this.validDaysSuggestions = dbHelper.getColumnValues(DatabaseHelper.KEY_VALID_DAYS);
        this.anyNetCallsSuggestions = dbHelper.getColumnValues(DatabaseHelper.KEY_ANYNET_CALLS);
        this.thisNetCallsSuggestions = dbHelper.getColumnValues(DatabaseHelper.KEY_THISNET_CALLS);
        this.anyNetSmsSuggestions = dbHelper.getColumnValues(DatabaseHelper.KEY_ANYNET_SMS);
        this.thisNetSmsSuggestions = dbHelper.getColumnValues(DatabaseHelper.KEY_THISNET_SMS);
        this.appDataLimitSuggestions = dbHelper.getColumnValues(DatabaseHelper.KEY_APP_LIMIT);
        this.payTypeSuggestions = dbHelper.getColumnValues(DatabaseHelper.KEY_DATA_PAY_TYPE);
        this.anyTimeDataSuggestions = dbHelper.getColumnValues(DatabaseHelper.KEY_ANYTIME_DATA);
        this.dayTimeDataSuggestions = dbHelper.getColumnValues(DatabaseHelper.KEY_DAYTIME_DATA);
        this.nightTimeDataSuggestions = dbHelper.getColumnValues(DatabaseHelper.KEY_NIGHTTIME_DATA);

        this.allCallsSuggestions = combineTowArrays(anyNetCallsSuggestions, thisNetCallsSuggestions);
        this.allSmsSuggestions = combineTowArrays(anyNetSmsSuggestions, thisNetSmsSuggestions);
        this.allDataSuggestions = combineThreeArrays(anyTimeDataSuggestions, dayTimeDataSuggestions,
                nightTimeDataSuggestions);
        this.appsSuggestions = splitElements(dbHelper.getColumnValues(DatabaseHelper.KEY_AVAILABLE_APPS), ",");
        this.descriptionSuggestions = splitElements(dbHelper.getColumnValues(DatabaseHelper.KEY_DESCRIPTION), "\n");
    }

    private String[] combineTowArrays(String[] array1, String[] array2) {
        if (array1 == null && array2 == null) {
            return new String[0];
        }

        array1 = (array1 != null) ? array1 : new String[0];
        array2 = (array2 != null) ? array2 : new String[0];

        Set<String> set = new HashSet<>();
        set.addAll(Arrays.asList(array1));
        set.addAll(Arrays.asList(array2));

        return set.toArray(new String[0]);
    }

    private String[] combineThreeArrays(String[] array1, String[] array2, String[] array3) {
        if (array1 == null && array2 == null && array3 == null) {
            return new String[0];
        }

        array1 = (array1 != null) ? array1 : new String[0];
        array2 = (array2 != null) ? array2 : new String[0];
        array3 = (array3 != null) ? array3 : new String[0];

        Set<String> set = new HashSet<>();
        set.addAll(Arrays.asList(array1));
        set.addAll(Arrays.asList(array2));
        set.addAll(Arrays.asList(array3));

        return set.toArray(new String[0]);
    }

    public static String[] splitElements(String[] inputArray, String delimiter) {
        if (inputArray == null || inputArray.length == 0) {
            return new String[0];
        }

        Set<String> uniqueElements = new HashSet<>();

        for (String str : inputArray) {
            if (str == null)
                continue;

            if (delimiter.equals(",")) {
                str = str.replaceAll(",\\s*", ",");
            }

            String[] elements = str.split(delimiter);
            for (String element : elements) {
                String trimmed = element.trim();
                if (!trimmed.isEmpty()) {
                    uniqueElements.add(trimmed);
                }
            }
        }

        return uniqueElements.toArray(new String[0]);
    }

    public ArrayAdapter<String> ispNameAdapter(int layoutResource, String defaultValue) {
        if (defaultValue != null) {
            List<String> itemsList = new ArrayList<>();
            itemsList.add(defaultValue);
            itemsList.addAll(Arrays.asList(ispSuggestions));
            return new ArrayAdapter<>(context, layoutResource, itemsList);
        } else {
            return new ArrayAdapter<>(context, layoutResource, ispSuggestions);
        }
    }

    public ArrayAdapter<String> packageNameAdapter(int layoutResource, String defaultValue) {
        if (defaultValue != null) {
            List<String> itemsList = new ArrayList<>();
            itemsList.add(defaultValue);
            itemsList.addAll(Arrays.asList(packageNameSuggestions));
            return new ArrayAdapter<>(context, layoutResource, itemsList);
        } else {
            return new ArrayAdapter<>(context, layoutResource, packageNameSuggestions);
        }
    }

    public ArrayAdapter<String> packagePriceAdapter(int layoutResource, String defaultValue) {
        if (defaultValue != null) {
            List<String> itemsList = new ArrayList<>();
            itemsList.add(defaultValue);
            itemsList.addAll(Arrays.asList(packagePiceSuggestions));
            return new ArrayAdapter<>(context, layoutResource, itemsList);
        } else {
            return new ArrayAdapter<>(context, layoutResource, packagePiceSuggestions);
        }
    }

    public ArrayAdapter<String> validDaysAdapter(int layoutResource, String defaultValue) {
        if (defaultValue != null) {
            List<String> itemsList = new ArrayList<>();
            itemsList.add(defaultValue);
            itemsList.addAll(Arrays.asList(validDaysSuggestions));
            return new ArrayAdapter<>(context, layoutResource, itemsList);
        } else {
            return new ArrayAdapter<>(context, layoutResource, validDaysSuggestions);
        }
    }

    public ArrayAdapter<String> callsAdapter(int layoutResource, String defaultValue) {
        if (defaultValue != null) {
            List<String> itemsList = new ArrayList<>();
            itemsList.add(defaultValue);
            itemsList.addAll(Arrays.asList(allCallsSuggestions));
            return new ArrayAdapter<>(context, layoutResource, itemsList);
        } else {
            return new ArrayAdapter<>(context, layoutResource, allCallsSuggestions);
        }
    }

    public ArrayAdapter<String> smsAdapter(int layoutResource, String defaultValue) {
        if (defaultValue != null) {
            List<String> itemsList = new ArrayList<>();
            itemsList.add(defaultValue);
            itemsList.addAll(Arrays.asList(allSmsSuggestions));
            return new ArrayAdapter<>(context, layoutResource, itemsList);
        } else {
            return new ArrayAdapter<>(context, layoutResource, allSmsSuggestions);
        }
    }

    public ArrayAdapter<String> appLimitAdapter(int layoutResource, String defaultValue) {
        if (defaultValue != null) {
            List<String> itemsList = new ArrayList<>();
            itemsList.add(defaultValue);
            itemsList.addAll(Arrays.asList(appDataLimitSuggestions));
            return new ArrayAdapter<>(context, layoutResource, itemsList);
        } else {
            return new ArrayAdapter<>(context, layoutResource, appDataLimitSuggestions);
        }
    }

    public ArrayAdapter<String> appsAdapter(int layoutResource, String defaultValue) {
        if (defaultValue != null) {
            List<String> itemsList = new ArrayList<>();
            itemsList.add(defaultValue);
            itemsList.addAll(Arrays.asList(appsSuggestions));
            return new ArrayAdapter<>(context, layoutResource, itemsList);
        } else {
            return new ArrayAdapter<>(context, layoutResource, appsSuggestions);
        }
    }

    public ArrayAdapter<String> payTypeAdapter(int layoutResource, String defaultValue) {
        if (defaultValue != null) {
            List<String> itemsList = new ArrayList<>();
            itemsList.add(defaultValue);
            itemsList.addAll(Arrays.asList(payTypeSuggestions));
            return new ArrayAdapter<>(context, layoutResource, itemsList);
        } else {
            return new ArrayAdapter<>(context, layoutResource, payTypeSuggestions);
        }
    }

    public ArrayAdapter<String> dataAdapter(int layoutResource, String defaultValue) {
        if (defaultValue != null) {
            List<String> itemsList = new ArrayList<>();
            itemsList.add(defaultValue);
            itemsList.addAll(Arrays.asList(allDataSuggestions));
            return new ArrayAdapter<>(context, layoutResource, itemsList);
        } else {
            return new ArrayAdapter<>(context, layoutResource, allDataSuggestions);
        }
    }

    public ArrayAdapter<String> descriptionAdapter(int layoutResource, String defaultValue) {
        if (defaultValue != null) {
            List<String> itemsList = new ArrayList<>();
            itemsList.add(defaultValue);
            itemsList.addAll(Arrays.asList(descriptionSuggestions));
            return new ArrayAdapter<>(context, layoutResource, itemsList);
        } else {
            return new ArrayAdapter<>(context, layoutResource, descriptionSuggestions);
        }
    }
}