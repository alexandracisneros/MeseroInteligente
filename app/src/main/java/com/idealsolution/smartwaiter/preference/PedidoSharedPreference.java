package com.idealsolution.smartwaiter.preference;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.idealsolution.smartwaiter.model.PedidoDetObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PedidoSharedPreference {
    //http://androidopentutorials.com/android-how-to-store-list-of-values-in-sharedpreferences/
    public static final String PREFS_NAME = "Pref_pedido";
    public static final String PREF_PEDIDO_ACTUAL = "pedido_actual";

    // This four methods are used for maintaining pedido.
    public static void saveItems(Context context, List<PedidoDetObject> items) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonItems = gson.toJson(items);

        editor.putString(PREF_PEDIDO_ACTUAL, jsonItems);
        //editor.clear();//BORRAR
        editor.commit();
    }

    public static void addItem(Context context, PedidoDetObject item) {
        List<PedidoDetObject> items = getItems(context);
        if (items == null)
            items = new ArrayList<PedidoDetObject>();
        if (items.contains(item)) {  //This requires that the PedidoDetObject overrides the equals method
            PedidoDetObject detalle=items.get(items.indexOf(item));
            detalle.setCantidad(detalle.getCantidad() + 1);
        } else {
            items.add(item);
        }
        saveItems(context, items);
    }

    public static void removeItem(Context context, PedidoDetObject item) {
        ArrayList<PedidoDetObject> items = getItems(context);
        if (items != null) {
            items.remove(item);
            saveItems(context, items);
        }
    }

    public static ArrayList<PedidoDetObject> getItems(Context context) {
        SharedPreferences preferences;
        List<PedidoDetObject> items;

        preferences = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (preferences.contains(PREF_PEDIDO_ACTUAL)) {
            String jsonItems = preferences.getString(PREF_PEDIDO_ACTUAL, null);
            Gson gson = new Gson();
            PedidoDetObject[] pedidoItems = gson.fromJson(jsonItems,
                    PedidoDetObject[].class);

            items = Arrays.asList(pedidoItems);
            items = new ArrayList<PedidoDetObject>(items);
        } else
            return null;

        return (ArrayList<PedidoDetObject>) items;
    }
}
