package com.electronic.store.services.implimentation;

import com.electronic.store.dtos.ProductDto;
import com.electronic.store.entities.Category;
import com.electronic.store.entities.Product;
import com.electronic.store.exception.ResourceNotFoundExc;
import com.electronic.store.helper.CustomPaginationResponse;
import com.electronic.store.helper.Helper;
import com.electronic.store.repositories.CategoryRepository;
import com.electronic.store.repositories.ProductRepository;
import com.electronic.store.services.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class ProductServiceImp implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private Helper helper;

    @Value("${product.profile.image.path}")
    private String path;

    Logger logger = LoggerFactory.getLogger(ProductServiceImp.class);

    @Override
    public ProductDto addProduct(ProductDto productDto) {
        String random = UUID.randomUUID().toString();
        productDto.setProductId(random);
        Product product = mapper.map(productDto, Product.class);
        Product product1 = productRepository.save(product);
        ProductDto map = mapper.map(product1, ProductDto.class);
        return map;
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundExc("Product of the given id does not exist!"));
       if(productDto.getProductName()!=null)product.setProductName(productDto.getProductName());
       if (productDto.getProductDescription()!=null)product.setProductDescription(productDto.getProductDescription());
       if (productDto.getProductImage()!=null)product.setProductImage(productDto.getProductImage());
       if (productDto.getLive()!=null)product.setLive(productDto.getLive());
       if (productDto.getStock()!=null)product.setStock(productDto.getStock());
       if (productDto.getPrice()!=null)product.setPrice(productDto.getPrice());
       if (productDto.getAddedDate()!=null)product.setAddedDate(productDto.getAddedDate());
       if (productDto.getQuantity()!=null)product.setQuantity(productDto.getQuantity());
       if (productDto.getDiscountedPrice()!=null)product.setDiscountedPrice(productDto.getDiscountedPrice());
       if(productDto.getCategoryIds()!=null){
           List<Category> categories = new ArrayList<>();
           Set<String> categoryIds = productDto.getCategoryIds();
           categoryIds.forEach((id)->{
               Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundExc("Category of the given id is not present!"));
             categories.add(category);
           });
           product.setCategories(categories);
       };
        Product saved = productRepository.save(product);
        ProductDto map = mapper.map(product, ProductDto.class);
        helper.categoriesInfoFromProduct(product,map);
       return map;
    }

    @Override
    public void deleteProduct(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundExc("Product of the given id does not exist!"));
        String productImage = product.getProductImage();
        String fullPath= path+productImage;
        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
        }catch (FileNotFoundException ex){
            logger.error("Image of the given product is not available to delete!");
            ex.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        productRepository.delete(product);
    }

    @Override
    public ProductDto getOne(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundExc("Product of the given id does not exist!"));
        ProductDto map = mapper.map(product, ProductDto.class);
        helper.categoriesInfoFromProduct(product, map);
        return map;
    }

    @Override
    public CustomPaginationResponse<ProductDto> allProduct(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepository.findAll(pageable);
        List<Product> content = page.getContent();
        List<ProductDto>list= new ArrayList<>();
        content.forEach(product -> {
            ProductDto map = mapper.map(product, ProductDto.class);
            helper.categoriesInfoFromProduct(product,map);
            list.add(map);
        });

        CustomPaginationResponse<ProductDto> paginatioResponse = helper.getPageableResponse(page, list, pageNumber, pageSize);
        return paginatioResponse;
    }

    @Override
    public CustomPaginationResponse<ProductDto>getByProductName(String subName,int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepository.findByProductNameContainingIgnoreCase(subName,pageable);
        List<Product> content = page.getContent();
        List<ProductDto>list= new ArrayList<>();
        content.forEach(product -> {
            ProductDto map = mapper.map(product, ProductDto.class);
            helper.categoriesInfoFromProduct(product,map);
            list.add(map);
        });

        CustomPaginationResponse<ProductDto> paginatioResponse = helper.getPageableResponse(page, list, pageNumber, pageSize);
        return paginatioResponse;
    }

    @Override
    public CustomPaginationResponse<ProductDto>allLive(int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Product> page = productRepository.findByLiveTrue(pageable);
        List<Product> content = page.getContent();
        List<ProductDto>list= new ArrayList<>();
        content.forEach(product -> {
            ProductDto map = mapper.map(product, ProductDto.class);
            helper.categoriesInfoFromProduct(product,map);
            list.add(map);
        });
        CustomPaginationResponse<ProductDto> paginatioResponse = helper.getPageableResponse(page, list, pageNumber, pageSize);
        return paginatioResponse;
    }

    @Override
    public ProductDto addProductWithCategory(ProductDto productDto) {
        Set<String>ids=new HashSet<>();
        Set<String>names=new HashSet<>();
        List<Category>categories=new ArrayList<>();
        Set<String> categoryIds = productDto.getCategoryIds();
        categoryIds.forEach(id->{
            Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundExc("Category of the given id is not present!"));
           categories.add(category);
            ids.add(category.getCategoryId());
           names.add(category.getTitle());
        });
        String random = UUID.randomUUID().toString();
        productDto.setProductId(random);
        productDto.setCategoryIds(ids);
        productDto.setCategoryNames(names);
        Product product = mapper.map(productDto, Product.class);
        product.setCategories(categories);
        Product product1 = productRepository.save(product);
        ProductDto map = mapper.map(product1, ProductDto.class);
        List<Category> categories1 = product1.getCategories();
        categories1.forEach(category -> {
            map.setCategoryNames(names);
            map.setCategoryIds(ids);
        });
        return map;
    }

   // add product in a category
    @Override
    public ProductDto addProductInCategory(String categoryId, ProductDto productDto) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundExc("Category of the given id is not present!"));
        String random = UUID.randomUUID().toString();
        productDto.setProductId(random);
        Product product = mapper.map(productDto, Product.class);
        List<Category>categories=new ArrayList<>();
        categories.add(category);
       product.setCategories(categories);
        productRepository.save(product);
        ProductDto map = mapper.map(product, ProductDto.class);
        helper.categoriesInfoFromProduct(product,map);
       return map;
    }

    //assign savedProduct to a category
    @Override
    public ProductDto assignProductToCategory(String categoryId, String productId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundExc("Category of the given id is not present!"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundExc("Product of the given id does not exist!"));
       List<Category>categories=new ArrayList<>();
       categories.add(category);
        product.setCategories(categories);
        productRepository.save(product);
        ProductDto map = mapper.map(product, ProductDto.class);
        helper.categoriesInfoFromProduct(product,map);
        return map;

    }

// to get list of products in a category
    @Override
    public CustomPaginationResponse<ProductDto> allProductsInACategory(String categoryId,int pageNumber,int pageSize,String sortBy,String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepository.findAllByCategories_CategoryId( pageable,categoryId);
        List<Product> content = page.getContent();
        List<ProductDto> list = new ArrayList<>();
        content.forEach(product -> {
            ProductDto map = mapper.map(product, ProductDto.class);
            helper.categoriesInfoFromProduct(product,map);
            list.add(map);

        });
        CustomPaginationResponse<ProductDto> paginatioResponse = helper.getPageableResponse(page, list, pageNumber, pageSize);
        return paginatioResponse;
    }

}
