package com.example.maciekBro.cardatabase.listing;

import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.maciekBro.cardatabase.CarsTableContract;
import com.example.maciekBro.cardatabase.R;

import butterknife.ButterKnife;

import static butterknife.ButterKnife.*;

/**
 * Created by RENT on 2017-03-27.
 */

public class RecyclerViewCursorAdapter extends RecyclerView.Adapter<RecyclerViewCursorAdapter.ViewHolder> {

    private OnCarItemClickListener onCarItemClickListener;
    private Cursor cursor;
    private CarsTableContract carsTableContract;

    public void setOnCarItemClickListener(OnCarItemClickListener onCarItemClickListener) {
        this.onCarItemClickListener = onCarItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursor.moveToPosition(position);  //nie trzeba sprawdzac null bo onBindViewHolder nie zostanie wywolane jak getItemCount=0
        String imageUrl = cursor.getString(cursor.getColumnIndex(CarsTableContract.COLUMN_IMAGE));
        String make = cursor.getString(cursor.getColumnIndex(CarsTableContract.COLUMN_MAKE));
        String model = cursor.getString(cursor.getColumnIndex(CarsTableContract.COLUMN_MODEL));
        int year = cursor.getInt(cursor.getColumnIndex(CarsTableContract.COLUMN_YEAR));
        holder.makeAndModel.setText(make + " " + model);
        holder.year.setText("Rocznik: " + year);
        Glide.with(holder.image.getContext()).load(imageUrl).into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCarItemClickListener != null) {
                    cursor.moveToPosition(position);
                    onCarItemClickListener.onCarItemClick(cursor.getString(0));    //bierzemy pieerwsza kolumne "0"
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    public void setCursor(@Nullable Cursor cursor) {    //nullable jest dobra praktyką, zeby mozna bylo wrzucic w niego null
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView makeAndModel;
        TextView year;

        public ViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.image);
            makeAndModel = (TextView) itemView.findViewById(R.id.make_and_model);
            year = findById(itemView, R.id.year);
        }
    }

}
