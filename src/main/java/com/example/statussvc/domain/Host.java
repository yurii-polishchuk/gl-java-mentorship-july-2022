package com.example.statussvc.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

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
    @NotNull
    private String title;
    private String description;
    @NotNull
    private String url;
    @NotNull
    @Min(1)
    private Integer connectionTime;
    @NotNull
    private Date lastCheck;
    @Enumerated(EnumType.STRING)
    private Status status;
}
