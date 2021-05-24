import model.DBConnection;
import web.Server;

import java.io.IOException;

import static source.Constant.DB_MAIN_INIT_SCRIPT_PATH;

public class Main {
    public static void main(String[] args) {
        try {
            DBConnection.getInstance().start();
            DBConnection.getInstance().init(DB_MAIN_INIT_SCRIPT_PATH);
            Server.getInstance().startServer();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}
