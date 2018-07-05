package ru.melnikov.hdAgent.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TabPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.apache.log4j.Logger;
import ru.melnikov.hdAgent.ObservableResourceFactory;
import ru.melnikov.hdAgent.dao.GenerealDAO;
import ru.melnikov.hdAgent.ossi.MyOssiTerminal;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;

class GeneralController implements Initializable {

    private static final Logger log = Logger.getLogger(GeneralController.class);

    private  ResourceBundle bundle;

    private final String PROPERTY_FILE_NAME = "/properties.xml";
    GenerealDAO generealDAO;
    private MyOssiTerminal terminal;
    private  String[] agentList;
    private  String[] phoneList;
    private Task currentTask;
    private Label lable;
    private ProgressBar progress;
    TabPane generalTabPain;
    AgentController agentController;
    PhoneController phoneController;

    private  String HOST;
    private  String USER;
    private  String PASSWORD;

    private ObservableResourceFactory RESOURCE_FACTORY;



    void init(){
        log.info("General Controller initialization....");
        Properties props = null;
        try {
            props = loadProperty();
        } catch (IOException e) {
            log.error("Cannot load propperty ",e);
        }
        HOST = (String) Objects.requireNonNull(props).get("cm_host");
        USER = (String) Objects.requireNonNull(props).get("cm_user");
        PASSWORD = (String) Objects.requireNonNull(props).get("cm_password");
        this.terminal=new MyOssiTerminal(USER, PASSWORD, HOST);
        this.agentList = ((String) props.get("agents")).split(",");
        this.phoneList = ((String) props.get("stations")).split(",");
        this.generealDAO = new GenerealDAO(terminal);


    }

    private Properties loadProperty() throws IOException {
        Properties properties = new Properties();
        properties.loadFromXML(this.getClass().getResourceAsStream(PROPERTY_FILE_NAME));
        return properties;
    }

 /*   public void reconect() {
        currentTask = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                generealDAO.reconnect();
                return null;
            }
        };
        startTask(currentTask,"Подключение к CM...","Не удалось подключиться к CM","Подключено к CM",false);
    }*/

    public void refresh() {
        int tab = generalTabPain.getSelectionModel().getSelectedIndex();
        log.info("refresh tab " + tab);

        switch (tab) {
            case 0:
                agentController.refresh();
                break;
            case 1:
                phoneController.refresh();
                break;
        }
    }


    void startTask(Task task, String onAction, String onFailed, String onSucceeded, boolean addRezult) {
        setCurrentTask(task);
        setEnviroment();

        task.setOnRunning(event -> setUI(task.progressProperty(), Color.BLACK, onAction,""));
        task.setOnFailed(event -> setUI(null, Color.RED, onFailed,""));
        task.setOnSucceeded(event -> {
            Object value=task.getValue();
            setUI(null, Color.GREEN, onSucceeded, (value!=null)?(String) task.getValue():"");
        });
        Thread thread = new Thread(task);
        task.setOnCancelled(event -> onCancel(thread));

        thread.start();
    }

    void startTaskNoSuccessMethod(Task task, String onAction, String onFailed) {
        setCurrentTask(task);
        setEnviroment();
        task.setOnRunning(event -> setUI(task.progressProperty(), Color.BLACK, onAction,""));
        task.setOnFailed(event -> setUI(null, Color.RED, onFailed,""));
        Thread thread = new Thread(task);
        task.setOnCancelled(event -> onCancel(thread));
        thread.start();
    }



    private void onCancel(Thread thread){
        log.info("cancelling Task...");
        thread.interrupt();
        setUI(null,Color.RED,bundle.getString("message.cancel"),"");
    }

    void setUI(ObservableValue progress, Paint paint, String lable, String value) {

        if (progress != null) {
            this.progress.progressProperty().bind(progress);
            this.progress.setVisible(true);
        } else {
            this.progress.setVisible(false);
            this.progress.progressProperty().unbind();
        }
        this.lable.setTextFill(paint);
        if (value.length()>0){
            lable=RESOURCE_FACTORY.getStringBinding(lable).get()+" "+value;
            this.lable.textProperty().bind(new SimpleStringProperty(lable));
        } else this.lable.textProperty().bind(RESOURCE_FACTORY.getStringBinding(lable));
    }


    private void setEnviroment() {

        int tab = generalTabPain.getSelectionModel().getSelectedIndex();
        log.info("Setting Enviroment for tab" + tab);
        switch (tab) {
            case 0: {
                progress = agentController.getAgentTableprogress();
                lable = agentController.getEventAgentLable();
                break;
            }
            case 1: {
                progress = phoneController.getPhonesTableprogress();
                lable = phoneController.getEventPhoneLable();
                break;
            }
        }
        progress.progressProperty().unbind();
    }

    //------------Getters and Setters------------


    MyOssiTerminal getTerminal() {
        return terminal;
    }

    String[] getAgentList() {
        return agentList;
    }

    String[] getPhoneList() {
        return phoneList;
    }

    void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize....");
        this.bundle=resources;
    }

    void setRESOURCE_FACTORY(ObservableResourceFactory RESOURCE_FACTORY) {
        this.RESOURCE_FACTORY = RESOURCE_FACTORY;
    }
}
