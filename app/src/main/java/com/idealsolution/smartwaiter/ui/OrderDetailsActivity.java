package com.idealsolution.smartwaiter.ui;

import android.app.Activity;
import android.os.Bundle;

import com.idealsolution.smartwaiter.R;
import com.idealsolution.smartwaiter.model.PedidoDetObject;
import com.idealsolution.smartwaiter.preference.PedidoSharedPreference;

import java.util.ArrayList;

/**
 * Created by Usuario on 20/04/2015.
 */
public class OrderDetailsActivity extends Activity {
    private OrderFragment ordersFrag=null;
    private ArrayList<PedidoDetObject> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ordersFrag= (OrderFragment) getFragmentManager().findFragmentById(R.id.orders);

        if(ordersFrag==null){
            ordersFrag=new OrderFragment();
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content,ordersFrag)
                    .commit();
        }
        items= PedidoSharedPreference.getItems(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ordersFrag.setOrderItems(items);
    }
}
