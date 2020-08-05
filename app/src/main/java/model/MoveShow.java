package model;

import java.util.List;

public class MoveShow
{
    List<String>timeFrom,timeTo;
    String price;
    String name;
    String hname;
    String id;
    String uid;

    public MoveShow() {
    }

    public String getHname() {
        return hname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setHname(String hname) {
        this.hname = hname;
    }

    public List<String> getTimeFrom() {
        return timeFrom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
