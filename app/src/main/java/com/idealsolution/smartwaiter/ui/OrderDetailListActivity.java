package com.idealsolution.smartwaiter.ui;

import android.app.Activity;
import android.os.Bundle;

import com.idealsolution.smartwaiter.R;

public class OrderDetailListActivity extends Activity {
    public static final String EXTRA_ORDER_ID = "extra_order_id";
    private int mOrderID = 0;
    private OrderDetailFragment mOrderDetailFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOrderDetailFragment = (OrderDetailFragment) getFragmentManager()
                .findFragmentById(R.id.detalle_pedido_frame);
        if (mOrderDetailFragment == null) {
            mOrderDetailFragment = new OrderDetailFragment();
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, mOrderDetailFragment)
                    .commit();
        }
        mOrderID = getIntent().getIntExtra(EXTRA_ORDER_ID, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mOrderDetailFragment.loadDetail(mOrderID);
    }
}
