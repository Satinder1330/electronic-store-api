package com.electronic.store.services;


import com.electronic.store.dtos.UserDto;
import com.electronic.store.helper.CustomExceptionResponse;
import com.electronic.store.helper.CustomPaginationResponse;

import java.io.IOException;
import java.util.List;

public interface UserService {

 UserDto createUser(UserDto userDto);
 UserDto updateUser(UserDto userDto,String userId);
 CustomExceptionResponse deleteUser(String userId) throws IOException;
 CustomPaginationResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);
 UserDto getUserById(String userId);
 UserDto getUserByEmail(String userEmail);
 List<UserDto>searchUserByName(String keyword);





}
