package controller.employee_controller;

import com.sun.net.httpserver.HttpExchange;
import model.entity.User;

import static exception.ExceptionHandler.sendErrorResponse;
import static source.Constant.CREATED;
import static source.Constant.OK;
import static util.UtilMethods.*;

import model.service.UserService;
import model.service.UserServiceImpl;
import source.annotation.All;
import source.annotation.Controller;
import source.annotation.Get;
import source.annotation.Post;

@Controller(endpoint = "/user")
public class EmployeeUserController {

    private final UserService userService;

    public EmployeeUserController(){
        userService = new UserServiceImpl();
    }

    /**
     * Добавление нового физ лица
     * POST localhost:8000/api/v1/employee/user
     * Пример JSON запроса:
     * {
     *   "firstName" : "Ivan",
     *   "lastName" : "Petrov"
     * }
     */
    @Post
    public void saveUser(HttpExchange exchange) {
        try {
            sendJsonResponse(convertToJson(userService.save(readJsonRequest(User.class, exchange))), CREATED, exchange);
        }catch (Exception e){
            e.printStackTrace();
            sendErrorResponse(e, exchange);
        }
    }

}
