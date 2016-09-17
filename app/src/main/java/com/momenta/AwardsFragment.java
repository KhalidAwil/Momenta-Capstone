package com.momenta;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by Joe on 2016-02-01.
 * For Momenta
 */
public class AwardsFragment extends Fragment {


    public static final String ARG_PAGE = "ARG_PAGE";

    // Firebase instance variables
    private String directory = "tests";
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<Award, AwardViewHolder> mFirebaseAdapter;

    private RecyclerView mRecyclerView;


    //    WaterWaveProgress waveProgress;


    public static AwardsFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        AwardsFragment fragment = new AwardsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int mPage = getArguments().getInt(ARG_PAGE);

        FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mFirebaseUser != null) {
            directory = mFirebaseUser.getUid() + "/awards";
        }
        // New child entries
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Award, AwardViewHolder>(
                Award.class,
                R.layout.list_item,
                AwardViewHolder.class,
                mFirebaseDatabaseReference.child(directory)) {

            @Override
            protected void populateViewHolder(AwardViewHolder viewHolder,
                                              Award award, int position) {
                viewHolder.name.setText(award.getName());

            }

            @Override
            public void onBindViewHolder(AwardViewHolder viewHolder, int position) {
                super.onBindViewHolder(viewHolder, position);

                final Award award = getItem(position);
                //Set onClick listener for each activity
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), TaskActivity.class);
                        intent.putExtra(Task.ID, award.getId());
                        getContext().startActivity(intent);
                    }
                });
            }

        };
    }
    public static class AwardViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView timeSpent;
        public ProgressBar progressBar;

        public AwardViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.list_item_name);
            timeSpent = (TextView) itemView.findViewById(R.id.list_item_time_spent);

        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_awards, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.activity_recycler_view);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setAdapter(mFirebaseAdapter);

        return view;
    }



}
