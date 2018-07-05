package ru.melnikov.hdAgent;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import ru.melnikov.hdAgent.controllers.MainController;
import ru.melnikov.hdAgent.ossi.MyOssiTerminal;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;


public class Main extends Application {

    private static final Logger log = Logger.getLogger(Main.class);


    private final KeyCombination keyComb = new KeyCodeCombination(KeyCode.F5, KeyCombination.SHIFT_ANY);

    private MyOssiTerminal terminal;

    private static final String PROPERTY_FILE_NAME = "/properties.xml";

    private static final Properties props = loadProperty();

    private boolean isTTerminal = false;

    private static Locale local;
    private static ResourceBundle bundle;


    @Override
    public void start(Stage primaryStage) throws Exception {

        log.info("Start app...");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));

        loader.setResources(bundle);

        Parent root = loader.load();

        MainController mainController = loader.getController();
        mainController.setPrimaryStage(primaryStage);


        primaryStage.setScene(new Scene(root));


        root.setOnKeyPressed(event -> {
            log.info("key pressed " + mainController);
            if (event.getCode().equals(KeyCode.F5)) mainController.refresh();
            if (event.getCode().equals(KeyCode.ESCAPE)) mainController.cancelCurrentTask();

        });


        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> mainController.exitApplication());



    }

    public static void main(String[] args) {
        if (args.length>0) {
           switch (args[0]){
               case "ENG":{
                   local=new Locale("ENG");
                   bundle= ResourceBundle.getBundle("bundle",local);
                   break;
               }
               case "RUS":{
                   local=new Locale("RUS");
                   bundle= ResourceBundle.getBundle("bundle",local);
                   break;
               }
           }
        } else {
            local=new Locale("RUS");
            bundle= ResourceBundle.getBundle("bundle",local);
        }

        launch(args);
    }

    public  MyOssiTerminal getTerminal(){
        System.out.println("get terminal " + terminal);
        return terminal;
    }

    public  void setTerminal(MyOssiTerminal terminal){
        System.out.println("set terminal " + terminal);
        this.terminal=terminal;
        isTTerminal=true;
    }



    private static Properties loadProperty()  {
        Properties properties = new Properties();
        try {
            properties.loadFromXML(Main.class.getResourceAsStream(PROPERTY_FILE_NAME));
        } catch (IOException e) {
          log.error("cannot load properties",e);
        }

        return properties;
    }


    public static Properties getProps() {
        return props;
    }


    public boolean getIsTerminal(){
        return isTTerminal;
    }
}
