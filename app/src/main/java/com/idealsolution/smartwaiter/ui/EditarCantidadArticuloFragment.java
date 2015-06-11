package com.idealsolution.smartwaiter.ui;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.idealsolution.smartwaiter.R;
import com.idealsolution.smartwaiter.events.OnEditarCantArticuloClickEvent;
import com.idealsolution.smartwaiter.model.PedidoDetObject;
import com.idealsolution.smartwaiter.preference.PedidoSharedPreference;

import de.greenrobot.event.EventBus;


public class EditarCantidadArticuloFragment extends DialogFragment implements
        DialogInterface.OnClickListener {
    //http://android-developers.blogspot.com/2009/01/avoiding-memory-leaks.html
    private View form = null;
    private PedidoDetObject mItem; //JOSE /If you want to keep the AlertDialog opened and with data http://stackoverflow.com/questions/13934951/dialogfragment-with-setretaininstancestatetrue-is-not-displayed-after-the-devi
    private String mNuevaCantidad;

    public static EditarCantidadArticuloFragment newInstance(PedidoDetObject item) {
        EditarCantidadArticuloFragment frag = new EditarCantidadArticuloFragment();
        frag.setItemDetalle(item);
        return frag;
    }

    public void setItemDetalle(PedidoDetObject item) {
        this.mItem = item;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        form = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_editar_cant_articulo, null);
        TextView descripcionTextView = (TextView) form.findViewById(R.id.articuloDescTextView);
        EditText cantidadEditText = (EditText) form.findViewById(R.id.articuloCantidadEditText);
        descripcionTextView.setText(mItem.getDescripcionArticulo());
        if (mNuevaCantidad == null) {
            cantidadEditText.setText(String.valueOf(mItem.getCantidad()));
        } else {
            cantidadEditText.setText(mNuevaCantidad);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setTitle("Editar Cantidad").setView(form)
                .setPositiveButton("Aceptar", this)
                .setNegativeButton("Cancelar", this).create();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        EditText cantidadEditText = (EditText) form.findViewById(R.id.articuloCantidadEditText);
        mNuevaCantidad = cantidadEditText.getText().toString();
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            EditText cantidad = (EditText) form.findViewById(R.id.articuloCantidadEditText);
            mItem.setCantidad(Float.valueOf(cantidad.getText().toString()));
        }
        EventBus.getDefault().post(new OnEditarCantArticuloClickEvent(mItem,which));

    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }
}
