package com.idealsolution.smartwaiter.ui;

import android.app.Activity;
import android.os.Bundle;

import com.idealsolution.smartwaiter.R;

/**
 * Created by Usuario on 20/04/2015.
 */
public class OrderDetailsActivity extends Activity {
    public static final String EXTRA_DESC="EXTRA_DESC";
    private String desc=null;
    private OrderFragment ordersFrag=null;

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
        desc=getIntent().getStringExtra(EXTRA_DESC);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ordersFrag.loadDescripcion(desc);
    }
}
