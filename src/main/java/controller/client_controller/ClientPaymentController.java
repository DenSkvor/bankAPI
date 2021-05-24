package controller.client_controller;

import com.sun.net.httpserver.HttpExchange;
import model.entity.Payment;
import model.service.PaymentService;
import model.service.PaymentServiceImpl;
import source.annotation.All;
import source.annotation.Controller;
import source.annotation.Get;
import source.annotation.Post;

import java.sql.SQLException;

import static exception.ExceptionHandler.sendErrorResponse;
import static source.Constant.CREATED;
import static source.Constant.OK;
import static util.UtilMethods.*;

@Controller(endpoint = "/payment")
public class ClientPaymentController {

    private PaymentService paymentService;

    public ClientPaymentController(){
        paymentService = new PaymentServiceImpl();
    }

    /**
     * Перевод средств (ожидает подтверждения)
     * POST localhost:8000/api/v1/client/payment
     * Пример JSON запроса:
     * {
     *   "counterpartyId" : 4,
     *   "money" : 500
     * }
     */
    @Post
    public void savePayment(HttpExchange exchange) {
        try {
            sendJsonResponse(convertToJson(paymentService.save(readJsonRequest(Payment.class, exchange))), CREATED, exchange);
        }catch (Exception e){
            e.printStackTrace();
            sendErrorResponse(e, exchange);
        }
    }

}
