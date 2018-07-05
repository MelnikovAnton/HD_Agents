package ru.melnikov.hdAgent.dao;



import ru.melnikov.hdAgent.models.Phone;
import ru.melnikov.hdAgent.ossi.MyOssiTerminal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class PhoneDao extends GenerealDAO<Phone> {

    private final String DISPLAY="display station ";


    public PhoneDao(MyOssiTerminal terminal) {
        super(terminal);
    }

    @Override
    public Phone getItem(String qulifier) throws TimeoutException {
        ArrayList<HashMap<String, String>> result = terminal.sendCommand(DISPLAY + qulifier);
        return (result.size()>0)? new Phone(result.get(0)):null;
    }

}
