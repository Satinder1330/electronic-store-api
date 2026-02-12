package com.electronic.store.controllerTest;

import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.Role;
import com.electronic.store.entities.User;
import com.electronic.store.helper.CustomExceptionResponse;
import com.electronic.store.helper.CustomPaginationResponse;
import com.electronic.store.helper.ImageApiResponse;
import com.electronic.store.services.UserService;
import com.electronic.store.services.imageService.ImageService;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.web.JsonPath;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    @MockitoBean
    private UserService userService;
    @MockitoBean(name = "userImageService")
    private ImageService imageService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ModelMapper mapper;

    Role role;
    User user;
    User user1;
  UserDto userDto;
  UserDto userDto1;
  List<UserDto>list;
    @BeforeEach
        public  void init() {
            role = Role.builder().name("ROLE_USER").roleId("123").build();
            user = User.builder()
                    .userId("423").name("name")
                    .email("email123@gmail.com").password("name").gender("male").imageName("img123.jpg").roles(List.of(role)).build();
            userDto = mapper.map(user, UserDto.class);
        user1 = User.builder()
                .userId("4w3").name("matt")
                .email("matt123@gmail.com").password("matt").gender("male").imageName("img1223.jpg").roles(List.of(role)).build();
        userDto1 = mapper.map(user1, UserDto.class);
        list= List.of(userDto,userDto1);

    }

    @Test
    public void createUserTest() throws Exception {

        Mockito.when(userService.createUser(Mockito.any())).thenReturn(userDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectToJsonStringConvertor(userDto))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect( jsonPath("$.name").exists());

    }
    @Test
    public void updateUserTest() throws Exception {
        String id = "23rr43";
        userDto.setName("Mathew");
Mockito.when(userService.updateUser(Mockito.any(UserDto.class),Mockito.anyString())).thenReturn(userDto);
mockMvc.perform(MockMvcRequestBuilders.put("/user/update/"+id)
 // if security filters Active
                .header(HttpHeaders.AUTHORIZATION,"Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbDEyM0BnbWFpbC5jb20iLCJpYXQiOjE3NzA4MzE3MTAsImV4cCI6MTc3MDgzMzUxMH0.2dEQMywJ4-Ks-4b0LlaT1tEx6SOE11rGjydThJSWHpQ")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectToJsonStringConvertor(userDto))
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Mathew"));
    }
    @Test
    public void deleteTest() throws Exception {
        String id ="31242";
        Mockito.when(userService.deleteUser(id)).thenReturn(new CustomExceptionResponse("deleted", HttpStatus.OK));
        mockMvc.perform(MockMvcRequestBuilders.delete("/user/delete/"+id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("deleted"));

    }
    @Test
    public void getAllTest() throws Exception {
        CustomPaginationResponse<UserDto> build = new CustomPaginationResponse<>();
        build.setContent(list);build.setPageNumber(0);build.setPageSize(2);build.setLastPage(true);
        build.setTotalElements(2);build.setTotalPages(1);
        Mockito.when(userService.getAllUser(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString(),Mockito.anyString())).thenReturn(build);
        mockMvc.perform(MockMvcRequestBuilders.get("/user/getAll")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void getSingleByIdTest() throws Exception {
        String id = "32r23";
Mockito.when(userService.getUserById(Mockito.anyString())).thenReturn(userDto);
mockMvc.perform(MockMvcRequestBuilders.get("/user/getSingle/"+id)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
    }
    @Test
    public void uploadImageTest() throws Exception {
        String id = "deqwe2";
        MockMultipartFile file= new MockMultipartFile("userImage",
                "profile.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "image content".getBytes());
        ImageApiResponse response = ImageApiResponse.builder().imageName("abc.jpg").message("uploaded successfully").status(HttpStatus.OK).build();
        Mockito.when(imageService.uploadImage(Mockito.any(),Mockito.anyString(),Mockito.anyString())).thenReturn(response);
        Mockito.when(userService.getUserById(Mockito.anyString())).thenReturn(userDto);
        mockMvc.perform(MockMvcRequestBuilders.multipart("/user/uploadImage/{id}", id)
                .file(file)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON,MediaType.IMAGE_JPEG))
                .andDo(print())
                .andExpect(status().isOk())
               .andExpect(jsonPath("$.imageName").value("img123.jpg"));
    }


    private String objectToJsonStringConvertor(UserDto userDto) {
        try {
            return new ObjectMapper().writeValueAsString(userDto);
        }catch (Exception e){
            e.printStackTrace();
        }return null;
    }

}
