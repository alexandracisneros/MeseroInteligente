package com.idealsolution.smartwaiter.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
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
import android.widget.Toast;

import com.idealsolution.smartwaiter.R;
import com.idealsolution.smartwaiter.contract.SmartWaiterContract;
import com.idealsolution.smartwaiter.contract.SmartWaiterContract.PedidoDetalle;
import com.idealsolution.smartwaiter.contract.SmartWaiterContract.PedidoCabecera;
import com.idealsolution.smartwaiter.events.OnProductCabClickEvent;
import com.idealsolution.smartwaiter.model.PedidoCabObject;
import com.idealsolution.smartwaiter.service.AsyncQueryService;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by Usuario on 12/07/2015.
 */
public class OrderListFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> , AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener{

    //Projection that defines just the data to display
    static final String[] PEDIDO_CABECERA_PROJECTION = new String[]{
            PedidoCabecera.ID,
            PedidoCabecera.CODIGO_CLIENTE,
            PedidoCabecera.AMBIENTE,
            PedidoCabecera.NRO_MESA,
            PedidoCabecera.FECHA
    };
    private static final int PEDIDOS_LOADER_ID = 0;
    private static final int TOKEN_PEDIDO_ID = 0;
    private ListView mPedidosListView;
    private ArrayList<PedidoCabObject> mPedidos;
    private SimpleCursorAdapter mAdapter;
    private QueryHandler mHandler;
    private class QueryHandler extends AsyncQueryService {

        public QueryHandler(Context context) {
            super(context);
        }

        @Override
        protected void onBatchComplete(int token, Object cookie, ContentProviderResult[] results) {
            switch (token){
                case TOKEN_PEDIDO_ID:
                    if (results[0].count <= 0) { // result=number of records affected
                        Toast.makeText(getActivity(), "Delete operation failed.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Delete operation successful",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order_list, parent, false);
        mPedidosListView = (ListView) v.findViewById(R.id.cabeceras_pedidos_listView);
        mPedidosListView.setOnItemClickListener(this);
        mPedidosListView.setOnItemLongClickListener(this);
        mHandler = new QueryHandler(getActivity());
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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor c= ((SimpleCursorAdapter)parent.getAdapter()).getCursor();
        c.moveToPosition(position);
        int i=c.getColumnIndex(PedidoDetalle.ID);
        String pedidoID=c.getString(i);
        String wherePedido="_id= ?";
        String[] whereArgsPedido={pedidoID};

        String whereDetalle="pedido_id= ?";
        String[] whereArgsDetalle={pedidoID};


        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
        operations.add(
                ContentProviderOperation.newDelete(PedidoCabecera.CONTENT_URI)
                        .withSelection(wherePedido, whereArgsPedido)
                        .build());
        operations.add(
                ContentProviderOperation.newDelete(PedidoDetalle.CONTENT_URI)
                        .withSelection(whereDetalle,whereArgsDetalle)
                        .build());

        mHandler.startBatch(TOKEN_PEDIDO_ID, null, SmartWaiterContract.CONTENT_AUTHORITY, operations, 0);
        return true;
    }
}
