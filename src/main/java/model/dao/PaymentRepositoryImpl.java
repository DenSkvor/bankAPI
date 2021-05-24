package model.dao;

import exception.InternalServerException;
import exception.NotFoundException;
import model.DBConnection;
import model.entity.BankAccount;
import model.entity.Counterparty;
import model.entity.Payment;
import source.annotation.Table;
import util.UtilMethods;

import java.sql.*;

public class PaymentRepositoryImpl implements PaymentRepository{

    private static final String THIS_TABLE = Payment.class.getAnnotation(Table.class).name();
    private static final String ACCOUNT_TABLE = BankAccount.class.getAnnotation(Table.class).name();
    private static final String COUNTERPARTY_TABLE = Counterparty.class.getAnnotation(Table.class).name();
    private static final String INSERT_PAYMENT = "insert into " + THIS_TABLE + " (counterparty_id, money) values (?, ?);";
    private static final String SELECT_PAYMENT_WITH_LAST_ID = "select * from " + THIS_TABLE + " where payment_id = (select max(payment_id) from " + THIS_TABLE + ");";
    private static final String UPDATE_BANK_ACCOUNT_FROM_SUB_MONEY = "update " + ACCOUNT_TABLE + " set money = money - ? where money - ? > 0 and bank_account_id = (select bank_account_from_id from " + COUNTERPARTY_TABLE + " where counterparty_id = ?);";
    private static final String UPDATE_BANK_ACCOUNT_TO_ADD_MONEY = "update " + ACCOUNT_TABLE + " set money = money + ? where bank_account_id = (select bank_account_to_id from " + COUNTERPARTY_TABLE + " where counterparty_id = ?);";
    private static final String UPDATE_PAYMENT_SET_ACCEPTED = "update " + THIS_TABLE + " set is_accepted = ? where payment_id = ?;";

    @Override
    public Payment insert(Payment payment) {
        Payment returnPayment;
        Connection connection = null;
        PreparedStatement insertPs = null;
        PreparedStatement selectPs = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            insertPs = connection.prepareStatement(INSERT_PAYMENT);
            insertPs.setLong(1, payment.getCounterpartyId());
            insertPs.setBigDecimal(2, payment.getMoney());
            if (insertPs.executeUpdate() == 0) throw new NotFoundException();
            selectPs = connection.prepareStatement(SELECT_PAYMENT_WITH_LAST_ID);
            ResultSet rs = selectPs.executeQuery();
            if (!rs.next()) throw new InternalServerException();
            do {
                returnPayment = new Payment(rs.getLong(1), rs.getLong(2), rs.getBigDecimal(3), rs.getBoolean(4));
            }
            while (rs.next());
            connection.commit();
        }catch (SQLException e) {
            e.printStackTrace();
            UtilMethods.doRollback(connection);
            throw new InternalServerException();
        }
        return returnPayment;
    }

    public Payment update(Payment payment){
        Payment returnPayment = null;
        Connection connection = null;
        PreparedStatement updateSubMoneyPs = null;
        PreparedStatement updateAddMoneyPs = null;
        PreparedStatement updateAcceptedPs = null;
        PreparedStatement selectPs = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            updateSubMoneyPs = connection.prepareStatement(UPDATE_BANK_ACCOUNT_FROM_SUB_MONEY);
            subMoney(updateSubMoneyPs, payment);

            updateAddMoneyPs = connection.prepareStatement(UPDATE_BANK_ACCOUNT_TO_ADD_MONEY);
            addMoney(updateAddMoneyPs, payment);

            updateAcceptedPs = connection.prepareStatement(UPDATE_PAYMENT_SET_ACCEPTED);
            updatePaymentSetAccepted(updateAcceptedPs, payment);

            selectPs = connection.prepareStatement(SELECT_PAYMENT_WITH_LAST_ID);
            ResultSet rs = selectPs.executeQuery();
            if (!rs.next()) throw new InternalServerException();
            do {
                returnPayment = new Payment(rs.getLong(1), rs.getLong(2), rs.getBigDecimal(3), rs.getBoolean(4));
            }
            while (rs.next());
            connection.commit();
        }catch (SQLException e) {
            e.printStackTrace();
            UtilMethods.doRollback(connection);
            throw new InternalServerException();
        }finally {
            UtilMethods.closeResources(updateSubMoneyPs, updateAddMoneyPs, updateAcceptedPs, connection);
        }
        return returnPayment;
    }

    private void subMoney(PreparedStatement updateSubMoneyPs, Payment payment) throws SQLException {
        updateSubMoneyPs.setBigDecimal(1, payment.getMoney());
        updateSubMoneyPs.setBigDecimal(2, payment.getMoney());
        updateSubMoneyPs.setLong(3, payment.getCounterpartyId());
        if (updateSubMoneyPs.executeUpdate() == 0) throw new NotFoundException();
    }

    private void addMoney(PreparedStatement updateAddMoneyPs, Payment payment) throws SQLException {
        updateAddMoneyPs.setBigDecimal(1, payment.getMoney());
        updateAddMoneyPs.setLong(2, payment.getCounterpartyId());
        if (updateAddMoneyPs.executeUpdate() == 0) throw new NotFoundException();
    }

    private void updatePaymentSetAccepted(PreparedStatement updateAcceptedPs, Payment payment) throws SQLException {
        updateAcceptedPs.setBoolean(1, payment.getIsAccepted());
        updateAcceptedPs.setLong(2, payment.getId());
        if (updateAcceptedPs.executeUpdate() == 0) throw new NotFoundException();
    }
}
