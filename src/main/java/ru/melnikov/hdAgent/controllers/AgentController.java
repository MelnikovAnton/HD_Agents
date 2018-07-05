package ru.melnikov.hdAgent.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import org.apache.log4j.Logger;
import ru.melnikov.hdAgent.ObservableResourceFactory;
import ru.melnikov.hdAgent.dao.AgentsDAO;
import ru.melnikov.hdAgent.dao.GenerealDAO;
import ru.melnikov.hdAgent.models.Agent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;


public class AgentController extends GeneralController implements Initializable {

    private static final Logger log = Logger.getLogger(AgentController.class);


    @FXML
    private TableView<Agent> agentsTable;
    @FXML
    private ProgressBar agentTableprogress;
    @FXML
    private Label eventAgentLable;

    private String[] agentList;
    private MainController mainController;
    private GenerealDAO<Agent> agentsDao;

    private final SimpleStringProperty ext = new SimpleStringProperty();

    private ObservableResourceFactory RESOURCE_FACTORY;


    public AgentController() {
        super();
        log.info("Agent Controller constructor");
    }


    private void changeName(TableColumn.CellEditEvent<Agent, String> event) {
        String name = event.getNewValue();
        TablePosition<Agent, String> pos = event.getTablePosition();
        Agent agent = agentsTable.getItems().get(pos.getRow());
        log.info("change name from " + event.getOldValue() + " to " + name);
        log.info("event type " + event.getClass());
        agent.setName(name);
        Task task = new Task() {
            @Override
            protected Void call() throws Exception {
                agentsDao.changeItem(agent);
                return null;
            }
        };
        mainController.setCurrentTask(task);
        startTask(task,
               "message.agent.changing",
                "message.onFaild",
                "message.agent.changed", false);
    }


    public void injectMainController(MainController mainController) {
        log.info("Agent controller injection...");
        this.mainController = mainController;
        this.agentsDao = new AgentsDAO(mainController.getTerminal());
        this.agentList = mainController.getAgentList();
        generalTabPain = mainController.getMainTabPain();
        agentController = mainController.getAgentsTableTabController();
        phoneController = mainController.getPhonesTableTabController();

        RESOURCE_FACTORY=mainController.getRESOURCE_FACTORY();
        super.setRESOURCE_FACTORY(RESOURCE_FACTORY);
        postInject();

    }

    public void refresh() {

        Task task = new Task<List<Agent>>() {

            @Override
            protected List<Agent> call() throws Exception {
                int i = 0;
                List<Agent> rez = new ArrayList<>();
                int count = agentList.length;
                for (String ext : agentList) {
                    rez.add(agentsDao.getItem(ext));
                    updateProgress(i + 1, count);
                    i++;
                }
                return rez;
            }
        };
        mainController.setCurrentTask(task);
        task.setOnSucceeded(event -> onSuccess(task));
        startTaskNoSuccessMethod(task,
                "message.agent.update",
                "message.onFaild");
    }

    private void onSuccess(Task task) {
        List<Agent> list = (List<Agent>) task.getValue();
        agentsTable.setItems(FXCollections.observableArrayList(list));
        setUI(null, Color.GREEN,"message.agent.updated","");
    }

    public void changeLang(){
        log.info("before ext = " +ext.get());
        RESOURCE_FACTORY.setResources(ResourceBundle.getBundle("bundle", new Locale("ENG")));

        log.info("current value = " +RESOURCE_FACTORY.getStringBinding("agents.ext").get());

        log.info("after ext = " +ext.get());
    }

    private void postInject(){
        Label extHeader = new Label();
        extHeader.textProperty().bind(RESOURCE_FACTORY.getStringBinding("agents.ext"));
        Label nameHeader = new Label();
        nameHeader.textProperty().bind(RESOURCE_FACTORY.getStringBinding("agents.name"));


        TableColumn<Agent, String> extColumn = new TableColumn<>();
        extColumn.setGraphic(extHeader);

        TableColumn<Agent, String> nameColumn = new TableColumn<>();
        nameColumn.setGraphic(nameHeader);

        extColumn.setCellValueFactory(new PropertyValueFactory("extension"));
        nameColumn.setCellValueFactory(new PropertyValueFactory("name"));
        agentsTable.getColumns().addAll(extColumn, nameColumn);

        Label placeholder = new Label();
        placeholder.textProperty().bind(RESOURCE_FACTORY.getStringBinding("table.placeholder"));
        agentsTable.setPlaceholder(placeholder);

        agentsTable.setEditable(true);

        extColumn.setEditable(false);
        nameColumn.setEditable(true);

        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(this::changeName);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("Agent controller FXML initialazition...");
        agentTableprogress.setVisible(false);



       /* Label extHeader = new Label();
        extHeader.textProperty().bind(RESOURCE_FACTORY.getStringBinding("agents.ext"));
        Label nameHeader = new Label();
        nameHeader.textProperty().bind(RESOURCE_FACTORY.getStringBinding("agents.name"));


        TableColumn<Agent, String> extColumn = new TableColumn<>();
        extColumn.setGraphic(extHeader);

        TableColumn<Agent, String> nameColumn = new TableColumn<>();
        nameColumn.setGraphic(nameHeader);

        extColumn.setCellValueFactory(new PropertyValueFactory("extension"));
        nameColumn.setCellValueFactory(new PropertyValueFactory("name"));
        agentsTable.getColumns().addAll(extColumn, nameColumn);

        Label placeholder = new Label();
        placeholder.textProperty().bind(RESOURCE_FACTORY.getStringBinding("table.placeholder"));
        agentsTable.setPlaceholder(placeholder);

        agentsTable.setEditable(true);

        extColumn.setEditable(false);
        nameColumn.setEditable(true);

        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(this::changeName);*/
    }

    //-------------getters-------

    public ProgressBar getAgentTableprogress() {
        return agentTableprogress;
    }

    public Label getEventAgentLable() {
        return eventAgentLable;
    }
}
