package model;

public class User
{
    String uname,uimage,uemail,ucity,ucountry,ustate;

    public User() {
    }

    public User(String uname, String uimage, String uemail, String ucity, String ucountry, String ustate) {
        this.uname = uname;
        this.uimage = uimage;
        this.uemail = uemail;
        this.ucity = ucity;
        this.ucountry = ucountry;
        this.ustate = ustate;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUimage() {
        return uimage;
    }

    public void setUimage(String uimage) {
        this.uimage = uimage;
    }

    public String getUemail() {
        return uemail;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail;
    }

    public String getUcity() {
        return ucity;
    }

    public void setUcity(String ucity) {
        this.ucity = ucity;
    }

    public String getUcountry() {
        return ucountry;
    }

    public void setUcountry(String ucountry) {
        this.ucountry = ucountry;
    }

    public String getUstate() {
        return ustate;
    }

    public void setUstate(String ustate) {
        this.ustate = ustate;
    }
}
