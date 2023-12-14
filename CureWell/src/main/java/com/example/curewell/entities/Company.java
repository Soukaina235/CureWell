package com.example.curewell.entities;


public class Company {
    private final int id;
    private String name;
    private String tel;
    private String address;
    private StatusCompany status;

    public Company(int id, String name, String tel, String address) {
        super();
        this.id = id;
        this.name = name;
        this.tel = tel;
        this.address = address;
        this.status = StatusCompany.Normal;
    }

    public Company(int id, String name, String tel, String address, StatusCompany status) {
        super();
        this.id = id;
        this.name = name;
        this.tel = tel;
        this.address = address;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

    public String getAddress() {
        return address;
    }

    public StatusCompany getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStatus(StatusCompany status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", address='" + address + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Company company = (Company) obj;
        return company.id == id;
    }

}
