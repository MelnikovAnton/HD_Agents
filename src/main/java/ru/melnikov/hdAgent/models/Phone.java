package ru.melnikov.hdAgent.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.HashMap;

public class Phone extends GeneralModel {

 //   public static final String qualifier = "station";

    private  StringProperty ext;

    private  StringProperty name;

    private PhoneType type;



    //-------------------Constructors -----------------------------

    public Phone(HashMap<String, String> fields) {
        super(fields);

    }

    @Override
    public void init() {
        ext =new SimpleStringProperty();
        name = new SimpleStringProperty();
        this.ext.setValue(fields.get("8005ff00"));
        this.name.setValue(fields.get("8003ff00"));
        this.type =PhoneType.getByType(fields.get("004fff00"));
     }

    @Override
    public HashMap<String, String> getMapFields() {
        fields.clear();
        fields.put("8005ff00",ext.getValue());
        fields.put("8003ff00",name.getValue());
        fields.put("004fff00",type.getType());
        return fields;
    }

    @Override
    public String command() {
        return "station";
    }

    @Override
    public String qualifier() {
        return this.ext.getValue();
    }


    @Override
    public String toString() {
        return (ext.getValue()!=null)?this.ext.getValue() + " " + this.name.getValue():"";
    }

    //-------------------------------------------------------------

    public HashMap<String, String> getMapName() {
        HashMap<String,String> map=new HashMap<>();
        map.put("8003ff00",name.getValue());
        return map;
    }

    public HashMap<String,String> getMapType() {
        HashMap<String,String> map=new HashMap<>();
        map.put("004fff00",type.getType());
        return map;
    }



    // ------------------Getters Setters-----------------------------


    public String getExt() {
        return ext.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }
    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public PhoneType getType() {
        return type;
    }

    public void setType(PhoneType type) {
        this.type = type;
    }


}
