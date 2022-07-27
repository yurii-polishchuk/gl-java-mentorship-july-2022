package com.example.statussvc.domain;

import com.example.statussvc.domain.type.Status;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String url;

    @NotNull
    private Duration connectionTime;

    private LocalDateTime lastCheck;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

}
