package yp.ar.helpme;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

public class RequestEmergency extends Fragment{

    final String TAG = RequestEmergency.class.getSimpleName();
    Location mLocation;
    ValueEventListener removeEventListener;
    public static LatLng latLng;

    FusedLocationProviderClient mFusedLocationClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.request_emergency_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button emergency_button = view.findViewById(R.id.request_emergency_button);
        readFirebase();
        if(getMember().equals("1"))
        {
            ImageView imageView = getView().findViewById(R.id.profilePic);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.yp));
            TextView name = getView().findViewById(R.id.rel_name);
            name.setText("Yash Pradhan");
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        emergency_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });
    }

    private void readFirebase()
    {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Help Requests/User");
        removeEventListener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    if (!((String) ds.child("id").getValue()).equals(getMember())) {
                        //Toast.makeText(getContext(), "There is an emergency near you:" + (String)ds.child("name").getValue(), Toast.LENGTH_SHORT).show();
                        String name = (String) ds.child("name").getValue();
                        String msg = (String) ds.child("msg").getValue();
                        String lat = (String) ds.child("lat").getValue();
                        String lng = (String) ds.child("lng").getValue();
                        latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                        showEmergencyDetails(name, msg);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getLocation()
    {
        mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mLocation = location;
                Log.d(TAG,String.valueOf(location));
                writeLocationToFirebase();
            }
        });
    }

    private void writeLocationToFirebase()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Help Requests/User");

        User mem_user=null;
        EditText et = getView().findViewById(R.id.rel_extra_text);

        /*
        Users IDs:
        Yash is 1
        Aakash is 2
         */

        if(Build.MODEL.equals("ONEPLUS A5010"))
        {
            mem_user= new User("Yash","1","+919845359774","M",String.valueOf(mLocation.getLatitude()),String.valueOf(mLocation.getLongitude()),et.getText().toString());
        }
        else if (Build.MODEL.equals("XT1686"))
        {
            mem_user= new User("Aakash","2","+919987552227","M",String.valueOf(mLocation.getLatitude()),String.valueOf(mLocation.getLongitude()),et.getText().toString());
        }

        String key = myRef.push().getKey();
        myRef.child(key).setValue(mem_user);
        //myRef.removeEventListener(removeEventListener);

        ((MainActivity)getActivity()).showHIP();
    }

    private String getMember()
    {
        String s="0";
        if(Build.MODEL.equals("ONEPLUS A5010"))
        {
            s="1";

        }
        else if (Build.MODEL.equals("XT1686"))
        {
            s="2";
        }
        return s;
    }



    private void showEmergencyDetails(String name,String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Help Requests/User");
        final DatabaseReference myRef2 = database.getReference("accepted_id");
        builder.setTitle("Assistance Required!");
        builder.setMessage(name + " requires your assistance.\n" + msg + "\nCan you help?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getContext(), "You accepted!", Toast.LENGTH_SHORT).show();
                myRef2.setValue(getMember());
                ((MainActivity) getActivity()).showUMF();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                Toast.makeText(getContext(), "You rejected.", Toast.LENGTH_SHORT).show();
                myRef.removeValue();

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private User authenticateUser()
    {
        User mem_user=null;
        EditText et = getView().findViewById(R.id.rel_extra_text);

        /*
        Users IDs:
        Yash is 1
        Aakash is 2
         */

        if(Build.MODEL.equals("ONEPLUS A5010"))
        {
            mem_user= new User("Yash","1","+919845359774","M",String.valueOf(mLocation.getLatitude()),String.valueOf(mLocation.getLongitude()),et.getText().toString());

        }
        else if (Build.MODEL.equals("XT1686"))
        {
            mem_user= new User("Aakash","2","+919987552227","M",String.valueOf(mLocation.getLatitude()),String.valueOf(mLocation.getLongitude()),et.getText().toString());

        }
        return mem_user;
    }





    }
@IgnoreExtraProperties
class User {

    String name;
    String id;
    String contact;
    String gender;
    String lat;
    String lng;
    String msg;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name,String id,String contact,String gender,String lat,String lng,String msg) {
        this.name = name;
        this.contact = contact;
        this.gender = gender;
        this.lat=lat;
        this.lng=lng;
        this.msg = msg;
        this.id=id;
    }

}

@IgnoreExtraProperties
class UserID {

    String id;
    public UserID() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserID(String id) {
        this.id=id;
    }

}