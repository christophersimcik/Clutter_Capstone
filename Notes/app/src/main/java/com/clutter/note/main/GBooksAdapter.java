package com.clutter.note.main;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class GBooksAdapter extends RecyclerView.Adapter <GBooksAdapter.ViewHolder> {
    public Context mContext;

    public GBooksAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public GBooksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.gbooks_recycle_item, parent, false);
        return new GBooksAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GBooksAdapter.ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return GoogleBooks.list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
    TextView titleView;
    TextView subTitleView;
    TextView authorsView;
    TextView publishingView;
    TextView languageView;
    TextView pageCountView;
    TextView descriptionView;
    TextView descriptionHeader;
    ImageView bookImgView;
        public ViewHolder(final View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.book_title);
            subTitleView =(TextView) itemView.findViewById(R.id.book_subtitle);
            authorsView = (TextView) itemView.findViewById(R.id.author);
            publishingView = (TextView) itemView.findViewById(R.id.publisher_date);
            languageView = (TextView) itemView.findViewById(R.id.language);
            pageCountView = (TextView) itemView.findViewById(R.id.page_count);
            descriptionView = (TextView) itemView.findViewById(R.id.description);
            descriptionHeader = (TextView) itemView.findViewById(R.id.description_header);
            bookImgView = (ImageView) itemView.findViewById(R.id.book_image);
            descriptionView.getLayoutParams().width = MainActivity.w;

        }
        public void setData(int position){
            if(GoogleBooks.list.get(position)[GoogleBooksJSON.titles]!=null) {
                titleView.setTextColor(Color.DKGRAY);
                titleView.setTextSize(20);
                titleView.setText(GoogleBooks.list.get(position)[GoogleBooksJSON.titles]);
            }else{
                titleView.setTextColor(Color.LTGRAY);
                titleView.setTextSize(20);
                titleView.setText("No Title Available");
            }
            if(GoogleBooks.list.get(position)[GoogleBooksJSON.subtitles]!=null) {
                subTitleView.setTextColor(mContext.getResources().getColor(R.color.textCol));
                subTitleView.setText("(" + GoogleBooks.list.get(position)[GoogleBooksJSON.subtitles] + ")");
            }else{
                subTitleView.setTextColor(Color.LTGRAY);
                subTitleView.setText("N/A");
            }
            if(GoogleBooks.list.get(position)[GoogleBooksJSON.authors] != null) {
                authorsView.setTextColor(mContext.getResources().getColor(R.color.textCol));
                authorsView.setText(GoogleBooks.list.get(position)[GoogleBooksJSON.authors]);
            }else{
                authorsView.setTextColor(Color.LTGRAY);
                authorsView.setText("N/A");
            }
            if(GoogleBooks.list.get(position)[GoogleBooksJSON.publishers]!=null) {
                publishingView.setTextColor(mContext.getResources().getColor(R.color.textCol));
                publishingView.setText(GoogleBooks.list.get(position)[GoogleBooksJSON.publishers]);
            }else{
                publishingView.setTextColor(Color.LTGRAY);
                publishingView.setText("N/A");
            }
            if(GoogleBooks.list.get(position)[GoogleBooksJSON.descriptions]!=null) {
                descriptionView.setTextColor(mContext.getResources().getColor(R.color.textCol));
                descriptionView.setText(GoogleBooks.list.get(position)[GoogleBooksJSON.descriptions]);
            }else{
                descriptionView.setTextColor(Color.LTGRAY);
                descriptionView.setText("No Description Available");
            }
            if(GoogleBooks.list.get(position)[GoogleBooksJSON.pages] != null) {
                pageCountView.setTextColor(mContext.getResources().getColor(R.color.textCol));
                pageCountView.setText(GoogleBooks.list.get(position)[GoogleBooksJSON.pages] + " " + "pgs.");
            }else{
                pageCountView.setTextColor(Color.LTGRAY);
                pageCountView.setText("N/A");
            }
            if(GoogleBooks.list.get(position)[GoogleBooksJSON.languages] != null) {
                languageView.setTextColor(mContext.getResources().getColor(R.color.textCol));
                languageView.setText(GoogleBooks.list.get(position)[GoogleBooksJSON.languages]);
            }else{
                languageView.setTextColor(Color.LTGRAY);
                languageView.setText("N/A");
            }
            if(GoogleBooks.list.get(position)[GoogleBooksJSON.descriptions] != null) {
                descriptionHeader.setTextColor(mContext.getResources().getColor(R.color.textCol));
                descriptionHeader.setTextSize(16);
                descriptionHeader.setText("Description:");
            }
            Uri uri = Uri.parse(GoogleBooks.list.get(position)[GoogleBooksJSON.images]);
            Picasso.with(mContext)
                    .load(uri)
                    .into(bookImgView);
        }
    }
}
