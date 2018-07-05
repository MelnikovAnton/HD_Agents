package ru.melnikov.hdAgent.ossi;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;



import java.io.*;
import java.util.concurrent.TimeoutException;


class MySSH {

    private final JSch jSch;
    private Session sshSession;
    private Channel channel;

  private InputStreamReader input;
    private PrintWriter output;

    private boolean cancelTask =false;

    private final int TIME_OUT=3000;

    public MySSH(String username, String password,String host) {
        jSch = new JSch();
        JSch.setConfig("StrictHostKeyChecking", "no");
        try
        {
            sshSession = jSch.getSession(username,host,5022);
            sshSession.setPassword(password);
        }
        catch (JSchException e)
        {
            e.printStackTrace();
        }
    }

    public void connect() throws JSchException, IOException {
         sshSession.connect();
            this.channel=sshSession.openChannel("shell");
            this.channel.connect();
            output = new PrintWriter(channel.getOutputStream());
        //    input = new BufferedReader(new InputStreamReader(channel.getInputStream()));
        input = new InputStreamReader(channel.getInputStream());
    }

    public void disconect(){
        channel.disconnect();
        sshSession.disconnect();
    }

    public boolean isConnected(){
        if(channel==null||sshSession==null){
            return false;
        }else {
            return channel.isConnected() && sshSession.isConnected();
        }
    }

/*    public String readLine(){
        try {
            return input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }*/

    private String readChar() {


        try {
            return String.valueOf((char) input.read());
        } catch (IOException e) {
            System.out.println("IO exception in readChar.  Status input is " + input);
            e.printStackTrace();
            return "";
        }


    }

    public String readUntil(String s) throws TimeoutException {
        StringBuilder line = new StringBuilder();
        long timeout = System.currentTimeMillis() + TIME_OUT;
        do{

                line.append(readChar());


            if ((System.currentTimeMillis()>timeout)){
                throw new TimeoutException("timeout read ssh method");
            }
           if (Thread.currentThread().isInterrupted()) {
               System.out.println("Thread Interrupt");
               System.out.println("Thread chanel is connected " + channel.isConnected());
    //         throw new TimeoutException("test interrupt");
               Thread.currentThread().interrupt();
               return "";
           }

        }while (!line.toString().contains(s));
        System.out.println("<= " + line);
        return line.toString();
    }



    public void write(String line){
        System.out.println("=>" + line);
        output.print(line);
        output.flush();
    }

    //---------------------------


    public void setCancelTask(boolean cancel) {
        this.cancelTask = cancel;
    }
}

