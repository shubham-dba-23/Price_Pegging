package com.price.pegging.Controller;

import com.price.pegging.Model.CommonResponse;
import com.price.pegging.Model.UserDetail;
import com.price.pegging.Entity.User;
import com.price.pegging.Service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class Controller {

    @Autowired
    private Service service;

    @PostMapping("/loginValidation")
    public ResponseEntity<UserDetail> loginAuthentication(@RequestBody User userRequest)
    {

        List<User> userDetail=null;
        UserDetail commonResponse= new UserDetail();
        String userEmail=userRequest.getEmail();
        String userPassword=userRequest.getPassword();

try {
    if (!userEmail.isEmpty() && userEmail.contains("@shubham") && !userPassword.isEmpty()) {
        userDetail = service.userExist(userEmail);

        if (!CollectionUtils.isEmpty(userDetail)) {
            // System.out.print(userDetail.get(0).getPassword());
            commonResponse = service.passwordMatch(userPassword, userDetail.get(0));

        } else {
            System.out.println("invalid email");
            commonResponse.setCode("1111");
            commonResponse.setMsg("user does not exist");
        }
    } else {
        System.out.println("invalid email");
        commonResponse.setCode("1111");
        commonResponse.setMsg("invalid user email");
    }
}
catch (Exception e)
{
    System.out.println(e);
}


        return new ResponseEntity<UserDetail>(commonResponse, HttpStatus.OK);
    }


    @PostMapping("/pricePeggingUpload")
    public ResponseEntity<CommonResponse> fileUpload(@RequestParam("file") MultipartFile file)
    {
        CommonResponse commonResponse=new CommonResponse();
        commonResponse=service.readData(file);

        return new ResponseEntity<CommonResponse>(commonResponse, HttpStatus.OK);
    }


}
