package model;

import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.List;

public class Office
{
    private String address,headName,phone,description,officeType,id;
    private List<String> timeFrom,timeTo;
    private HashMap<String, Boolean> days;
    private String sortOfficeType;
    private GeoPoint location;
    String image;

    public Office() {
    }

    public String getSortOfficeType() {
        return sortOfficeType;
    }

    public void setSortOfficeType(String sortOfficeType) {
        this.sortOfficeType = sortOfficeType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOfficeType() {
        return officeType;
    }

    public void setOfficeType(String officeType) {
        this.officeType = officeType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }
}
