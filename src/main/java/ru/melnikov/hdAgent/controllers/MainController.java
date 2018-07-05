package ru.melnikov.hdAgent.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import ru.melnikov.hdAgent.ObservableResourceFactory;


import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;


public class MainController extends GeneralController implements Initializable {

    //controllers
    @FXML
    private MenuController menuController;
    @FXML
    private AgentController agentsTableTabController;
    @FXML
    private PhoneController phonesTableTabController;

    @FXML private TabPane mainTabPain;
    @FXML private Tab agentTab;
    @FXML private Tab phoneTab;

    @FXML
    private Stage primaryStage;

    private final ObservableResourceFactory RESOURCE_FACTORY = new ObservableResourceFactory();

    private Task currentTask;

    public MainController() {
       super();
       System.out.println("Main Controller constructor");
    }

    public void testConnection() {
        currentTask = new Task<String>() {
            @Override
            protected String call() throws Exception {
                return generealDAO.testConnection();
            }
        };
        startTask(currentTask,"message.connect",
                "message.conetionFailed",
                "messge.cmVersion",true);
    }

    @FXML
    public void exitApplication() {
        generealDAO.disconect();
        System.out.println("exiting");
        Platform.exit();
        System.exit(0);
    }

    @FXML
    public void cancelCurrentTask() {
        if (currentTask != null) {
            System.out.println("controller thread is " + Thread.currentThread().getName());
            System.out.println("cancel current task " + currentTask);
            currentTask.cancel();
            currentTask = null;
        }
    }

    public void changeLang(Locale locale){
        RESOURCE_FACTORY.setResources(ResourceBundle.getBundle("bundle", locale));
    }

    @FXML
    public void reconect() {
        Task task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                generealDAO.reconnect();
                return null;
            }
        };
        startTask(task, "message.connect",
                "message.conetionFailed",
                "message.connected", false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        System.out.println("initialize....");

        RESOURCE_FACTORY.setResources(resources);
        super.setRESOURCE_FACTORY(RESOURCE_FACTORY);

        System.out.println("Main controller initialize...");
        init();

        setLables();

        menuController.injectControllers(this);
        agentsTableTabController.injectMainController(this);
        phonesTableTabController.injectMainController(this);


        generalTabPain=mainTabPain;
        agentController=agentsTableTabController;
        phoneController=phonesTableTabController;

        super.setRESOURCE_FACTORY(RESOURCE_FACTORY);

// Connectiong to CM
        currentTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                generealDAO.connectCM();
                return null;
            }
        };
        startTask(currentTask,
                "message.connect",
                "message.conetionFailed",
                "message.connected",false);
    }

    private void setLables() {
        agentTab.textProperty().bind(RESOURCE_FACTORY.getStringBinding("tab.agents"));
        phoneTab.textProperty().bind(RESOURCE_FACTORY.getStringBinding("tab.stations"));
    }

    public void setPrimaryStage(Stage stage){
        this.primaryStage=stage;
        System.out.println(stage);
        StringProperty title = new SimpleStringProperty();
        title.bind(RESOURCE_FACTORY.getStringBinding("title"));
        stage.titleProperty().bind(title);
    }

    //------------Getters and Setters------------


    public AgentController getAgentsTableTabController() {
        return agentsTableTabController;
    }

    public PhoneController getPhonesTableTabController() {
        return phonesTableTabController;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public TabPane getMainTabPain() {
        return mainTabPain;
    }

    public ObservableResourceFactory getRESOURCE_FACTORY() {
        return RESOURCE_FACTORY;
    }


}
