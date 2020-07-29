package com.example.abhikalpana;

public class MembersData {
    String name, branch, email, dpUrl, nest_captain, last_attended;
    int nest;

    public MembersData(String name, String branch, String dpUrl, int nest) {
        this.name = name;
        this.branch = branch;
        this.dpUrl = dpUrl;
        this.nest = nest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDpUrl() {
        return dpUrl;
    }

    public void setDpUrl(String dpUrl) {
        this.dpUrl = dpUrl;
    }

    public String getNest_captain() {
        return nest_captain;
    }

    public void setNest_captain(String nest_captain) {
        this.nest_captain = nest_captain;
    }

    public String getLast_attended() {
        return last_attended;
    }

    public void setLast_attended(String last_attended) {
        this.last_attended = last_attended;
    }

    public int getNest() {
        return nest;
    }

    public void setNest(int nest) {
        this.nest = nest;
    }
}
