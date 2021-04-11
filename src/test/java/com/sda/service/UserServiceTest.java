package com.sda.service;

import com.sda.dto.UserDto;
import com.sda.dto.UserHeaderDto;
import com.sda.mapper.UserMapper;
import com.sda.model.User;
import com.sda.repository.RoleRepository;
import com.sda.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
 class UserServiceTest {

    @Mock
    private  UserRepository userRepository;

    @Mock
    private UserMapper userMapper;
    @Mock
    private  RoleRepository roleRepository;
    @Mock
    private  BCryptPasswordEncoder bCryptPasswordEncoder;
    @Captor
    private ArgumentCaptor<User> postArgumentCaptor;

    @InjectMocks
    private UserService userService;



    @Test
    @DisplayName("Test should pass when the object User has been registered")
    void register() {

        UserDto userDto = new UserDto();
        userDto.setFirstname("Adrian");
        userDto.setLastName("Enache");
        userDto.setEmail("constantin.adrian.enache@gmail.com");
        userDto.setPassword("1234");

        User user = new User();
        user.setFirstName("Adrian");
        user.setLastName("Vasile");
        user.setEmail("constantin.adrian.enache@gmail.com");
        user.setPassword("1234");
        userRepository.save(user);
        Mockito.verify(userRepository,Mockito.times(1)).save(postArgumentCaptor.capture());

        Assertions.assertThat(postArgumentCaptor.getValue().getEmail()).isEqualTo(userDto.getLastName());

    }

    @Test
    @DisplayName("Test should throw exception when user email not found")
    void whenTheUserEmailIsNotFound(){
        User user = new User();
        user.setFirstName("Adrian");
        user.setEmail("constantin.adrian.enache@gmail.com");

        User expectedUser = new User();
        expectedUser.setEmail("constantin.adrian.enache@gmail.com");
        expectedUser.setFirstName("Adrian");

        UserHeaderDto expectedUserHeaderDto = new UserHeaderDto();
        expectedUserHeaderDto.setFirstName("Adrian");

        Mockito.when(userRepository.findByEmail("constantin.adrian.enache@gmail.com")).thenReturn(Optional.of(user));
        Mockito.when(userMapper.map(Mockito.any(User.class))).thenReturn(expectedUserHeaderDto);

        UserHeaderDto actualUserHeaderDto = userService.getUserHeaderDto("constantin.adrian.enache@gmail.com");

        Assertions.assertThat(actualUserHeaderDto.getFirstName()).isEqualTo("Adrian");
    }


    @Test
    @DisplayName("Test should return the object user using email")
    void findUserUsingEmail(){
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            Mockito.when(userRepository.findByEmail(" ")).thenReturn(Optional.empty());
            Optional<User> optionalUser = userRepository.findByEmail(" ");
            if (!optionalUser.isPresent()){
                throw new RuntimeException("Invalid user email !");
            }
        });

        assertEquals("Invalid user email !", exception.getMessage());




    }
}