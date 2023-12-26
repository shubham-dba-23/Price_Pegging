package com.price.pegging.ServiceImp;

import com.price.pegging.Entity.DsaExport;
import com.price.pegging.Entity.PricePegging;
import com.price.pegging.FileUtilittyValidation;
import com.price.pegging.Model.CommonResponse;
import com.price.pegging.Model.ExportModel;
import com.price.pegging.Model.PricePeggingData;
import com.price.pegging.Model.UserDetail;
import com.price.pegging.Entity.User;
import com.price.pegging.Repository.DsaExportRepository;
import com.price.pegging.Repository.PricePeggingRepository;
import com.price.pegging.Repository.UserRepository;
import com.price.pegging.Service.Service;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private DsaExportRepository dsaExportRepository;
    @Autowired
    private PricePeggingRepository pricePeggingRepository;
    @Autowired
    private FileUtilittyValidation fileUtilittyValidation;

    @Override
    public List<User> userExist(String userEmail) {


        return userRepository.findUser(userEmail);
    }

    @Override
    public UserDetail passwordMatch(String userPassword, User userDetail) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


        UserDetail commonResponse = new UserDetail();
        //  System.out.println(savePassword);

        if (passwordEncoder.matches(userPassword, userDetail.getPassword())) {
            System.out.println("password correct");
            commonResponse.setCode("0000");
            commonResponse.setMsg("Login successfully");
            commonResponse.setUserId(userDetail.getUserId());
            commonResponse.setEmail(userDetail.getEmail());
            commonResponse.setName(userDetail.getName());
        } else {
            System.out.println("Incorrect password");
            commonResponse.setCode("1111");
            commonResponse.setMsg("Password did not matched");
        }


        return commonResponse;
    }

    @Override
    public CommonResponse readDataDsa(MultipartFile file) {

        List<DsaExport> dsaExports = new ArrayList<>();
        String errorMsg = "";
        CommonResponse commonResponse = new CommonResponse();
        int count = 0;

        try {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            Boolean fileFormat = true;
            Row headerRow = rowIterator.next();

            fileFormat = fileUtilittyValidation.dsaFileFormat(headerRow);

            if (fileFormat) {

                System.out.println("file format matched");

                while (rowIterator.hasNext()) {

                    count++;
                    Row row = rowIterator.next();
                    DsaExport dsaExport = new DsaExport();


                    for (int i = 0; i < 13; i++) {
                        Cell cell = row.getCell(i);

                        errorMsg = (cell == null || cell.getCellType() == CellType.BLANK) ? "file upload error due to row no " + (row.getRowNum() + 1) + " is empty" : "";

                        if (errorMsg.isEmpty()) {
                            System.out.println("value=" + cell.toString());

                            switch (i) {

                                case 1:
                                    dsaExport.setApplicationNo(row.getCell(1).toString());
                                    break;
                                case 2:
                                    dsaExport.setProduct(row.getCell(2).toString());
                                    break;
                                case 3:
                                    dsaExport.setDisbursalDate(row.getCell(3).toString());
                                    break;
                                case 4:
                                    dsaExport.setProperty_address(row.getCell(4).toString());
                                    break;
                                case 5:
                                    dsaExport.setPropertyPincode(row.getCell(5).toString().replace(".0", ""));
                                    break;
                                case 6:
                                    dsaExport.setRegion(row.getCell(6).toString());
                                    break;
                                case 7:
                                    dsaExport.setZone(row.getCell(7).toString());
                                    break;
                                case 8:
                                    dsaExport.setLocation(row.getCell(8).toString());
                                    break;
                                case 9:
                                    dsaExport.setRate_per_sqft(row.getCell(9).toString().replace(".0", ""));

                                    break;
                                case 10:
                                    dsaExport.setProperty_type(row.getCell(10).toString());
                                    break;
                                case 11:
                                    dsaExport.setLattitude(row.getCell(11).toString());
                                    break;
                                case 12:
                                    dsaExport.setLongitude(row.getCell(12).toString());
                                    break;
                            }


                        }
                        if (!errorMsg.isEmpty())
                            break;
                    }
                    if (!errorMsg.isEmpty())
                        break;
                    dsaExports.add(dsaExport);

                }

            } else {
                //   System.out.println("file format is not matched");
                errorMsg = "file format is not matching or technical issue.";
            }

            System.out.println(errorMsg);
            //System.out.println(count);
        } catch (Exception e) {
            System.out.println(e);
            errorMsg = "file is empty or technical issue.";
        }

        if (errorMsg.isEmpty() && count > 0) {
            dsaExportRepository.saveAll(dsaExports);
            commonResponse.setCode("0000");
            commonResponse.setMsg("file uploaded successfully " + dsaExports.size() + " row uploaded.");
        } else {
            if (errorMsg.isEmpty()) {
                errorMsg = "file is empty or technical issue";
                System.out.println(errorMsg);
                commonResponse.setCode("1111");
                commonResponse.setMsg(errorMsg);
            } else {
                System.out.println(errorMsg);
                commonResponse.setCode("1111");
                commonResponse.setMsg(errorMsg);
            }
        }

        return commonResponse;
    }

    @Override
    public CommonResponse peggingFileReadData(MultipartFile file) {
        List<PricePegging> peggingUploads = new ArrayList<>();
        String errorMsg = "";
        CommonResponse commonResponse = new CommonResponse();
        int count = 0;

        try {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            // rowIterator.next();
            Row headerRow = rowIterator.next();
            Boolean fileFormat = true;

            fileFormat = fileUtilittyValidation.pricePeggingFileFormat(headerRow);
            System.out.println("true/false " + fileFormat);
            if (fileFormat) {


                while (rowIterator.hasNext()) {

                    count++;
                    Row row = rowIterator.next();
                    PricePegging pricePeggingUpload = new PricePegging();

                    for (int i = 0; i < 6; i++) {

                        Cell cell = row.getCell(i);

                        errorMsg = (cell == null || cell.getCellType() == CellType.BLANK) ? "file upload error due to row no " + (row.getRowNum() + 1) + " is empty" : "";


                        if (errorMsg.isEmpty()) {
                            switch (i) {
                                //    case 0: pricePeggingUpload.setsNo(Long.valueOf(row.getCell(0).toString()));
                                //            System.out.println(Long.valueOf(row.getCell(0).toString()));
                                //   break;
                                case 0:
                                    pricePeggingUpload.setZone(row.getCell(0).toString());
                                    break;
                                case 1:
                                    pricePeggingUpload.setLocations(row.getCell(1).toString());
                                    break;
                                case 2:
                                    pricePeggingUpload.setMinimumRate(row.getCell(2).toString());
                                    break;
                                case 3:
                                    pricePeggingUpload.setMaximumRate(row.getCell(3).toString());
                                    break;
                                case 4:
                                    pricePeggingUpload.setAverageRate(row.getCell(4).toString());
                                    break;
                                case 5:
                                    pricePeggingUpload.setPinCode(row.getCell(5).toString().replace(".0", ""));
                                    break;
                            }
                        }

                        if (!errorMsg.isEmpty())
                            break;
                    }
                    if (!errorMsg.isEmpty())
                        break;
                    peggingUploads.add(pricePeggingUpload);

                }
            } else {
                System.out.println("file format is not matched");
                errorMsg = "file format is not matching or technical issue.";
            }

            System.out.println(errorMsg);
            System.out.println(count);
        } catch (Exception e) {
            System.out.println(e);
            errorMsg = "file is empty or technical issue";
        }

        if (errorMsg.isEmpty() && count > 0) {
            pricePeggingRepository.saveAll(peggingUploads);
            commonResponse.setCode("0000");
            commonResponse.setMsg("file uploaded successfully " + peggingUploads.size() + " row uploaded.");
        } else {
            if (errorMsg.isEmpty()) {
                errorMsg = "file is empty or technical issue";
                System.out.println(errorMsg);
                commonResponse.setCode("1111");
                commonResponse.setMsg(errorMsg);
            } else {
                System.out.println(errorMsg);
                commonResponse.setCode("1111");
                commonResponse.setMsg(errorMsg);
            }
        }

        return commonResponse;
    }

    @Override
    public ExportModel getAllExportData(String applicationNo, String uploadDate) {
        List<DsaExport> exportsData = new ArrayList<>();
        ExportModel exportModel = new ExportModel();


        Long count;
        int pageNumber = 0;
        int pageSize = 3;

        Page<DsaExport> exportDataPage = null;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        if (applicationNo != null && uploadDate != null) {
            exportDataPage = dsaExportRepository.findByApplicationNoAndUploadDate(applicationNo, uploadDate, pageable);
            exportsData = exportDataPage.getContent();
            count = dsaExportRepository.countByApplicationNoAndUploadDate(applicationNo, uploadDate);
            System.out.println("Count" +" " +count);
        } else if (applicationNo != null) {

            exportDataPage = dsaExportRepository.findByApplicationNo(applicationNo, pageable);
            exportsData = exportDataPage.getContent();
            count = dsaExportRepository.countByApplicationNo(applicationNo);
            System.out.println("count" +" "+ count);
        } else if (uploadDate != null) {
            exportDataPage = dsaExportRepository.findByUploadDate(uploadDate, pageable);
            exportsData = exportDataPage.getContent();
            count = dsaExportRepository.countByUploadDate(uploadDate);
            System.out.println("count" +" " +count);
        } else {
            exportDataPage = dsaExportRepository.findAll(pageable);
            exportsData = exportDataPage.getContent();
            count = dsaExportRepository.countByfindAll();
            System.out.println("count" +" "+ count);

        }
        if (exportsData.isEmpty()) {
            exportModel.setCode("1111");
            exportModel.setMsg("Data not found");
            exportModel.setDsaExportList(null);
        } else {
            exportModel.setCode("0000");
            exportModel.setMsg("Data found successfully");
            exportModel.setDsaExportList(exportsData);
            exportModel.setTotalCount(String.valueOf(count));
        }

        return exportModel;
    }

    /**
     * @param zone
     * @return
     */
    @Override
    public PricePeggingData getAllPricePeggingData(String zone, String uploadDate) {
        List<PricePegging> pricePeggings = new ArrayList<>();
        PricePeggingData pricePeggingData = new PricePeggingData();

        Long count;
        int pageNumber = 0;
        int pageSize = 100;

        Page<PricePegging> pricePeggingPage = null;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        if (zone != null && uploadDate != null) {
            pricePeggingPage = (Page<PricePegging>) pricePeggingRepository.findByZoneAndUploadDate(zone, uploadDate, pageable);
            pricePeggings = pricePeggingPage.getContent();
            count = pricePeggingRepository.countByZoneAndUploadDate(zone, uploadDate);
            System.out.println("Count" + count);


        } else if (zone != null) {
            pricePeggingPage = pricePeggingRepository.findByZone(zone, pageable);
            pricePeggings = pricePeggingPage.getContent();
            count = pricePeggingRepository.countByZone(zone);
            System.out.println("Count" + count);

        } else if (uploadDate != null) {
            pricePeggingPage = pricePeggingRepository.findByUploadDate(uploadDate, pageable);
            pricePeggings = pricePeggingPage.getContent();
            count = pricePeggingRepository.countByUploadDate(uploadDate);
            System.out.println("Count" + count);


        } else {
            pricePeggingPage = pricePeggingRepository.findAll(pageable);
            pricePeggings = pricePeggingPage.getContent();
            count = pricePeggingRepository.countByfindAll();
            System.out.println("Count" + count);
        }
        if (pricePeggings.isEmpty()) {
            pricePeggingData.setCode("1111");
            pricePeggingData.setMsg("Data not found");
            pricePeggingData.setPricePeggingList(null);
        } else {
            pricePeggingData.setCode("0000");
            pricePeggingData.setMsg("Data found successfully");
            pricePeggingData.setPricePeggingList(pricePeggings);
            pricePeggingData.setTotalCount(String.valueOf(count));
        }

        return pricePeggingData;
//    }

        /**
         * @return
         */


        /**
         * @return
         */
//    @Override
//    public List getAllZone() {
//        List zones = null;
//
//        zones = pricePeggingRepository.getUniqeZones();
//        return zones;
//    }
//}
    }

    /**
     * @return
     */
    @Override
    public List getAllZone() {
        return null;
    }
}
