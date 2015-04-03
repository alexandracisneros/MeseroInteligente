package com.idealsolution.smartwaiter.events;

import com.idealsolution.smartwaiter.ui.ArticuloItemAdapter;

/**
 * Created by Usuario on 02/04/2015.
 */
public class OnArticuloCartaClickEvent {
    public final ArticuloItemAdapter.ItemHolder item;
    public final int position;

    public OnArticuloCartaClickEvent(ArticuloItemAdapter.ItemHolder item, int position) {
        this.item = item;
        this.position = position;
    }
}
