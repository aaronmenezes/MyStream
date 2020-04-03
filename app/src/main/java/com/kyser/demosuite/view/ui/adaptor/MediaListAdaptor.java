package com.kyser.demosuite.view.ui.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.kyser.demosuite.R;
import com.kyser.demosuite.service.model.ListingModel;
import com.kyser.demosuite.view.ui.MediaList;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MediaListAdaptor extends RecyclerView.Adapter<MediaListAdaptor.ViewHolder> {

    private Context mContext;
    List<ListingModel> mMediaList;
    private ItemSelection mItemSelection;
    public interface ItemSelection{ void onItemSelection(ListingModel MediaList, int position);}

    public MediaListAdaptor(Context mContext, ItemSelection mItemSelection) {
        this.mContext = mContext;
        this.mItemSelection = mItemSelection;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.listing_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.title.setText(mMediaList.get(position).getTitle());
        StringBuilder b_url = new StringBuilder(mContext.getString(R.string.BASE_URL));
        b_url.append("getImageAsset/");
        b_url.append(mMediaList.get(position).getPoster());
        Glide.with(holder.itemView)
                .load(b_url.toString())
                .override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                .error(Glide.with(holder.poster).load(R.drawable.poster_unavailable))
                .into(holder.poster);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemSelection.onItemSelection(mMediaList.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMediaList!=null?mMediaList.size():0;
    }

    public void setCategoryList(List<ListingModel> catlist ){this.mMediaList = catlist;notifyDataSetChanged();}

    class ViewHolder  extends RecyclerView.ViewHolder {
        TextView title ;
        ImageView poster;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.list_label);
            poster = (ImageView) itemView.findViewById(R.id.list_poster);
        }
    }
}
