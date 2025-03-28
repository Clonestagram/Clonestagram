package com.goorm.clonestagram.search.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSuggestionDto {
    private String username;
    private String image;
    private String bio;
}
