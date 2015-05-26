package com.idealsolution.smartwaiter.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.idealsolution.smartwaiter.R;
import com.idealsolution.smartwaiter.model.MesaPisoObject;

import java.util.List;

/**
 * Created by Usuario on 30/03/2015.
 */
public class MesaItemAdapter  extends RecyclerView.Adapter<MesaItemAdapter.ItemHolder>{
    /**
     * Click handler interface. RecyclerView does not have
     * its own built in like AdapterView do.
     */
    public interface OnItemClickListener{
        public void onItemClick(ItemHolder item, int position);
    }
    private OnItemClickListener mOnItemClickListener;
    private LayoutInflater mLayoutInflater;
    private List<MesaPisoObject> mItems;

    public MesaItemAdapter(Context context,List<MesaPisoObject> listaMesas) {
        mLayoutInflater=LayoutInflater.from(context);
        mItems=listaMesas;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=mLayoutInflater.inflate(R.layout.mesa_recycler_row,parent,false);

        return new ItemHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.setNroMesa(mItems.get(position).getNro_mesa());
        holder.setNro("Mesa #" + holder.getNroMesa()+ "(" + mItems.get(position).getNro_asientos() + ")");
        holder.setEstado(mItems.get(position).getDesc_estado());
        holder.setReserva(String.valueOf(mItems.get(position).getCod_reserva()));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
    public OnItemClickListener getOnItemClickListener() {
        return this.mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    /* Required implementation of ViewHolder to wrap item view */
    public static class ItemHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener{
        private MesaItemAdapter mParent;
        private TextView mNroTextView,mEstadoTextView,mReservaTextView;
        private int mNroMesa;
        public ItemHolder(View itemView,MesaItemAdapter parent) {
            super(itemView);
            itemView.setOnClickListener(this);
            mParent=parent;

            mNroTextView= (TextView) itemView.findViewById(R.id.nro_textview);
            mEstadoTextView= (TextView) itemView.findViewById(R.id.estado_textview);
            mReservaTextView= (TextView) itemView.findViewById(R.id.reserva_textview);
            mNroMesa=0;

        }

        public void setNro(CharSequence nro) {
            this.mNroTextView.setText(nro);
        }

        public void setEstado(CharSequence estado) {
            this.mEstadoTextView.setText(estado);
        }

        public void setReserva(CharSequence reserva) {
            this.mReservaTextView.setText(reserva);
        }

        public CharSequence getNro() {
            return mNroTextView.getText();
        }

        public int getNroMesa() {
            return mNroMesa;
        }

        public void setNroMesa(int mNroMesa) {
            this.mNroMesa = mNroMesa;
        }

        @Override
        public void onClick(View v) {
            final OnItemClickListener listener=mParent.getOnItemClickListener();
            if(listener!=null){
                listener.onItemClick(this,getPosition());
            }
        }
    }
}
