package com.idealsolution.smartwaiter.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.idealsolution.smartwaiter.R;
import com.idealsolution.smartwaiter.events.OnArticuloCartaClickEvent;
import com.idealsolution.smartwaiter.events.OnCategoriaClickEvent;
import com.idealsolution.smartwaiter.model.ArticuloObject;

import de.greenrobot.event.EventBus;

/**
 * Created by Usuario on 20/04/2015.
 */
public class TakeOrderActivity extends Activity {
        private CategoryDishFragment mCategDishesFragement=null;
        private OrderFragment mOrderFragment=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_order);

        mCategDishesFragement= (CategoryDishFragment) getFragmentManager().findFragmentById(R.id.categ_dishes);
        if(mCategDishesFragement==null){
            mCategDishesFragement=new CategoryDishFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.categ_dishes,mCategDishesFragement)
                    .commit();
        }
        mOrderFragment= (OrderFragment) getFragmentManager().findFragmentById(R.id.orders);
        if(mOrderFragment==null && findViewById(R.id.orders)!=null){
            mOrderFragment=new OrderFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.orders,mOrderFragment).commit();
        }
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
    private void onActiculoSelected(ArticuloObject art){
        String nombre=art.getDescripcionNorm();
        if(mOrderFragment!=null && mOrderFragment.isVisible()){
            mOrderFragment.loadDescripcion(nombre);
        }
        else{
            Intent i=new Intent(this,OrderDetailsActivity.class);
            i.putExtra(OrderDetailsActivity.EXTRA_DESC,nombre);
            startActivity(i);
        }
    }
}
