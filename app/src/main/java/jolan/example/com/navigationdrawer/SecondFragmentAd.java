package jolan.example.com.navigationdrawer;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Jolan on 30-Oct-16.
 */

public class SecondFragmentAd extends Fragment {

    View view;
    private Button start;


    public SecondFragmentAd(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.second_layout_ad, container, false);

        start = (Button) view.findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), advertiseService1.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
