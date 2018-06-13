package com.huawei.fileshandlingapi;

import com.huawei.fileshandlingapi.service.IHandlingFilesService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class FilesHandlingApiApplication implements CommandLineRunner {

    @Resource
    IHandlingFilesService handlingFilesService;

    public static void main(String[] args) {
        SpringApplication.run(FilesHandlingApiApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        handlingFilesService.initDirectory();
    }
}
