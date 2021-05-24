package controller.employee_controller;

import com.sun.net.httpserver.HttpExchange;
import model.dto.CardIsActiveDto;
import model.service.CardService;
import model.service.CardServiceImpl;
import source.annotation.Controller;
import source.annotation.Put;

import java.sql.SQLException;
import static exception.ExceptionHandler.*;


import static source.Constant.*;
import static util.UtilMethods.*;


@Controller(endpoint = "/card")
public class EmployeeCardController {

    private final CardService cardService;

    public EmployeeCardController(){
        cardService = new CardServiceImpl();
    }

    /**
     * Активация карты
     * PUT localhost:8000/api/v1/employee/card
     * Пример JSON запроса:
     * {
     *   "id" : 8,
     *   "accountId" : 3,
     *   "isActive" : true
     * }
     */
    @Put
    public void updateCard(HttpExchange exchange) throws SQLException {
        try {
            sendJsonResponse(convertToJson(cardService.update(readJsonRequest(CardIsActiveDto.class, exchange))), OK, exchange);
        }catch (Exception e){
            e.printStackTrace();
            sendErrorResponse(e, exchange);
        }
    }
}
