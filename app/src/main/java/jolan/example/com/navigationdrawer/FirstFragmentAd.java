package jolan.example.com.navigationdrawer;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static jolan.example.com.navigationdrawer.advertiser.UID;

/**
 * Created by Jolan on 30-Oct-16.
 */

public class FirstFragmentAd extends Fragment {

    View view;
    private static DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    static List<infoToStore> info = new ArrayList<>();
    LinearLayout layout;
    ScrollView scrollView;



    public FirstFragmentAd(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.first_layout_ad, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        scrollView= new ScrollView(getActivity());
        final LinearLayout mainLayout = new LinearLayout(getActivity());
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setBackgroundResource(R.drawable.background);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Advertiser").orderByChild("key").equalTo(firebaseAuth.getCurrentUser().getUid().toString()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (final DataSnapshot advertiser : dataSnapshot.getChildren()) {
                            final String catString = advertiser.child("cat").getValue(String.class);
                            final String URLString = advertiser.child("downloadURL").getValue(String.class);
                            final String keyString = advertiser.child("key").getValue(String.class);
                            final String lat = advertiser.child("latitude").getValue(String.class);
                            final String lng = advertiser.child("longitude").getValue(String.class);
                            final String descString = advertiser.child("serviceDesc").getValue(String.class);
                            final String nameString = advertiser.child("serviceName").getValue(String.class);
                            final String idString = advertiser.getKey();
                            info.add(new infoToStore(catString, URLString, keyString, lat, lng, descString, nameString, idString));

                            LinearLayout ll = new LinearLayout(getActivity());
                            ll.setOrientation(LinearLayout.VERTICAL);

                            TextView name = new TextView(getActivity());
                            name.setText(nameString);
                            name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            ll.addView(name);

                            TextView desc = new TextView(getActivity());
                            desc.setText(descString);
                            desc.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            ll.addView(desc);

                            TextView cat=new TextView(getActivity());
                            cat.setText(catString);
                            cat.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            ll.addView(cat);

                            ImageView image = new ImageView(getActivity());
                            Picasso.with(getActivity())
                                    .load(URLString)
                                    .into(image);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.gravity = Gravity.CENTER;
                            image.setLayoutParams(params);
                            image.setAdjustViewBounds(true);
                            ll.addView(image);

                            final Button delete = new Button(getActivity());
                            delete.setText("Remove service");
                            delete.setLayoutParams(params);

                            ll.addView(delete);

                            delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new AlertDialog.Builder(getActivity())
                                            .setTitle("Delete service")
                                            .setMessage("Do you want to delete this service?")
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setPositiveButton(android.R.string.yes, new DialogInterface
                                                    .OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    databaseReference.child("Advertiser").child(advertiser.getKey()).removeValue()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "",
                                                                        "Deleting service", true);
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(getActivity(), "Service deleted", Toast.LENGTH_LONG).show();
                                                                }
                                                            });
                                                }
                                            }).setNegativeButton(android.R.string.no, null).show();
                                }
                            });

                            View v = new View(getActivity());
                            v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5));
                            v.setBackgroundColor(Color.parseColor("#000000"));
                            ll.addView(v);

                            mainLayout.addView(ll);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        scrollView.addView(mainLayout);
        return scrollView;
    }


}
