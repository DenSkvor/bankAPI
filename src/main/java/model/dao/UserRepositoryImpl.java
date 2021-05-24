package model.dao;

import exception.InternalServerException;
import exception.NotFoundException;
import model.DBConnection;
import model.entity.User;
import source.annotation.Table;
import util.UtilMethods;

import java.sql.*;

public class UserRepositoryImpl implements UserRepository{

    private static final String THIS_TABLE = User.class.getAnnotation(Table.class).name();
    private static final String INSERT_USER = "insert into " + THIS_TABLE + " (firstname, lastname) values (?, ?);";
    private static final String SELECT_USER_WITH_LAST_ID = "select * from " + THIS_TABLE + " where user_id = (select max(user_id) from " + THIS_TABLE + ");";

    @Override
    public User insert(User user) {
        User returnUser;
        Connection connection = null;
        PreparedStatement insertPs = null;
        PreparedStatement selectPs = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            insertPs = connection.prepareStatement(INSERT_USER);
            insertPs.setString(1, user.getFirstName());
            insertPs.setString(2, user.getLastName());
            if (insertPs.executeUpdate() == 0) throw new NotFoundException();
            selectPs = connection.prepareStatement(SELECT_USER_WITH_LAST_ID);
            ResultSet rs = selectPs.executeQuery();
            if (!rs.next()) throw new InternalServerException();
            do {
                returnUser = new User(rs.getLong(1), rs.getString(2), rs.getString(3));
            }
            while (rs.next());
            connection.commit();
        }catch (SQLException e) {
            e.printStackTrace();
            UtilMethods.doRollback(connection);
            throw new InternalServerException();
        }finally {
            UtilMethods.closeResources(insertPs, selectPs, connection);
        }
        return returnUser;

    }
}
