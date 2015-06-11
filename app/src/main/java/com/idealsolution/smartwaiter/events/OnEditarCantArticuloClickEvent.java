package com.idealsolution.smartwaiter.events;

import com.idealsolution.smartwaiter.model.PedidoDetObject;

/**
 * Created by Usuario on 10/06/2015.
 */
public class OnEditarCantArticuloClickEvent {
    public final PedidoDetObject item;
    public final int which;

    public OnEditarCantArticuloClickEvent(PedidoDetObject item, int which) {
        this.item = item;
        this.which=which;
    }
}
