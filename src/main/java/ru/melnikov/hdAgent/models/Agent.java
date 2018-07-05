package ru.melnikov.hdAgent.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.log4j.Logger;

import java.util.HashMap;

public class Agent extends GeneralModel {

    private static final Logger log = Logger.getLogger(Agent.class);

    private StringProperty name;//= new SimpleStringProperty();
    private StringProperty extension;// = new SimpleStringProperty();
    private StringProperty password;// = new SimpleStringProperty();
    private StringProperty passwordConfirm;// = new SimpleStringProperty();
    private Skill[] skills;// = new Skill[120];
//    private HashMap<String, String> fields = new HashMap<>();


    public HashMap<String, String> getMapName() {
        HashMap<String, String> map = new HashMap<>();
        map.put("0fa3ff00", name.getValue());
        return map;
    }


    @Override
    public HashMap<String, String> getMapFields() {
        fields.clear();
        fields.put("0fa3ff00", name.getValue());
        fields.put("0fa1ff00", extension.getValue());
        fields.put("0fa5ff00", password.getValue());
        fields.put("0fa6ff00", passwordConfirm.getValue());
        for (int i = 0; i < 120; i++) {
            String index = Integer.toHexString(i + 1);
            fields.put("8fdeff" + index, skills[i].skillNumber);
            fields.put("7800ff" + index, skills[i].rl);
            fields.put("ce2dff" + index, skills[i].skillLevel);
        }
        return fields;
    }

    @Override
    public String command() {
        return "agent-loginID";
    }

    @Override
    public String qualifier() {
        return this.extension.getValue();
    }

    public Agent(HashMap<String, String> map) {
        super(map);


    }




    @Override
    public void init() {

        name= new SimpleStringProperty();
        extension = new SimpleStringProperty();
        password = new SimpleStringProperty();
        passwordConfirm= new SimpleStringProperty();
        skills = new Skill[120];


        log.info(name);

        name.setValue(fields.get("0fa3ff00"));
        extension.setValue(fields.get("0fa1ff00"));
        password.setValue(fields.get("0fa5ff00"));
        passwordConfirm.setValue(fields.get("0fa6ff00"));
        for (int i = 0; i < 120; i++) {
            String index = Integer.toHexString(i + 1);
            StringBuilder zero = new StringBuilder("00");
            index = zero.replace((2 - index.length()), 2, index).toString();
            Skill skill = new Skill();
            skill.setSkillNumber(fields.get("8fdeff" + index));
            skill.setRl(fields.get("7800ff" + index));
            skill.setSkillLevel(fields.get("ce2dff" + index));
            skills[i] = skill;
        }

    }

    @Override
    public String toString() {
        return (extension.getValue() != null) ? this.extension.getValue() + " " + this.name.getValue() : "";
    }


    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getExtension() {
        return extension.get();
    }

    public StringProperty extensionProperty() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension.set(extension);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getPasswordConfirm() {
        return passwordConfirm.get();
    }

    public StringProperty passwordConfirmProperty() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm.set(passwordConfirm);
    }

    public Skill[] getSkills() {
        return this.skills;
    }


    private class Skill {
        private String skillNumber;
        private String rl;
        private String skillLevel;

        public String getSkillNumber() {
            return skillNumber;
        }

        void setSkillNumber(String skillNumber) {
            this.skillNumber = skillNumber;
        }

        public String getRl() {
            return rl;
        }

        void setRl(String rl) {
            this.rl = rl;
        }

        public String getSkillLevel() {
            return skillLevel;
        }

        void setSkillLevel(String skillLevel) {
            this.skillLevel = skillLevel;
        }

        @Override
        public String toString() {
            return (skillNumber.length() > 0) ? ("Скилл: " + this.skillNumber + " уровень: " + this.skillLevel) : "";
        }
    }

}
