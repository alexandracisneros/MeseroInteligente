package com.idealsolution.smartwaiter.model;


import android.database.Cursor;
import android.os.AsyncTask;

import com.idealsolution.smartwaiter.contract.SmartWaiterContract.Familia;
import com.idealsolution.smartwaiter.contract.SmartWaiterContract.Articulo;
import com.idealsolution.smartwaiter.ui.CategoriaItemAdapter;
import com.idealsolution.smartwaiter.ui.CategoryActivity;


import static com.idealsolution.smartwaiter.util.LogUtils.makeLogTag;

public class CategoriaHelper {
    private static final String TAG = makeLogTag(CategoriaHelper.class);
    private CategoryActivity mContext;

    public CategoriaHelper(CategoryActivity context) {
        this.mContext = context;
    }

    public void getCategoriasAsync() {

        new AsyncTask<Void, Void, Cursor>() {
            @Override
            protected Cursor doInBackground(Void... params) {
                return mContext.getApplicationContext().getContentResolver().query(
                        Familia.CONTENT_URI,
                        CategoriasQuery.PROJECTION,
                        null,
                        null,
                        null);
            }

            @Override
            protected void onPostExecute(Cursor cursor) {
                while (cursor.moveToNext()) {
                    CategoriaObject item = new CategoriaObject();
                    item.setId(cursor.getInt(CategoriasQuery.ID));
                    item.setCodigo(cursor.getString(CategoriasQuery.CODIGO));
                    item.setDescripcion(cursor.getString(CategoriasQuery.DESCRIPCION));
                    item.setUrl(cursor.getString(CategoriasQuery.URL));
                    mContext.getListaCategorias().add(item);
                }
                cursor.close();
                mContext.setAdapterCateg(new CategoriaItemAdapter(mContext, mContext.getListaCategorias()));
                //mContext.getAdapterCateg().setOnItemClickListener(mContext);
                mContext.getRecyclerViewCateg().setAdapter(mContext.getAdapterCateg());

            }
        }.execute();
    }

    private interface CategoriasQuery {
        String[] PROJECTION = {
                Familia.ID,
                Familia.CODIGO,
                Familia.DESCRIPCION,
                Familia.URL
        };
        int ID = 0;
        int CODIGO = 1;
        int DESCRIPCION = 2;
        int URL = 3;
    }

}
