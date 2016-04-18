package com.munk.quicknote.loader;

import com.munk.quicknote.listener.IQuickActionListener;
import com.munk.quicknote.listener.IQuickEvent;
import com.munk.quicknote.listener.QuickClipboardDaemon;
import com.munk.quicknote.models.NoteItem;
import com.munk.quicknote.models.ViewList;
import com.munk.quicknote.ui.QuickNoteFrame;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;

/**
 * Created by kmayank on 4/17/16.
 */
public class QuickNoteApp {
    QuickNoteFrame mainFrame;
    QuickClipboardDaemon clipboardDaemon;
    ViewList guiViewList;
    public static void main (String [] xargs){
        QuickNoteApp noteApp = new QuickNoteApp();
        noteApp.initiateApp();
    }

    public void initiateApp(){
        guiViewList= new ViewList();
        initDaemon();
        initQuickFrame();
        setupTrayIcon();
    }

    private void initDaemon(){
        clipboardDaemon = new QuickClipboardDaemon();
        clipboardDaemon.addActionListener(new IQuickActionListener() {
            @Override
            public void actionPerformed(IQuickEvent e) {
                guiViewList.add(e.getNoteItem());
            }
        });
        clipboardDaemon.startListening();
    }


    private void setupTrayIcon(){
        TrayIcon item = new TrayIcon(Toolkit.getDefaultToolkit().getImage("res/notes-512x512.png"));
        PopupMenu menu = new PopupMenu();

        MenuItem exitMenuItem = new MenuItem("Exit");
        MenuItem showClipboardItem = new MenuItem("Show clipboard");
        menu.add(showClipboardItem);
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
                //isRunning=false;
                System.exit(0);//bad method of exit
            }
        });
        showClipboardItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initQuickFrame();
            }
        });
    }

    private void initQuickFrame(){
        QuickNoteApp noteApp = this;
        if (mainFrame==null) {
            mainFrame = new QuickNoteFrame(guiViewList, new IQuickActionListener() {
                @Override
                public void actionPerformed(IQuickEvent e) {
                    clipboardDaemon.copyToClipboard(e.getNoteItem());
                    noteApp.mainFrame.setVisible(false);//todo: Best way to kill the app?
                }
            });
            mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("res/notes-512x512.png"));
            mainFrame.setTitle("Quicknote");
        }
        mainFrame.setVisible(true);//Todo: Find a way to bring the app to foreground, not working on Mac
    }


}
