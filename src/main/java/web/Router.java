package web;

import com.sun.net.httpserver.HttpExchange;
import exception.ExceptionHandler;
import exception.NotImplementedException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static util.UtilMethods.*;

public class Router {

    private static Router instance;
    public synchronized static Router getInstance(){
        if (instance == null) instance = new Router();
        return instance;
    }
    private Router(){}

    public void redirect(HttpExchange exchange, String controllerPath) {
        try {
            List<String> params = parsePath(exchange.getRequestURI().getRawPath());
            if(params.isEmpty()) throw new NotImplementedException();
            String methodType = exchange.getRequestMethod();
            String controllerType = params.get(0);
            Long id = params.size() == 2 ? Long.valueOf(params.get(1)) : null;
            boolean isAll = id == null && !exchange.getRequestHeaders().containsKey("Content-length");
            List<Class<?>> classList = findClasses(new File(controllerPath), getPackageNameFromPath(controllerPath));
            Class<?> controller = findController(classList, "/" + controllerType);
            Method method = findMethod(controller, methodType, isAll);
            System.out.println("Method: " + method.getName());
            method.invoke(controller.getConstructor().newInstance(), exchange);
        }catch (Exception e){
            e.printStackTrace();
            ExceptionHandler.sendErrorResponse(e, exchange);
        }

    }
}
