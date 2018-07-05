package ru.melnikov.hdAgent.models;

import java.util.HashMap;

public abstract class GeneralModel {

    HashMap<String,String> fields=new HashMap<>();

    public GeneralModel(HashMap<String, String> map) {
        fields.clear();
        fields.putAll(map);
        init();
    }

    protected abstract void init();

    public abstract HashMap<String, String> getMapFields();

    public HashMap<String, String> getFields() {
        return fields;
    }

    public void setFields(HashMap<String, String> fields) {
        this.fields = fields;
    }


    public abstract  String command();

    public abstract String qualifier();
}
