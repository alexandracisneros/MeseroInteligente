package com.idealsolution.smartwaiter.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.idealsolution.smartwaiter.R;
import com.idealsolution.smartwaiter.events.MiEvento;
import com.idealsolution.smartwaiter.events.OnCategoriaClickEvent;
import com.idealsolution.smartwaiter.model.CategoriaObject;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;


/**
 * Created by Usuario on 02/04/2015.
 */
public class CategoriaItemAdapter extends RecyclerView.Adapter<CategoriaItemAdapter.ItemHolder> {
    private ArrayList<CategoriaObject> mItems;
    private LayoutInflater mLayoutInflater;
    private int mSize;
    private View mSelectedView;
    //private Context mContext; // ALTERNATIVE TO EVENTBUS

    public CategoriaItemAdapter(Context context, ArrayList<CategoriaObject> categorias) {
        mLayoutInflater = LayoutInflater.from(context);
        mItems = categorias;
        mSize = context.getResources()
                .getDimensionPixelSize(R.dimen.icon);
        mSelectedView = null;
//        mContext=context; // ALTERNATIVE TO EVENTBUS

    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.gallery_recycler_row, parent, false);
        return new ItemHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.setTituloTextView(mItems.get(position).getDescripcion());
        Ion.with(holder.getPicImageView())
                .placeholder(R.drawable.owner_placeholder)
                .resize(mSize, mSize)
                .centerCrop()
                .error(R.drawable.owner_error)
                .load(mItems.get(position).getUrl());
    }

    //Inicio-ALTERNATIVE TO EVENTBUS
//    public ArrayList<CategoriaObject> getItems() {
//        return mItems;
//    }
//    public Context getContext() {
//        return mContext;
//    }
    //Fin-ALTERNATIVE TO EVENTBUS
    @Override
    public int getItemCount() {
        return mItems.size();
    }
    /* To highlight selected item*/

    public View getSelectedView() {
        return mSelectedView;
    }

    public void setSelectedView(View selectedView) {
        this.mSelectedView = selectedView;
    }

    /* Required implementation of ViewHolder to wrap item view */
    public static class ItemHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        private CategoriaItemAdapter mParent;
        private TextView mTituloTextView;
        private ImageView mPicImageView;

        public ItemHolder(View itemView, CategoriaItemAdapter parent) {
            super(itemView);
            itemView.setOnClickListener(this);

            mParent = parent;

            mTituloTextView = (TextView) itemView.findViewById(R.id.desc_text_view);
            mPicImageView = (ImageView) itemView.findViewById(R.id.picture_image_view);
        }

        public void setTituloTextView(CharSequence titulo) {
            this.mTituloTextView.setText(titulo);
        }

        public ImageView getPicImageView() {
            return mPicImageView;
        }

        public CharSequence getImageText() {
            return mTituloTextView.getText();
        }

        @Override
        public void onClick(View v) {
            EventBus.getDefault().post(new OnCategoriaClickEvent(this, getPosition()));
            //((CategoryActivity)mParent.getContext()).loadArticulosObject(Integer.parseInt(mParent.getItems().get(getPosition()).getCodigo().trim())); //ALTERNATIVE TO EVENTBUS
            setItemActivated(v);
        }

        public void setItemActivated(View v) {

            if (mParent.getSelectedView() != null) {
                mParent.getSelectedView().findViewById(R.id.picture_image_view).setBackgroundColor(Color.TRANSPARENT);
            }
            v.findViewById(R.id.picture_image_view).setBackgroundColor(Color.CYAN);
            mParent.setSelectedView(v);
        }
    }
}
