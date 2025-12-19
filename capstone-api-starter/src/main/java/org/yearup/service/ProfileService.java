package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.data.mysql.MySqlProfileDao;
import org.yearup.exception.EmailAlreadyExitsException;
import org.yearup.exception.ProfileNotFoundException;
import org.yearup.models.Profile;

@Service
public class ProfileService {
    private final ProfileDao profileDao;
    private final UserDao userDao;

    public ProfileService(ProfileDao profileDao, UserDao userDao) {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

    // Create a new profile
    public Profile create(Profile profile){
        return profileDao.create(profile);
    }

    // Update an existing profile for a user
    // Throws exception if profile not found or email already exists
    public Profile update(Profile profile, int userId){
        findByUserId(userId); // ensure profile exists

        if(profileDao.existsByEmailAndIdNot(profile.getEmail(), userId)){
            throw new EmailAlreadyExitsException("Email already exists: " + profile.getEmail());
        }

        return profileDao.update(profile, userId);
    }

    // Retrieve profile by userId
    // Throws exception if profile not found
    public Profile findByUserId(int userId){
        Profile profile = profileDao.findByUserId(userId);
        if(profile == null){
            throw new ProfileNotFoundException("Profile not found: " + userId);
        }
        return profile;
    }
}

