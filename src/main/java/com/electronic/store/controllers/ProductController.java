package com.electronic.store.controllers;

import com.electronic.store.dtos.ProductDto;
import com.electronic.store.helper.CustomPaginationResponse;
import com.electronic.store.helper.ImageApiResponse;
import com.electronic.store.services.ProductService;
import com.electronic.store.services.imageService.ImageService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

   private ImageService imageService;

    public ProductController(@Qualifier("productImageService") ImageService imageService) {
        this.imageService = imageService;
    }

    @Value("${product.profile.image.path}")
    private String path;

    @PostMapping("/addProduct")
    public ResponseEntity<ProductDto>add(@RequestBody @Valid ProductDto productDto){
        ProductDto productDto1 = productService.addProduct(productDto);
        return new ResponseEntity<>(productDto1, HttpStatus.OK);
    }

    @GetMapping("/getSingle/{id}")
    public ResponseEntity<ProductDto> getSingle(@PathVariable String id){
        ProductDto productDto = productService.getOne(id);
        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<CustomPaginationResponse> getAll(@RequestParam int pageNumber,
                                                           @RequestParam int pageSize,
                                                           @RequestParam String sortBy,
                                                           @RequestParam String sortDir){
        CustomPaginationResponse<ProductDto> response = productService.allProduct(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable String id,
                                         @RequestBody ProductDto productDto){
        ProductDto productDto1 = productService.updateProduct(productDto, id);
        return  new ResponseEntity<>(productDto1,HttpStatus.OK);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable String id){
        productService.deleteProduct(id);
        return new ResponseEntity<>("Product of the given id has been deleted",HttpStatus.OK);
    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<CustomPaginationResponse> getByName( @PathVariable String name,
                                       @RequestParam int pageNumber,
                                       @RequestParam int pageSize,
                                       @RequestParam String sortBy,
                                       @RequestParam String sortDir){
        CustomPaginationResponse<ProductDto> byProductName = productService.getByProductName(name, pageNumber, pageSize, sortBy, sortDir);
    return new ResponseEntity<>(byProductName,HttpStatus.OK);
    }

    @GetMapping("/getAllLive")
    public ResponseEntity<CustomPaginationResponse> getAllLive(@RequestParam int pageNumber,
                                                               @RequestParam int pageSize,
                                                               @RequestParam String sortBy,
                                                               @RequestParam String sortDir){

        CustomPaginationResponse<ProductDto> response = productService.allLive(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(response,HttpStatus.OK);

    }

    @PostMapping("/uploadImage/{id}")
    public  ResponseEntity<ImageApiResponse> uploadImage(@PathVariable String id,
                                         @RequestParam("image") MultipartFile file) throws IOException {
        ImageApiResponse imageApiResponse = imageService.uploadImage(file, path, id);
        return  new ResponseEntity<>(imageApiResponse,HttpStatus.OK);
    }

    @GetMapping("/getImage/{id}")
    public ResponseEntity<HttpServletResponse> getImage(@PathVariable String id,
                                     HttpServletResponse response) throws IOException {
    String productImage = productService.getOne(id).getProductImage();
    InputStream resource = imageService.getResource(path, productImage);
    response.setContentType(MediaType.IMAGE_JPEG_VALUE);
    StreamUtils.copy(resource,response.getOutputStream());
    return new ResponseEntity<>(response,HttpStatus.OK);
}

   // add the categories in the json directly in a product
    @PostMapping("/addWithCategoryIds")
    public ResponseEntity<ProductDto> addWithCategoryIds(@RequestBody @Valid ProductDto productDto){
    ProductDto productDto1 = productService.addProductWithCategory(productDto);
    return new ResponseEntity<>(productDto1,HttpStatus.OK);
    }
}
