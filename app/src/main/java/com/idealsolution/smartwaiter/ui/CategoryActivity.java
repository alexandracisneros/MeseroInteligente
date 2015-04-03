package com.idealsolution.smartwaiter.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.idealsolution.smartwaiter.R;
import com.idealsolution.smartwaiter.events.MiEvento;
import com.idealsolution.smartwaiter.events.OnArticuloCartaClickEvent;
import com.idealsolution.smartwaiter.events.OnCategoriaClickEvent;
import com.idealsolution.smartwaiter.model.ArticuloHelper;
import com.idealsolution.smartwaiter.model.ArticuloObject;
import com.idealsolution.smartwaiter.model.CategoriaHelper;
import com.idealsolution.smartwaiter.model.CategoriaObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class CategoryActivity extends Activity {
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

    public CategoryActivity() {
        this.mDataHelper = new CategoriaHelper(this);
        this.mDataArticuloHelper = new ArticuloHelper(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

//        mListaCategorias=getImageList(MyGalleryData.categories);
//        mListaArticulos=new ArrayList<GalleryObject>();

        mRecyclerViewCateg = (RecyclerView) findViewById(R.id.categoria_recycler_view);
        mRecyclerViewCateg.setHasFixedSize(true);

        mRecyclerViewPlatos = (RecyclerView) findViewById(R.id.plato_recycler_view);
        mRecyclerViewPlatos.setHasFixedSize(true);

        mCategHorizontalManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mPlatosHorizontalManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        mRecyclerViewCateg.setLayoutManager(mCategHorizontalManager);
        mRecyclerViewPlatos.setLayoutManager(mPlatosHorizontalManager);

        loadCategoriasObject();
        loadArticulosObject(0);

    }

    public void loadCategoriasObject() {
        mListaCategorias = new ArrayList<CategoriaObject>();
        mDataHelper.getCategoriasAsync();
    }

    public void loadArticulosObject(int categoriaId) {
        mListaArticulos = new ArrayList<ArticuloObject>();
        mDataArticuloHelper.getArticuloPorFamiliaAsync(categoriaId);
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
        int categoriaId = Integer.parseInt(mListaCategorias.get(event.position).getCodigo().trim());
        Toast.makeText(this, "Familia ID:" + categoriaId, Toast.LENGTH_SHORT).show();
        loadArticulosObject(categoriaId);
    }

    //This method is called when a OnArticuloCartaClickEvent is posted
    public void onEventMainThread(OnArticuloCartaClickEvent event) {
        String descripcion = mListaArticulos.get(event.position).getDescripcionNorm();
        Toast.makeText(this, descripcion, Toast.LENGTH_SHORT).show();
    }
}
