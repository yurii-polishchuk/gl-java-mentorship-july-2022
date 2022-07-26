package com.example.statussvc.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDateTime;

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
    private Duration connectionTime;
    @NotNull
    private LocalDateTime lastCheck;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;
}
