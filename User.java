package com.example.pickuplaundry;

public class User {
    public String name, email, phone, gender, role, college;

    public User(){

    }

    public User(String name, String email, String phone, String gender, String role, String college) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.role = role;
        this.college = college;
    }
}
