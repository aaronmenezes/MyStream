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
import com.kyser.demosuite.service.model.TrackModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrackListAdaptor extends RecyclerView.Adapter<TrackListAdaptor.ViewHolder> {

    private Context mContext;
    List<TrackModel> mTracklistModel;
    private ItemSelection mItemSelection;
    public interface ItemSelection{ void onItemSelection(TrackModel MediaList, int position);}

    public TrackListAdaptor(Context mContext, ItemSelection mItemSelection) {
        this.mContext = mContext;
        this.mItemSelection = mItemSelection;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.track_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.title.setText(mTracklistModel.get(position).getTitle());
        holder.srn_no.setText(String.valueOf(position+1));
        holder.duration.setText(mTracklistModel.get(position).getDuration());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemSelection.onItemSelection(mTracklistModel.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTracklistModel!=null?mTracklistModel.size():0;
    }

    public void setCategoryList(List<TrackModel> catlist ){this.mTracklistModel = catlist;notifyDataSetChanged();}

    public  List<TrackModel> getTracklistModel(){return mTracklistModel;}
    class ViewHolder  extends RecyclerView.ViewHolder {
        TextView title ,srn_no,duration;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.track_title);
            srn_no = (TextView) itemView.findViewById(R.id.sr_no);
            duration = (TextView) itemView.findViewById(R.id.track_duration);
        }
    }
}
