package com.munk.quicknote.listener;

import com.munk.quicknote.loader.Main;
import com.munk.quicknote.models.NoteItem;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by kmayank on 3/27/16.
 */
public class ClipboardListener implements Runnable {
    Boolean isRunning;
    @Override
    public void run() {
        isRunning = true;
        TrayIcon item = new TrayIcon(Toolkit.getDefaultToolkit().getImage("res/notes-512x512.png"));
        PopupMenu menu = new PopupMenu();

        MenuItem exitMenuItem = new MenuItem("Exit");
        menu.add(exitMenuItem);
        item.setPopupMenu(menu);
        try {
            SystemTray.getSystemTray().add(item);
        } catch (AWTException e) {
            e.printStackTrace();
            System.exit(1);
        }

        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(false);
                isRunning=false;
                System.exit(0);//bad method of exit
            }
        });

      String pastData="";
      String data = "";
            while (isRunning) {

                try {
                    data = (String) Toolkit.getDefaultToolkit()
                            .getSystemClipboard().getData(DataFlavor.stringFlavor);
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!data.equals(pastData)){
                    Main.addToList(data);
                    pastData=data;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    }

}
