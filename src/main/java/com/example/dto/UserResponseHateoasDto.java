package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;

public class UserResponseHateoasDto extends RepresentationModel<UserResponseHateoasDto> {
    
    private Long myUserId;
    private String myUserName;
    private String myUserEmail;
    private Integer myUserAge;
    private LocalDateTime myUserCreatedAt;
    
    public UserResponseHateoasDto() {}
    
    public UserResponseHateoasDto(Long myUserId, String myUserName, String myUserEmail, 
                                 Integer myUserAge, LocalDateTime myUserCreatedAt) {
        this.myUserId = myUserId;
        this.myUserName = myUserName;
        this.myUserEmail = myUserEmail;
        this.myUserAge = myUserAge;
        this.myUserCreatedAt = myUserCreatedAt;
    }
    
    public UserResponseHateoasDto(UserResponseDto myUserResponseDto) {
        this.myUserId = myUserResponseDto.getUserId();
        this.myUserName = myUserResponseDto.getUserName();
        this.myUserEmail = myUserResponseDto.getUserEmail();
        this.myUserAge = myUserResponseDto.getUserAge();
        this.myUserCreatedAt = myUserResponseDto.getUserCreatedAt();
    }
    
    @JsonProperty("userId")
    public Long getUserId() { return myUserId; }
    public void setUserId(Long myUserId) { this.myUserId = myUserId; }
    
    @JsonProperty("userName")
    public String getUserName() { return myUserName; }
    public void setUserName(String myUserName) { this.myUserName = myUserName; }
    
    @JsonProperty("userEmail")
    public String getUserEmail() { return myUserEmail; }
    public void setUserEmail(String myUserEmail) { this.myUserEmail = myUserEmail; }
    
    @JsonProperty("userAge")
    public Integer getUserAge() { return myUserAge; }
    public void setUserAge(Integer myUserAge) { this.myUserAge = myUserAge; }
    
    @JsonProperty("userCreatedAt")
    public LocalDateTime getUserCreatedAt() { return myUserCreatedAt; }
    public void setUserCreatedAt(LocalDateTime myUserCreatedAt) { this.myUserCreatedAt = myUserCreatedAt; }
    
    @JsonProperty("_links")
    @Override
    public List<Link> getLinks() {
        return super.getLinks();
    }
}