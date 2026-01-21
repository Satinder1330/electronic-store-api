package com.electronic.store.services.imageService;

import com.electronic.store.dtos.UserDto;
import com.electronic.store.helper.ImageApiResponse;
import com.electronic.store.services.UserService;
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
@Service("userImageService")
public class UserImageServiceImp implements ImageService {
    @Autowired
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(UserImageServiceImp.class);

    @Override
    public ImageApiResponse uploadImage(MultipartFile file, String path,String userId) throws IOException {
        String originalName = file.getOriginalFilename(); // image can have same names so change it to unique name
        String extension = originalName.substring(originalName.indexOf("."));
        String uniqueRandomId = UUID.randomUUID().toString()+extension;
        String fullPath = path+File.separator+uniqueRandomId;
        UserDto userById = userService.getUserById(userId);
        System.out.println("old image name"+userById.getImageName());
        userById.setImageName(uniqueRandomId);
        userService.updateUser(userById,userId);
        System.out.println("new image name "+userById.getImageName());

        if((extension.equalsIgnoreCase(".jpg"))||(extension.equalsIgnoreCase(".png"))||(extension.equalsIgnoreCase(".jpeg")))
        {
            logger.info("name of the image {}",uniqueRandomId);
            File folder = new File(path);
            if(!folder.exists()){
                folder.mkdirs();
            }
            long copy = Files.copy(file.getInputStream(), Path.of(fullPath));
            logger.info("copied files {}",copy);
            return new ImageApiResponse(userById.getImageName(),"Image uploaded successfully", HttpStatus.OK);
        }else throw new BadRequestException("Image type is invalid");

    }

    @Override
    public InputStream getResource(String path , String imageName) throws FileNotFoundException {
    String finalPath  = path+imageName;
    InputStream inputStream = new FileInputStream(finalPath);

        return inputStream;
    }
}



