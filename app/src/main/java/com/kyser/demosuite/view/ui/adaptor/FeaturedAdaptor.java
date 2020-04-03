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
import com.kyser.demosuite.service.model.FeaturedModel;
import com.kyser.demosuite.service.model.ListingModel;

import java.util.List;

import androidx.viewpager.widget.PagerAdapter;

public class FeaturedAdaptor extends PagerAdapter {

    private Context mContext;
    private List<ListingModel> mFeaturedModel;
    private ItemSelection mItemSelection;
    public interface ItemSelection{
        void onPlaySelection(ListingModel MediaList, int position);
        void onInfoSelection(ListingModel MediaList, int position);
    }

    public FeaturedAdaptor(Context context, ItemSelection itemSelection) {
        mContext = context;
        mItemSelection = itemSelection;
    }

    public void setCarouselModel(List<ListingModel> featuredModel){
        this.mFeaturedModel = featuredModel;
        this.notifyDataSetChanged();
    }

    public List<ListingModel> getCarouselModel(){
        return this.mFeaturedModel;
    }

    @Override
    public int getCount() {
        return this.mFeaturedModel!=null?mFeaturedModel.size():0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.featured_item, collection, false);
        StringBuilder b_url = new StringBuilder(mContext.getString(R.string.BASE_URL));
        b_url.append("getImageAsset/");
        b_url.append(mFeaturedModel.get(position).getSynopsisposter());
        ImageView poster_img = layout.findViewById(R.id.featured_poster);
        Glide.with(poster_img)
                .load(b_url.toString())
                .override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                .error(Glide.with(poster_img).load(R.drawable.poster_unavailable ))
                .into(poster_img);
        ((TextView)layout.findViewById(R.id.feature_title)).setText(mFeaturedModel.get(position).getTitle());
        layout.findViewById(R.id.feature_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemSelection.onPlaySelection(mFeaturedModel.get(position),position);
            }
        });

        layout.findViewById(R.id.feature_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemSelection.onInfoSelection(mFeaturedModel.get(position),position);
            }
        });
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }
}
