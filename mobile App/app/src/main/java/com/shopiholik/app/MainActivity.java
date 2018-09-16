package com.shopiholik.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.common.util.Hex;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shopiholik.app.com.shopiholik.app.login.LoginActivity;
import com.shopiholik.app.com.shopiholik.app.search.OfferBrandImageApi;
import com.shopiholik.app.com.shopiholik.app.search.SearchActivity;
import com.shopiholik.app.model.OfferData;
import com.shopiholik.app.model.OfferLogo;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author agrawroh
 * @version v1.0
 */
public class MainActivity extends AppCompatActivity {
    private Retrofit retrofit = null;

    /* Logo Retrieval URI */
    private static final String BASE_URL = "https://autocomplete.clearbit.com/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Set Content View */
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        /* Get References */
        LottieAnimationView animationView = findViewById(R.id.lottieAnimationView);
        LottieAnimationView progressView = findViewById(R.id.progressView);
        AppCompatImageView logoView = findViewById(R.id.logoView);
        logoView.setImageResource(R.drawable.shopiholik_main_logo_color);

        /* Play Boot Animation */
        animationView.playAnimation();
        animationView.loop(true);

        /* Play Progress Animation */
        progressView.playAnimation();
        progressView.loop(true);

        /* Login Activity */
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));

        /* Set Retrofit Instance */
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }

        /* Get Active User */
        FirebaseUser currentUser = getCurrentUser();
        String currentUserEmail;
        if (null != currentUser) {
            currentUserEmail = currentUser.getEmail();

            /* Fetch Offers */
            final Set<OfferData> offerDataList = new HashSet<>();
            FirebaseDatabase.getInstance().getReference().child("offers").child("users")
                    .child(getMd5Hash(currentUserEmail)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot innerDataSnapshot : dataSnapshot.getChildren()) {
                        OfferData offerData = innerDataSnapshot.getValue(OfferData.class);
                        if (!offerDataList.contains(offerData) ||
                                (null != offerData.getCouponCode() && !"".equals(offerData.getCouponCode()))) {
                            /* Get Logos */
                            getLogoImage(offerData);
                            offerDataList.add(offerData);
                        }
                    }

                    /* Launch Search Activity */
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(getApplicationContext(), HomePageActivity.class);
                            i.putExtra("content", new ArrayList<>(offerDataList));
                            startActivity(i);
                        }
                    }, 7000);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    /* Do Nothing */
                }
            });
        }

        /* Logout
        initialTextMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseAuth firebaseInstance = FirebaseAuth.getInstance();
                if (null != firebaseInstance.getCurrentUser() && firebaseInstance.getCurrentUser().getProviderData().size() > 0) {
                    LoginActivity.signOut(firebaseInstance.getCurrentUser()
                            .getProviderData().get(1).toString());
                    startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 357);
                }
            }
        });
        */
    }

    /**
     * Get MD5.
     *
     * @param input String Data
     * @return MD5 Data
     */
    public static String getMd5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String md5 = number.toString(16);

            while (md5.length() < 32)
                md5 = "0" + md5;

            return md5;
        } catch (NoSuchAlgorithmException e) {
            Log.e("MD5", e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * Get Current User
     *
     * @return currentUser Current User
     */
    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    /**
     * Get Logo Image.
     *
     * @param offerData Brand Data
     */
    private void getLogoImage(final OfferData offerData) {
        OfferBrandImageApi offerLogoApi = retrofit.create(OfferBrandImageApi.class);
        Call<List<OfferLogo>> call = offerLogoApi.getBrandLogoImage(offerData.getBrandName());
        call.enqueue(new Callback<List<OfferLogo>>() {
            @Override
            public void onResponse(Call<List<OfferLogo>> call, retrofit2.Response<List<OfferLogo>> response) {
                Log.i("***", offerData.toString());
                if (!CollectionUtils.isEmpty(response.body())) {
                    offerData.setBrandeImageURL(response.body().get(0).getLogo());
                }
            }

            @Override
            public void onFailure(Call<List<OfferLogo>> call, Throwable throwable) {
                Log.e("BRAND_LOGO_RETRIEVAL", throwable.toString());
            }
        });
    }
}
