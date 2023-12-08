package com.price.pegging.Service;

import com.price.pegging.Model.UserDetail;
import com.price.pegging.Entity.User;

import java.util.List;
public interface Service {

    public List<User> userExist(String userEmail);

    UserDetail passwordMatch(String userPassword, User userDetail);
}
