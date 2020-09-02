package model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Seller
{
    private String ownerName,storeName,storeCity,storeState,storeCountry,storeAddress,backgroundImage,ownerImage
            ,phone1,phone2,storeCategory,storeSubCategory,storeEmail,storeDescription,companyName;
    private String sortOwnerName,sortStoreName,sortStoreAddress,sortStoreSubCategory;

    private String rate;
    boolean shopStatus;
    private  String geohash;
    double rating;
    Double sortRating;
    private List<String>timeFrom,timeTo;
    private HashMap<String, Boolean>days;
    private boolean deliveryStatus,workersRequred;
    private Integer  noOfRatings;
    private Double storeLongitude,storeLatitude;
    private String sUid;
    private Double totalStar;
    private GeoPoint geoPoint;
    private  String wqualification;
    //education
    private String principalName,boardName;
    private boolean hostel,transport;
    public Seller() {
    }

    public String getSortStoreSubCategory() {
        return sortStoreSubCategory;
    }

    public void setSortStoreSubCategory(String sortStoreSubCategory) {
        this.sortStoreSubCategory = sortStoreSubCategory;
    }

    public String getSortStoreAddress() {
        return sortStoreAddress;
    }

    public void setSortStoreAddress(String sortStoreAddress) {
        this.sortStoreAddress = sortStoreAddress;
    }

    public boolean isShopStatus() {
        return shopStatus;
    }

    public void setShopStatus(boolean shopStatus) {
        this.shopStatus = shopStatus;
    }

    public String getSortOwnerName() {
        return sortOwnerName;
    }

    public void setSortOwnerName(String sortOwnerName) {
        this.sortOwnerName = sortOwnerName;
    }

    public String getSortStoreName() {
        return sortStoreName;
    }

    public void setSortStoreName(String sortStoreName) {
        this.sortStoreName = sortStoreName;
    }

    public Double getSortRating() {
        return sortRating;
    }

    public void setSortRating(Double sortRating) {
        this.sortRating = sortRating;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
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

    public Double getTotalStar() {
        return totalStar;
    }

    public String getGeohash() {
        return geohash;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
    }

    public void setTotalStar(Double totalStar) {
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

    public String getStoreDescription() {
        return storeDescription;
    }

    public void setStoreDescription(String storeDescription) {
        this.storeDescription = storeDescription;
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

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public boolean isHostel() {
        return hostel;
    }

    public void setHostel(boolean hostel) {
        this.hostel = hostel;
    }

    public boolean isTransport() {
        return transport;
    }

    public void setTransport(boolean transport) {
        this.transport = transport;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getWqualification() {
        return wqualification;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setWqualification(String wqualification) {
        this.wqualification = wqualification;
    }
}
