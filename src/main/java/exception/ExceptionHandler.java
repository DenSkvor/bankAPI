package exception;

import com.sun.net.httpserver.HttpExchange;
import source.Constant;
import util.UtilMethods;

public class ExceptionHandler {

    public static void sendErrorResponse(Exception exp, HttpExchange exchange){
        int errorCode = 500;
        for (Class<? extends Exception> clazz : Constant.HTTP_ERROR_CODE.keySet()) {
            if(clazz == exp.getClass()) errorCode = Constant.HTTP_ERROR_CODE.get(clazz);
        }
        UtilMethods.sendJsonResponse("", errorCode, exchange);
    }
}
