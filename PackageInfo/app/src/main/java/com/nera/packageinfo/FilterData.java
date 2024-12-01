package com.nera.packageinfo;

public class FilterData {

	private String isp;
	private String packageName;
	private String validDays;
	private String minPrice;
	private String maxPrice;
	private String includedApps;
	private boolean calls;
	private boolean sms;
	private boolean data;

	public String getIsp() {
		return isp;
	}

	public void setIsp(String isp) {
		this.isp = isp;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getValidDays() {
		return validDays;
	}

	public void setValidDays(String validDays) {
		this.validDays = validDays;
	}

	public String getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(String minPrice) {
		this.minPrice = minPrice;
	}

	public String getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getIncludedApps() {
		return includedApps;
	}

	public void setIncludedApps(String includedApps) {
		this.includedApps = includedApps;
	}

	public boolean isCalls() {
		return calls;
	}

	public void setCalls(boolean calls) {
		this.calls = calls;
	}

	public boolean isSms() {
		return sms;
	}

	public void setSms(boolean sms) {
		this.sms = sms;
	}

	public boolean isData() {
		return data;
	}

	public void setData(boolean data) {
		this.data = data;
	}
}