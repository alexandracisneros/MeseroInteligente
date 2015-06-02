package com.idealsolution.smartwaiter.ui;

import android.app.Fragment;
import android.os.Bundle;
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
import com.idealsolution.smartwaiter.model.PedidoDetObject;



import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Usuario on 24/04/2015.
 */
public class OrderFragment extends Fragment  {
    private TextView mSubTotalPedidoTextView;
    private TextView mIGVPedidoTextView;
    private TextView mTotalPedidoTextView;
    private ListView mItemsPedidoListView;
    private ArrayList<PedidoDetObject> mItems;
    public void setOrderItems(ArrayList<PedidoDetObject> items){
        this.mItems=items;
        showItems();
        //https://github.com/Prototik/HoloEverywhere/issues/406
        //http://gotoanswer.com/?q=Android%3A+Change+ActionBar+Menu+Items+from+Fragment

        //Pon en ask question de stackoverflow "contextual action mode inside fragment"
        //http://stackoverflow.com/questions/24865949/contextual-action-mode-in-fragment-doesnt-show-up
        //http://www.101apps.co.za/articles/using-menus-in-your-apps-a-tutorial.html
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_pedido_cliente,parent,false);

        mSubTotalPedidoTextView= (TextView) v.findViewById(R.id.subTotalPedidoTextView);
        mIGVPedidoTextView= (TextView) v.findViewById(R.id.igvPedidoTextView);
        mTotalPedidoTextView= (TextView) v.findViewById(R.id.totalPedidoTextView);
        mItemsPedidoListView= (ListView) v.findViewById(R.id.detallePedidoListView);

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
                getActivity().getMenuInflater().inflate(R.menu.contextmenu,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_delete:
                        Toast.makeText(getActivity(), "DELETE clicked",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_edit:
                        Toast.makeText(getActivity(), "EDIT clicked",
                                Toast.LENGTH_SHORT).show();
                        break;
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
                int count = mItemsPedidoListView.getCheckedItemCount();
                mode.setTitle(String.format("%d Selected", count));
            }
        });

        if(savedInstanceState == null) {
            //http://stackoverflow.com/questions/11297210/after-orientation-change-optionsmenu-of-fragment-doesnt-disappear
            setHasOptionsMenu(true);
        }
        return v;
    }
    private void showItems(){
        //create a List of Map<String,?> objects
        ArrayList<HashMap<String, String>> data=
                new ArrayList<HashMap<String,String>>();
        for(PedidoDetObject item: mItems){
            HashMap<String, String> map=new HashMap<String, String>();
            map.put("articuloDescripcion", item.getDescripcionArticulo());
            map.put("articuloCantidad", String.valueOf(item.getCantidad()));
            map.put("articuloPrecio",String.valueOf(item.getPrecio()));
            data.add(map);
        }

        //create the resouces, from, and to variables
        int resource=R.layout.order_item;
        String[] from={"articuloDescripcion","articuloCantidad","articuloPrecio"};
        int[] to={R.id.productoPedidoDescTextView,R.id.cantidadPedidoTextView,
                R.id.precioPedidoTextView};

        //create and set the adapter
        SimpleAdapter adapter=new SimpleAdapter(getActivity(), data, resource, from, to);
        mItemsPedidoListView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_pedido_actual,menu);
    }

}
