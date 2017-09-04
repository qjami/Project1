package cn.app.model;

public class AppBean extends BaseBean {
	private int isLatest;
	private int forceUpdate;
	private String downloadAddress;
	private String uptInfo;

	public int getIsLatest() {
		return isLatest;
	}

	public void setIsLatest(int isLatest) {
		this.isLatest = isLatest;
	}

	public int getForceUpdate() {
		return forceUpdate;
	}

	public void setForceUpdate(int forceUpdate) {
		this.forceUpdate = forceUpdate;
	}

	public String getDownloadAddress() {
		return downloadAddress;
	}

	public void setDownloadAddress(String downloadAddress) {
		this.downloadAddress = downloadAddress;
	}

	public String getUptInfo() {
		return uptInfo;
	}

	public void setUptInfo(String uptInfo) {
		this.uptInfo = uptInfo;
	}

	@Override
	public String toString() {
		return "AppBean [isLatest=" + isLatest + ", forceUpdate=" + forceUpdate
				+ ", downloadAddress=" + downloadAddress + ", uptInfo="
				+ uptInfo + "]";
	}



}
