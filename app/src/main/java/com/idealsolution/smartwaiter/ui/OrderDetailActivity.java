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
public class OrderDetailActivity extends Activity {
    private OrderFragment ordersFrag = null;
    private ArrayList<PedidoDetObject> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ordersFrag = (OrderFragment) getFragmentManager().findFragmentById(R.id.orders);
        //http://stackoverflow.com/questions/28246075/how-to-maintain-listview-fragment-state-after-orientation-configuration-change

        //http://stackoverflow.com/questions/3014089/maintain-save-restore-scroll-position-when-returning-to-a-listview/5688490#5688490  //IMPORTANTE
        //http://stackoverflow.com/questions/11964647/restore-actionmode-after-orientation-change //IMPORTANT
        if (savedInstanceState == null) {
            if (ordersFrag == null) {
                ordersFrag = new OrderFragment();
                getFragmentManager().beginTransaction()
                        .add(android.R.id.content, ordersFrag)
                        .commit();
            }
//        items = PedidoSharedPreference.getItems(this);
//        ordersFrag.setOrderItems(items);
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        ordersFrag.setOrderItems(items);
//    }

}
