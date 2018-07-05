package ru.melnikov.hdAgent.ossi;


import com.jcraft.jsch.JSchException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeoutException;


public class MyOssiTerminal {

    private MySSH sshClient;

    private final String username;
    private final String password;
    private final String host;

    public MyOssiTerminal(String username, String password, String host) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.sshClient = new MySSH(username, password, host);
    }

    public void connectCM() throws JSchException, TimeoutException, InterruptedException, IOException {

        if (!sshClient.isConnected()) {
            sshClient = new MySSH(username, password, host);
            sshClient.connect();

            Thread.sleep(500);

            sshClient.readUntil("[513]");

            sshClient.write("ossi\n");

            sshClient.readUntil("t");
        }

    }

    public void disconect() {
        if (sshClient.isConnected()) sshClient.disconect();
    }

    public ArrayList<HashMap<String, String>> sendCommand(String command) throws TimeoutException {

        return sendCommand(command, new HashMap<>());
    }

    public ArrayList<HashMap<String, String>> sendCommand(String command, HashMap<String, String> params) throws TimeoutException {

        ArrayList<HashMap<String, String>> rezultArray = new ArrayList<>();
        sshClient.write("c" + command + "\n");
        if (params.size() > 0) {
            Set<String> pFields = params.keySet();
            String filedsString = "f";
            String valuesString = "d";
            for (String key : pFields) {
                filedsString = filedsString.concat(key + "\t");
                valuesString = valuesString.concat(params.get(key) + "\t");
            }
            sshClient.write(filedsString);
            sshClient.write("\n");
            sshClient.write(valuesString);
            sshClient.write("\n");

        }
        sshClient.write("t\n");

        String rezultString = sshClient.readUntil("t\n");
        String[] lines = rezultString.split("\n");
        ArrayList<ArrayList<String>> dataArray = new ArrayList<>();
        ArrayList<String> dataLine = new ArrayList<>();
        ArrayList<String> fieldLine = new ArrayList<>();
        int count = 0;
        for (String line : lines) {
            if (line.startsWith("d")) {
                dataLine.add(line.substring(1));
            }
            if (line.startsWith("f")) {
                fieldLine.add(line.substring(1));
            }
            if (line.startsWith("n")) {
                dataArray.add(dataLine);
                dataLine = new ArrayList<>();
                count = count + 1;
            }
        }
        if (!dataLine.isEmpty()) dataArray.add(dataLine);


        for (ArrayList<String> dataLn : dataArray) {
            HashMap<String, String> rezult = new HashMap<>();
            for (String item : dataLn) {
                for (int i = 0; i < fieldLine.size(); i++) {
                    String[] lineFields = fieldLine.get(i).split("\t");
                    String[] lineData = dataLn.get(i).split("\t");
                    for (int j = 0; j < lineFields.length; j++) {
                        if (j >= lineData.length) {
                            rezult.put(lineFields[j], "");
                        } else {
                            rezult.put(lineFields[j], lineData[j]);
                        }
                    }
                }
            }
            rezultArray.add(rezult);
        }
        return rezultArray;
    }

    public void cancel(boolean cancel){
        sshClient.setCancelTask(cancel);
    }

}
