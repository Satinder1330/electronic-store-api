package com.electronic.store.services.imageService;

import com.electronic.store.helper.ImageApiResponse;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public interface ImageService {

    ImageApiResponse uploadImage(MultipartFile file, String path,String id) throws IOException;
    InputStream getResource(String path, String name) throws FileNotFoundException;
}
