package model.service;

import exception.BadRequestException;
import model.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    private static UserService userService;
    private User user;

    @BeforeAll
    static void init() {
        userService = new UserServiceImpl();
    }

    @BeforeEach
    void setUp(){
        user = new User(1L, "Ivan", "Ivanov");
    }


    @Test
    void saveNullUserTest() {
        Assertions.assertThrows(BadRequestException.class,  () -> {
            userService.save(null);
        });
    }

    @Test
    void saveNullFirstNameTest() {
        user.setFirstName(null);
        Assertions.assertThrows(BadRequestException.class,  () -> {
            userService.save(user);
        });
    }

    @Test
    void saveNullLastNameTest() {
        user.setLastName(null);
        Assertions.assertThrows(BadRequestException.class,  () -> {
            userService.save(user);
        });
    }
}