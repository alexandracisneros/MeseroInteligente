package com.idealsolution.smartwaiter.events;

/**
 * Created by Usuario on 13/07/2015.
 */
public class OnProductCabClickEvent {
    public final int selectedOrderId ;

    public OnProductCabClickEvent(int selectedOrderId) {
        this.selectedOrderId = selectedOrderId;
    }
}
