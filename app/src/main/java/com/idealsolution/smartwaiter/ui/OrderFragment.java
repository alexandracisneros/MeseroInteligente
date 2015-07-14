package com.idealsolution.smartwaiter.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.idealsolution.smartwaiter.R;
import com.idealsolution.smartwaiter.contract.SmartWaiterContract;
import com.idealsolution.smartwaiter.events.OnEditarCantArticuloClickEvent;
import com.idealsolution.smartwaiter.model.PedidoCabObject;
import com.idealsolution.smartwaiter.model.PedidoDetObject;
import com.idealsolution.smartwaiter.preference.PedidoSharedPreference;
import com.idealsolution.smartwaiter.service.AsyncQueryService;


import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by Usuario on 24/04/2015.
 */
public class OrderFragment extends Fragment {
    private TextView mSubTotalPedidoTextView;
    private TextView mIGVPedidoTextView;
    private TextView mTotalPedidoTextView;
    private ListView mItemsPedidoListView;
    private ArrayList<PedidoDetObject> mItems;

    private ActionMode mActionMode;
    private int mSelectedItemsCount;
    private boolean mIsInActionMode = false;
    private float mTotal=0;
    private QueryHandler mHandler;
    private class QueryHandler extends AsyncQueryService {

        public QueryHandler(Context context) {
            super(context);
        }

        @Override
        protected void onBatchComplete(int token, Object cookie, ContentProviderResult[] results) {
            //super.onBatchComplete(token, cookie, results);
            Toast.makeText(getActivity(),"Pedido Registrado Correctamente",Toast.LENGTH_LONG).show();
        }

    }
    public void setOrderItems(ArrayList<PedidoDetObject> items) {
        this.mItems = items;
        showItems();
        //http://www.101apps.co.za/articles/using-menus-in-your-apps-a-tutorial.html
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pedido_cliente, parent, false);

        mSubTotalPedidoTextView = (TextView) v.findViewById(R.id.subTotalPedidoTextView);
        mIGVPedidoTextView = (TextView) v.findViewById(R.id.igvPedidoTextView);
        mTotalPedidoTextView = (TextView) v.findViewById(R.id.totalPedidoTextView);
        mItemsPedidoListView = (ListView) v.findViewById(R.id.detallePedidoListView);
        mItemsPedidoListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mItemsPedidoListView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                ((ListView) parent).setItemChecked(position, ((ListView) parent).isItemChecked(position));
                return false;
            }
        });
        mItemsPedidoListView.setMultiChoiceModeListener(new ActionModeCallbacks());
