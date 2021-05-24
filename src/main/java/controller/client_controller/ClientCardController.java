package controller.client_controller;

import com.sun.net.httpserver.HttpExchange;
import model.entity.Card;
import model.service.CardService;
import model.service.CardServiceImpl;
import source.annotation.All;
import source.annotation.Controller;
import source.annotation.Get;
import source.annotation.Post;

import java.sql.SQLException;

import static exception.ExceptionHandler.sendErrorResponse;
import static source.Constant.CREATED;
import static source.Constant.OK;
import static util.UtilMethods.*;


@Controller(endpoint = "/card")
public class ClientCardController {

    private final CardService cardService;

    public ClientCardController(){
        cardService = new CardServiceImpl();
    }

    /**
     * Получение всех карт по id счета
     * GET localhost:8000/api/v1/client/card/{id}
     */
    @Get
    public void getAllByAccountId(HttpExchange exchange) {
        try {
            sendJsonResponse(convertToJson(cardService.getAllByAccountId(getIdFromExchange(exchange))), OK, exchange);
        }catch (Exception e){
            e.printStackTrace();
            sendErrorResponse(e, exchange);
        }
    }

    /**
     * Выпуск новой карты
     * POST localhost:8000/api/v1/client/card
     * Пример JSON запроса:
     * {
     *   "number" : "00000000",
     *   "accountId" : 3
     * }
     */
    @Post
    public void saveCard(HttpExchange exchange) throws SQLException {
        try {
            sendJsonResponse(convertToJson(cardService.save(readJsonRequest(Card.class, exchange))), CREATED, exchange);
        }catch (Exception e){
            e.printStackTrace();
            sendErrorResponse(e, exchange);
        }
    }

}
