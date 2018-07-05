package ru.melnikov.hdAgent.dao;



import ru.melnikov.hdAgent.models.Agent;
import ru.melnikov.hdAgent.ossi.MyOssiTerminal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class AgentsDAO extends GenerealDAO<Agent>{


    private final String DISPLAY="display agent-loginID ";

    public AgentsDAO(MyOssiTerminal terminal) {
        super(terminal);
    }



    @Override
    public Agent getItem(String qulifier) throws TimeoutException {
        ArrayList<HashMap<String, String>> result = terminal.sendCommand(DISPLAY + qulifier);
        return (result.size()>0)? new Agent(result.get(0)):null;
    }


}
