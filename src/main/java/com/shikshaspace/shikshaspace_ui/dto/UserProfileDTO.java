package com.shikshaspace.shikshaspace_ui.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Integer age;
    private String bio;
    private String mobileNumber;
    private Double experience;
    private String profileImageUrl;
    private String linkedinUrl;
    private String githubUrl;
}
