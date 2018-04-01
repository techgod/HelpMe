package yp.ar.helpme;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserMapFragment extends Fragment implements OnMapReadyCallback {
    protected SupportMapFragment mMapFragment;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.usermap_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_frag);
        mMapFragment.getMapAsync(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        googleMap.addMarker(new MarkerOptions().position(RequestEmergency.latLng)
                .title("Help Seeker"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(RequestEmergency.latLng));

/*
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Help Requests/User");

        ValueEventListener removeEventListener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    //Toast.makeText(getContext(), "There is an emergency near you:" + (String)ds.child("name").getValue(), Toast.LENGTH_SHORT).show();
                    Location loc = (Location) ds.child("location").getValue();
                    LatLng desti = new LatLng(loc.getLatitude(),loc.getLongitude());
                    Log.d("helloworld",String.valueOf(desti));
                    mGoogleMap.addMarker(new MarkerOptions().position(desti)
                            .title("Help Seeker"));
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(desti));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myRef.removeEventListener(removeEventListener);
*/
    }
}
