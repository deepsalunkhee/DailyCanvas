package com.deepsalunkhee.dailycanvasSever.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deepsalunkhee.dailycanvasSever.models.UserModel;
import com.deepsalunkhee.dailycanvasSever.repository.UserRepo;

@Service
public class UserServices {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(UserServices.class);
    @Autowired
    private UserRepo userRepo;



    public  Optional<UserModel> getUserByEmail(String email) {
        logger.info("Fetching user with email: {}", email);
        Optional<UserModel> user = userRepo.findByEmail(email);
        if (user.isPresent()) {
            logger.info("User found: {}", user.get());
        } else {
            logger.warn("No user found with email: {}", email);
        }
        return user;
    }

    public UserModel saveUser(UserModel user) {
        return userRepo.save(user);
    }


}
