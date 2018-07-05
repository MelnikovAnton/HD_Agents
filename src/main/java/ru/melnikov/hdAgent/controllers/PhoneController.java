package ru.melnikov.hdAgent.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import ru.melnikov.hdAgent.ObservableResourceFactory;
import ru.melnikov.hdAgent.dao.GenerealDAO;
import ru.melnikov.hdAgent.dao.PhoneDao;
import ru.melnikov.hdAgent.models.Phone;
import ru.melnikov.hdAgent.models.PhoneType;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PhoneController extends GeneralController implements Initializable {



    @FXML private TableView<Phone> phonesTable;
    @FXML private ProgressBar phonesTableprogress;
    @FXML private Label eventPhoneLable;


    private MainController mainController;

    private GenerealDAO<Phone> phoneDao;

    private  String[] phoneList;

    private ObservableResourceFactory RESOURCE_FACTORY;



    public PhoneController() {
        super();
        System.out.println("Phone Controller constructor");
    }


    public void injectMainController(MainController mainController) {
        System.out.println("Phones ingect");
        this.mainController=mainController;
        this.phoneDao = new PhoneDao(mainController.getTerminal());
        this.phoneList=mainController.getPhoneList();
        generalTabPain=mainController.getMainTabPain();
        agentController=mainController.getAgentsTableTabController();
        phoneController=mainController.getPhonesTableTabController();

        RESOURCE_FACTORY=mainController.getRESOURCE_FACTORY();
        super.setRESOURCE_FACTORY(RESOURCE_FACTORY);
        postInject();

    }


    private void changeName(TableColumn.CellEditEvent<Phone,String> event) {
        String name = event.getNewValue();
        TablePosition<Phone, String> pos = event.getTablePosition();
        Phone phone = phonesTable.getItems().get(pos.getRow());
        System.out.println("change name from " + event.getOldValue() + " to " + name);
        phone.setName(name);
        changePhone(phone);

    }

    private void changePhone(Phone phone){
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                phoneDao.changeItem(phone);
                return null;
            }
        };
        mainController.setCurrentTask(task);
        startTask(task,"message.station.change","message.onFaild","message.station.changed",false);
    }

    public void refresh(){
        Task task = new Task<List<Phone>>() {

            @Override
            protected List<Phone> call() throws Exception {
                int i=0;
                List<Phone> rez = new ArrayList<>();
                int count=phoneList.length;
                for (String ext:phoneList){
                    rez.add(phoneDao.getItem(ext));
                    updateProgress(i+1,count);
                    updateMessage(ext);
                    i++;
                }
                return rez;
            }
        };
        mainController.setCurrentTask(task);
        task.setOnSucceeded(event -> {
            List<Phone> list = (List<Phone>)task.getValue();
            phonesTable.setItems(FXCollections.observableArrayList(list));
            setUI(null,Color.GREEN,"message.station.updated","");
        });
        startTaskNoSuccessMethod(task,"message.station.update","message.onFaild");
    }


    //---------getters-------------

    public ProgressBar getPhonesTableprogress() {
        return phonesTableprogress;
    }

    public Label getEventPhoneLable() {
        return eventPhoneLable;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("initialize....");


        System.out.println("Phone Controller FXML initialization...");

  //      eventPhoneLable.textProperty().bind();


    }

    private void postInject(){
        phonesTableprogress.setVisible(false);
        TableColumn<Phone, String> extColumn = new TableColumn<>();
        TableColumn<Phone, String> nameColumn = new TableColumn<>();
        TableColumn<Phone, PhoneType> typeColumn = new TableColumn<>();


        extColumn.setGraphic(bindLable("station.ext"));
        nameColumn.setGraphic(bindLable("station.name"));
        typeColumn.setGraphic(bindLable("station.type"));


        extColumn.setCellValueFactory(new PropertyValueFactory("ext"));
        nameColumn.setCellValueFactory(new PropertyValueFactory("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory("type"));


        phonesTable.getColumns().addAll(extColumn,nameColumn,typeColumn);

        phonesTable.setEditable(true);
        nameColumn.setEditable(true);

        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(this::changeName);
        phonesTable.setPlaceholder(bindLable("table.placeholder"));

        // phones type

        ObservableList<PhoneType> typeList = FXCollections.observableArrayList(PhoneType.values());

        typeColumn.setCellValueFactory(param -> {
            Phone phone = param.getValue();
            PhoneType type = (phone!=null)?phone.getType():PhoneType.uncknown;

            return new SimpleObjectProperty<>(type);
        });

        typeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(typeList));

        typeColumn.setOnEditCommit((TableColumn.CellEditEvent<Phone, PhoneType> event) -> {
            TablePosition<Phone, PhoneType> pos = event.getTablePosition();
            PhoneType newType = event.getNewValue();
            int row = pos.getRow();
            Phone phone = event.getTableView().getItems().get(row);
            phone.setType(newType);
            changePhone(phone);
        });
    }

    private Label bindLable(String key){
        Label lable=new Label();
        lable.textProperty().bind(RESOURCE_FACTORY.getStringBinding(key));
        return lable;
    }
}
