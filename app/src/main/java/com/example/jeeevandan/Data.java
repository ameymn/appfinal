package com.example.jeeevandan;

public class Data {
    private String city ;
    private String contact_number;
    private String gender ;
    private String id ;
    private String name ;
    private Boolean success ;

    public Data(String city, String contact_number, String gender, String id, String name, Boolean success) {
        this.city = city;
        this.contact_number = contact_number;
        this.gender = gender;
        this.id = id;
        this.name = name;
        this.success = success;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
