package com.munk.quicknote.loader;

import com.munk.quicknote.listener.IQuickActionListener;
import com.munk.quicknote.listener.IQuickEvent;
import com.munk.quicknote.listener.QuickClipboardDaemon;
import com.munk.quicknote.models.NoteItem;
import com.munk.quicknote.models.ViewList;
import com.munk.quicknote.persistence.FileWriter;
import com.munk.quicknote.ui.QuickNoteFrame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by kmayank on 4/17/16.
 */
public class QuickNoteApp {
    private static final String FILENAME = "Quicknote.save";
    private static final String FILEPATH = System.getProperty("user.home")+"/"+FILENAME;
    private static final String ICON_PATH = "/notes-512x512.png";
    QuickNoteFrame mainFrame;
    QuickClipboardDaemon clipboardDaemon;
    ViewList guiViewList;
    public static void main (String [] xargs){
        System.out.println(FILEPATH);
        QuickNoteApp noteApp = new QuickNoteApp();
        noteApp.initiateApp();
    }

    public void initiateApp(){
        try {
            guiViewList= FileWriter.getViewListFromFile(FILEPATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        initDaemon();
        initQuickFrame();
        try {
            setupTrayIcon();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void initDaemon(){
        clipboardDaemon = new QuickClipboardDaemon();
        clipboardDaemon.addActionListener(new IQuickActionListener() {
            @Override
            public void actionPerformed(IQuickEvent e) {
                if (!guiViewList.hasItemWithString(e.getNoteItem().getNoteContent())) {
                    guiViewList.add(e.getNoteItem());
                    try {
                        FileWriter.saveViewListToFile(FILEPATH, guiViewList);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        clipboardDaemon.startListening();
    }


    private void setupTrayIcon() throws IOException, URISyntaxException {
        Image trayIconImage = ImageIO.read(this.getClass().getResource(ICON_PATH).toURI().toURL().openStream());

        TrayIcon item = new TrayIcon(trayIconImage);
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
                try {
                    FileWriter.saveViewListToFile(FILEPATH,guiViewList);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
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
                    clipboardDaemon.copyToClipboard(e.getNoteItem().toString());
                    noteApp.mainFrame.setVisible(false);//todo: Best way to kill the app?
                }
            });
            mainFrame.setNoteAddButtonListener(new IQuickActionListener() {
                @Override
                public void actionPerformed(IQuickEvent e) {
                    guiViewList.add(e.getNoteItem());
                }
            });
            mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("res/notes-512x512.png"));
            mainFrame.setTitle("Quicknote");
        }
        mainFrame.setVisible(true);//Todo: Find a way to bring the app to foreground, not working on Mac
    }


}
