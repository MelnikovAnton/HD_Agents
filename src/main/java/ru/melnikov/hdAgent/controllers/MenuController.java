package ru.melnikov.hdAgent.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.apache.log4j.Logger;
import ru.melnikov.hdAgent.ObservableResourceFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class MenuController extends GeneralController implements Initializable {

    private static final Logger log = Logger.getLogger(MenuController.class);

    private ResourceBundle bundle;

    private MainController mainController;
    private AgentController agentController;
    private PhoneController phoneController;

    @FXML private Menu menu_connection;
    @FXML private ToggleGroup selectLang;
    @FXML private Menu menu_actions;
    @FXML private Menu menu_lang;
    @FXML private MenuItem testConnection;
    @FXML private MenuItem re_connect;
    @FXML private MenuItem exitApp;
    @FXML private MenuItem refreshAgents;
    @FXML private MenuItem refreshPhones;
    @FXML private MenuItem cancelCurrent;
    @FXML private RadioMenuItem RUS;
    @FXML private RadioMenuItem ENG;




    private ObservableResourceFactory RESOURCE_FACTORY;

    public MenuController() {
        super();
    }

    public void injectControllers(MainController mainController){
        this.mainController=mainController;
        this.agentController=mainController.getAgentsTableTabController();
        this.phoneController=mainController.getPhonesTableTabController();

        RESOURCE_FACTORY=mainController.getRESOURCE_FACTORY();

        setLables();
    }

    public void reconnect(){
        mainController.reconect();
    }

    public void exitApplication(){
        mainController.exitApplication();
    }

    public void refreshAgents(){
        agentController.refresh();
    }


    public void refreshPhones(){
        phoneController.refresh();
    }

    public void canselCurrent(){
        mainController.cancelCurrentTask();
    }

    public void testConnection(){
       mainController.testConnection();
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize....");

        menu_lang.setVisible(true);
        RUS.setVisible(true);
        ENG.setVisible(true);

        selectLang.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            RadioMenuItem node = (RadioMenuItem) selectLang.getSelectedToggle();

            if (node!=null){
                String lang=node.getId();
                mainController.changeLang(new Locale(lang));
                      }
        });
    }

    private void setLables(){
        menu_connection.setGraphic(bindLable("menu.connection"));

        testConnection.textProperty().bind(RESOURCE_FACTORY.getStringBinding("menu.connection.testConnection"));
        re_connect.textProperty().bind(RESOURCE_FACTORY.getStringBinding("menu.connection.Re-connect"));
        exitApp.textProperty().bind(RESOURCE_FACTORY.getStringBinding("menu.connection.exit"));

        menu_actions.setGraphic(bindLable("menu.actions"));
        refreshAgents.textProperty().bind(RESOURCE_FACTORY.getStringBinding("menu.actions.refreshAgents"));
        refreshPhones.textProperty().bind(RESOURCE_FACTORY.getStringBinding("menu.actions.refreshStations"));
        cancelCurrent.textProperty().bind(RESOURCE_FACTORY.getStringBinding("menu.actions.cancelCurrent"));

        menu_lang.setGraphic(bindLable("menu.lang"));
       RUS.textProperty().bind(RESOURCE_FACTORY.getStringBinding("menu.lang.ru"));
       ENG.textProperty().bind(RESOURCE_FACTORY.getStringBinding("menu.lang.en"));
    }

    private Label bindLable(String key){
        Label lable=new Label();
        lable.textProperty().bind(RESOURCE_FACTORY.getStringBinding(key));
        return lable;
    }
}
