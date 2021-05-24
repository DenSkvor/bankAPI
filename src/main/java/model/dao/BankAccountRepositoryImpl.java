package model.dao;

import exception.InternalServerException;
import exception.NotFoundException;
import model.DBConnection;
import model.dto.BankAccountMoneyDto;
import model.entity.BankAccount;
import source.annotation.Table;
import util.UtilMethods;

import java.sql.*;

public class BankAccountRepositoryImpl implements BankAccountRepository{

    private static final String THIS_TABLE = BankAccount.class.getAnnotation(Table.class).name();
    private static final String SELECT_ACCOUNT_BY_ID = "select * from " + THIS_TABLE + " where bank_account_id = ?;";
    private static final String INSERT_ACCOUNT = "insert into " + THIS_TABLE + " (`number`, money, user_id) values (?, ?, ?);";
    private static final String SELECT_ACCOUNT_WITH_LAST_ID = "select * from " + THIS_TABLE + " where bank_account_id = (select max(bank_account_id) from " + THIS_TABLE + ");";
    private static final String UPDATE_ACCOUNT_SET_MONEY = "update " + THIS_TABLE + " set money = money + ? where bank_account_id = ?;";

    @Override
    public BankAccount findById(Long id) {
        BankAccount bankAccount = null;
        try (Connection connection = DBConnection.getInstance().getConnection()){
            PreparedStatement selectByIdPs = connection.prepareStatement(SELECT_ACCOUNT_BY_ID);
            selectByIdPs.setLong(1, id);
            ResultSet rs = selectByIdPs.executeQuery();
            if (!rs.next()) throw new NotFoundException();
            do {
                bankAccount = new BankAccount(rs.getLong(1), rs.getString(2), rs.getBigDecimal(3), rs.getLong(4));
            }
            while (rs.next());
        }catch (SQLException e){
            e.printStackTrace();
            throw new InternalServerException();
        }
        return bankAccount;
    }

    @Override
    public BankAccount insert(BankAccount bankAccount) {
        BankAccount returnBankAccount;
        Connection connection = null;
        PreparedStatement insertPs = null;
        PreparedStatement selectPs = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            insertPs = connection.prepareStatement(INSERT_ACCOUNT);
            insertPs.setString(1, bankAccount.getNumber());
            insertPs.setBigDecimal(2, bankAccount.getMoney());
            insertPs.setLong(3, bankAccount.getUserId());
            if (insertPs.executeUpdate() == 0) throw new NotFoundException();
            selectPs = connection.prepareStatement(SELECT_ACCOUNT_WITH_LAST_ID);
            ResultSet rs = selectPs.executeQuery();
            if (!rs.next()) throw new InternalServerException();
            do {
                returnBankAccount = new BankAccount(rs.getLong(1), rs.getString(2), rs.getBigDecimal(3), rs.getLong(4));
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
        return returnBankAccount;
    }

    @Override
    public BankAccount update(BankAccountMoneyDto bankAccountMoneyDto) {
        BankAccount returnBankAccount = null;
        Connection connection = null;
        PreparedStatement updatePs = null;
        PreparedStatement selectPs = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            updatePs = connection.prepareStatement(UPDATE_ACCOUNT_SET_MONEY);
            updatePs.setBigDecimal(1, bankAccountMoneyDto.getMoney());
            updatePs.setLong(2, bankAccountMoneyDto.getId());
            if (updatePs.executeUpdate() == 0) throw new NotFoundException();
            selectPs = connection.prepareStatement(SELECT_ACCOUNT_BY_ID);
            selectPs.setLong(1, bankAccountMoneyDto.getId());
            ResultSet rs = selectPs.executeQuery();
            if (!rs.next()) throw new NotFoundException();
            do {
                returnBankAccount = new BankAccount(rs.getLong(1), rs.getString(2), rs.getBigDecimal(3), rs.getLong(4));
            }
            while (rs.next());
            connection.commit();
        }catch (SQLException e){
            e.printStackTrace();
            UtilMethods.doRollback(connection);
            throw new InternalServerException();
        }finally {
            UtilMethods.closeResources(updatePs, selectPs, connection);
        }
        return returnBankAccount;
    }
}
