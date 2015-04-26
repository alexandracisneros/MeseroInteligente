package com.idealsolution.smartwaiter.events;

import com.idealsolution.smartwaiter.model.CategoriaObject;
import com.idealsolution.smartwaiter.ui.CategoriaItemAdapter;

/**
 * Created by Usuario on 02/04/2015.
 */
public class OnCategoriaClickEvent {
    public final CategoriaItemAdapter.ItemHolder item;
    public final CategoriaObject catObj;
    public final int position;

    public OnCategoriaClickEvent(CategoriaItemAdapter.ItemHolder item, CategoriaObject catObj, int position) {
        this.item = item;
        this.catObj=catObj;
        this.position = position;
    }
}
