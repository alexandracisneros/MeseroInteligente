package com.idealsolution.smartwaiter.ui;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.idealsolution.smartwaiter.R;
import com.idealsolution.smartwaiter.events.OnArticuloCartaClickEvent;
import com.idealsolution.smartwaiter.events.OnCategoriaClickEvent;
import com.idealsolution.smartwaiter.model.ArticuloObject;
import com.idealsolution.smartwaiter.model.MesaPisoObject;
import com.idealsolution.smartwaiter.model.PedidoDetObject;
import com.idealsolution.smartwaiter.preference.PedidoSharedPreference;


import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by Usuario on 20/04/2015.
 */
public class TakeOrderActivity extends BaseActivity {
    //http://androidopentutorials.com/android-how-to-store-list-of-values-in-sharedpreferences/
    private CategoryDishFragment mCategDishesFragement = null;
    private OrderFragment mOrderFragment = null;
    private MesaPisoObject mMesaSeleccionada;
    private ArrayList<PedidoDetObject> items;

//    public static final String PREFERENCES = "pref_order";
//    public static final String PREF_CURRENT_ORDER = "current_order";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.opcion_tomar_pedido);
        overridePendingTransition(0, 0);

        mCategDishesFragement = (CategoryDishFragment) getFragmentManager().findFragmentById(R.id.categ_dishes);
        if (mCategDishesFragement == null) {
            mCategDishesFragement = new CategoryDishFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.categ_dishes, mCategDishesFragement)
                    .commit();
        }
        mOrderFragment = (OrderFragment) getFragmentManager().findFragmentById(R.id.orders);
        if (mOrderFragment == null && findViewById(R.id.orders) != null) {
            mOrderFragment = new OrderFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.orders, mOrderFragment).commit();
        }
        String mesaJSON = getIntent().getStringExtra(MesasActivity.EXTRA_MESA_SELECCIONADA);
        Gson gson = new Gson();
        mMesaSeleccionada = gson.fromJson(mesaJSON, MesaPisoObject.class);


    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_TOMAR_PEDIDO;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Create the ActionBar actions
        getMenuInflater().inflate(R.menu.menu_tomar_pedido, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                //Delete Action
                Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_settings:
                //Settings Action
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    //This method is called when a OnCategoriaClickEvent is posted
    public void onEventMainThread(OnCategoriaClickEvent event) {
        int categoriaId = Integer.parseInt(event.catObj.getCodigo().trim());
        Toast.makeText(this, "Familia ID:" + categoriaId, Toast.LENGTH_SHORT).show();
        mCategDishesFragement.loadArticulosObject(categoriaId);
    }

    //This method is called when a OnArticuloCartaClickEvent is posted
    public void onEventMainThread(OnArticuloCartaClickEvent event) {
        String descripcion = event.artObj.getDescripcionNorm();
        Toast.makeText(this, descripcion, Toast.LENGTH_SHORT).show();
        mCategDishesFragement.insertPedidoBatch();
        onActiculoSelected(event.artObj);
    }

    private void onActiculoSelected(ArticuloObject art) {
        PedidoDetObject itemDetalle = new PedidoDetObject(art);
        PedidoSharedPreference.addItem(this, itemDetalle);
//        items = PedidoSharedPreference.getItems(this);
//        String nombre = art.getDescripcionNorm();
        if (mOrderFragment != null && mOrderFragment.isVisible()) {
            items = PedidoSharedPreference.getItems(this);
            mOrderFragment.setOrderItems(items);
        } else {
            Intent i = new Intent(this, OrderDetailsActivity.class);
            //i.putExtra(OrderDetailsActivity.EXTRA_DESC, nombre);
            startActivity(i);
        }
    }
}
