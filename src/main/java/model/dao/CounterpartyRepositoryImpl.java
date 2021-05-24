package model.dao;

import exception.InternalServerException;
import exception.NotFoundException;
import model.DBConnection;
import model.dto.CounterpartyDto;
import model.entity.BankAccount;
import model.entity.Counterparty;
import source.annotation.Table;
import util.UtilMethods;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CounterpartyRepositoryImpl implements CounterpartyRepository{

    private static final String THIS_TABLE = Counterparty.class.getAnnotation(Table.class).name();
    private static final String ACCOUNT_TABLE = BankAccount.class.getAnnotation(Table.class).name();
    private static final String SELECT_COUNTERPARTIES = "select * from " + THIS_TABLE + " join " + ACCOUNT_TABLE + " on bank_account_to_id = bank_account_id join user_tbl on " + ACCOUNT_TABLE + ".user_id = user_tbl.user_id where bank_account_from_id = ?";
    private static final String INSERT_COUNTERPARTY = "insert into " + THIS_TABLE + " (bank_account_from_id, bank_account_to_id) values (?, ?);";
    private static final String SELECT_COUNTERPARTY_WITH_LAST_ID = "select * from " + THIS_TABLE + " where counterparty_id = (select max(counterparty_id) from " + THIS_TABLE + ");";


    @Override
    public List<CounterpartyDto> findAllByAccountId(Long accountId) {
        List<CounterpartyDto> listCounterpartyDto = new ArrayList<>();
        try (Connection connection = DBConnection.getInstance().getConnection()){
            PreparedStatement selectPs = connection.prepareStatement(SELECT_COUNTERPARTIES);
            selectPs.setLong(1, accountId);
            ResultSet rs = selectPs.executeQuery();
            if(!rs.next()) throw new NotFoundException();
            do {
                listCounterpartyDto.add(new CounterpartyDto(rs.getString("number"), rs.getString("firstname"), rs.getString("lastname"), rs.getBoolean("is_active")));
            }
            while (rs.next());
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServerException();
        }
        return listCounterpartyDto;
    }

    @Override
    public Counterparty insert(Counterparty counterparty) {
        Counterparty returnCounterparty;
        Connection connection = null;
        PreparedStatement insertPs = null;
        PreparedStatement selectPs = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            insertPs = connection.prepareStatement(INSERT_COUNTERPARTY);
            insertPs.setLong(1, counterparty.getAccountFromId());
            insertPs.setLong(2, counterparty.getAccountToId());
            if (insertPs.executeUpdate() == 0) throw new NotFoundException();
            selectPs = connection.prepareStatement(SELECT_COUNTERPARTY_WITH_LAST_ID);
            ResultSet rs = selectPs.executeQuery();
            if (!rs.next()) throw new InternalServerException();
            do {
                returnCounterparty = new Counterparty(rs.getLong(1), rs.getLong(2), rs.getLong(3), rs.getBoolean(4));
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
        return returnCounterparty;
    }
}
