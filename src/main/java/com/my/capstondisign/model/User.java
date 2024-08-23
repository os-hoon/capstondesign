package com.my.capstondisign.model;

import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private int user_id;
    private String id;
    private String passwd;
    private String name;
    private String nickname;
    private String email;

    @Column(name = "category", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> category;

    private String region;
}
