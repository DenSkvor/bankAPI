package controller.client_controller;

import com.sun.net.httpserver.HttpExchange;
import model.entity.Counterparty;
import model.service.CounterpartyService;
import model.service.CounterpartyServiceImpl;
import source.annotation.All;
import source.annotation.Controller;
import source.annotation.Get;
import source.annotation.Post;

import static exception.ExceptionHandler.sendErrorResponse;
import static source.Constant.CREATED;
import static source.Constant.OK;
import static util.UtilMethods.*;

@Controller(endpoint = "/counterparty")
public class ClientCounterpartyController {

    private final CounterpartyService counterpartyService;

    public ClientCounterpartyController() {
        this.counterpartyService = new CounterpartyServiceImpl();
    }

    /**
     * Получение всех контрагентов по id счета
     * GET localhost:8000/api/v1/client/counterparty/{id}
     */
    @Get
    public void getAllByAccountId(HttpExchange exchange) {
        try {
            sendJsonResponse(convertToJson(counterpartyService.getAllByAccountId(getIdFromExchange(exchange))), OK, exchange);
        }catch (Exception e){
            e.printStackTrace();
            sendErrorResponse(e, exchange);
        }
    }

    /**
     * Добавление контрагента
     * POST localhost:8000/api/v1/client/counterparty
     * Пример JSON запроса:
     * {
     *   "accountFromId" : 1,
     *   "accountToId" : 4
     * }
     */
    @Post
    public void saveCounterparty(HttpExchange exchange) {
        try {
            sendJsonResponse(convertToJson(counterpartyService.save(readJsonRequest(Counterparty.class, exchange))), CREATED, exchange);
        }catch (Exception e){
            e.printStackTrace();
            sendErrorResponse(e, exchange);
        }
    }
}
