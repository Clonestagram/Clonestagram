package com.goorm.clonestagram.user.controller;

import com.goorm.clonestagram.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * username으로 userId 조회
     * @return userId (Long)
     */
    @GetMapping("/id")
    public ResponseEntity<Long> getUserIdByUsername(@RequestParam String username) {
        Long userId = userService.findUserIdByUsername(username);
        return ResponseEntity.ok(userId);
    }
}
