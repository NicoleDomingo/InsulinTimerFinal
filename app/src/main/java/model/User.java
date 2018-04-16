package model;

public class User {

    private String username;
    private String fname;
    private String lname;
    private String email;
    private String password;
    private String bday;
    private String height;
    private String weight;
    private String insulintype;
    private String maxblood;
    private String minblood;
    private String tips;
    private String mincount;
    private String maxcount;

    public User(){

    }
    public User(String username, String fname, String lname, String password, String height, String weight, String maxblood, String minblood, String mincount, String maxcount) {
        this.username = username;
        this.fname = fname;
        this.lname = lname;
        this.password = password;
        this.height = height;
        this.weight = weight;
        this.maxblood = maxblood;
        this.minblood = minblood;
        this.mincount = mincount;
        this.maxcount = maxcount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getBday() {
        return bday;
    }

    public void setBday(String bday) {
        this.bday = bday;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getInsulintype() {
        return insulintype;
    }

    public void setInsulintype(String insulintype) {
        this.insulintype = insulintype;
    }

    public String getMaxblood() {
        return maxblood;
    }

    public void setMaxblood(String maxblood) {
        this.maxblood = maxblood;
    }

    public String getMinblood() {
        return minblood;
    }

    public String getMincount() {
        return mincount;
    }

    public void setMincount(String mincount) {
        this.mincount = mincount;
    }

    public String getMaxcount() {
        return maxcount;
    }

    public void setMaxcount(String maxcount) {
        this.maxcount = maxcount;
    }


    public void setMinblood(String minblood) {
        this.minblood = minblood;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }
}