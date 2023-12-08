package com.price.pegging.ServiceImp;

import com.price.pegging.Model.UserDetail;
import com.price.pegging.Entity.User;
import com.price.pegging.Repository.UserRepository;
import com.price.pegging.Service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
@org.springframework.stereotype.Service

public class ServiceImpl implements Service {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> userExist(String userEmail) {

        return userRepository.findUser(userEmail);
    }

    @Override
    public UserDetail passwordMatch(String userPassword, User userDetail) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


        UserDetail commonResponse = new UserDetail();
      //  System.out.println(savePassword);

        if(passwordEncoder.matches(userPassword,userDetail.getPassword()))
        {
            System.out.println("password correct");
            commonResponse.setCode("user login successfully");
            commonResponse.setMsg("0000");
            commonResponse.setUserId(userDetail.getUserId());
            commonResponse.setEmail(userDetail.getEmail());
            commonResponse.setName(userDetail.getName());
        }
        else
        {
            System.out.println("incorrect password");
            commonResponse.setCode("password did not match");
            commonResponse.setMsg("1111");
        }


        return commonResponse;
    }
}