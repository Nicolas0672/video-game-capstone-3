package org.yearup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.yearup.service.ProfileService;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("profile")
public class ProfileController {
    private final ProfileService profileService;
    private final UserDao userDao;

    public ProfileController(ProfileService profileService, UserDao userDao) {
        this.profileService = profileService;
        this.userDao = userDao;
    }

    @GetMapping
    public ResponseEntity<Profile> findByUserId(Principal principal){

        int userId = getUserId(principal);
        Profile profile = profileService.findByUserId(userId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping
    public ResponseEntity<Profile> update(@RequestBody Profile profile, Principal principal){

        int userId = getUserId(principal);
        Profile updatedProfile = profileService.update(profile, userId);
        return ResponseEntity.ok(updatedProfile);
    }

    // Helper
    private int getUserId(Principal principal){
        String name = principal.getName();
        User user = userDao.getByUserName(name);
        return user.getId();
    }

}
