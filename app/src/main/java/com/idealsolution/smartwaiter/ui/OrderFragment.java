package com.idealsolution.smartwaiter.ui;

import android.app.Fragment;
import android.os.Bundle;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.idealsolution.smartwaiter.R;
import com.idealsolution.smartwaiter.model.PedidoDetObject;
import com.idealsolution.smartwaiter.preference.PedidoSharedPreference;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Usuario on 24/04/2015.
 */
public class OrderFragment extends Fragment {
    private TextView mSubTotalPedidoTextView;
    private TextView mIGVPedidoTextView;
    private TextView mTotalPedidoTextView;
    private ListView mItemsPedidoListView;
    private ArrayList<PedidoDetObject> mItems;
    private int mSelectedItemsCount;
    private ActionMode mActionMode;

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
        mItemsPedidoListView.setMultiChoiceModeListener(new MultiChoiceModeListener() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                getActivity().getMenuInflater().inflate(R.menu.contextmenu, menu);
                mActionMode = mode;
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                if (mSelectedItemsCount == 1) {
                    showOptionsOnSingleSelectedItem(true,menu);
                    return true;
                } else {
                    showOptionsOnSingleSelectedItem(false,menu);
                    return true;
                }
            }
            private void showOptionsOnSingleSelectedItem(boolean showOnSingle,Menu menu){
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
                        Toast.makeText(getActivity(), "EDIT clicked",
                                Toast.LENGTH_SHORT).show();
                        break;
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
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                mSelectedItemsCount = mItemsPedidoListView.getCheckedItemCount();
                mode.setTitle(String.format("%d Selected", mSelectedItemsCount));
                mode.invalidate();
            }
        });

        if (savedInstanceState == null) {
            //http://stackoverflow.com/questions/11297210/after-orientation-change-optionsmenu-of-fragment-doesnt-disappear
            setHasOptionsMenu(true);
        }
        return v;
    }

    private void showItems() {
        float subTotal = 0;
        float total = 0;
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
        total = subTotal + igv;
        mSubTotalPedidoTextView.setText(String.valueOf(subTotal));
        mIGVPedidoTextView.setText(String.valueOf(igv));
        mTotalPedidoTextView.setText(String.valueOf(total));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pedido_actual, menu);
    }

    @Override
    public void onPause() {
        if (mActionMode != null) {
            mActionMode.finish();
        }
        super.onPause();
    }
}
