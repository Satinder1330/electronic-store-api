package com.electronic.store.services.imageService;

import com.electronic.store.dtos.ProductDto;
import com.electronic.store.helper.ImageApiResponse;
import com.electronic.store.repositories.ProductRepository;
import com.electronic.store.services.ProductService;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service("productImageService")
public class ProductImageServiceImp implements  ImageService{

    @Autowired
    private ProductService productService;



//    @Value("product.profile.image.path")
//    private String path;

    @Override
    public ImageApiResponse uploadImage(MultipartFile file, String path, String id) throws IOException {
        ProductDto productDto = productService.getOne(id);
        String coverImage = file.getOriginalFilename();
        String random = UUID.randomUUID().toString();
        String extension = coverImage.substring(coverImage.indexOf("."));
        String newName= random+extension;
        productDto.setProductImage(newName);
        productService.updateProduct(productDto,id);
        String fullPath=path+newName;
        if(extension.equalsIgnoreCase(".jpg")||extension.equalsIgnoreCase(".jpeg")||extension.equalsIgnoreCase(".png")){
            File folder = new File(path);
            if(!folder.exists()){
                folder.mkdirs();
            }
            Files.copy(file.getInputStream(), Path.of(fullPath));
            return  new ImageApiResponse(newName,"image is uploaded successfully", HttpStatus.OK);
        }else throw new BadRequestException("image type is not valid");

    }

    @Override
    public InputStream getResource(String path, String imageName) throws FileNotFoundException {
       String fullPath = path+imageName;
       InputStream inputStream = new FileInputStream(fullPath);

        return inputStream;
    }
}
