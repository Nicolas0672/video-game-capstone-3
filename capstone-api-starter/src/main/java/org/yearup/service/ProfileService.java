package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.data.mysql.MySqlProfileDao;
import org.yearup.models.Profile;

@Service
public class ProfileService {
    private final MySqlProfileDao profileDao;

    public ProfileService(MySqlProfileDao profileDao) {
        this.profileDao = profileDao;
    }

    public Profile create(Profile profile){
        return profileDao.create(profile);
    }

    public Profile update(Profile profile, int userId){
        return profileDao.update(profile, userId);
    }

    public Profile findByUserId(int userId){
        return profileDao.findByUserId(userId);

    }
}
