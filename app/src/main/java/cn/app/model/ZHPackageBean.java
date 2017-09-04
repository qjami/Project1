package cn.app.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ZHPackageBean implements Serializable {
	private String packageID;//套餐ID
	private String packageName;//套餐名称
	private double totalCredit;//总点数
	private double usedCredit;//已使用点数
	private String costStr;//服务费
	private String expiryDate;//过期时间
	private boolean isDefault;//是否是默认
	private ArrayList<ZHCostBean> cost = new ArrayList<ZHCostBean>();

	public String getPackageID() {
		return packageID;
	}

	public void setPackageID(String packageID) {
		this.packageID = packageID;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public double getTotalCredit() {
		return totalCredit;
	}

	public void setTotalCredit(double totalCredit) {
		this.totalCredit = totalCredit;
	}

	public double getUsedCredit() {
		return usedCredit;
	}

	public void setUsedCredit(double usedCredit) {
		this.usedCredit = usedCredit;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public ArrayList<ZHCostBean> getCost() {
		return cost;
	}

	public void setCost(ArrayList<ZHCostBean> cost) {
		this.cost = cost;
	}

	public String getCostStr() {
		return costStr;
	}

	public void setCostStr(String costStr) {
		this.costStr = costStr;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean aDefault) {
		isDefault = aDefault;
	}
}
