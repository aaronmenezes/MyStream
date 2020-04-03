package com.kyser.demosuite.view.ui.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kyser.demosuite.R;
import com.kyser.demosuite.service.model.CategoryModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryListAdaptor extends RecyclerView.Adapter<CategoryListAdaptor.ViewHolder> {

    private Context mContext;
    List<CategoryModel> mCategoryList;
    private ItemSelection mItemSelection;


    public interface ItemSelection{ void onItemSelection(CategoryModel categoryModel, int position);}

    public CategoryListAdaptor(Context mContext,ItemSelection mItemSelection) {
        this.mContext = mContext;
        this.mItemSelection = mItemSelection;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.category_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.title.setText(mCategoryList.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemSelection.onItemSelection(mCategoryList.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCategoryList!=null?mCategoryList.size():0;
    }

    public void setCategoryList(List<CategoryModel> catlist ){this.mCategoryList = catlist;notifyDataSetChanged();}

    public List<CategoryModel> getListModel() {
        return this.mCategoryList;
    }

    class ViewHolder  extends RecyclerView.ViewHolder {
        TextView title ;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.cat_label);
        }
    }
}
