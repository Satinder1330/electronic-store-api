package com.electronic.store.controllers;

import com.electronic.store.dtos.UserDto;
import com.electronic.store.helper.CustomExceptionResponse;
import com.electronic.store.helper.CustomPaginationResponse;
import com.electronic.store.helper.ImageApiResponse;
import com.electronic.store.services.imageService.ImageService;
import com.electronic.store.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    private ImageService imageService;

    public UserController(@Qualifier("userImageService") ImageService imageService) {
        this.imageService = imageService;
    }

    @Value("${user.profile.image.path}")
    private  String imageUploadPath;
    @Value("${user.profile.image.get.path}")
    private String imagePath;

    @PostMapping("/create")
    public ResponseEntity<UserDto>CreateUser(@RequestBody @Valid UserDto userDto){
        UserDto user = userService.createUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDto>updateUser(@RequestBody  UserDto userDto ,
                                             @PathVariable("id") String id ){
        UserDto userDto1 = userService.updateUser(userDto, id);
        return new ResponseEntity<>(userDto1,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CustomExceptionResponse>delete(@PathVariable String id) throws IOException {
        CustomExceptionResponse customExceptionResponse = userService.deleteUser(id);
        return new ResponseEntity<>(customExceptionResponse,HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<CustomPaginationResponse<UserDto>>getAll(@RequestParam(value = "pageNumber",defaultValue ="0",required = false ) int pageNumber,
                                                                   @RequestParam(value = "pageSize",defaultValue ="10",required = false) int pageSize,
                                                                   @RequestParam(value = "sortBy",defaultValue ="name",required = false) String sortBy,
                                                                   @RequestParam(value = "sortDir",defaultValue ="asc",required = false) String sortDir)
    {
        CustomPaginationResponse<UserDto> allUser = userService.getAllUser(pageNumber, pageSize, sortBy, sortDir);
        return  new ResponseEntity<>(allUser,HttpStatus.OK);
    }

    @GetMapping("/getSingle/{id}")
    public ResponseEntity<UserDto>getSingleById(@PathVariable String id){
        UserDto userById = userService.getUserById(id);
        return  new ResponseEntity<>(userById,HttpStatus.OK);
    }

    @GetMapping("/getByEmail/{email}")
    public ResponseEntity<UserDto>getByEmail(@PathVariable String email){
        UserDto userByEmail = userService.getUserByEmail(email);
        return new ResponseEntity<>(userByEmail,HttpStatus.OK);
    }
    @GetMapping("/getByName/{name}")
    public ResponseEntity<List<UserDto>>getByName(@PathVariable String name){
        return new ResponseEntity<>(userService.searchUserByName(name),HttpStatus.OK);
    }

    @PostMapping("/uploadImage/{userId}")
    public   ResponseEntity<ImageApiResponse>upload(@PathVariable String userId, @RequestParam("userImage") MultipartFile file) throws IOException {

        imageService.uploadImage(file,imageUploadPath,userId);// @value ,path is in the properties
        UserDto userById = userService.getUserById(userId);
        String imageName = userById.getImageName();
        ImageApiResponse imageApiResponse = new ImageApiResponse(imageName,"Image is uploaded Successfully",HttpStatus.OK);
        return new ResponseEntity<>(imageApiResponse,HttpStatus.OK);

    }

    @GetMapping("/getImage/{userId}")
    public ResponseEntity<HttpServletResponse> getImage(@PathVariable String userId,HttpServletResponse response) throws IOException {
        UserDto userById = userService.getUserById(userId);
        String imageName = userById.getImageName();
        System.out.println("image name is "+imageName);
        InputStream resource = imageService.getResource(imagePath, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
