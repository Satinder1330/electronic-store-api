package com.electronic.store.services.implimentation;

import com.electronic.store.dtos.UserDto;
import com.electronic.store.entities.Role;
import com.electronic.store.entities.User;
import com.electronic.store.exception.ResourceNotFoundExc;
import com.electronic.store.helper.CustomExceptionResponse;
import com.electronic.store.helper.CustomPaginationResponse;
import com.electronic.store.helper.Helper;
import com.electronic.store.repositories.RoleRepository;
import com.electronic.store.repositories.UserRepository;
import com.electronic.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
            private Helper helper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Value("${user.profile.image.get.path}")
    private String imagePath;

    Logger logger = LoggerFactory.getLogger(UserServiceImp.class);

    @Override
    public UserDto createUser(UserDto userDto) {
        // Random userId
        String userId= UUID.randomUUID().toString();
        userDto.setUserId(userId);
        // Dto to user to save in the database(or use modelmapper directly(mapper.map))
        User user = dtoToEntity(userDto);
        //encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //set role to the user
        Role role = new Role(); // if not present
        role.setRoleId(UUID.randomUUID().toString());
        role.setName("ROLE_USER");
        Role roleUser = roleRepository.findByName("ROLE_USER").orElse(role);
        user.setRoles(List.of(roleUser));
        User save = userRepository.save(user);//save user in the database
        //user to Dto again to send back in response
        UserDto userDto1 = entityToDto(save);
        return userDto1;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundExc("User with the given id does not exist"));

        if(userDto.getName()!=null)user.setName(userDto.getName());
        if(userDto.getEmail()!=null)user.setEmail(userDto.getEmail());
        if (userDto.getPassword()!=null)user.setPassword(userDto.getPassword());
        if (userDto.getGender()!=null)user.setGender(userDto.getGender());
        if (userDto.getImageName()!=null)user.setImageName(userDto.getImageName());
        if(userDto.getPassword()!=null)user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User saveduser = userRepository.save(user);
        UserDto userDto1 = entityToDto(saveduser);
        return userDto1;
    }

    @Override
    public CustomExceptionResponse deleteUser(String userId)  {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundExc("User with the given id does not exist"));
       String newPath=imagePath+user.getImageName();
       try{
           Path path = Paths.get(newPath);
           Files.delete(path);//delete user files(images)
       }catch (NoSuchFileException ex){
           logger.error("User image not found in the folder");
           ex.printStackTrace();
       }catch (IOException ex){
           ex.printStackTrace();
       }
        userRepository.delete(user);//delete user
        return new CustomExceptionResponse("User has been deleted", HttpStatus.OK);
    }

    @Override
    public CustomPaginationResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("Asc"))?(Sort.by(sortBy).ascending()):(Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<User> page = userRepository.findAll(pageable);
        List<User> list = page.getContent();

        List<UserDto>userDtoList=new ArrayList<>();
        list.forEach(user -> {
            UserDto userDto = entityToDto(user);
            userDtoList.add(userDto);
        });
        CustomPaginationResponse<UserDto> response = helper.getPageableResponse(page, userDtoList, pageNumber, pageSize);// info in the response
        return response;
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundExc("User with the given id does not exist"));
        UserDto userDto = entityToDto(user);

        return userDto;
    }
//custom method
    @Override
    public UserDto getUserByEmail(String userEmail) {
        User byEmail = userRepository.findByEmail(userEmail).orElseThrow(()-> new ResourceNotFoundExc("User with the given email does not exist"));
        UserDto userDto = entityToDto(byEmail);
        return userDto;
    }

    @Override
    public List<UserDto> searchUserByName(String keyword) {
        List<User> byNameContaining = userRepository.findByNameContaining(keyword);
        List<UserDto>userDtoList=new ArrayList<>();
        byNameContaining.forEach(user -> {
            UserDto userDto = entityToDto(user);
            userDtoList.add(userDto);
        });
        return userDtoList;
    }
    // Other methods
    User dtoToEntity(UserDto userDto){
        User map = mapper.map(userDto, User.class);// autowired modelmapper to map(project config)
        return map;
    }
    UserDto entityToDto(User user){
        UserDto map = mapper.map(user, UserDto.class);
        return map;
    }
}
