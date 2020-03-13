package org.rbusjahn.compactdao;

import org.apache.log4j.Logger;

import java.sql.DriverManager;

public class DatabaseShutdown {

    private static Logger log = Logger.getLogger(DatabaseShutdown.class);

    public String start() {
        String exceptionMessage = "";
        try {
            log.info("shuting down DerbyDB");
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
            log.info("shuting down DerbyDB - done");
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
        }

        return exceptionMessage;
    }

    public static void register(){
        try {
            final DatabaseShutdown app = new DatabaseShutdown();

            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    log.info("start shutdown hook");
                    String message = app.start();
                    log.info("database shutdown response: " + message);
                    log.info("shutdown hook - done");
                }
            }));
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }
}
