package com.momenta;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.content.Context;

import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Joe on 2016-02-01.
 * For Momenta
 */
public class DashboardFragment extends Fragment implements View.OnClickListener{
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    private View activityView;
    private NumberPicker numberPicker;
    private Button button;
    private helperPreferences helperPreferences;



    // Firebase instance variables
    private String directory = "tests";
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<Task, LogFragment.TaskViewHolder> mFirebaseAdapter;

    public static DashboardFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        DashboardFragment fragment = new DashboardFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activityView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        super.onCreate(savedInstanceState);
        helperPreferences = new helperPreferences(getActivity());

        FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mFirebaseUser != null) {
            directory = mFirebaseUser.getUid() + "/goals";
        }


        // New child entries
        mFirebaseDatabaseReference = FirebaseProvider.getInstance().getReference();

        mFirebaseDatabaseReference.child(directory).addValueEventListener(
                new ValueEventListener() {
                    @Override

                    public void onDataChange(DataSnapshot wholeSnap) {
                        DataSnapshot timeDir = wholeSnap.child(Task.TIME_SPENT);
                        Log.w("************", "Entering this stuff ***********************************");
                        //Iterate through to get all dates.
                        for ( DataSnapshot date: timeDir.getChildren() ) {
                            Log.w("************", "Entering this stuff1 ***********************************");
                            // Variable to hold the sum of time spent, for the date.
                            int totalTime = 0;
                            int totalGoal = 0;
                            for ( DataSnapshot id : date.getChildren() ) {
                                Log.w("************", "Entering this stuff2 ***********************************");
                                Task t =  new Task();
                                t.setId(id.getKey());
                                t.setTimeSpent( id.child(Task.TIME_SPENT).getValue(Integer.class) );
                                t.setGoal( id.child(Task.GOAL).getValue(Integer.class) );
                                totalTime += t.getTimeSpent();
                                totalGoal += t.getGoal();
                                Log.w("************", totalTime + "");
                            }

                            TextView totalTime2 = (TextView)activityView.findViewById(R.id.totalTimeSpent);
                            totalTime2.setText(totalTime + "");

                            TextView totalTime3 = (TextView)activityView.findViewById(R.id.goalTime);
                            totalTime3.setText(totalGoal + "");

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                }
        );
        Bundle args = getActivity().getIntent().getExtras();

        String name = args.getString("displayName");
        Log.d("pls", name + " test");
        String email = args.getString("email");

        String photo = args.getString("personPhoto");

        TextView displayNameText = (TextView)activityView.findViewById(R.id.displayName);
        displayNameText.setText(name);

        TextView emailText = (TextView)activityView.findViewById(R.id.email);
        emailText.setText(email);

        ImageView imgView = (ImageView)activityView.findViewById(R.id.imageView);
        Picasso.with(getActivity()).load(photo).into(imgView);

        return activityView;
    }

    @Override
    public void onClick(View v) {
        //  switch ( v.getId() ) {
        //      case R.id.button1:
        //         Intent intent = new Intent(this.getContext(), SelectTasksActivity.class);
        //         startActivity(intent);
        //          break;
        // }
    }
}
