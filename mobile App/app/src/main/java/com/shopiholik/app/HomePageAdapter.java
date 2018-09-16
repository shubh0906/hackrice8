package com.shopiholik.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shopiholik.app.com.shopiholik.app.search.OfferSearchAdapter;
import com.shopiholik.app.model.MainCard;
import com.shopiholik.app.model.OfferData;

import java.util.List;

/**
 * @author agrawroh
 * @version v1.0
 */
public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.MyViewHolder> {
    private Context mContext;
    private HomePageAdapterListener listener;
    private List<MainCard> homePageEntriesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView prediction;
        public ImageView thumbnail;

        MyViewHolder(View view) {
            super(view);
            prediction = view.findViewById(R.id.prediction);
            prediction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCardSelected(homePageEntriesList.get(getAdapterPosition()));
                }
            });

            thumbnail = view.findViewById(R.id.thumbnail);
            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCardSelected(homePageEntriesList.get(getAdapterPosition()));
                }
            });
        }
    }

    /**
     * Copy Constructor.
     *
     * @param mContext            Context
     * @param homePageEntriesList Home Page Entries List
     */
    HomePageAdapter(Context mContext, List<MainCard> homePageEntriesList, HomePageAdapterListener listener) {
        this.mContext = mContext;
        this.listener = listener;
        this.homePageEntriesList = homePageEntriesList;
    }

    @Override
    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_page, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    @NonNull
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        MainCard card = homePageEntriesList.get(position);
        holder.prediction.setText(card.getDescription());

        /* Loading Images */
        Glide.with(mContext).load(card.getThumbnail()).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return homePageEntriesList.size();
    }

    public interface HomePageAdapterListener {
        void onCardSelected(MainCard mainCard);
    }
}
