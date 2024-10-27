package com.hawk.sdufeforumpro.file.service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * mock文件服务
 */
@Slf4j
@Setter
public class MockFileServiceImpl implements FileService {

    @Override
    public boolean upload(String path, InputStream fileStream) {
        return true;
    }
}
