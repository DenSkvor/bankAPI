package util;

import static source.Constant.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import exception.BadRequestException;
import exception.MethodNotAllowedException;
import exception.NotImplementedException;
import source.annotation.All;
import source.annotation.Controller;
import web.Router;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class UtilMethods {

    public static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return Collections.emptyList();
        }
        File[] files = directory.listFiles();
        if(files == null) return Collections.emptyList();
        for (File file : files) {
            if (file.getName().endsWith(".java")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 5)));
            }
        }
        return classes;
    }

    public static Class<?> findController(List<Class<?>> controllers, String endpoint){
        for (Class<?> controller : controllers) {
            Controller a = controller.getAnnotation(Controller.class);
            if((a) != null && a.endpoint().equals(endpoint)) return controller;
        }
        throw new NotImplementedException();
    }

    public static Method findMethod(Class<?> controller, String methodType, boolean isAll){

        for (Method method : controller.getMethods()) {
            System.out.println(method.getName());
            Annotation a = method.getAnnotation(REQUEST_ANNO_MAP.get(methodType));
            if((a) != null) {
                if(isAll){
                    if(method.getAnnotation(All.class) != null) return method;
                    else continue;
                }
                else {
                    if(method.getAnnotation(All.class) == null) return method;
                    else continue;
                }
            }
        }
        throw new MethodNotAllowedException();
    }

    public static List<String> parsePath(String path){
        String[] strArr = path.substring(1).split("[/]");
        int fullRootApiPathLength = ROOT_PATH_LENGTH + 1;
        if(strArr.length <= fullRootApiPathLength) return Collections.emptyList();
        List<String> list = Arrays
                .stream(strArr)
                .collect(Collectors.toList())
                .subList(fullRootApiPathLength, fullRootApiPathLength + (strArr.length - fullRootApiPathLength > 1 ? 2 : 1));
        return normalizeEndpoint(list);
    }

    private static List<String> normalizeEndpoint(List<String> list) {
        if(list.size() == 2) {
            try {
                Long.parseLong(list.get(1));
                System.out.println("Resource: /" + list.get(0) + "/" + list.get(1));
                return list;
            } catch (NumberFormatException e) {
                System.out.println("Resource: /" + list.subList(1, 2).get(0));
                return list.subList(1, 2);
            }
        }else {
            System.out.println("Resource: /" + list.subList(0, 1).get(0));
            return list.subList(0, 1);
        }
    }

    public static Long getIdFromExchange(HttpExchange exchange){
        return Long.valueOf(parsePath(exchange.getRequestURI().getRawPath()).get(1));
    }

    public static String getPackageNameFromPath(String controllerPath){
        String[] strArr = controllerPath.split("[/]");
        return new StringBuilder().append(strArr[strArr.length-2]).append(".").append(strArr[strArr.length-1]).toString();
    }

    public static String[] getResponseContent(String httpCode, String jsonObj){
        return new String[]{httpCode, jsonObj};
    }

    public static <T> String convertToJson(T obj) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        return om.writeValueAsString(obj);
    }

    public static <T> void sendJsonResponse(T obj, HttpExchange exchange){
        String respText;
        try (OutputStream output = exchange.getResponseBody()){
            respText = convertToJson(obj);
            System.out.println(respText);
            exchange.setAttribute("content-type", "application/json");
            exchange.sendResponseHeaders(200, respText.getBytes().length);
            output.write(respText.getBytes());
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendJsonResponse(String json, Integer httpCode, HttpExchange exchange){
        try (OutputStream output = exchange.getResponseBody()){
            exchange.setAttribute("content-type", "application/json");
            exchange.sendResponseHeaders(httpCode, json.getBytes().length);
            output.write(json.getBytes());
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T readJsonRequest(Class<T> clazz, HttpExchange exchange){
        T obj = null;
        try (InputStream is = exchange.getRequestBody()) {
            String requestStr = new String(is.readAllBytes());
            ObjectMapper om = new ObjectMapper();
            obj = om.readValue(requestStr, clazz);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            throw new BadRequestException();
        } catch (IOException e){
            e.printStackTrace();
        }
        return obj;
    }

    public static Set<Map.Entry<Object, Object>> readAllProperties(String path) throws IOException {
        System.out.println("Read all properties.");
        File f = new File(path);
        Properties p = new Properties();
        try (FileReader fr = new FileReader(f)){
            p.load(fr);
        }
        return p.entrySet();
    }

    public static Properties readProperties(String path) throws IOException {
        File f = new File(path);
        Properties p = new Properties();
        p.load(new FileReader(f));
        return p;
    }

    public static void createContext(HttpServer server, String rootPath, Set<Map.Entry<Object, Object>> api){
        for (Map.Entry<Object, Object> entry : api) {
            server.createContext(rootPath + entry.getKey(), (exchange -> {
                System.out.println("URL: " + exchange.getRequestURI().getRawPath());
                try {
                    System.out.println("Create context: " + entry.getKey());
                    Router.getInstance().redirect(exchange, (String)entry.getValue());
                } finally {
                    exchange.close();
                    System.out.println("Exchange close.");
                }
            }));
        }
    }

    public static void closeResources(AutoCloseable... resources){
        for (AutoCloseable resource : resources) {
            try{
                if(resource != null) resource.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void doRollback(Connection connection){
        if(connection != null) {
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

}
