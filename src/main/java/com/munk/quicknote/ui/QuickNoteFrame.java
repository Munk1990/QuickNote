package com.munk.quicknote.ui;

import com.munk.quicknote.listener.IQuickActionListener;
import com.munk.quicknote.listener.IQuickEvent;
import com.munk.quicknote.listener.NoteEvent;
import com.munk.quicknote.models.NoteItem;
import com.munk.quicknote.models.ViewList;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
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
    public static final int HEIGHT = 500;
    public static final int WIDTH = 500;

    private ViewList viewList;
    private NoteTableModel noteTableModel;

    private KeyAdapter jListKeyAdapter;


    private IQuickActionListener addToListButtonListener;
    public QuickNoteFrame(ViewList viewList, IQuickActionListener returnKeyListener){
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.viewList = viewList;
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
                    viewList.add(new NoteItem(new Date(), textArea.getText(),"notes"));
                    //addToListButtonListener.actionPerformed(new NoteEvent(textArea.getText(),"notes"));
                    textArea.selectAll();
                    textArea.grabFocus();
                }
            }
        });

        JScrollPane inputTextPane = new JScrollPane(textArea);
        inputTextPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        inputTextPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        updateClipboardListModel(viewList);
        noteTableModel.addElement(new NoteItem(new Date(),"test","notes"),0);
        noteTableModel.addElement(new NoteItem(new Date(),"test2","notes"),0);
        JList<NoteItem> stringList = new JList<>();
        JTable noteTable = new JTable(noteTableModel);
        noteTable.setDefaultRenderer(Object.class, noteTableModel);
        noteTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        noteTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        stringList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        noteTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER){
                    returnKeyListener.actionPerformed(new NoteEvent(stringList.getSelectedValue()));//TODO Pass selected NoteItem reference
                } else if (e.getKeyCode()==8) {
                    System.out.println("deleting..");
                    int [] selectedRows = noteTable.getSelectedRows();
                    NoteItem [] selectedItems = noteTableModel.getItemsAt(selectedRows);
                    viewList.remove(selectedItems);
                    noteTableModel.removeElements(selectedItems);
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
        JScrollPane listScroller = new JScrollPane(noteTable);
        //JScrollPane listScroller = new JScrollPane(stringList);

        listScroller.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        textArea.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "Downkey");
        textArea.getActionMap().put("Downkey",new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                stringList.requestFocusInWindow();
                stringList.setSelectedIndex(0);
            }
        });

        viewList.addActionListener(new IQuickActionListener() {
            @Override
            public void actionPerformed(IQuickEvent e) {
                if (textArea.getText().equals("")) {
                    noteTableModel.addElement(e.getNoteItem(), 0);
                }else {
                    filterListFromString(textArea.getText());
                }
            }
        });



        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(inputTextPane)
                                .addComponent(addToList)
                                .addGroup(layout.createSequentialGroup()
                                        .addComponent(note)
                                        .addComponent(clip)
                                        .addComponent(all))
                                .addComponent(listScroller))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                        .addComponent(inputTextPane)
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
        if (noteTableModel==null){noteTableModel=new NoteTableModel();}
        else {
            noteTableModel.removeAllElements();
        }
        for (NoteItem item: viewList.getNoteList()){
            noteTableModel.addElement(item, 0);
        }
    }

    private boolean filterListFromString(String filterString){
        noteTableModel.removeAllElements();
        for(NoteItem item:viewList.getFilteredList(filterString)){
            noteTableModel.addElement(item, 0);
        }
        return true;
    }


}
