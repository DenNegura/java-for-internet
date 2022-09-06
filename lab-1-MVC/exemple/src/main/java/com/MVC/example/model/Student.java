package com.MVC.example.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Student {

    @Id
    @Column(name = "roll_num")
    private Long rollNum;

    @Column(name = "name")
    private String Name;

//    Student() {}
}
