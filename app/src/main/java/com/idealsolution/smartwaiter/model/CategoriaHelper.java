package com.idealsolution.smartwaiter.model;


import android.database.Cursor;
import android.os.AsyncTask;

import com.idealsolution.smartwaiter.contract.SmartWaiterContract.Familia;
import com.idealsolution.smartwaiter.ui.CategoriaItemAdapter;
import com.idealsolution.smartwaiter.ui.CategoryDishFragment;
import com.idealsolution.smartwaiter.ui.TakeOrderActivity;


import static com.idealsolution.smartwaiter.util.LogUtils.makeLogTag;

public class CategoriaHelper {
    private static final String TAG = makeLogTag(CategoriaHelper.class);
    private TakeOrderActivity mContext;

    public CategoriaHelper(TakeOrderActivity context) {
        this.mContext = context;
    }

    public void getCategoriasAsync(final CategoryDishFragment fragment) {

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
                    fragment.getListaCategorias().add(item);
                }
                cursor.close();
                fragment.setAdapterCateg(new CategoriaItemAdapter(mContext,fragment.getListaCategorias()));
                fragment.getRecyclerViewCateg().setAdapter(fragment.getAdapterCateg());
                //By default load all the dishes under the first category
                int familiaId=Integer.parseInt(fragment.getListaCategorias().get(0).getCodigo().trim());
                fragment.loadArticulosObject(familiaId);

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
