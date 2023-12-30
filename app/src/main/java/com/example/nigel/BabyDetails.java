package com.example.nigel;

public class BabyDetails {
    private String nigID;
    private String dob;
    private String age;
    private String group;

    private String weight;

    public String getBabyID() {
        return nigID;
    }

    public void setBabyID(String NigID) {
        this.nigID = NigID;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String DoB) {
        this.dob = DoB;
    }
    public String getAge() {
        return age;
    }

    public void setAge(String Age) {
        this.age = Age;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}

