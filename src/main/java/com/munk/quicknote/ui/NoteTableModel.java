package com.munk.quicknote.ui;

import com.munk.quicknote.models.NoteItem;
import com.munk.quicknote.models.ViewList;
import com.sun.tools.javap.TypeAnnotationWriter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kmayank on 12/06/16.
 */
public class NoteTableModel extends AbstractTableModel implements TableCellRenderer{

    private SimpleDateFormat sdf =new SimpleDateFormat("HH:mm, dd MMM");
    private String[] COLUMN_LIST = new String[] {"Creation Date", "Data"};

    List<NoteItem> noteList;


    public void addElement(NoteItem item, int pos){
        noteList.add(0, item);
        fireTableRowsInserted(pos, pos);
    };

    public void removeElement(NoteItem item){
        int pos = noteList.indexOf(item);
        fireTableRowsDeleted(pos, pos);
        noteList.remove(item);
    }

    public void removeElements(NoteItem[] items){
        for (NoteItem i: items){
            removeElement(i);
        }

    }



    public void removeAllElements(){
        noteList.clear();
    }


    public NoteItem[] getItemsAt(int[] indexes){
        NoteItem[] items = new NoteItem[indexes.length];
        for (int i = 0;i < indexes.length; i++){
            items[i] = noteList.get(indexes[i]);
        }
        return items;
    }

    public NoteItem getItemsAt(int index){
        return noteList.get(index);
    }


    public NoteTableModel(){
        noteList = new ArrayList<NoteItem>();
    }


    @Override
    public int getRowCount() {
        return noteList.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_LIST.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_LIST[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex==0){
            return sdf.format(noteList.get(rowIndex).getCreationDate());
        } else {
            return noteList.get(rowIndex).getNoteContent();
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        DefaultTableCellRenderer dr = new DefaultTableCellRenderer();
        Component c = dr.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(column==0){
            ;
        }
        return c;
    }

    private static Color getColor(int daysFromToday){
        255/30*
    }
}