//        if (savedInstanceState == null) {
//            //http://stackoverflow.com/questions/11297210/after-orientation-change-optionsmenu-of-fragment-doesnt-disappear
//            setHasOptionsMenu(true);
//            mSelectedItemsCount = 0;
//        } else {
        setHasOptionsMenu(true);
        mSelectedItemsCount = 0;
        if(savedInstanceState!=null){
            Parcelable listViewState = savedInstanceState.getParcelable("ListViewState");
            mItemsPedidoListView.onRestoreInstanceState(listViewState);
            mSelectedItemsCount = savedInstanceState.getInt("SelectedItemsCount", 0);
        }
        mItems = PedidoSharedPreference.getItems(getActivity());
        showItems();
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("SelectedItemsCount", mSelectedItemsCount);
        outState.putBoolean("ActionMode", mIsInActionMode);
        outState.putParcelable("ListViewState", mItemsPedidoListView.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }
    private void showItems() {
        float subTotal = 0;
        float igv = 0;
        //create a List of Map<String,?> objects
        ArrayList<HashMap<String, String>> data =
                new ArrayList<HashMap<String, String>>();
        for (PedidoDetObject item : mItems) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("articuloDescripcion", item.getDescripcionArticulo());
            map.put("articuloCantidad", String.valueOf(item.getCantidad()));
            map.put("articuloPrecio", String.valueOf(item.getPrecio()));
            data.add(map);

            subTotal += item.getCantidad() * item.getPrecio();
        }

        //create the resouces, from, and to variables
        int resource = R.layout.order_item;
        String[] from = {"articuloDescripcion", "articuloCantidad", "articuloPrecio"};
        int[] to = {R.id.productoPedidoDescTextView, R.id.cantidadPedidoTextView,
                R.id.precioPedidoTextView};

        //create and set the adapter
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), data, resource, from, to);
        mItemsPedidoListView.setAdapter(adapter);

        //Calculate and display summary data
        igv = subTotal * 0.19f;
        mTotal = subTotal + igv;
        mSubTotalPedidoTextView.setText(String.valueOf(subTotal));
        mIGVPedidoTextView.setText(String.valueOf(igv));
        mTotalPedidoTextView.setText(String.valueOf(mTotal));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear(); //To show menu even if a rotate the screen
        inflater.inflate(R.menu.menu_pedido_actual, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_send:
                Toast.makeText(getActivity(), "Save & Send", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_just_save:
                saveOrder();
                //Toast.makeText(getActivity(), "Just Save", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_cancelar:
                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    //This method is called when a OnCategoriaClickEvent is posted
    public void onEventMainThread(OnEditarCantArticuloClickEvent event) {
        if (event.which == DialogInterface.BUTTON_POSITIVE) {
            PedidoSharedPreference.updateItem(getActivity(), event.item);
            mItems = PedidoSharedPreference.getItems(getActivity());
            showItems();
        }
        mActionMode.finish();
    }

    public class ActionModeCallbacks implements MultiChoiceModeListener {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            getActivity().getMenuInflater().inflate(R.menu.contextmenu, menu);
            mActionMode = mode;
            mIsInActionMode = true;
            mode.setTitle(String.format("%d Selected", mSelectedItemsCount));
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            if (mSelectedItemsCount == 1) {
                showOptionsOnSingleSelectedItem(true, menu);
                return true;
            } else {
                showOptionsOnSingleSelectedItem(false, menu);
                return true;
            }
        }

        private void showOptionsOnSingleSelectedItem(boolean showOnSingle, Menu menu) {
            MenuItem item = menu.findItem(R.id.menu_add);
            item.setVisible(showOnSingle);
            item = menu.findItem(R.id.menu_substract);
            item.setVisible(showOnSingle);
            item = menu.findItem(R.id.menu_edit);
            item.setVisible(showOnSingle);
        }

        //"Don't you fucking play the hero again OK! Because I'll die if something happens to you". If only Fer would have remembered what David said to him :'(
        private void modifyProductQuantiy(int accion, SparseBooleanArray checkedItemPositions) {
            if (checkedItemPositions.valueAt(0)) {
                int position = checkedItemPositions.keyAt(0);
                PedidoDetObject itemDetalle = mItems.get(position);
                if (accion == 0) {
                    itemDetalle.setCantidad(itemDetalle.getCantidad() + 1);
                } else {
                    if (itemDetalle.getCantidad() > 1) {
                        itemDetalle.setCantidad(itemDetalle.getCantidad() - 1);
                    } else {
                        mItems.remove(itemDetalle); //If quantity equals 1 then remove it!
                    }
                }
                PedidoSharedPreference.saveItems(getActivity(), mItems);
                showItems();
            }
        }

        private void removeSelectedItems(SparseBooleanArray checkedItemPositions) {
            for (int i = (checkedItemPositions.size() - 1); i >= 0; i--) {
                if (checkedItemPositions.valueAt(i)) {
                    int position = checkedItemPositions.keyAt(i);
                    PedidoDetObject itemDetalle = mItems.get(position);
                    mItems.remove(itemDetalle);
                }
            }
            PedidoSharedPreference.saveItems(getActivity(), mItems);
            showItems();
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            //ArrayAdapter<String> adapter = (ArrayAdapter<String>) mItemsPedidoListView.getAdapter();
            SparseBooleanArray checkedItemPositions = mItemsPedidoListView.getCheckedItemPositions();
            switch (item.getItemId()) {
                case R.id.menu_delete:
                    removeSelectedItems(checkedItemPositions);
                    mode.finish();
                    return true;
                case R.id.menu_edit:
                    int position = checkedItemPositions.keyAt(0);
                    PedidoDetObject itemDetalle = mItems.get(position);
                    EditarCantidadArticuloFragment.newInstance(itemDetalle).show(getFragmentManager(), "EditarCantidadFragment");
                    return true;
                case R.id.menu_add:
                    modifyProductQuantiy(0, checkedItemPositions);
                    mode.finish();
                    return true;
                case R.id.menu_substract:
                    modifyProductQuantiy(1, checkedItemPositions);
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mIsInActionMode = false;
        }

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            mSelectedItemsCount = mItemsPedidoListView.getCheckedItemCount();
            mode.setTitle(String.format("%d Selected", mSelectedItemsCount));
            mode.invalidate();
        }
    }
    private void saveOrder(){
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
        ContentValues cvPedido = new ContentValues();
        //ContentValues cvDetalle;
        cvPedido.put(SmartWaiterContract.PedidoCabecera.FECHA, "20150708");
        cvPedido.put(SmartWaiterContract.PedidoCabecera.NRO_MESA, 2);
        cvPedido.put(SmartWaiterContract.PedidoCabecera.AMBIENTE, 1);
        cvPedido.put(SmartWaiterContract.PedidoCabecera.CODIGO_USUARIO, "200");
        cvPedido.put(SmartWaiterContract.PedidoCabecera.CODIGO_CLIENTE, 100);
        cvPedido.put(SmartWaiterContract.PedidoCabecera.TIPO_VENTA, "020");
        cvPedido.put(SmartWaiterContract.PedidoCabecera.TIPO_PAGO,"030");
        cvPedido.put(SmartWaiterContract.PedidoCabecera.MONEDA, "SOL");
        cvPedido.put(SmartWaiterContract.PedidoCabecera.MONTO_TOTAL, mTotal);
        cvPedido.put(SmartWaiterContract.PedidoCabecera.MONTO_RECIBIDO, 1500);
        cvPedido.put(SmartWaiterContract.PedidoCabecera.ESTADO, 1);

        operations.add(
                ContentProviderOperation.newInsert(SmartWaiterContract.PedidoCabecera.CONTENT_URI)
                        .withValues(cvPedido)
                        .build());
        for (PedidoDetObject det :mItems) {
            operations.add(ContentProviderOperation.newInsert(SmartWaiterContract.PedidoDetalle.CONTENT_URI)
                    .withValueBackReference(SmartWaiterContract.PedidoDetalle.PEDIDO_ID, 0) //This field references back the returned value of the first element
                    .withValue(SmartWaiterContract.PedidoDetalle.COD_ART, det.getCod_articulo())
                    .withValue(SmartWaiterContract.PedidoDetalle.CANTIDAD, det.getCantidad())
                    .withValue(SmartWaiterContract.PedidoDetalle.PRECIO, det.getPrecio())
                    .withValue(SmartWaiterContract.PedidoDetalle.TIPO_ART, det.getTipo_articulo())
                    .withValue(SmartWaiterContract.PedidoDetalle.COD_ART_PRINCIPAL, det.getCod_art_principal())
                    .withValue(SmartWaiterContract.PedidoDetalle.COMENTARIO, det.getComentario())
                    .withValue(SmartWaiterContract.PedidoDetalle.ESTADO_ART, det.getEstado_articulo())
                    .build());

        }
        mHandler = new QueryHandler(getActivity());
        mHandler.startBatch(0, null, SmartWaiterContract.CONTENT_AUTHORITY, operations, 0);
    }
}
