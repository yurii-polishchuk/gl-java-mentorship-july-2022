package com.example.statussvc.domain;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * Main Host domain biz object
 */
@Getter
@Setter
@ToString
@Data
@Table(name = "URLS")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Host implements Domain {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "URL")
    @NotBlank(message = "URL is mandatory")
    private String url;
    @Column(name = "CONNECTION_TIME")
    private int connectionTime; // ms
    @Column(name = "LAST_CHECK")
    private LocalDateTime lastCheck;
    @Column(name = "STATUS")
    private String status; // available/unavailable

}
