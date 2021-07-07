package com.softgyan.test.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.softgyan.test.R;
import com.softgyan.test.models.TempModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TempAdapter extends RecyclerView.Adapter<TempAdapter.ViewHolder> implements Filterable {
    private final List<TempModel> tempModelList;
    private final List<TempModel> tempModelListAll;
    private final Context context;

    public TempAdapter(Context context, List<TempModel> tempModelList) {
        this.context = context;
        this.tempModelList = tempModelList;
        this.tempModelListAll = new ArrayList<>(tempModelList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TempModel tempModel = tempModelList.get(position);
        holder.textView.setText(tempModel.getImageName());
        Log.d("my_tag", "onBindViewHolder: imageUri :"+tempModel.getImageUri());


        Glide.with(context).load(tempModel.getImageUri()).into(holder.imageView);
       /* try {
            holder.imageView.setImageURI(Uri.parse(tempModel.getImageUri()));
        }catch (Exception e){
            Log.d("my_tag", "onBindViewHolder: error : "+e.getMessage());
        }*/
    }

    @Override
    public int getItemCount() {
        return tempModelList.size();
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            final List<TempModel> tempModelFiltered = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                tempModelFiltered.addAll(tempModelListAll);
            } else {
                for (TempModel tempModel : tempModelListAll) {
                    if (tempModel.getImageName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempModelFiltered.add(tempModel);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = tempModelFiltered;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            tempModelList.clear();
            tempModelList.addAll((Collection<? extends TempModel>) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return filter;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tvMessage);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
