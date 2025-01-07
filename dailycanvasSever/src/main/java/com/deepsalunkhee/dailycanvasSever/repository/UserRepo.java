package com.deepsalunkhee.dailycanvasSever.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deepsalunkhee.dailycanvasSever.models.UserModel;

public interface UserRepo extends JpaRepository<UserModel,UUID> {
        //custom query to find user by email that may or may not exist
        Optional<UserModel> findByEmail(String email);
}
