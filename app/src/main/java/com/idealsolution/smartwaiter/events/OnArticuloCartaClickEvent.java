package com.idealsolution.smartwaiter.events;

import com.idealsolution.smartwaiter.model.ArticuloObject;
import com.idealsolution.smartwaiter.ui.ArticuloItemAdapter;

/**
 * Created by Usuario on 02/04/2015.
 */
public class OnArticuloCartaClickEvent {
    public final ArticuloItemAdapter.ItemHolder item;
    public final ArticuloObject artObj;
    public final int position;

    public OnArticuloCartaClickEvent(ArticuloItemAdapter.ItemHolder item,ArticuloObject artObj, int position) {
        this.item = item;
        this.artObj=artObj;
        this.position = position;
    }
}
