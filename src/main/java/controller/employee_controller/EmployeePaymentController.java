package controller.employee_controller;

import com.sun.net.httpserver.HttpExchange;
import model.entity.Payment;
import model.service.PaymentService;
import model.service.PaymentServiceImpl;
import source.annotation.Controller;
import source.annotation.Put;

import java.sql.SQLException;

import static exception.ExceptionHandler.sendErrorResponse;
import static source.Constant.OK;
import static util.UtilMethods.*;

@Controller(endpoint = "/payment")
public class EmployeePaymentController {

    private PaymentService paymentService;

    public EmployeePaymentController(){
        paymentService = new PaymentServiceImpl();
    }

    /**
     * Подтверждение платежа
     * PUT localhost:8000/api/v1/employee/payment
     * Пример JSON запроса:
     * {
     *   "id" : 4,
     *   "counterpartyId" : 4,
     *   "money" : 500,
     *   "isAccepted" : true
     * }
     */
    @Put
    public void updatePayment(HttpExchange exchange) throws SQLException {
        try {
            sendJsonResponse(convertToJson(paymentService.update(readJsonRequest(Payment.class, exchange))), OK, exchange);
        }catch (Exception e){
            e.printStackTrace();
            sendErrorResponse(e, exchange);
        }
    }
}
