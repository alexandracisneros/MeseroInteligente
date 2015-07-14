package com.idealsolution.smartwaiter.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.idealsolution.smartwaiter.R;
import com.idealsolution.smartwaiter.contract.SmartWaiterContract;
import com.idealsolution.smartwaiter.contract.SmartWaiterContract.PedidoCabecera;
import com.idealsolution.smartwaiter.events.OnProductCabClickEvent;
import com.idealsolution.smartwaiter.model.PedidoCabObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by Usuario on 12/07/2015.
 */
public class OrderListFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> , AdapterView.OnItemClickListener{

    //Projection that defines just the data to display
    static final String[] PEDIDO_CABECERA_PROJECTION = new String[]{
            PedidoCabecera.ID,
            PedidoCabecera.CODIGO_CLIENTE,
            PedidoCabecera.AMBIENTE,
            PedidoCabecera.NRO_MESA,
            PedidoCabecera.FECHA
    };
    private static final int PEDIDOS_LOADER_ID = 0;
    private ListView mPedidosListView;
    private ArrayList<PedidoCabObject> mPedidos;
    private SimpleCursorAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order_list, parent, false);
        mPedidosListView = (ListView) v.findViewById(R.id.cabeceras_pedidos_listView);
        mPedidosListView.setOnItemClickListener(this);
        initAdapter();
        getLoaderManager().initLoader(PEDIDOS_LOADER_ID, null, this);
        return v;
    }

    private void initAdapter(){
        mAdapter=new SimpleCursorAdapter(getActivity(),
                R.layout.pedido_cabecera_row,null,
                new String[]{PedidoCabecera.CODIGO_CLIENTE,PedidoCabecera.AMBIENTE,PedidoCabecera.NRO_MESA,PedidoCabecera.FECHA},
                new int[]{R.id.cliente_textView,R.id.ambiente_textView,R.id.mesa_textView,R.id.fecha_textView},0
            );
        mPedidosListView.setAdapter(mAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), SmartWaiterContract.PedidoCabecera.CONTENT_URI,
                PEDIDO_CABECERA_PROJECTION, null, null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int count=cursor.getCount();
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //TODO http://stackoverflow.com/questions/14899904/solved-onitemclicklistener-on-a-listview-inside-a-fragment-not-working
        //Implementar con interfaces o event buses para enviar a la activity y que esta llame y decida si selecciona el item o llama a la otra actividad.
        int pedidoId=((Cursor)parent.getAdapter().getItem(position)).getInt(0);
        EventBus.getDefault().post(new OnProductCabClickEvent(pedidoId));

    }
}
