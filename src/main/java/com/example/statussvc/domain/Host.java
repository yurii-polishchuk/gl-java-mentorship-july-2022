package com.example.statussvc.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Main Host domain biz object
 */
@Getter
@Setter
@ToString
@Table
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Host implements Domain {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;
    private String url;
    private int connectionTime; //in ms
    private String lastCheck;
    private String status; //maybe enum like available/unavailable



}
