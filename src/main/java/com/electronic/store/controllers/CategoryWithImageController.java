package com.electronic.store.controllers;
import com.electronic.store.dtos.CategoryDto;
import com.electronic.store.services.CategoryService;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;


// Extra controller  to add JSON and image together at the same time -just for extra practice

@RestController
@RequestMapping("/cat")
public class CategoryWithImageController {
    @Autowired
    CategoryService categoryService;

    Logger logger = LoggerFactory.getLogger(CategoryWithImageController.class);

    @Value("${category.profile.image.path}")
    private String imagePath;

@PostMapping(value = "/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public String add(@RequestPart("category") String categoryJson, @RequestPart("image") MultipartFile file) throws IOException {
    ObjectMapper mapper = new ObjectMapper();// because String from postman , if JSON then no need
    CategoryDto categoryDto =
            mapper.readValue(categoryJson, CategoryDto.class);
    CategoryDto categoryDto1 = categoryService.addCategory(categoryDto);
    String categoryId = categoryDto1.getCategoryId();
    String originalName = file.getOriginalFilename(); // image can have same names so change it to unique name
    String extension = originalName.substring(originalName.indexOf("."));
    String uniqueRandomId = UUID.randomUUID().toString()+extension;
    String fullPath = imagePath+ File.separator+uniqueRandomId;
    CategoryDto categoryDto2 = categoryService.getOne(categoryId);
   categoryDto2.setCoverImage(uniqueRandomId);
   categoryService.updateCategory(categoryDto2,categoryId);

    if((extension.equalsIgnoreCase(".jpg"))||(extension.equalsIgnoreCase(".png"))||(extension.equalsIgnoreCase(".jpeg")))
    {
        File folder = new File(imagePath);
        if(!folder.exists()){
            folder.mkdirs();
        }
        long copy = Files.copy(file.getInputStream(), Path.of(fullPath));
return "added";
    }else throw new BadRequestException("Image type is invalid");

}



}
