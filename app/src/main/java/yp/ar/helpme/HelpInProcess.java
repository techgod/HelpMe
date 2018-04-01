package yp.ar.helpme;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HelpInProcess extends Fragment {

    final String TAG = HelpInProcess.class.getSimpleName();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.help_in_progress, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final TextView tv = view.findViewById(R.id.hip_tv);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("accepted_id");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG,"value:"+dataSnapshot.getValue());
                    if (dataSnapshot.getValue().equals("1")) {
                        tv.setText("Yash is on the way.");
                    } else if(dataSnapshot.getValue().equals("2")){
                        tv.setText("Aakash is on the way.");
                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final TextView tv2 = view.findViewById(R.id.hip_tv);
        Button b = view.findViewById(R.id.hip_cancel);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatabaseReference myRef = database.getReference("Help Requests/User");
                final DatabaseReference myRef2 = database.getReference("accepted_id");
                myRef.removeValue();
                myRef2.setValue(new UserID(""));
                tv2.setText("Waiting for assistance...");
                ((MainActivity) getActivity()).showRE();
            }
        });

    }
}