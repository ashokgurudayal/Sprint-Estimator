/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sprintplannerjava;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author guruda
 */
class EchoThread extends Thread {
    protected Socket socket;
    
    public EchoThread(Socket s) {
        this.socket = s;
    }
    @Override
    public void run() {
        try {
            String msgin = "";
            DataInputStream din;
            DataOutputStream dout;
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());
            chatserver.dout.add(dout);
            while(!msgin.equals("exit")){
                try {
                    msgin = din.readUTF();
                    String name = msgin.split(":")[0];
                    int highestEstimate = 0;
                    chatserver.valuesList.put(msgin.split(":")[0], msgin.split(":")[1]);
                    for(String s : chatserver.valuesList.values()){
                        if(Integer.parseInt(s)>highestEstimate){
                            highestEstimate = Integer.parseInt(s);
                        }
                    }
                    chatserver.estimateValue = highestEstimate;
                    if(chatserver.moveButton.isEnabled()){
                        chatserver.highestEstimate.setText(Integer.toString(chatserver.estimateValue));
                        chatserver.dtm.setRowCount(0);
                        for (Map.Entry<String,String> entry : chatserver.valuesList.entrySet())
                            chatserver.dtm.addRow(new Object[] {entry.getKey(),entry.getValue()});
                    }
                    else{
                        chatserver.highestEstimate.setText("x");
                        chatserver.dtm.setRowCount(0);
                        for (Map.Entry<String,String> entry : chatserver.valuesList.entrySet())
                            chatserver.dtm.addRow(new Object[] {entry.getKey(),"X"});
                    }
                } catch (IOException ex) {
                    Logger.getLogger(EchoThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(EchoThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
