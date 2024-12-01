package com.nera.packageinfo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.MacAddress;
import android.widget.LinearLayout;
import android.widget.ViewAnimator;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import android.view.MenuItem;
import androidx.recyclerview.widget.RecyclerView;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.List;

public class PackageRecyclerAdapter extends RecyclerView.Adapter<PackageRecyclerAdapter.PackageViewHolder> {

	private List<Package> packages;
	Context context;

	public PackageRecyclerAdapter(Context context, List<Package> packages) {
		this.context = context;
		this.packages = packages;
	}

	public void updatePackages(List<Package> newPackages) {
		this.packages = newPackages;
		notifyDataSetChanged();
	}

	@NonNull
	@Override
	public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.package_recycler, parent, false);
		return new PackageViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {
		Package pkg = packages.get(position);
		holder.bindData(pkg);

		holder.ibtnShowMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				showPopupMenu(pkg, view);

			}
		});
	}

	@Override
	public int getItemCount() {
		return packages.size();
	}

	private void showPopupMenu(Package pkg, View view) {

		PopupMenu popup = new PopupMenu(context, view);
		popup.getMenuInflater().inflate(R.menu.package_menu, popup.getMenu());

		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				int itemId = item.getItemId();
				if (itemId == R.id.menu_edit) {

					Intent intent = new Intent(context, UpdateActivity.class);
					intent.putExtra("package_id", pkg.getId());
					context.startActivity(intent);

					return true;
				} else if (itemId == R.id.menu_delete) {

					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("Are you sure ?").setMessage(
							"Do you want to delete this " + pkg.getIspName() + " " + pkg.getPackagePrice() + " package")
							.setPositiveButton("Yes", (dialog, which) -> {

								DatabaseHelper dbHelper = new DatabaseHelper(context);
								dbHelper.deletePackage(pkg.getId());
								((MainActivity) context).loadPackages();
								Toast.makeText(context,
										pkg.getIspName() + " " + pkg.getPackagePrice() + " package deleted!",
										Toast.LENGTH_SHORT).show();

							}).setNegativeButton("No", (dialog, which) -> {

								dialog.dismiss();

							});

					AlertDialog dialog = builder.create();
					dialog.show();

					return true;
				}
				return false;
			}
		});

		popup.show();
	}

	static class PackageViewHolder extends RecyclerView.ViewHolder {
		private TextView tvIspName, tvPackageName, tvValidDays, tvPackagePrice, tvAnyNetCalls, tvThisNetCalls,
				tvAnyNetSms, tvThisNetSms, tvAvailableApps, tvAvailableAppsLimit, tvAnyTimeData, tvDayTimeData,
				tvNightTimeData, tvDataPayType, tvDescription, tvAnyNetCallsLabel, tvThisNetCallsLabel,
				tvAnyNetSmsLabel, tvThisNetSmsLabel;

		private ImageButton ibtnShowMenu;
		private LinearLayout callsLayout, smsLayout;
		private View viewBottomLine, callsSmsDividLine;

		PackageViewHolder(View itemView) {
			super(itemView);
			tvIspName = itemView.findViewById(R.id.ispName_txt);
			tvPackageName = itemView.findViewById(R.id.packageName_txt);
			tvValidDays = itemView.findViewById(R.id.validDayCount_txt);
			tvPackagePrice = itemView.findViewById(R.id.priceCount_txt);
			tvAnyNetCalls = itemView.findViewById(R.id.anyNetCallsCount_txt);
			tvThisNetCalls = itemView.findViewById(R.id.thisNetCallsCount_txt);
			tvAnyNetSms = itemView.findViewById(R.id.anyNetSmsCount_txt);
			tvThisNetSms = itemView.findViewById(R.id.thisNetSmsCount_txt);
			tvAvailableApps = itemView.findViewById(R.id.specialApps_txt);
			tvAvailableAppsLimit = itemView.findViewById(R.id.specialAppsLimitStatus_txt);
			tvAnyTimeData = itemView.findViewById(R.id.anytimeData_txt);
			tvDayTimeData = itemView.findViewById(R.id.daytimeData_txt);
			tvNightTimeData = itemView.findViewById(R.id.nighttimeData_txt);
			tvDataPayType = itemView.findViewById(R.id.dataCycle_txt);
			tvDescription = itemView.findViewById(R.id.description_txt);
			ibtnShowMenu = itemView.findViewById(R.id.showMenu_ibtn);
			callsLayout = itemView.findViewById(R.id.calls_layout);
			smsLayout = itemView.findViewById(R.id.sms_layout);
			tvAnyNetCallsLabel = itemView.findViewById(R.id.anyNetCallsLabel_txt);
			tvThisNetCallsLabel = itemView.findViewById(R.id.thisNetCallsLabel_txt);
			tvAnyNetSmsLabel = itemView.findViewById(R.id.anyNetSmsLabel_txt);
			tvThisNetSmsLabel = itemView.findViewById(R.id.thisNetSmsLabel_txt);
			viewBottomLine = itemView.findViewById(R.id.bottomLine_view);
			callsSmsDividLine = itemView.findViewById(R.id.CallsSmsDividLine_view);

		}

		void bindData(Package pkg) {

			String ispName = pkg.getIspName();
			String packageName = pkg.getPackageName();
			String validDays = pkg.getValidDays();
			String packagePrice = pkg.getPackagePrice();
			String anyNetCalls = pkg.getAnyNetCalls();
			String thisNetCalls = pkg.getThisNetCalls();
			String anyNetSms = pkg.getAnyNetSms();
			String thisNetSms = pkg.getThisNetSms();
			String availableApps = pkg.getAvailableApps();
			String availableAppsLimit = pkg.getAvailableAppsLimit();
			String anyTimeData = pkg.getAnyTimeData();
			String dayTimeData = pkg.getDayTimeData();
			String nightTimeData = pkg.getNightTimeData();
			String dataPayType = pkg.getDataPayType();
			String description = pkg.getDescription();

			tvIspName.setText(ispName);
			tvPackageName.setText(packageName);
			tvValidDays.setText(validDays);
			tvPackagePrice.setText(packagePrice);
			tvAnyNetCalls.setText(anyNetCalls);
			tvThisNetCalls.setText(thisNetCalls);
			tvAnyNetSms.setText(anyNetSms);
			tvThisNetSms.setText(thisNetSms);
			tvAvailableApps.setText(availableApps);
			tvAvailableAppsLimit.setText(availableAppsLimit);
			tvAnyTimeData.setText(anyTimeData);
			tvDayTimeData.setText(dayTimeData);
			tvNightTimeData.setText(nightTimeData);
			tvDataPayType.setText(dataPayType);
			tvDescription.setText(description);

			if (availableApps.isEmpty()) {
				tvAvailableApps.setVisibility(View.GONE);
				tvAvailableAppsLimit.setVisibility(View.GONE);
			} else {
				tvAvailableApps.setVisibility(View.VISIBLE);
				tvAvailableAppsLimit.setVisibility(View.VISIBLE);
			}

			if (description.isEmpty()) {
				tvDescription.setVisibility(View.GONE);
			} else {
				tvDescription.setVisibility(View.VISIBLE);
			}

			if (anyTimeData.isEmpty()) {
				tvAnyTimeData.setVisibility(View.GONE);
			} else {
				tvAnyTimeData.setVisibility(View.VISIBLE);
			}

			if (dayTimeData.isEmpty()) {
				tvDayTimeData.setVisibility(View.GONE);
			} else {
				tvDayTimeData.setVisibility(View.VISIBLE);
			}

			if (nightTimeData.isEmpty()) {
				tvNightTimeData.setVisibility(View.GONE);
			} else {
				tvNightTimeData.setVisibility(View.VISIBLE);
			}

			if (dataPayType.isEmpty()) {
				tvDataPayType.setVisibility(View.GONE);
			} else {
				tvDataPayType.setVisibility(View.VISIBLE);
			}

			if (anyNetCalls.isEmpty() && thisNetCalls.isEmpty()) {
				callsLayout.setVisibility(View.GONE);
				callsSmsDividLine.setVisibility(View.GONE);
			} else {
				callsLayout.setVisibility(View.VISIBLE);
				callsSmsDividLine.setVisibility(View.VISIBLE);

				if (anyNetCalls.isEmpty()) {
					tvAnyNetCallsLabel.setVisibility(View.GONE);
					tvAnyNetCalls.setVisibility(View.GONE);
				} else {
					tvAnyNetCallsLabel.setVisibility(View.VISIBLE);
					tvAnyNetCalls.setVisibility(View.VISIBLE);
				}

				if (thisNetCalls.isEmpty()) {
					tvThisNetCallsLabel.setVisibility(View.GONE);
					tvThisNetCalls.setVisibility(View.GONE);
				} else {
					tvThisNetCallsLabel.setVisibility(View.VISIBLE);
					tvThisNetCalls.setVisibility(View.VISIBLE);
				}
			}

			if (anyNetSms.isEmpty() && thisNetSms.isEmpty()) {
				smsLayout.setVisibility(View.GONE);
				callsSmsDividLine.setVisibility(View.GONE);
			} else {
				smsLayout.setVisibility(View.VISIBLE);
				callsSmsDividLine.setVisibility(View.VISIBLE);

				if (anyNetSms.isEmpty()) {
					tvAnyNetSmsLabel.setVisibility(View.GONE);
					tvAnyNetSms.setVisibility(View.GONE);
				} else {
					tvAnyNetSmsLabel.setVisibility(View.VISIBLE);
					tvAnyNetSms.setVisibility(View.VISIBLE);
				}

				if (thisNetSms.isEmpty()) {
					tvThisNetSmsLabel.setVisibility(View.GONE);
					tvThisNetSms.setVisibility(View.GONE);
				} else {
					tvThisNetSmsLabel.setVisibility(View.VISIBLE);
					tvThisNetSms.setVisibility(View.VISIBLE);
				}
			}

			if (availableApps.isEmpty() && description.isEmpty() && anyTimeData.isEmpty() && dayTimeData.isEmpty()
					&& nightTimeData.isEmpty()) {
				viewBottomLine.setVisibility(View.GONE);
			} else {
				viewBottomLine.setVisibility(View.VISIBLE);
			}

			setIspColor(tvIspName);
		}

		private void setIspColor(TextView ispName) {
			if (ispName == null || ispName.getText() == null) {
				return;
			}

			switch (ispName.getText().toString().toLowerCase()) {
			case "dialog":
				ispName.setTextColor(
						androidx.core.content.ContextCompat.getColor(itemView.getContext(), R.color.isp_dialog_color));
				break;
			case "mobitel":
				ispName.setTextColor(
						androidx.core.content.ContextCompat.getColor(itemView.getContext(), R.color.isp_mobitel_color));
				break;
			case "hutch":
				ispName.setTextColor(
						androidx.core.content.ContextCompat.getColor(itemView.getContext(), R.color.isp_hutch_color));
				break;
			case "airtel":
				ispName.setTextColor(
						androidx.core.content.ContextCompat.getColor(itemView.getContext(), R.color.isp_airtel_color));
				break;
			default:
				ispName.setTextColor(Color.BLACK);
				break;
			}
		}

	}

}