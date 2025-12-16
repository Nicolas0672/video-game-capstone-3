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

    public Profile create(Profile profile){
        return profileDao.create(profile);
    }

    public Profile update(Profile profile, int userId){
        findByUserId(userId);
        if(profileDao.existsByEmail(profile.getEmail())){
            throw new EmailAlreadyExitsException("Email already exits " + profile.getEmail());
        }
        return profileDao.update(profile, userId);
    }

    public Profile findByUserId(int userId){
        Profile profile = profileDao.findByUserId(userId);
        if(profile == null){
            throw new ProfileNotFoundException("Profile not found: " + userId);
        }
        return profile;
    }
}
