package com.munk.quicknote.listener;

import com.munk.quicknote.models.NoteItem;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kmayank on 4/16/16.
 */
public class QuickClipboardDaemon implements IEventListener, Runnable {
    private static final int POLLING_SLEEP_DURATION = 35;
    String pastData;
    String curData;
    List<IQuickActionListener> actionListenerList = new ArrayList<>();
    boolean isRunning = false;

    public boolean startListening() {
        Thread t = new Thread(this);
        t.start();
        return true;
    }

    @Override
    public void addActionListener(IQuickActionListener listener) {
        actionListenerList.add(listener);
    }

    @Override
    public void run() {
        isRunning= true;
        curData = "";
        pastData = "";
        while (isRunning) {
            try {
                curData = (String) Toolkit.getDefaultToolkit()
                        .getSystemClipboard().getData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!curData.equals(pastData)){
                performActionOnListeners(curData);
                pastData=curData;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean stopDaemon(){
        isRunning=false;
        return true;
    }


    public void copyToClipboard(String noteString){
        System.out.println(String.format("Copying \"%s\" to clipboard", noteString));
        if(noteString!=null&&noteString.trim().length()>0){
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(noteString), new ClipboardOwner() {
                @Override
                public void lostOwnership(Clipboard clipboard, Transferable contents) {

                }
            });
        }
    }

    private void performActionOnListeners(String data){
        IQuickEvent clipBoardEvent = new NoteEvent(data,"clipboard");
        for (IQuickActionListener actionListener:actionListenerList){
            actionListener.actionPerformed(clipBoardEvent);
        }

    }
}
