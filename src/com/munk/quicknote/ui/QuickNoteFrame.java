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
    public static final int HEIGHT = 500;
    public static final int WIDTH = 500;

    private ViewList viewList;
    private DefaultListModel<NoteItem> listModel;
    private KeyAdapter jListKeyAdapter;


    private IQuickActionListener addToListButtonListener;
    public QuickNoteFrame(ViewList viewList, IQuickActionListener returnKeyListener){
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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


        JTextArea textArea = new JTextArea(5,30);
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterListFromString(textArea.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterListFromString(textArea.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterListFromString(textArea.getText());
            }
        });
        JButton note = new JButton("note");
        JButton clip = new JButton("clip");
        JButton all = new JButton("all");
        JButton addToList = new JButton("Add as note");
        addToList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textArea.getText().trim().length() > 0) {
                    addToListButtonListener.actionPerformed(new NoteEvent(textArea.getText()));
                    textArea.selectAll();
                    textArea.grabFocus();
                }
            }
        });

        JScrollPane inputTextPane = new JScrollPane(textArea);


        updateClipboardListModel(viewList);
        JList<NoteItem> stringList = new JList<>(listModel);
        stringList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        stringList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER){
                    returnKeyListener.actionPerformed(new NoteEvent(stringList.getSelectedValue()));//TODO Pass selected NoteItem reference
                } else if (e.getKeyCode()==8) {
                    System.out.println("deleting..");
                    viewList.remove(stringList.getSelectedValue());
                    listModel.removeElement(stringList.getSelectedValue());
                }
            }
        });
        for (char c = 'a';c < 'z'; c++){
            stringList.getInputMap().put(KeyStroke.getKeyStroke(c), c);
            stringList.getActionMap().put(c, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Todo: When setting single character, it remains selected
                    textArea.setText(e.getActionCommand()+"<Continue typing your query>");
                    textArea.grabFocus();
                    textArea.setCaretPosition(1);
                    int end = textArea.getColumns();
                    textArea.setSelectionStart(1);
                    textArea.setSelectionEnd(end);
                }
            });
        }
        JScrollPane listScroller = new JScrollPane(stringList);
        listScroller.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        textArea.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "Downkey");
        textArea.getActionMap().put("Downkey",new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                stringList.requestFocusInWindow();
                stringList.setSelectedIndex(0);
            }
        });



        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(textArea)
                                .addComponent(addToList)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(note)
                                        .addComponent(clip)
                                        .addComponent(all))
                                .addComponent(listScroller))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                        .addComponent(textArea)
                        .addComponent(addToList)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(note)
                                .addComponent(clip)
                                .addComponent(all))
                        .addComponent(listScroller)
        );
        pack();
    }

    public void setNoteAddButtonListener(IQuickActionListener addToListButtonListener){
        this.addToListButtonListener = addToListButtonListener;
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
