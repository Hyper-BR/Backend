package br.com.hyper.dtos.requests;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class FollowRequestDTO {

    private UUID id;

    @NotEmpty(message = "Invalid email, can not be empty")
    private String email;

    @NotEmpty(message = "Invalid followingId, can not be empty")
    private Long followingId;

    private ZonedDateTime followDate;
}
