package com.idealsolution.smartwaiter.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.idealsolution.smartwaiter.R;
import com.idealsolution.smartwaiter.model.PedidoDetObject;

import org.w3c.dom.Text;

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
    public void setOrderItems(ArrayList<PedidoDetObject> items){
        this.mItems=items;
        showItems();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_pedido_cliente,parent,false);

        mSubTotalPedidoTextView= (TextView) v.findViewById(R.id.subTotalPedidoTextView);
        mIGVPedidoTextView= (TextView) v.findViewById(R.id.igvPedidoTextView);
        mTotalPedidoTextView= (TextView) v.findViewById(R.id.totalPedidoTextView);
        mItemsPedidoListView= (ListView) v.findViewById(R.id.detallePedidoListView);
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

}
