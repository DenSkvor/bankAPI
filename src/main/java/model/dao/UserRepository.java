package model.dao;

import model.entity.User;

import java.sql.SQLException;
import java.util.List;

public interface UserRepository {

    User insert(User user);

}
