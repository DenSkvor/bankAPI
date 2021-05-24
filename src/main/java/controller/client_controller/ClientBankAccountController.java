package controller.client_controller;

import com.sun.net.httpserver.HttpExchange;
import exception.ExceptionHandler;
import model.dto.BankAccountMoneyDto;
import model.service.BankAccountService;
import model.service.BankAccountServiceImpl;
import source.annotation.Controller;
import source.annotation.Get;
import source.annotation.Put;

import static source.Constant.OK;
import static util.UtilMethods.*;

@Controller(endpoint = "/account")
public class ClientBankAccountController {

    private final BankAccountService bankAccountService;

    public ClientBankAccountController(){
        bankAccountService = new BankAccountServiceImpl();
    }

    /**
     * Проверка баланса по id счета
     * GET localhost:8000/api/v1/client/account/{id}
     */
    @Get
    public void getAccountById(HttpExchange exchange) {
        try{
        sendJsonResponse(convertToJson(bankAccountService.getById(getIdFromExchange(exchange))), OK, exchange);
        }catch (Exception e){
            e.printStackTrace();
            ExceptionHandler.sendErrorResponse(e, exchange);
        }
    }

    /**
     * Пополнение баланса
     * PUT localhost:8000/api/v1/client/account
     * Пример JSON запроса:
     * {
     *   "id" : 3,
     *   "money" : 1000
     * }
     */
    @Put
    public void updateAccount(HttpExchange exchange) {
        try {
            BankAccountMoneyDto bankAccountMoneyDto = readJsonRequest(BankAccountMoneyDto.class, exchange);
            sendJsonResponse(convertToJson(bankAccountService.update(bankAccountMoneyDto)), OK, exchange);
        }catch (Exception e){
            e.printStackTrace();
            ExceptionHandler.sendErrorResponse(e, exchange);
        }
    }

}
