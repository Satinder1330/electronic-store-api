package com.electronic.store.serviceTest;

import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.Role;
import com.electronic.store.entities.User;
import com.electronic.store.helper.CustomPaginationResponse;
import com.electronic.store.repositories.RoleRepository;
import com.electronic.store.repositories.UserRepository;
import com.electronic.store.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
@MockitoBean
private RoleRepository roleRepository;
@MockitoBean
private UserRepository userRepository;
@Autowired
private ModelMapper mapper;

User user;
Role role;
User user1;
User user2;
List<User> list;
List<User> sameNameList;
@BeforeEach
public  void init(){
    role = Role.builder().name("ROLE_USER").roleId("123").build();
    user = User.builder()
            .name("name")
            .email("email123@gmail.com").password("name").gender("male").imageName("img123.jpg").roles(List.of(role)).build();
     user1 = User.builder()
            .name("abc")
            .email("email123@gmail.com").password("name").gender("male").imageName("img123.jpg").roles(List.of(role)).build();
    user2 = User.builder()
            .name("name")
            .email("email13233@gmail.com").password("name1").gender("male").imageName("img1223.jpg").roles(List.of(role)).build();
     list = List.of(user1,user);
     sameNameList=List.of(user,user2);
}

    @Test
    public void createUserTest(){
    Mockito.when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(role));
    Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        UserDto map = mapper.map(user, UserDto.class);
        UserDto userDto = userService.createUser(map);
       // Assertions.assertEquals(map,userDto);
        Assertions.assertNotNull(userDto);
        System.out.println("user _"+user.toString());
    }
    @Test
    public void updateUserTest(){
        UserDto userDto = UserDto.builder().name("hello").gender("male").imageName("img44.jpg").build();
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(user));
    Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        UserDto userDto1 = userService.updateUser(userDto, "566");
        System.out.println(userDto1.getName());
        System.out.println(userDto.getName());
        Assertions.assertEquals(userDto.getName(),userDto1.getName());

    }

    @Test
    public void deleteUserTest() throws IOException {
    String userId="123";
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        userService.deleteUser(userId);
        Mockito.verify(userRepository,Mockito.times(1)).delete(user);
    }
    @Test
    public void getAllUserTest(){
        Page<User>page = new PageImpl<>(list);
    Mockito.when(userRepository.findAll((Pageable) Mockito.any())).thenReturn(page);
        CustomPaginationResponse<UserDto> allUser = userService.getAllUser(0, 2, "name", "Asc");
        long totalElements = allUser.getContent().size();
        long expectedElements = 2;
        System.out.println(totalElements+" "+expectedElements);
        Assertions.assertEquals(expectedElements,totalElements);
    }
    @Test
    public void getUserByIdTest(){
    String id ="213";
    Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
        UserDto userById = userService.getUserById(id);
        System.out.println(user.getName()+" "+userById.getName());
        Assertions.assertEquals(user.getName(),userById.getName());
    }
    @Test
    public void getUserByIdEmail(){
        String email ="email.com";
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        UserDto userByEmail = userService.getUserByEmail(email);
        System.out.println(user.getName()+" "+userByEmail.getName());
        Assertions.assertEquals(user.getName(),userByEmail.getName(),"Name Not Matched!!");
    }
    @Test
    public void searchUserByName(){
    String nameKeyword="name";
Mockito.when(userRepository.findByNameContaining(nameKeyword)).thenReturn(sameNameList);
        List<UserDto> userDtos = userService.searchUserByName(nameKeyword);
        int expectedElements=2;
        int result = userDtos.size();
        System.out.println("Number of elements "+userDtos.size());
        System.out.println(userDtos.get(0).getEmail()+" "+userDtos.get(1).getEmail());
        Assertions.assertEquals(expectedElements,result,"Total elements are not same !!");
        Assertions.assertNotEquals(userDtos.get(0).getEmail(),userDtos.get(1).getEmail(),"email is same !!");
    }

}
