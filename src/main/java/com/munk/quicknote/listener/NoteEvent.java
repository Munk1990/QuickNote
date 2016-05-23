package com.munk.quicknote.listener;

import com.munk.quicknote.models.NoteItem;

import java.util.Date;

/**
 * Created by kmayank on 4/16/16.
 */
public class NoteEvent implements IQuickEvent {
    NoteItem item;
    public NoteEvent(String noteString, String noteType){
        item = new NoteItem(new Date(), noteString, noteType);
    }
    public NoteEvent(NoteItem item){
        this.item = item;
    }

    @Override
    public NoteItem getNoteItem() {
        return item;
    }
}
