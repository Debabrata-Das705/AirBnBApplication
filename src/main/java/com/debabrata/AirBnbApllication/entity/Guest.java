package com.debabrata.AirBnbApllication.entity;

import com.debabrata.AirBnbApllication.enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JoinColumnOrFormula;

import java.util.Set;

@Getter
@Setter
@Entity
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @ManyToOne
    @JoinColumn(name = "User_Id")
    private User user;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;


    private Integer age;

}
