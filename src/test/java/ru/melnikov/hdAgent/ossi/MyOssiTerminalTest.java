package ru.melnikov.hdAgent.ossi;

import com.jcraft.jsch.JSchException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import ru.melnikov.hdAgent.Main;
import ru.melnikov.hdAgent.models.Phone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MyOssiTerminalTest {

    private static  MyOssiTerminal terminal;
    private static Properties properties = new Properties();

    @BeforeClass
    public static void init() throws InterruptedException, TimeoutException, JSchException, IOException {
         try {
             properties.loadFromXML(MyOssiTerminal.class.getResourceAsStream("/properties.xml"));
        } catch (IOException e) {
           e.printStackTrace();
        }

        terminal = new MyOssiTerminal(properties.getProperty("cm_user")
                ,properties.getProperty("cm_password")
                ,properties.getProperty("cm_host"));
        terminal.connectCM();

    }

@AfterClass
public static void clean(){
        terminal.disconect();
}



    @Test(expected = JSchException.class)
    public void wrongConnect() throws InterruptedException, TimeoutException, JSchException, IOException {
        new MyOssiTerminal("bad_user","badPassword_password",properties.getProperty("cm_host")).connectCM();
    }

    @Test(expected = JSchException.class)
    public void wrongHostConnect() throws InterruptedException, TimeoutException, JSchException, IOException {
        new MyOssiTerminal("bad_user","badPassword_password","wrongHost").connectCM();
    }

    @Test
    public void sendCommand() throws TimeoutException {
        ArrayList<HashMap<String, String>> rez = terminal.sendCommand("list configuration software-versions ");
        assertEquals("R016x.03.0.124.0",rez.get(0).get("0faeff00"));
    }

    @Test
    public void sendWrongCommand() throws TimeoutException {
        ArrayList<HashMap<String, String>> rez = terminal.sendCommand("list configuration software-version1s ");
        assert(rez.isEmpty());
    }


    @Test
    public void phoneRename() throws TimeoutException {
        String testExt= (String) properties.get("testPhone");
        String oldName = terminal.sendCommand("display station " + testExt )
                .get(0)
                .get("8003ff00");

        System.out.println(oldName);

        HashMap<String, String> testField = new HashMap<String, String>();
        testField.put("8003ff00","testName");

        terminal.sendCommand("change station " + testExt,testField);

        String newName = terminal.sendCommand("display station " + testExt )
                .get(0)
                .get("8003ff00");

        testField.put("8003ff00",oldName);

        terminal.sendCommand("change station " + testExt,testField);

        assertEquals("testName",newName);


    }

    @Test(expected = TimeoutException.class)
    public void zZ_timeoutTest() throws TimeoutException {
        terminal.disconect();
        terminal.sendCommand("list configuration software-versions ");
    }



}