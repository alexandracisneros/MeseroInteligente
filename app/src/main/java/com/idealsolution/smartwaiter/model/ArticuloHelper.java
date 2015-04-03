package com.idealsolution.smartwaiter.model;

import android.database.Cursor;
import android.os.AsyncTask;


import com.idealsolution.smartwaiter.contract.SmartWaiterContract.Articulo;
import com.idealsolution.smartwaiter.ui.ArticuloItemAdapter;
import com.idealsolution.smartwaiter.ui.CategoriaItemAdapter;
import com.idealsolution.smartwaiter.ui.CategoryActivity;

import java.util.ArrayList;

import static com.idealsolution.smartwaiter.util.LogUtils.makeLogTag;

/**
 * Created by Usuario on 02/04/2015.
 */
public class ArticuloHelper {
    private static final String TAG = makeLogTag(CategoriaHelper.class);
    private CategoryActivity mContext;

    public ArticuloHelper(CategoryActivity context) {
        this.mContext = context;
    }
    public void getArticuloPorFamiliaAsync(final int familiaId) {

        new AsyncTask<Void, Void, Cursor>() {
            @Override
            protected Cursor doInBackground(Void... params) {
                return mContext.getApplicationContext().getContentResolver().query(
                        Articulo.buildArticuloFamiliaUri(familiaId),
                        ArticulosQuery.PROJECTION,
                        null,
                        null,
                        null);
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                while (cursor.moveToNext()) {
                    ArticuloObject item = new ArticuloObject();
                    item.setId(cursor.getInt(ArticulosQuery.ID));
                    item.setDescripcionNorm(cursor.getString(ArticulosQuery.DESCRIPCION_NORM));
                    item.setUm(cursor.getString(ArticulosQuery.UM));
                    item.setUmDescripcion(cursor.getString(ArticulosQuery.UM_DESC));
                    item.setPrecio(cursor.getFloat(ArticulosQuery.PRECIO));
                    item.setUrl(cursor.getString(ArticulosQuery.URL));

                    mContext.getListaArticulos().add(item);
                }
                cursor.close();
                mContext.setAdapterPlatos(new ArticuloItemAdapter(mContext, mContext.getListaArticulos()));
//                mContext.getMesasAdapter().setOnItemClickListener(mContext);
                mContext.getRecyclerViewPlatos().setAdapter(mContext.getAdapterPlatos());

            }
        }.execute();
    }
    private interface ArticulosQuery{
        String[] PROJECTION={
                Articulo.ID,
                Articulo.DESCRIPCION_NORM,
                Articulo.UM,
                Articulo.UM_DESC,
                Articulo.PRECIO,
                Articulo.URL
        };
        int ID=0;
        int DESCRIPCION_NORM=1;
        int UM=2;
        int UM_DESC=3;
        int PRECIO=4;
        int URL=5;
    }
}
