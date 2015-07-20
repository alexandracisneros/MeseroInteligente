package com.idealsolution.smartwaiter.ui;

import android.app.Fragment;
import android.app.LoaderManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.idealsolution.smartwaiter.R;
import com.idealsolution.smartwaiter.contract.SmartWaiterContract.PedidoDetalle;
import com.idealsolution.smartwaiter.service.AsyncQueryService;

/**
 * Created by Usuario on 10/07/2015.
 */
//http://www.grokkingandroid.com/using-loaders-in-android/
public class OrderDetailFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> , AdapterView.OnItemLongClickListener{
    private static final int DETALLE_PEDIDO_LOADER_ID = 0;
    private static final int TOKEN_DETALLE_ID = 0;
    private static final String IMPORTE_UNIT = "importe_unit";
    //Projection that defines just the data to display
    static final String[] PEDIDO_DETALLE_PROJECTION = new String[]{
            PedidoDetalle.ID,
            PedidoDetalle.COD_ART,
            PedidoDetalle.CANTIDAD,
            PedidoDetalle.PRECIO,
            "(" + PedidoDetalle.CANTIDAD + " * " + PedidoDetalle.PRECIO + " ) AS " + IMPORTE_UNIT
    };
    private TextView mSubTotalPedidoTextView;
    private TextView mIGVPedidoTextView;
    private TextView mTotalPedidoTextView;
    private float mTotal = 0;
    private ListView mItemsDetalleListView;
    private SimpleCursorAdapter mAdapter;
    private QueryHandler mHandler;
    private class QueryHandler extends AsyncQueryService {

        public QueryHandler(Context context) {
            super(context);
        }

        @Override
        protected void onDeleteComplete(int token, Object cookie, int result) {
            switch (token){
                case TOKEN_DETALLE_ID:
                    if (result <= 0) { // result=number of records affected
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
        View v = inflater.inflate(R.layout.fragment_pedido_cliente, parent, false);
        mSubTotalPedidoTextView = (TextView) v.findViewById(R.id.subTotalPedidoTextView);
        mIGVPedidoTextView = (TextView) v.findViewById(R.id.igvPedidoTextView);
        mTotalPedidoTextView = (TextView) v.findViewById(R.id.totalPedidoTextView);
        mItemsDetalleListView = (ListView) v.findViewById(R.id.detallePedidoListView);
        mItemsDetalleListView.setOnItemLongClickListener(this);
        mHandler = new QueryHandler(getActivity());
        initAdapter();

        return v;
    }

    public void loadDetail(int selectedOrderID) {
        Bundle args = new Bundle();
        args.putInt("selectedOrderID", selectedOrderID);
        getLoaderManager().restartLoader(DETALLE_PEDIDO_LOADER_ID, args, this);
    }

    private void initAdapter() {
        mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.order_item, null,
                new String[]{PedidoDetalle.COD_ART, PedidoDetalle.CANTIDAD, PedidoDetalle.PRECIO},
                new int[]{R.id.productoPedidoDescTextView, R.id.cantidadPedidoTextView, R.id.precioPedidoTextView},
                0);
        mItemsDetalleListView.setAdapter(mAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        int selectedOrderID = args.getInt("selectedOrderID");
        return new CursorLoader(getActivity(), PedidoDetalle.buildPedidoDetalleUri(String.valueOf(selectedOrderID)),
                PEDIDO_DETALLE_PROJECTION, PedidoDetalle.PEDIDO_ID + "= ? ", new String[]{String.valueOf(selectedOrderID)}, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        //http://stackoverflow.com/questions/30907726/sum-query-in-contentprovider
        //http://stackoverflow.com/questions/29414170/sqlite-how-to-sum-the-values-of-column-in-gridview
        //https://github.com/ArtemMikhaylov/my_android_notes/wiki/SQLite,-ContentProvider,-CursorLoader
        //http://stackoverflow.com/questions/11150527/how-does-cursorloader-with-loadermanager-know-to-send-the-cursor-to-a-cursoradap
        int count = cursor.getCount();
        calculateTotals(cursor);
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    private void calculateTotals(Cursor c) {
        //TODO Move this code to a new thread
        float subTotal = 0;
        float igv;
        while (c.moveToNext()) {
            // Extract data.
            subTotal += c.getFloat(c.getColumnIndex(IMPORTE_UNIT));
        }
        //Calculate and display summary data
        igv = subTotal * 0.19f;
        mTotal = subTotal + igv;
        mSubTotalPedidoTextView.setText(String.valueOf(subTotal));
        mIGVPedidoTextView.setText(String.valueOf(igv));
        mTotalPedidoTextView.setText(String.valueOf(mTotal));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor c= ((SimpleCursorAdapter)parent.getAdapter()).getCursor();
        c.moveToPosition(position);
        int i=c.getColumnIndex(PedidoDetalle.ID);
        String detalleID=c.getString(i);
        String where="_id= ?";
        String[] whereArgs={detalleID};

        mHandler.startDelete(TOKEN_DETALLE_ID,null,PedidoDetalle.CONTENT_URI,where,whereArgs,0);
                return true;
    }
}
