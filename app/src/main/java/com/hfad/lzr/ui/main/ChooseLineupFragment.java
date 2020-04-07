package com.hfad.lzr.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hfad.lzr.R;
import com.hfad.lzr.adapter.LineupViewHolder;
import com.hfad.lzr.model.Player;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChooseLineupFragment extends Fragment {

    TextView tv;
    String teamName;
    String teamId;

    DatabaseReference databaseReference;
    RecyclerView lineupRV;
    FirebaseRecyclerOptions<Player> options;
    FirebaseRecyclerAdapter adapter;
    TextView playerNumberTV;
    TextView playerNameTV;
    CheckBox lineupCheckbox;

    ArrayList<Player> playersChecked = new ArrayList<>();

    public static ChooseLineupFragment newInstance(String teamName, String teamId) {
        ChooseLineupFragment myFragment = new ChooseLineupFragment();

        Bundle args = new Bundle();
        args.putString("teamName", teamName);
        args.putString("teamId", teamId);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        teamName = getArguments().getString("teamName");
        teamId = getArguments().getString("teamId");


    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lineup, container, false);

        /*tv = root.findViewById(R.id.section_label);
        tv.setText(teamName + "    "  + teamId);*/

        lineupRV = root.findViewById(R.id.lineup_RV);
        playerNumberTV = root.findViewById(R.id.player_number_tv);
        playerNameTV = root.findViewById(R.id.player_name_tv);
        lineupCheckbox = root.findViewById(R.id.lineup_checkbox);

        lineupRV.setLayoutManager(new LinearLayoutManager(getContext()));
        lineupRV.setHasFixedSize(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("players");

        Query query = databaseReference.orderByChild("teamId").equalTo(teamId);
        options = new FirebaseRecyclerOptions.Builder<Player>().setQuery(query, Player.class).build();
        adapter = new FirebaseRecyclerAdapter<Player, LineupViewHolder>(options){

            @NonNull
            @Override
            public LineupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lineup_item, parent, false);
                return new LineupViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final LineupViewHolder holder, int position, @NonNull final Player model) {
                holder.playerNumberTV.setText(model.getNumber());
                holder.playerNameTV.setText(model.getNameAndLastname());

                holder.lineupCheckbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.lineupCheckbox.isChecked()){
                            onItemCheck(model);
                        } else {
                            onItemUncheck(model);
                        }
                    }
                });
            }
        };
        adapter.startListening();
        lineupRV.setAdapter(adapter);


        return root;
    }

    public void onItemCheck(Player player){
        playersChecked.add(player);
    }

    public void onItemUncheck(Player player){
        playersChecked.remove(player);
    }

    public ArrayList<Player> getData(){
        return playersChecked;
    }
}