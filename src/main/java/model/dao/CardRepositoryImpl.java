package model.dao;

import exception.InternalServerException;
import exception.NotFoundException;
import model.DBConnection;
import model.dto.CardIsActiveDto;
import model.entity.Card;
import source.annotation.Table;
import util.UtilMethods;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardRepositoryImpl implements CardRepository{

    private static final String THIS_TABLE = Card.class.getAnnotation(Table.class).name();
    private static final String SELECT_CARDS = "select * from " + THIS_TABLE + " where bank_account_id = ?;";
    private static final String SELECT_CARD = "select * from " + THIS_TABLE + " where card_id = ?";
    private static final String INSERT_CARD = "insert into " + THIS_TABLE + " (`number`, bank_account_id) values (?, ?);";
    private static final String SELECT_CARD_WITH_LAST_ID = "select * from " + THIS_TABLE + " where card_id = (select max(card_id) from " + THIS_TABLE + ");";
    private static final String UPDATE_CARD_SET_CURRENT_ACTIVE_FALSE = "update " + THIS_TABLE + " set is_active = false where bank_account_id = ? and is_active = true;";
    private static final String UPDATE_CARD_SET_CURRENT_ACTIVE_TRUE = "update " + THIS_TABLE + " set is_active = ? where card_id = ?;";

    @Override
    public List<Card> findAllByAccountId(Long accountId) {
        List<Card> listCard = new ArrayList<>();
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement selectPs = connection.prepareStatement(SELECT_CARDS)){
            selectPs.setLong(1, accountId);
            ResultSet rs = selectPs.executeQuery();
            if(!rs.next()) throw new NotFoundException();
            do {
                listCard.add(new Card(rs.getLong(1), rs.getString(2), rs.getLong(3), rs.getBoolean(4)));
            }
            while (rs.next());
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServerException();
        }
        return listCard;
    }

    @Override
    public Card insert(Card card) {
        Card returnCard = null;
        Connection connection = null;
        PreparedStatement insertPs = null;
        PreparedStatement selectPs = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            insertPs = connection.prepareStatement(INSERT_CARD);
            insertPs.setString(1, card.getNumber());
            insertPs.setLong(2, card.getAccountId());
            if (insertPs.executeUpdate() == 0) throw new NotFoundException();
            selectPs = connection.prepareStatement(SELECT_CARD_WITH_LAST_ID);
            ResultSet rs = selectPs.executeQuery();
            if (!rs.next()) throw new InternalServerException();
            do {
                returnCard = new Card(rs.getLong(1), rs.getString(2), rs.getLong(3), rs.getBoolean(4));
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
        return returnCard;
    }

    @Override
    public Card update(CardIsActiveDto cardIsActiveDto) {
        Card returnCard = null;
        Connection connection = null;
        PreparedStatement updateFalsePs = null;
        PreparedStatement updateTruePs = null;
        PreparedStatement selectPs = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            if (cardIsActiveDto.getIsActive()) {
                connection.setAutoCommit(false);
                updateFalsePs = connection.prepareStatement(UPDATE_CARD_SET_CURRENT_ACTIVE_FALSE);
                updateFalsePs.setLong(1, cardIsActiveDto.getAccountId());
            }
            updateTruePs = connection.prepareStatement(UPDATE_CARD_SET_CURRENT_ACTIVE_TRUE);
            updateTruePs.setBoolean(1, cardIsActiveDto.getIsActive());
            updateTruePs.setLong(2, cardIsActiveDto.getId());
            if (updateTruePs.executeUpdate() == 0) throw new NotFoundException();
            selectPs = connection.prepareStatement(SELECT_CARD);
            selectPs.setLong(1, cardIsActiveDto.getId());
            ResultSet rs = selectPs.executeQuery();
            if (!rs.next()) throw new NotFoundException();
            do {
                returnCard = new Card(rs.getLong(1), rs.getString(2), rs.getLong(3), rs.getBoolean(4));
            }
            while (rs.next());
            if (cardIsActiveDto.getIsActive()) connection.commit();
        }catch (SQLException e){
            e.printStackTrace();
            UtilMethods.doRollback(connection);
            throw new InternalServerException();
        }finally {
            UtilMethods.closeResources(updateFalsePs, updateTruePs, connection);
        }
        return returnCard;
    }
}
