package com.electronic.store.services.imageService;

import com.electronic.store.dtos.CategoryDto;
import com.electronic.store.helper.ImageApiResponse;
import com.electronic.store.services.implimentation.CategoryServiceImp;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service("categoryImageService")
public class CategoryImageServiceImp implements ImageService {

    @Autowired
    private CategoryServiceImp categoryServiceImp;
    Logger logger = LoggerFactory.getLogger(CategoryServiceImp.class);

    @Override
    public ImageApiResponse uploadImage(MultipartFile file, String path, String id) throws IOException {
        CategoryDto categoryDto = categoryServiceImp.getOne(id);
        String coverImage = file.getOriginalFilename();
        String extension = coverImage.substring(coverImage.indexOf("."));
        UUID uuid = UUID.randomUUID();
        String newName = uuid + extension;
        categoryDto.setCoverImage(newName);
        categoryServiceImp.updateCategory(categoryDto, id);
        String fullPath = path + newName;
        if ((extension.equalsIgnoreCase(".jpg")) || (extension.equalsIgnoreCase(".png")) || (extension.equalsIgnoreCase(".jpeg"))) {

            File folder = new File(path);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            long copy = Files.copy(file.getInputStream(), Path.of(fullPath));
            return new ImageApiResponse(categoryDto.getCoverImage(), "Image uploaded successfully", HttpStatus.OK);
        } else throw new BadRequestException("Image type is invalid");

    }@Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String finalPath  = path+name;
        InputStream inputStream = new FileInputStream(finalPath);
        return inputStream;
    }
}



