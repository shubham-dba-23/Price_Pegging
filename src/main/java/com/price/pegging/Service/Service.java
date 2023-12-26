package com.price.pegging.Service;

import com.price.pegging.Model.CommonResponse;
import com.price.pegging.Model.ExportModel;
import com.price.pegging.Model.PricePeggingData;
import com.price.pegging.Model.UserDetail;
import com.price.pegging.Entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
public interface Service {

    public List<User> userExist(String userEmail);

    UserDetail passwordMatch(String userPassword, User userDetail);

    CommonResponse      readDataDsa(MultipartFile file);

    CommonResponse peggingFileReadData(MultipartFile file);

    ExportModel getAllExportData(String applicationNo, String uploadDate);

    PricePeggingData getAllPricePeggingData(String zone, String uploadDate);

    List getAllZone();


}
