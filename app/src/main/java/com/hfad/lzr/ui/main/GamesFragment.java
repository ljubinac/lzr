package com.hfad.lzr.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hfad.lzr.R;
import com.hfad.lzr.adapter.GameViewHolder;
import com.hfad.lzr.model.Game;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GamesFragment extends Fragment {

    DatabaseReference databaseReference;
    RecyclerView gamesRV;
    FirebaseRecyclerOptions<Game> options;
    FirebaseRecyclerAdapter adapter;
    TextView teamAname;
    TextView teamBname;

    boolean isFinished;

    ArrayList<Game> games = new ArrayList<>();

    public GamesFragment() {
        // Required empty public constructor
    }

    public static GamesFragment newInstance(boolean recent){
        GamesFragment gamesFragment = new GamesFragment();

        Bundle args = new Bundle();
        args.putBoolean("recent", recent);
        gamesFragment.setArguments(args);
        return gamesFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isFinished = getArguments().getBoolean("recent");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_games, container, false);

        gamesRV = root.findViewById(R.id.games_RV);
        /*teamAname = root.findViewById(R.id.teamA_tv);
        teamBname = root.findViewById(R.id.teamB_tv);*/

        gamesRV.setLayoutManager(new LinearLayoutManager(getContext()));
        gamesRV.setHasFixedSize(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("games");

        Query query = databaseReference.orderByChild("finished").equalTo(isFinished);

        options = new FirebaseRecyclerOptions.Builder<Game>().setQuery(query, Game.class).build();
        adapter = new FirebaseRecyclerAdapter<Game, GameViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull GameViewHolder holder, int position, @NonNull Game model) {
                holder.teamAnameTV.setText(model.getTeamAnaziv());
                holder.teamBnameTV.setText(model.getTeamBnaziv());
            }

            @NonNull
            @Override
            public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_item, parent, false);
                return  new GameViewHolder(view);
            }
        };
        adapter.startListening();
        gamesRV.setAdapter(adapter);

        return root;
    }
}