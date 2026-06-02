package com.example.java_final_assignment.controllers.requests;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetPersonalTasksRequest {
    // Defaults to page 0 if missing
    private Integer page = 0;

    // Defaults to 5 entries if missing
    private Integer size = 5;
}
