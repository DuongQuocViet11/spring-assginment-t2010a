package com.example.springassignmentt2010a.entity;

import com.example.springassignmentt2010a.entity.enums.ProductSimpleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    private String id;
    private String fullName;
    private String phone;
    private String email;
    private ProductSimpleStatus status;
}
