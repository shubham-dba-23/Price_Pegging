package com.price.pegging.ServiceImp;

import com.price.pegging.Entity.PricePeggingUpload;
import com.price.pegging.Model.CommonResponse;
import com.price.pegging.Model.UserDetail;
import com.price.pegging.Entity.User;
import com.price.pegging.Repository.PricePeggingRepository;
import com.price.pegging.Repository.UserRepository;
import com.price.pegging.Service.Service;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@org.springframework.stereotype.Service

public class ServiceImpl implements Service {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PricePeggingRepository pricePeggingRepository;

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

    @Override
    public CommonResponse readData(MultipartFile file) {

        List<PricePeggingUpload> peggingUploads=new ArrayList<>();
        String errorMsg="";
        CommonResponse commonResponse=new CommonResponse();
        int count =0;
        try{
            InputStream inputStream= file.getInputStream();
            Workbook workbook= WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator= sheet.iterator();
              rowIterator.next();


            while(rowIterator.hasNext())
            {

                count++;
                Row row = rowIterator.next();
                PricePeggingUpload pricePeggingUpload=new PricePeggingUpload();

                for (int i = 0; i < 13; i++) {
                    Cell cell = row.getCell(i);

                    errorMsg=(cell==null|| cell.getCellType() == CellType.BLANK) ? "file upload error due to row no "+row.getRowNum()+" is empty": "";

                    if(errorMsg.isEmpty())
                    {
                        switch(i)
                        {
                        //    case 0: pricePeggingUpload.setsNo(Long.valueOf(row.getCell(0).toString()));
                        //            System.out.println(Long.valueOf(row.getCell(0).toString()));
                         //   break;
                            case 1:  pricePeggingUpload.setApplicationNo(row.getCell(1).toString());
                            break;
                            case 2:  pricePeggingUpload.setProduct(row.getCell(2).toString());
                            break;
                            case 3:  pricePeggingUpload.setDisbursalDate(row.getCell(3).toString());
                            break;
                            case 4:  pricePeggingUpload.setProperty_address(row.getCell(4).toString());
                            break;
                            case 5:  pricePeggingUpload.setPropertyPincode(row.getCell(5).toString());
                            break;
                            case 6:  pricePeggingUpload.setRegion(row.getCell(6).toString());
                            break;
                            case 7:  pricePeggingUpload.setZone(row.getCell(7).toString());
                            break;
                            case 8:  pricePeggingUpload.setLocation(row.getCell(8).toString());
                            break;
                            case 9:  pricePeggingUpload.setRate_per_sqft(row.getCell(9).toString());
                            break;
                            case 10:  pricePeggingUpload.setProperty_type(row.getCell(10).toString());
                            break;
                            case 11: pricePeggingUpload.setLattitude(row.getCell(11).toString());
                            break;
                            case 12: pricePeggingUpload.setLongitude(row.getCell(12).toString());
                            break;
                        }
                    }
                               peggingUploads.add(pricePeggingUpload);

                                if(!errorMsg.isEmpty())
                                    break;
                }
                               if(!errorMsg.isEmpty())
                                break;
            }

                                  System.out.println(errorMsg);
                                  System.out.println(count);
        }
        catch(Exception e)
        {
            System.out.println(e);
            errorMsg="file is empty or technical issue";
        }

                              if(errorMsg.isEmpty() && count>0) {
                                  pricePeggingRepository.saveAll(peggingUploads);
                                  commonResponse.setCode("0000");
                                  commonResponse.setMsg("file uploaded successfully");
                                }
                             else {
                                if(errorMsg.isEmpty())
                                 {
                                  errorMsg="file is empty or technical issue";
                                  System.out.println(errorMsg);
                                  commonResponse.setCode("1111");
                                  commonResponse.setMsg(errorMsg);
                                   }
                                else {
                                 System.out.println(errorMsg);
                                 commonResponse.setCode("1111");
                                 commonResponse.setMsg(errorMsg);
                                 }
        }

        return commonResponse;
    }
}