package org.yearup.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;
import org.yearup.service.ProfileService;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("profile")
@PreAuthorize("hasRole('USER')")
@CrossOrigin
@Tag(name = "Profile", description = "API for managing profile")
public class ProfileController {
    private final ProfileService profileService;
    private final UserDao userDao;

    public ProfileController(ProfileService profileService, UserDao userDao) {
        this.profileService = profileService;
        this.userDao = userDao;
    }

    @GetMapping
    @Operation(summary = "Get profile by userID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile found"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<Profile> findByUserId(Principal principal){

        int userId = getUserId(principal);
        Profile profile = profileService.findByUserId(userId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping
    @Operation(summary = "Update profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated"),
            @ApiResponse(responseCode = "404", description = "Profile not found to update"),
            @ApiResponse(responseCode = "400", description = "Email already exits")
    })
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
