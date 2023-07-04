package com.kuryaevao.lombok;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatedUser {
    private String name;
    private String job;
    private Integer id;
    private String createdAt;
}

