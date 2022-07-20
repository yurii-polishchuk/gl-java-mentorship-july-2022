package com.example.statussvc.wire.request;

import com.example.statussvc.wire.Request;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostCreateDto implements Request {
    private Long id;
    @NotBlank(message = "Empty title")
    private String title;
    private String description;
    @NotBlank(message = "Empty url")
    @Pattern(
            regexp = "^https?://(?:www\\.)?[-a-zA-Z\\d@:%._+~#=]{1,256}\\.[a-zA-Z\\d()]{1,6}\\b[-a-zA-Z\\d()@:%_+.~#?&/=]*$",
            message = "Invalid URL"
    )
    private String url;
    @Min(value = 0, message = "Connection time < 0ms")
    private int connectionTime;
    @NotBlank(message = "Empty status")
    @Pattern(
            regexp = "^ACTIVE|^INACTIVE",
            message = "Invalid status"
    )
    private String status;
    private Date lastCheck;
}
