package com.syy.tessystem.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Student {
    private Integer id;

    private Integer sno;

    private String name;

    private String phone;

    private String headImg;

    private String grade;

    private String password;

    private String gender;

    private Integer role;

}