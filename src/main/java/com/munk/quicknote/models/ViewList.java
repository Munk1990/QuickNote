package com.munk.quicknote.models;

import com.munk.quicknote.listener.IQuickActionListener;
import com.munk.quicknote.listener.NoteEvent;

import java.util.*;

/**
 * Created by kmayank on 3/31/16.
 */
public class ViewList {
    private LinkedList<NoteItem> noteList= new LinkedList<>();
    List<IQuickActionListener> viewListActionListeners = new ArrayList<>();

    public void add(String stringElement, String noteType){
        NoteItem item = new NoteItem(new Date(), stringElement, noteType);
        noteList.add(item);
        performActionOnListeners(item);
    }

    public void add(NoteItem item){
        noteList.add(item);
        performActionOnListeners(item);
    }

    public void remove(NoteItem item){
        noteList.remove(item);
    }

    public void remove(NoteItem[] items){
        noteList.removeAll(Arrays.asList(items));
    }

    public List<NoteItem> getListToClear(String searchString){
        List<NoteItem> clearList = new ArrayList<>();
        for (NoteItem item:noteList){
            if(!item.contains(searchString)){
                clearList.add(item);
            }
        }
        return clearList;
    }

    public List<NoteItem> getFilteredList(String searchString){
        List<NoteItem> clearList = new ArrayList<>();
        for (NoteItem item:noteList){
            if(item.contains(searchString)){
                clearList.add(item);
            }
        }
        return clearList;
    }


    public boolean hasItemWithString(String searchString){
        for (NoteItem item:noteList){
            if (item.getNoteContent().equals(searchString)){
                return true;
            }
        }
        return false;
    }

    public List<NoteItem> getNoteList (){
        return noteList;
    }
    public void setNoteList (LinkedList<NoteItem> noteList) { this.noteList=noteList; }
    public void addActionListener(IQuickActionListener actionListener){
        viewListActionListeners.add(actionListener);
    }

    private void performActionOnListeners(NoteItem item){
        NoteEvent event = new NoteEvent(item);
        for (IQuickActionListener actionListener:viewListActionListeners){
            actionListener.actionPerformed(event);
        }
    }

}
