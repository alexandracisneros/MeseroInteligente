package com.idealsolution.smartwaiter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.idealsolution.smartwaiter.R;
import com.idealsolution.smartwaiter.events.OnCategoriaClickEvent;
import com.idealsolution.smartwaiter.events.OnProductCabClickEvent;

import de.greenrobot.event.EventBus;

public class OrderListActivity extends BaseActivity {
    private OrderListFragment mListaPedidosFragment = null;
    private OrderDetailFragment mDetallePedidoFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.opcion_listar_pedidos);
        overridePendingTransition(0, 0);

        mListaPedidosFragment = (OrderListFragment) getFragmentManager().findFragmentById(R.id.lista_pedido_frame);
        if (mListaPedidosFragment == null) {
            mListaPedidosFragment = new OrderListFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.lista_pedido_frame, mListaPedidosFragment)
                    .commit();
        }

        mDetallePedidoFragment = (OrderDetailFragment) getFragmentManager().findFragmentById(R.id.detalle_pedido_frame);
        if (mDetallePedidoFragment == null && findViewById(R.id.detalle_pedido_frame) != null) {
            mDetallePedidoFragment = new OrderDetailFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.detalle_pedido_frame, mDetallePedidoFragment)
                    .commit();
        }
        //TODO Aqui me quede

    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_LISTAR_PEDIDO;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    //This method is called when a OnProductCabClickEvent is posted
    public void onEventMainThread(OnProductCabClickEvent event) {
        int selectedOrderID = event.selectedOrderId;
        if(mDetallePedidoFragment!=null && mDetallePedidoFragment.isVisible()){
            mDetallePedidoFragment.loadDetail(selectedOrderID);
        }
        else {
            Intent i=new Intent(this,OrderDetailListActivity.class);
            i.putExtra(OrderDetailListActivity.EXTRA_ORDER_ID,selectedOrderID);
            startActivity(i);
        }

    }
}
