package com.huawei.fileshandlingapi.service;

import com.huawei.fileshandlingapi.business.ExcelParsing;
import com.huawei.fileshandlingapi.model.ProductsExcel;
import com.huawei.fileshandlingapi.model.FilesStatus;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.StyledEditorKit;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.huawei.fileshandlingapi.constants.CooperadoresConstants.DETAIL_VIEW;
import static com.huawei.fileshandlingapi.constants.CooperadoresConstants.ISDP;


@Service
public class HandlingFilesService implements IHandlingFilesService {

    private final Path rootLocation = Paths.get("upload-dir");
    private final String BASE_PATH = "upload-dir/";

    private Map<String, List<ProductsExcel>> supplierMap;

    @Override
    public void initDirectory() {
        try {
            if (!Files.isDirectory(rootLocation)) {
                Files.createDirectory(rootLocation);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!");
        }
    }

    @Override
    public void storeFile(MultipartFile file, String dirName) {
        try {
            deleteFile(dirName);
            Files.copy(file.getInputStream(), Paths.get(BASE_PATH + dirName).resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("FAIL!");
        }
    }

    @Override
    public void parseDetailView() {
        supplierMap = ExcelParsing.parseDetailView();
    }

    @Override
    public Map<String, List<ProductsExcel>> processIsdpDv() {
        try {
            return ExcelParsing.processDvIsdp();
        } catch (IOException e) {
            return null;
        } catch (InvalidFormatException e) {
            System.out.println("Invalid Format");
            return null;
        }
    }

    @Override
    public List<ProductsExcel> getSupplier(String supplierName) {

        return supplierMap.get(supplierName);
    }

    @Override
    public FilesStatus getFilesStatus() {
        FilesStatus status = new FilesStatus();

        File folder = new File(BASE_PATH);
        String[] subDirectories = folder.list((dir, name) -> new File(dir, name).isDirectory());

        for(String name: subDirectories) {
            File file = new File(BASE_PATH + name);

            if (name.equalsIgnoreCase(DETAIL_VIEW)) {
                status.setDetailview(file.listFiles().length > 0);
                continue;
            }

            if (name.equalsIgnoreCase(ISDP)) {
                status.setIsdp(file.listFiles().length > 0);
            }
        }

        return status;
    }

    @Override
    public void deleteFile(String dirName) {
        try {
            FileUtils.cleanDirectory(new File(BASE_PATH + dirName + "/"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
