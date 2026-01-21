package com.electronic.store.services.implimentation;

import com.electronic.store.dtos.CategoryDto;
import com.electronic.store.entities.Category;
import com.electronic.store.exception.ResourceNotFoundExc;
import com.electronic.store.helper.CustomPaginationResponse;
import com.electronic.store.helper.Helper;
import com.electronic.store.repositories.CategoryRepository;
import com.electronic.store.services.CategoryService;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImp implements CategoryService {
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private Helper helper;

    @Value("${category.profile.image.path}")
    private String path;

    Logger logger = LoggerFactory.getLogger(CategoryServiceImp.class);

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        String random = UUID.randomUUID().toString();
        categoryDto.setCategoryId(random);
        Category category = mapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);
        CategoryDto categoryDto1 = mapper.map(savedCategory, CategoryDto.class);
        return categoryDto1;
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundExc("Category of the given id does not exist"));
         if(categoryDto.getTitle()!=null)category.setTitle(categoryDto.getTitle());
         if(categoryDto.getDescription()!=null)category.setDescription(categoryDto.getDescription());
         if(categoryDto.getCoverImage()!=null)category.setCoverImage(categoryDto.getCoverImage());
        Category savedCategory = categoryRepository.save(category);
        CategoryDto categoryDto1 = mapper.map(savedCategory, CategoryDto.class);
        return categoryDto1;
    }

    @Override
    public CategoryDto getOne(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundExc("Category of the given id does not exist"));
        CategoryDto categoryDto = mapper.map(category, CategoryDto.class);
        return categoryDto;
    }

    @Override
    public CustomPaginationResponse<CategoryDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc"))?(Sort.by(sortBy).ascending()):(Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber-1,pageSize,sort); //starts from page 1
        Page<Category> page = categoryRepository.findAll(pageable);
        List<Category> content = page.getContent();
        List<CategoryDto>list= new ArrayList<>();
        content.forEach(category -> {
            list.add(mapper.map(category, CategoryDto.class));
        });
        CustomPaginationResponse<CategoryDto> paginatioResponse = helper.getPaginatioResponse(page, list, pageNumber, pageSize);
        return paginatioResponse;
    }

    @Override
    public void delete(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundExc("Category of the given id does not exist"));
        String coverImage = category.getCoverImage();
        String fullPath = path + coverImage;
        try {
            Path path = Paths.get(fullPath);
            Files.delete(path);
            logger.info("image of the given category is deleted successfully");

        } catch (NoSuchFileException es){
            logger.error("No image is found for this category");
            es.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        categoryRepository.delete(category);
    }
        @Override
        public List<CategoryDto> findAllByTitle (String name){
            List<Category> allByTitle = categoryRepository.findAllByTitle(name);
            List<CategoryDto> list = new ArrayList<>();
            allByTitle.forEach(category ->
                    list.add(mapper.map(category, CategoryDto.class)));

            return list;
        }


    }
