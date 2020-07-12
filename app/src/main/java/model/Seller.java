package model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Seller
{
    private String ownerName,storeName,storeCity,storeState,storeCountry,storeAddress,backgroundImage,ownerImage
            ,phone1,phone2,storeCategory,storeSubCategory,storeEmail;
    private List<String>timeFrom,timeTo;
    private HashMap<String, Boolean>days;
    private boolean deliveryStatus,storeStatus,workersRequred;
    private Integer totalStar,noOfRatings;
    private Double storeLongitude,storeLatitude;
    private String sUid;
    public Seller() {
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getStoreCity() {
        return storeCity;
    }

    public void setStoreCity(String storeCity) {
        this.storeCity = storeCity;
    }

    public String getStoreState() {
        return storeState;
    }

    public void setStoreState(String storeState) {
        this.storeState = storeState;
    }

    public String getStoreCountry() {
        return storeCountry;
    }

    public void setStoreCountry(String storeCountry) {
        this.storeCountry = storeCountry;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getOwnerImage() {
        return ownerImage;
    }

    public void setOwnerImage(String ownerImage) {
        this.ownerImage = ownerImage;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getStoreCategory() {
        return storeCategory;
    }

    public void setStoreCategory(String storeCategory) {
        this.storeCategory = storeCategory;
    }

    public String getStoreSubCategory() {
        return storeSubCategory;
    }

    public void setStoreSubCategory(String storeSubCategory) {
        this.storeSubCategory = storeSubCategory;
    }

    public boolean isWorkersRequred() {
        return workersRequred;
    }

    public void setWorkersRequred(boolean workersRequred) {
        this.workersRequred = workersRequred;
    }

    public String getStoreEmail() {
        return storeEmail;
    }

    public void setStoreEmail(String storeEmail) {
        this.storeEmail = storeEmail;
    }

    public List<String> getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(List<String> timeFrom) {
        this.timeFrom = timeFrom;
    }

    public List<String> getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(List<String> timeTo) {
        this.timeTo = timeTo;
    }

    public HashMap<String, Boolean> getDays() {
        return days;
    }

    public void setDays(HashMap<String, Boolean> days) {
        this.days = days;
    }

    public boolean isDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(boolean deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Integer getTotalStar() {
        return totalStar;
    }

    public void setTotalStar(Integer totalStar) {
        this.totalStar = totalStar;
    }

    public Integer getNoOfRatings() {
        return noOfRatings;
    }

    public void setNoOfRatings(Integer noOfRatings) {
        this.noOfRatings = noOfRatings;
    }

    public String getsUid() {
        return sUid;
    }

    public void setsUid(String sUid) {
        this.sUid = sUid;
    }

    public String getStoreName() {
        return storeName;
    }

    public boolean isStoreStatus() {
        return storeStatus;
    }

    public void setStoreStatus(boolean storeStatus) {
        this.storeStatus = storeStatus;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Double getStoreLongitude() {
        return storeLongitude;
    }

    public void setStoreLongitude(Double storeLongitude) {
        this.storeLongitude = storeLongitude;
    }

    public Double getStoreLatitude() {
        return storeLatitude;
    }

    public void setStoreLatitude(Double storeLatitude) {
        this.storeLatitude = storeLatitude;
    }
}
