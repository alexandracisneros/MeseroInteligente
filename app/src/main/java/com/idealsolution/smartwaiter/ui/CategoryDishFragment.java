package com.idealsolution.smartwaiter.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idealsolution.smartwaiter.R;
import com.idealsolution.smartwaiter.model.ArticuloHelper;
import com.idealsolution.smartwaiter.model.ArticuloObject;
import com.idealsolution.smartwaiter.model.CategoriaHelper;
import com.idealsolution.smartwaiter.model.CategoriaObject;
import com.idealsolution.smartwaiter.model.PedidoCabObject;
import com.idealsolution.smartwaiter.model.PedidoDetObject;

import java.util.ArrayList;

/**
 * Created by Usuario on 24/04/2015.
 */
public class CategoryDishFragment extends Fragment {
    private ArrayList<CategoriaObject> mListaCategorias;
    private ArrayList<ArticuloObject> mListaArticulos;
    private RecyclerView mRecyclerViewCateg;
    private RecyclerView mRecyclerViewPlatos;
    private CategoriaItemAdapter mAdapterCateg;
    private ArticuloItemAdapter mAdapterPlatos;
    private LinearLayoutManager mCategHorizontalManager;
    private LinearLayoutManager mPlatosHorizontalManager;
    // The ScheduleHelper is responsible for feeding data in a format suitable to the Adapter.
    private CategoriaHelper mDataHelper;
    private ArticuloHelper mDataArticuloHelper;
    private TakeOrderActivity mActivity;

    public CategoryDishFragment() {
//        this.mActivity = (TakeOrderActivity) getActivity();
//        this.mDataHelper = new CategoriaHelper(this.mActivity);
//        this.mDataArticuloHelper = new ArticuloHelper(this.mActivity);
    }

    public TakeOrderActivity getParentActivity() {
        return mActivity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_category, parent, false);

        //Initiate global variables
        this.mActivity = (TakeOrderActivity) getActivity();
        this.mDataHelper = new CategoriaHelper(this.mActivity);
        this.mDataArticuloHelper = new ArticuloHelper(this.mActivity);
        //

        mRecyclerViewCateg = (RecyclerView) v.findViewById(R.id.categoria_recycler_view);
        mRecyclerViewCateg.setHasFixedSize(true);

        mRecyclerViewPlatos = (RecyclerView) v.findViewById(R.id.plato_recycler_view);
        mRecyclerViewPlatos.setHasFixedSize(true);

        mCategHorizontalManager = new LinearLayoutManager(getParentActivity(), LinearLayoutManager.HORIZONTAL, false);
        mPlatosHorizontalManager = new LinearLayoutManager(getParentActivity(), LinearLayoutManager.HORIZONTAL, false);

        mRecyclerViewCateg.setLayoutManager(mCategHorizontalManager);
        mRecyclerViewPlatos.setLayoutManager(mPlatosHorizontalManager);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadCategoriasObject();

    }

    public void loadCategoriasObject() {
        mListaCategorias = new ArrayList<CategoriaObject>();
        mDataHelper.getCategoriasAsync(this);
    }

    public void loadArticulosObject(int categoriaId) {
        mListaArticulos = new ArrayList<ArticuloObject>();
        mDataArticuloHelper.getArticuloPorFamiliaAsync(this, categoriaId);
    }

    public void insertPedidoBatch() {
        PedidoCabObject ped = new PedidoCabObject();
        PedidoDetObject det;
        ped.setFecha("20150407");
        ped.setNro_mesa(2);
        ped.setAmbiente(1);
        ped.setCod_usuario("100");
        ped.setCod_cliente(100);
        ped.setTipo_venta("020");
        ped.setTipo_pago("030");
        ped.setMoneda("SOL");
        ped.setMonto_total(1200);
        ped.setMonto_recibido(1500);
        ped.setEstado(1);
        ped.setDetalle(new ArrayList<PedidoDetObject>());
        for (int i = 0; i < 5; i++) {
            det = new PedidoDetObject();
            det.setCod_articulo(i + 10);
            det.setCantidad(100 * (i + 1));
            det.setPrecio(25 * (i + 1));
            det.setTipo_articulo(i + 2);
            det.setCod_art_principal(i + 3);
            det.setComentario("Comentario " + (i + 1));
            det.setEstado_articulo(1);
            ped.getDetalle().add(det);
        }
        mDataArticuloHelper.PedidoDetalleApplyBatch(ped);

    }

    public ArrayList<CategoriaObject> getListaCategorias() {
        return mListaCategorias;
    }

    public ArrayList<ArticuloObject> getListaArticulos() {
        return mListaArticulos;
    }

    public RecyclerView getRecyclerViewCateg() {
        return mRecyclerViewCateg;
    }

    public RecyclerView getRecyclerViewPlatos() {
        return mRecyclerViewPlatos;
    }

    public CategoriaItemAdapter getAdapterCateg() {
        return mAdapterCateg;
    }

    public ArticuloItemAdapter getAdapterPlatos() {
        return mAdapterPlatos;
    }

    public void setAdapterCateg(CategoriaItemAdapter adapterCateg) {
        this.mAdapterCateg = adapterCateg;
    }

    public void setAdapterPlatos(ArticuloItemAdapter adapterPlatos) {
        this.mAdapterPlatos = adapterPlatos;
    }
}
