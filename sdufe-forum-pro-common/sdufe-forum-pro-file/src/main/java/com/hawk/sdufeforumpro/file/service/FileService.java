package com.hawk.sdufeforumpro.file.service;

import java.io.InputStream;

/**
 * 文件服务
 */
public interface FileService {

    /**
     * 文件上传
     * @param path
     * @param fileStream
     * @return
     */
    public boolean upload(String path, InputStream fileStream);
}
