package com.munk.quicknote.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kmayank on 3/31/16.
 */
public class ViewList {
    private LinkedList<NoteItem> noteList= new LinkedList<>();
    public void add(String stringElement){
        noteList.add(new NoteItem(new Date(), stringElement));
    }
    public void add(NoteItem item){
        noteList.add(item);
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

}
