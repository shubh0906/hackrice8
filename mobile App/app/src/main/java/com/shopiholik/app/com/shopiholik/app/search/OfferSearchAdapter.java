package com.shopiholik.app.com.shopiholik.app.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shopiholik.app.R;
import com.shopiholik.app.model.OfferData;
import com.shopiholik.app.model.OfferLogo;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author agrawroh
 * @version v1.0
 */
public class OfferSearchAdapter extends RecyclerView.Adapter<OfferSearchAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<OfferData> offerList;
    private List<OfferData> offerListFiltered;
    private ContactsAdapterListener listener;

    /**
     * My View Holder.
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView brandName, offerValue, offerDetails;
        public ImageView thumbnail;

        MyViewHolder(View view) {
            super(view);
            brandName = view.findViewById(R.id.brand_name);
            offerValue = view.findViewById(R.id.offer_value);
            offerDetails = view.findViewById(R.id.offer_details);
            thumbnail = view.findViewById(R.id.thumbnail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onContactSelected(offerListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    /**
     * Copy Constructor.
     *
     * @param context   Context
     * @param offerList Offer List
     * @param listener  Listener
     */
    OfferSearchAdapter(Context context, List<OfferData> offerList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.offerList = offerList;
        this.offerListFiltered = offerList;
    }

    @Override
    @NotNull
    public MyViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offer_detail_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, final int position) {
        final OfferData offerData = offerListFiltered.get(position);
        holder.brandName.setText(offerData.getBrandName());
        holder.offerValue.setText(getDeal(offerData));
        holder.offerDetails.setText(getCouponDetails(offerData));

        /* Get Logo Image [If Not Available] */
        if (null != offerData.getBrandeImageURL() && !"".equals(offerData.getBrandeImageURL())) {
            Glide.with(context)
                    .load(offerData.getBrandeImageURL())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.thumbnail);
        }
    }

    /**
     * Get Offer Deal.
     *
     * @param offerData Offer Data
     * @return Deal
     */
    private String getDeal(final OfferData offerData) {
        StringBuilder builder = new StringBuilder();
        if (null != offerData.getOfferPercentage() && !"".equals(offerData.getOfferPercentage())) {
            builder.append("Deal: ").append(offerData.getOfferPercentage());
        }
        return builder.toString();
    }

    /**
     * Get Coupon Details.
     *
     * @param offerData Offer Data
     * @return Offer Details
     */
    private String getCouponDetails(final OfferData offerData) {
        StringBuilder builder = new StringBuilder();
        if (null != offerData.getCouponCode() && !"".equals(offerData.getCouponCode())) {
            builder.append("Coupon Code: ").append(offerData.getCouponCode());
        }
        if (null != offerData.getOfferExpiry() && !"".equals(offerData.getOfferExpiry())) {
            builder.append("\nExpiry Date: ").append(offerData.getOfferExpiry());
        }
        return builder.toString();
    }

    @Override
    public int getItemCount() {
        return offerListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    offerListFiltered = offerList;
                } else {
                    List<OfferData> filteredList = new ArrayList<>();
                    for (OfferData row : offerList) {
                        if (row.getBrandName().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getBrandeClassification().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    offerListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = offerListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                offerListFiltered = (ArrayList<OfferData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(OfferData offerData);
    }
}
