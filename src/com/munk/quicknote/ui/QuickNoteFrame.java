package com.munk.quicknote.ui;

import com.munk.quicknote.listener.IQuickActionListener;
import com.munk.quicknote.listener.IQuickEvent;
import com.munk.quicknote.listener.NoteEvent;
import com.munk.quicknote.models.NoteItem;
import com.munk.quicknote.models.ViewList;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;

/**
 * Created by kmayank on 3/27/16.
 */
public class QuickNoteFrame extends JFrame {
    private ViewList viewList;
    private DefaultListModel<NoteItem> listModel;
    private KeyAdapter jListKeyAdapter;
    public QuickNoteFrame(ViewList viewList, IQuickActionListener returnKeyListener){
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.viewList = viewList;
        viewList.addActionListener(new IQuickActionListener() {
            @Override
            public void actionPerformed(IQuickEvent e) {
                listModel.add(0,e.getNoteItem());
            }
        });
        JPanel buttonPanel = new JPanel();
        setContentPane(buttonPanel);

        GroupLayout layout = new GroupLayout(buttonPanel);
        getContentPane().setLayout(layout);


        JTextField field = new JTextField(30);
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterListFromString(field.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterListFromString(field.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterListFromString(field.getText());
            }
        });
        JButton note = new JButton("note");
        JButton clip = new JButton("clip");
        JButton all = new JButton("all");



        updateClipboardListModel(viewList);
        JList<NoteItem> stringList = new JList<>(listModel);
        stringList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScroller = new JScrollPane(stringList);

        field.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "Downkey");
        field.getActionMap().put("Downkey",new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                stringList.requestFocusInWindow();
                stringList.setSelectedIndex(0);
            }
        });

        stringList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER){
                    returnKeyListener.actionPerformed(new NoteEvent(stringList.getSelectedValue()));//TODO Pass selected NoteItem reference
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



    private void updateClipboardListModel(ViewList viewList){
        if (listModel==null){listModel=new DefaultListModel<NoteItem>();}
        else {listModel.removeAllElements();}
        for (NoteItem item: viewList.getNoteList()){
            listModel.add(0,item);
        }
    }

    private boolean filterListFromString(String filterString){
        listModel.removeAllElements();

        for(NoteItem item:viewList.getFilteredList(filterString)){
            listModel.add(0, item);
        }
        return true;
    }


}
