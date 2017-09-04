package cn.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ZHBean implements Serializable {

    public double getUnUsePoint() {
        return unUsePoint;
    }

    public void setUnUsePoint(double unUsePoint) {
        this.unUsePoint = unUsePoint;
    }

    public double getServiceCost() {
        return serviceCost;
    }

    public void setServiceCost(double serviceCost) {
        this.serviceCost = serviceCost;
    }

    public String getPackageID() {
        return packageID;
    }

    public void setPackageID(String packageID) {
        this.packageID = packageID;
    }

    public List<ZHPackageBean> getCompany() {
        return company;
    }

    public void setCompany(List<ZHPackageBean> company) {
        this.company = company;
    }

    public boolean getHasMonthlyPackage() {
        return hasMonthlyPackage;
    }

    public void setHasMonthlyPackage(boolean hasMonthlyPackage) {
        this.hasMonthlyPackage = hasMonthlyPackage;
    }

    public double getUnUsePower() {
        return unUsePower;
    }

    public void setUnUsePower(double unUsePower) {
        this.unUsePower = unUsePower;
    }

    public int getRemainedAmount() {
        return remainedAmount;
    }

    public void setRemainedAmount(int remainedAmount) {
        this.remainedAmount = remainedAmount;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getLessPower() {
        return lessPower;
    }

    public void setLessPower(int lessPower) {
        this.lessPower = lessPower;
    }

    public int getLessPoint() {
        return lessPoint;
    }

    public void setLessPoint(int lessPoint) {
        this.lessPoint = lessPoint;
    }

    public String getTipMsgPower() {
        return tipMsgPower;
    }

    public void setTipMsgPower(String tipMsgPower) {
        this.tipMsgPower = tipMsgPower;
    }

    public String getTipMsgPoint() {
        return tipMsgPoint;
    }

    public void setTipMsgPoint(String tipMsgPoint) {
        this.tipMsgPoint = tipMsgPoint;
    }

    public String getTipMsg() {
        return tipMsg;
    }

    public void setTipMsg(String tipMsg) {
        this.tipMsg = tipMsg;
    }

    private double unUsePoint;
    private double unUsePower;
    private double serviceCost;
    private String packageID;
    private List<ZHPackageBean> company = new ArrayList<ZHPackageBean>();
    private boolean hasMonthlyPackage;
    private int remainedAmount;
    private int userType;
    private int lessPower;
    private int lessPoint;
    private String tipMsgPower;
    private String tipMsgPoint;
    private String tipMsg;

}
