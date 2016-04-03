package com.munk.quicknote.ui;

import com.munk.quicknote.loader.Main;
import com.munk.quicknote.models.NoteItem;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.util.Date;

/**
 * Created by kmayank on 3/27/16.
 */
public class QuickNoteFrame extends JFrame {
    public static DefaultListModel<NoteItem> listModel;
    public QuickNoteFrame(){

        JPanel buttonPanel = new JPanel();
        setContentPane(buttonPanel);

        GroupLayout layout = new GroupLayout(buttonPanel);
        getContentPane().setLayout(layout);


        JTextField field = new JTextField(30);
        field.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Main.filterListFromString(field.getText());
                //System.out.println(field.getText());
            }
        });
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                Main.filterListFromString(field.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Main.filterListFromString(field.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                Main.filterListFromString(field.getText());
            }
        });
        JButton note = new JButton("note");
        JButton clip = new JButton("clip");
        JButton all = new JButton("all");
        listModel = new DefaultListModel();
        listModel.addElement(new NoteItem(new Date(), "one"));






        JList<NoteItem> stringList = new JList<>(listModel);
        stringList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScroller = new JScrollPane(stringList);

        field.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "Downkey");
        field.getActionMap().put("Downkey",new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                stringList.requestFocusInWindow();
            }
        });

        stringList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER){
                    copyToClipboard(stringList.getSelectedValue());
                }
            }
        });



        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(field)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(note)
                                        .addComponent(clip)
                                        .addComponent(all))
                                .addComponent(listScroller))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                        .addComponent(field)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(note)
                                .addComponent(clip)
                                .addComponent(all))
                        .addComponent(listScroller)
        );
        pack();

    }

    public static void copyToClipboard(NoteItem item){
        System.out.println(String.format("Copying \"%s\" to clipboard",item.getNoteContent()));
        if(item!=null&&item.getNoteContent()!=null&&item.getNoteContent().trim().length()>0){
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(item.getNoteContent()), new ClipboardOwner() {
                @Override
                public void lostOwnership(Clipboard clipboard, Transferable contents) {

                }
            });
        }
    }
}
