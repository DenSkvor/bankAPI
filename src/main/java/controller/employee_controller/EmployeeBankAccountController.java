package controller.employee_controller;

import com.sun.net.httpserver.HttpExchange;
import model.entity.BankAccount;
import model.service.BankAccountService;
import model.service.BankAccountServiceImpl;
import source.annotation.Controller;
import source.annotation.Post;

import static exception.ExceptionHandler.sendErrorResponse;
import static source.Constant.*;
import static util.UtilMethods.*;

@Controller(endpoint = "/account")
public class EmployeeBankAccountController {

    private final BankAccountService bankAccountService;

    public EmployeeBankAccountController(){
        bankAccountService = new BankAccountServiceImpl();
    }

    /**
     * Открытие нового счета
     * POST localhost:8000/api/v1/employee/account
     * Пример JSON запроса:
     * {
     *   "number" : "010101010",
     *   "money" : 1000,
     *   "userId" : 1
     * }
     */
    @Post
    public void save(HttpExchange exchange){
        try {
            sendJsonResponse(convertToJson(bankAccountService.save(readJsonRequest(BankAccount.class, exchange))), CREATED, exchange);
        }catch (Exception e){
            e.printStackTrace();
            sendErrorResponse(e, exchange);
        }
    }

}
