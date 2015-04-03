package com.idealsolution.smartwaiter.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.idealsolution.smartwaiter.R;

/**
 * Created by Usuario on 02/04/2015.
 */
abstract public class GalleryItemAdapter extends RecyclerView.Adapter<GalleryItemAdapter.ItemHolder>{

    /**
     * Click handler interface. RecyclerView does not have
     * its own built in like AdapterView do.
     */
    public interface OnItemClickListener{
        public void onItemClick(ItemHolder item, int position);
    }

    //private List<GalleryObject> mItems;

    protected OnItemClickListener mOnItemClickListener;
    protected LayoutInflater mLayoutInflater;
    protected int mSize;
    protected int mLevel; // 0=Category | 1=Dishes
    protected View mSelectedView;

    public GalleryItemAdapter(Context context,int level){
        super();
        mLayoutInflater=LayoutInflater.from(context);
        //mItems=picsList;
        mSize=context.getResources()
                .getDimensionPixelSize(R.dimen.icon);
        mLevel=level;
        mSelectedView=null;
    }

//    @Override
//    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView=mLayoutInflater.inflate(R.layout.gallery_recycler_row, parent,false);
//
//        return new ItemHolder(itemView,this);
//
//    }
//
//    @Override
//    public void onBindViewHolder(ItemHolder holder, int position) {
//        holder.setTituloTextView(mItems.get(position).getImageTitle());
//        Ion.with(holder.getPicImageView())
//                .placeholder(R.drawable.owner_placeholder)
//                .resize(mSize,mSize)
//                .centerCrop()
//                .error(R.drawable.owner_error)
//                .load(mItems.get(position).getImageUrl());
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return mItems.size();
//    }

    public OnItemClickListener getOnItemClickListener() {
        return this.mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
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
            View.OnClickListener{
        private GalleryItemAdapter mParent;
        private TextView mTituloTextView;
        private ImageView mPicImageView;
        public ItemHolder(View itemView, GalleryItemAdapter parent) {
            super(itemView);
            itemView.setOnClickListener(this);

            mParent=parent;

            mTituloTextView= (TextView) itemView.findViewById(R.id.desc_text_view);
            mPicImageView= (ImageView) itemView.findViewById(R.id.picture_image_view);
        }

        public void setTituloTextView(CharSequence titulo) {
            this.mTituloTextView.setText(titulo);
        }

        public ImageView getPicImageView() {
            return mPicImageView;
        }
        public CharSequence getImageText(){
            return mTituloTextView.getText();
        }
        public int getGalleryLevel(){
            return mParent.mLevel;
        }

        @Override
        public void onClick(View v) {
            final OnItemClickListener listener=mParent.getOnItemClickListener();
            if(listener!=null){
                listener.onItemClick(this,getPosition());
            }
            setItemActivated(v);
        }
        public void setItemActivated(View v){

            if(mParent.getSelectedView()!=null){
                mParent.getSelectedView().findViewById(R.id.picture_image_view).setBackgroundColor(Color.TRANSPARENT);
            }
            v.findViewById(R.id.picture_image_view).setBackgroundColor(Color.CYAN);
            mParent.setSelectedView(v);
        }
    }
}
