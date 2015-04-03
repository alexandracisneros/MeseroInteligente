package com.idealsolution.smartwaiter.events;

import com.idealsolution.smartwaiter.ui.CategoriaItemAdapter;

/**
 * Created by Usuario on 02/04/2015.
 */
public class OnCategoriaClickEvent {
    public final CategoriaItemAdapter.ItemHolder item;
    public final int position;

    public OnCategoriaClickEvent(CategoriaItemAdapter.ItemHolder item, int position) {
        this.item = item;
        this.position = position;
    }
}
