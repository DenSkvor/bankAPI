package source;

import exception.*;
import source.annotation.Delete;
import source.annotation.Get;
import source.annotation.Post;
import source.annotation.Put;

import java.util.HashMap;
import java.util.Map;

public class Constant {

    public static final int SERVER_PORT = 8000;
    public static final String ROOT_PATH = "/api/v1";
    public static final int ROOT_PATH_LENGTH = ROOT_PATH.substring(1).split("[/]").length;
    public static final String API_PROPERTIES_PATH = "src/main/resources/properties/api.properties";
    public static final String DB_PROPERTIES_PATH = "src/main/resources/properties/database.properties";
    public static final String DB_MAIN_INIT_SCRIPT_PATH = "src/main/resources/db/bank_api_db.sql";
    public static final String DB_TEST_INIT_SCRIPT_PATH = "src/main/resources/db/test_bank_api_db.sql";

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";

    public static final Map<String, Class> REQUEST_ANNO_MAP = new HashMap<>(){{
        put(GET, Get.class);
        put(POST, Post.class);
        put(PUT, Put.class);
        put(DELETE, Delete.class);
    }};

    public static final Integer OK = 200;
    public static final Integer CREATED = 201;
    public static final Integer BAD_REQUEST = 400;
    public static final Integer NOT_FOUND = 404;
    public static final Integer METHOD_NOT_ALLOWED = 405;
    public static final Integer INTERNAL_SERVER_ERROR = 500;
    public static final Integer NOT_IMPLEMENTED = 501;

    public static final Map<Class<? extends Exception>, Integer> HTTP_ERROR_CODE = new HashMap<>(){{
        put(BadRequestException.class, BAD_REQUEST);
        put(NotFoundException.class, NOT_FOUND);
        put(MethodNotAllowedException.class, METHOD_NOT_ALLOWED);
        put(InternalServerException.class, INTERNAL_SERVER_ERROR);
        put(NotImplementedException.class, NOT_IMPLEMENTED);
    }};



}
