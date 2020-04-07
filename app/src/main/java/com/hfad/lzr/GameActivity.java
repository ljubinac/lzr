package com.hfad.lzr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hfad.lzr.adapter.PlayersAdapter;
import com.hfad.lzr.adapter.PlayersGameAdapter;
import com.hfad.lzr.model.Player;
import com.hfad.lzr.model.PlayerGame;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    ArrayList<PlayerGame> playersGameA;
    ArrayList<PlayerGame> playersGameB;
    String teamAid;
    String teamBid;
    RecyclerView teamArv;
    RecyclerView teamBrv;
    PlayersGameAdapter adapterA;
    PlayersGameAdapter adapterB;
    DatabaseReference databaseReferenceA;
    DatabaseReference databaseReferenceB;
    RecyclerView.LayoutManager layoutManager;
    String teamA;
    String teamB;
    TextView teamATV;
    TextView teamBTV;
    PlayerGame current;
    LinearLayout ll2pm;
    LinearLayout ll2pa;
    TextView tv2pm;
    TextView tv2pa;
    TextView resAtv;
    TextView resBtv;
    int resA;
    int resB;
    boolean currentTeam;
    LinearLayout ll3pm;
    LinearLayout ll3pa;
    TextView tv3pm;
    TextView tv3pa;
    LinearLayout ll1pm;
    LinearLayout ll1pa;
    TextView tv1pm;
    TextView tv1pa;
    LinearLayout llOffReb;
    LinearLayout llDefReb;
    TextView tvOffReb;
    TextView tvDefReb;
    LinearLayout llAsist;
    LinearLayout llTurnov;
    TextView tvAsist;
    TextView tvTurnov;
    LinearLayout llBlock;
    LinearLayout llFoul;
    TextView tvBlock;
    TextView tvFoul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        playersGameA = (ArrayList<PlayerGame>) getIntent().getSerializableExtra("playersGameA");
        playersGameB = (ArrayList<PlayerGame>) getIntent().getSerializableExtra("playersGameB");
        teamAid = getIntent().getStringExtra("teamAid");
        teamBid = getIntent().getStringExtra("teamBid");
        teamA = getIntent().getStringExtra("teamA");
        teamB = getIntent().getStringExtra("teamB");


        teamATV = findViewById(R.id.teamA);
        teamBTV = findViewById(R.id.teamB);

        teamATV.setText(teamA);
        teamBTV.setText(teamB);

        ll2pm = findViewById(R.id.ll_2PM);
        ll2pa = findViewById(R.id.ll_2PA);
        tv2pm = findViewById(R.id.tv2pm);
        tv2pa = findViewById(R.id.tv2pa);
        resAtv = findViewById(R.id.scoreTeamA);
        resBtv = findViewById(R.id.scoreTeamB);
        ll3pm = findViewById(R.id.ll_3PM);
        ll3pa = findViewById(R.id.ll_3PA);
        tv3pm = findViewById(R.id.tv3pm);
        tv3pa = findViewById(R.id.tv3pa);
        ll1pm = findViewById(R.id.ll_1PM);
        ll1pa = findViewById(R.id.ll_1PA);
        tv1pm = findViewById(R.id.tv1pm);
        tv1pa = findViewById(R.id.tv1pa);
        llOffReb = findViewById(R.id.ll_offReb);
        llDefReb = findViewById(R.id.ll_defReb);
        tvOffReb = findViewById(R.id.offRebTV);
        tvDefReb = findViewById(R.id.defRebTV);
        llAsist = findViewById(R.id.ll_assist);
        llTurnov = findViewById(R.id.ll_turnovers);
        tvAsist = findViewById(R.id.assistTV);
        tvTurnov = findViewById(R.id.turnoversTV);
        llBlock = findViewById(R.id.ll_block);
        llFoul = findViewById(R.id.ll_foul);
        tvBlock = findViewById(R.id.blockTV);
        tvFoul = findViewById(R.id.foulTV);

        resA = 0;
        resB = 0;
        currentTeam = false;

        buildRecyclerViewA();
        buildRecyclerViewB();

        resAtv.setText(String.valueOf(resA));
        resBtv.setText(String.valueOf(resB));


        ll2pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current.setPm2(current.getPm2() + 1);
                current.setPa2(current.getPa2() + 1);
                tv2pm.setText(String.valueOf(current.getPm2()));
                tv2pa.setText(String.valueOf(current.getPa2()));
                setRes(2);
            }
        });

        ll2pa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current.setPa2(current.getPa2() + 1);
                tv2pa.setText(String.valueOf(current.getPa2()));
            }
        });

        ll3pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current.setPm3(current.getPm3() + 1);
                current.setPa3(current.getPa3() + 1);
                tv3pm.setText(String.valueOf(current.getPm3()));
                tv3pa.setText(String.valueOf(current.getPa3()));
                setRes(3);
            }
        });

        ll3pa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current.setPa3(current.getPa3() + 1);
                tv3pa.setText(String.valueOf(current.getPa3()));
            }
        });

        ll1pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current.setPm1(current.getPm1() + 1);
                current.setPa1(current.getPa1() + 1);
                tv1pm.setText(String.valueOf(current.getPm1()));
                tv1pa.setText(String.valueOf(current.getPa1()));
                setRes(1);
            }
        });

        ll1pa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current.setPa1(current.getPa1() + 1);
                tv1pa.setText(String.valueOf(current.getPa1()));
            }
        });

        llOffReb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current.setOffReb(current.getOffReb() + 1);
                tvOffReb.setText(String.valueOf(current.getOffReb()));
            }
        });

        llDefReb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current.setDefReb(current.getDefReb() + 1);
                tvDefReb.setText(String.valueOf(current.getDefReb()));
            }
        });

        llAsist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current.setAsist(current.getAsist() + 1);
                tvAsist.setText(String.valueOf(current.getAsist()));
            }
        });

        llTurnov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current.setTurnover(current.getTurnover() + 1);
                tvTurnov.setText(String.valueOf(current.getTurnover()));
            }
        });

        llBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current.setBlock(current.getBlock() + 1);
                tvBlock.setText(String.valueOf(current.getBlock()));
            }
        });

        llFoul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current.setFoul(current.getFoul() + 1);
                tvFoul.setText(String.valueOf(current.getFoul()));
            }
        });


    }

    public void setRes(int points){
        if (currentTeam){
            resB = resB + points;
            resBtv.setText(String.valueOf(resB));
        } else {
            resA = resA + points;
            resAtv.setText(String.valueOf(resA));
        }
    }

    public void setValues(){
        tv2pm.setText(String.valueOf(current.getPm2()));
        tv2pa.setText(String.valueOf(current.getPa2()));

        tv3pm.setText(String.valueOf(current.getPm3()));
        tv3pa.setText(String.valueOf(current.getPa3()));

        tv1pm.setText(String.valueOf(current.getPm1()));
        tv1pa.setText(String.valueOf(current.getPa1()));

        tvOffReb.setText(String.valueOf(current.getOffReb()));
        tvDefReb.setText(String.valueOf(current.getDefReb()));

        tvAsist.setText(String.valueOf(current.getAsist()));
        tvTurnov.setText(String.valueOf(current.getTurnover()));

        tvBlock.setText(String.valueOf(current.getBlock()));
        tvFoul.setText(String.valueOf(current.getFoul()));
    }


    public void buildRecyclerViewA() {
        teamArv = findViewById(R.id.firstTeamRV);
        teamArv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        adapterA = new PlayersGameAdapter(playersGameA);

        teamArv.setLayoutManager(layoutManager);
        teamArv.setAdapter(adapterA);
        adapterA.onItemClickListener(new PlayersGameAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                current = playersGameA.get(position);
                setValues();
                currentTeam =false;
            }
        });
    }

    public void buildRecyclerViewB() {
        teamBrv = findViewById(R.id.secondTeamRV);
        teamBrv.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        adapterB = new PlayersGameAdapter(playersGameB);

        teamBrv.setLayoutManager(layoutManager);
        teamBrv.setAdapter(adapterB);
        adapterB.onItemClickListener(new PlayersGameAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                current = playersGameB.get(position);
                setValues();
                currentTeam = true;
            }
        });
    }
}