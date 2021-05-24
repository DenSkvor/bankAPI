package model.service;

import exception.BadRequestException;
import model.dao.UserRepository;
import model.dao.UserRepositoryImpl;
import model.entity.User;

import java.util.List;

public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(){
        this.userRepository = new UserRepositoryImpl();
    }

    @Override
    public User save(User user) {
        if(user == null
                || user.getFirstName() == null
                || user.getLastName() == null) throw new BadRequestException();
        return userRepository.insert(user);
    }
}
