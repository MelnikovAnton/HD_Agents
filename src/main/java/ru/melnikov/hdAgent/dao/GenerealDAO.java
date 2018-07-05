package ru.melnikov.hdAgent.dao;

import com.jcraft.jsch.JSchException;
import org.apache.log4j.Logger;
import ru.melnikov.hdAgent.models.GeneralModel;
import ru.melnikov.hdAgent.ossi.MyOssiTerminal;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class GenerealDAO<T extends GeneralModel> {

    private static final Logger log = Logger.getLogger(GenerealDAO.class);

    final MyOssiTerminal terminal;

    private T type;


    public GenerealDAO(MyOssiTerminal terminal) {
        log.info("Set terminal "+ terminal); ;
        this.terminal=terminal;
    }

    public void connectCM() throws InterruptedException, JSchException, TimeoutException, IOException {
       terminal.connectCM();
    }


    public void disconect(){
        terminal.disconect();
    }

    public void reconnect() throws TimeoutException {
        disconect();
        try {
            terminal.connectCM();
        } catch (JSchException | InterruptedIOException | InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String testConnection() throws TimeoutException {
        ArrayList<HashMap<String, String>> rez = terminal.sendCommand("list configuration software-versions ");
        for (HashMap<String, String>item: rez) {
            for (String key:item.keySet()) {
                log.info("key:" + key+ " value:"+item.get(key));
            }
        }
    //    rez.get(0).get("0faeff00");
        return rez.get(0).get("0faeff00");
    }

    public T getItem(String qulifier) throws TimeoutException {
        // This method must be Overryde
        return null;
    }


    public void changeItem(T current) throws TimeoutException {
        String command="change " + current.command() +  " " + current.qualifier();
        terminal.sendCommand(command, current.getMapFields());
    }

    public String qualifier(){
        return type.qualifier();
    }

    public String element(){
        return type.command();
    }



}
