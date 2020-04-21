package com.hfad.lzr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.hfad.lzr.model.Game;
import com.hfad.lzr.model.PlayerGame;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Locale;

public class StatsActivity extends AppCompatActivity {


    private int STORAGE_PERMISSION_CODE = 1;

    ArrayList<PlayerGame> playersGameA, playersGameB;

    TableLayout tableTeamA, tableTeamB;

    TextView tableNum, tableName, tableTime, tableFG, table2pts, table3pts, table1pts, tableTotalReb, tableDefReb, tableOffReb, tableAssist, tableBlock, tableSteals, tableTurnov, tableFoul, tablePts, tableEff;

    TextView number, name, time, fg, pts2, pts3, pts1, totalReb, offReb, defReb, assist, block, steal, turnov, foul, pts, eff;

    LinearLayout ll;

    String myFilePath;

    Button share, create;

    Game game;

    int teamTime, teamFGM, teamFGA, team2PTSpm, team2PTSpa, team3PTSpm, team3PTSpa, team1PTSpm, team1PTSpa, teamOffReb, teamDefReb, teamReb, teamAssist, teamBlocks, teamSteals, teamTurnovers, teamFouls, teamPoints, teamEff;
    TextView teamTV, teamTitle, teamTimeTV, teamFGtv, team2ptsTV, team3ptsTV, team1ptsTV, teamOffRebTV, teamDefRebTV, teamRebTV, teamAssistTV, teamBlocksTV, teamStealsTV, teamTurnTV, teamFoulsTV, teamPointsTV, teamEffTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        ll = findViewById(R.id.ll);

        myFilePath = "";

        tableTeamA = findViewById(R.id.player_game_A);
        tableTeamB = findViewById(R.id.player_game_B);
        share = findViewById(R.id.sharePdf);
        create = findViewById(R.id.createPdf);

        playersGameA = ( ArrayList<PlayerGame> ) getIntent().getSerializableExtra("playersGameA");
        playersGameB = ( ArrayList<PlayerGame> ) getIntent().getSerializableExtra("playersGameB");
       /* firstLineupGameA = ( ArrayList<PlayerGame> ) getIntent().getSerializableExtra("firstLineupGameA");
        firstLineupGameB = ( ArrayList<PlayerGame> ) getIntent().getSerializableExtra("firstLineupGameB");*/
        game = ( Game ) getIntent().getSerializableExtra("game");

      /*  firstLineupGameA.addAll(playersGameA);
        firstLineupGameB.addAll(playersGameB);*/

        init(tableTeamA, playersGameA);
        setValue();
        init(tableTeamB, playersGameB);

        create.setEnabled(true);
        share.setEnabled(false);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePdf();
            }
        });

    }

    public void setValue(){
        teamTime = 0;
        teamFGM = 0;
        teamFGA = 0;
        team2PTSpm = 0;
        team2PTSpa = 0;
        team3PTSpm = 0;
        team3PTSpa = 0;
        team1PTSpm = 0;
        team1PTSpa = 0;
        teamDefReb = 0;
        teamOffReb = 0;
        teamReb = 0;
        teamAssist = 0;
        teamBlocks = 0;
        teamSteals = 0;
        teamTurnovers = 0;
        teamFouls = 0;
        teamPoints = 0;
        teamEff = 0;
    }

    public void sharePdf(){
        File fileWithinMyDir = new File(myFilePath);

        Uri pdfUri = Uri.fromFile(fileWithinMyDir);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            pdfUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", fileWithinMyDir);
        }

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            intentShareFile.setType("application/pdf");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, pdfUri);

            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                    "Sharing File...");
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

            startActivity(Intent.createChooser(intentShareFile, "Share File"));
    }

    public void createPdf(View view){

        if(ContextCompat.checkSelfPermission(StatsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestStoragePermission();
        }
        // get view group using reference
        // convert view group to bitmap
        byte[] bytesA = createImage(tableTeamA);
        byte[] bytesB = createImage(tableTeamB);
        try {
            imageToPDF(bytesA, bytesB);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        share.setEnabled(true);
    }

    public byte[] createImage(TableLayout table){
        table.setDrawingCacheEnabled(true);
        table.buildDrawingCache();
        Bitmap bm = table.getDrawingCache();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        return bytes.toByteArray();
    }

    public void imageToPDF(byte[] bytesA, byte[] bytesB) throws FileNotFoundException {
        try {
            Document document = new Document();
            String dirpath = android.os.Environment.getExternalStorageDirectory().toString();
            myFilePath = dirpath + "/" + game.getGameDate() + "_" + game.getTeamAnaziv() + "_" + game.getTeamBnaziv() + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(myFilePath)); //  Change pdf's name.
            document.open();
            Image imgA = Image.getInstance(bytesA);
            float scalerA = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin() - 0) / imgA.getWidth()) * 100;
            imgA.scalePercent(scalerA);

            //imgA.setSpacingAfter(50f);
            //imgA.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
            document.add(new Paragraph(game.getGameDate() + " " + game.getTeamAnaziv() + " vs. " + game.getTeamBnaziv()));
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph(game.getTeamAnaziv()));
            document.add(imgA);
            document.add(Chunk.NEWLINE);

            Image imgB = Image.getInstance(bytesB);
            float scalerB = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin() - 0) / imgB.getWidth()) * 100;
            imgB.scalePercent(scalerB);
            //imgB.setPaddingTop(20f);
            //imgB.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_CENTER);
            //imgB.setSpacingAfter();
            document.add(new Paragraph(game.getTeamBnaziv()));
            document.add(imgB);

            document.close();
            Toast.makeText(this, "PDF Generated successfully!..", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestStoragePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this).setTitle("Permission needed").setMessage("This permission is needed because of that and that").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(StatsActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                }
            }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).create().show();
        } else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Permission NOT GRANTED", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void init(TableLayout table, ArrayList<PlayerGame> playersList){

        TableRow row= new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);
        row.setBackgroundResource(R.drawable.table_border);

        tableNum = new TextView(this);
        tableNum.setBackgroundResource(R.drawable.table_border);
        tableNum.setPadding(15,15,15,15);

        tableName = new TextView(this);
        tableName.setBackgroundResource(R.drawable.table_border);
        tableName.setPadding(15,15,15,15);

        tableTime = new TextView(this);
        tableTime.setBackgroundResource(R.drawable.table_border);
        tableTime.setPadding(15,15,15,15);

        tableFG = new TextView(this);
        tableFG.setBackgroundResource(R.drawable.table_border);
        tableFG.setPadding(15,15,15,15);

        table2pts = new TextView(this);
        table2pts.setBackgroundResource(R.drawable.table_border);
        table2pts.setPadding(15,15,15,15);

        table3pts = new TextView(this);
        table3pts.setBackgroundResource(R.drawable.table_border);
        table3pts.setPadding(15,15,15,15);

        table1pts = new TextView(this);
        table1pts.setBackgroundResource(R.drawable.table_border);
        table1pts.setPadding(15,15,15,15);

        tableTotalReb = new TextView(this);
        tableTotalReb.setBackgroundResource(R.drawable.table_border);
        tableTotalReb.setPadding(15,15,15,15);

        tableDefReb = new TextView(this);
        tableDefReb.setBackgroundResource(R.drawable.table_border);
        tableDefReb.setPadding(15,15,15,15);

        tableOffReb = new TextView(this);
        tableOffReb.setBackgroundResource(R.drawable.table_border);
        tableOffReb.setPadding(15,15,15,15);

        tableAssist = new TextView(this);
        tableAssist.setBackgroundResource(R.drawable.table_border);
        tableAssist.setPadding(15,15,15,15);

        tableBlock = new TextView(this);
        tableBlock.setBackgroundResource(R.drawable.table_border);
        tableBlock.setPadding(15,15,15,15);

        tableSteals = new TextView(this);
        tableSteals.setBackgroundResource(R.drawable.table_border);
        tableSteals.setPadding(15,15,15,15);

        tableTurnov = new TextView(this);
        tableTurnov.setBackgroundResource(R.drawable.table_border);
        tableTurnov.setPadding(15,15,15,15);

        tableFoul = new TextView(this);
        tableFoul.setBackgroundResource(R.drawable.table_border);
        tableFoul.setPadding(15,15,15,15);

        tablePts = new TextView(this);
        tablePts.setBackgroundResource(R.drawable.table_border);
        tablePts.setPadding(15,15,15,15);

        tableEff = new TextView(this);
        tableEff.setBackgroundResource(R.drawable.table_border);
        tableEff.setPadding(15,15,15,15);

        tableNum.setText(R.string.no);
        tableName.setText(R.string.PLAYER);
        tableTime.setText(R.string.Time);
        tableFG.setText(R.string.FG);
        table2pts.setText(R.string.pts2);
        table3pts.setText(R.string.pts3);
        table1pts.setText(R.string.FT);
        tableOffReb.setText(R.string.OREB);
        tableDefReb.setText(R.string.DREB);
        tableTotalReb.setText(R.string.REB);
        tableAssist.setText(R.string.AST);
        tableBlock.setText(R.string.BLK);
        tableSteals.setText(R.string.STL);
        tableTurnov.setText(R.string.TOV);
        tableFoul.setText(R.string.PF);
        tablePts.setText(R.string.PTS);
        tableEff.setText(R.string.EFF);


        row.addView(tableNum);
        row.addView(tableName);
        row.addView(tableTime);
        row.addView(tableFG);
        row.addView(table2pts);
        row.addView(table3pts);
        row.addView(table1pts);
        row.addView(tableOffReb);
        row.addView(tableDefReb);
        row.addView(tableTotalReb);
        row.addView(tableAssist);
        row.addView(tableBlock);
        row.addView(tableSteals);
        row.addView(tableTurnov);
        row.addView(tableFoul);
        row.addView(tablePts);
        row.addView(tableEff);


        table.addView(row, 0);

        for (int i = 0; i < playersList.size(); i++){

            TableRow row2 = new TableRow(this);
            row2.setLayoutParams(lp);
            row2.setBackgroundResource(R.drawable.table_border);

            number = new TextView(this);
            number.setBackgroundResource(R.drawable.table_border);
            number.setPadding(15,15,15,15);

            name = new TextView(this);
            name.setBackgroundResource(R.drawable.table_border);
            name.setPadding(15,15,15,15);

            time = new TextView(this);
            time.setBackgroundResource(R.drawable.table_border);
            time.setPadding(15,15,15,15);

            fg = new TextView(this);
            fg.setBackgroundResource(R.drawable.table_border);
            fg.setPadding(15,15,15,15);

            pts2 = new TextView(this);
            pts2.setBackgroundResource(R.drawable.table_border);
            pts2.setPadding(15,15,15,15);

            pts3 = new TextView(this);
            pts3.setBackgroundResource(R.drawable.table_border);
            pts3.setPadding(15,15,15,15);

            pts1 = new TextView(this);
            pts1.setBackgroundResource(R.drawable.table_border);
            pts1.setPadding(15,15,15,15);

            offReb = new TextView(this);
            offReb.setBackgroundResource(R.drawable.table_border);
            offReb.setPadding(15,15,15,15);

            defReb = new TextView(this);
            defReb.setBackgroundResource(R.drawable.table_border);
            defReb.setPadding(15,15,15,15);

            totalReb = new TextView(this);
            totalReb.setBackgroundResource(R.drawable.table_border);
            totalReb.setPadding(15,15,15,15);

            assist = new TextView(this);
            assist.setBackgroundResource(R.drawable.table_border);
            assist.setPadding(15,15,15,15);

            block = new TextView(this);
            block.setBackgroundResource(R.drawable.table_border);
            block.setPadding(15,15,15,15);

            steal = new TextView(this);
            steal.setBackgroundResource(R.drawable.table_border);
            steal.setPadding(15,15,15,15);

            turnov = new TextView(this);
            turnov.setBackgroundResource(R.drawable.table_border);
            turnov.setPadding(15,15,15,15);

            foul = new TextView(this);
            foul.setBackgroundResource(R.drawable.table_border);
            foul.setPadding(15,15,15,15);

            pts = new TextView(this);
            pts.setBackgroundResource(R.drawable.table_border);
            pts.setPadding(15,15,15,15);

            eff = new TextView(this);
            eff.setBackgroundResource(R.drawable.table_border);
            eff.setPadding(15,15,15,15);

            number.setText(playersList.get(i).getNumber());
            name.setText(playersList.get(i).getNameAndLastname());
            int minutes = (playersList.get(i).getMinutes() / 1000) / 60;
            int seconds = (playersList.get(i).getMinutes() / 1000) % 60;

            String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

            time.setText(timeLeftFormatted);

            String fgm = String.valueOf(playersList.get(i).getPm2() + playersList.get(i).getPm3());
            String fga = String.valueOf(playersList.get(i).getPa2() + playersList.get(i).getPa3());
            String totalFG = fgm + "/" + fga;
            fg.setText(totalFG);

            String p2 = playersList.get(i).getPm2() + "/" + playersList.get(i).getPa2();
            pts2.setText(p2);

            String p3 = playersList.get(i).getPm3() + "/" + playersList.get(i).getPa3();
            pts3.setText(p3);

            String p1 = playersList.get(i).getPm1() + "/" + playersList.get(i).getPa1();
            pts1.setText(p1);

            offReb.setText(String.valueOf(playersList.get(i).getOffReb()));
            defReb.setText(String.valueOf(playersList.get(i).getDefReb()));
            String tReb = String.valueOf(playersList.get(i).getOffReb() + playersList.get(i).getDefReb());
            totalReb.setText(tReb);
            assist.setText(String.valueOf(playersList.get(i).getAsist()));
            block.setText(String.valueOf(playersList.get(i).getBlock()));
            steal.setText(String.valueOf(playersList.get(i).getSteal()));
            turnov.setText(String.valueOf(playersList.get(i).getTurnover()));
            foul.setText(String.valueOf(playersList.get(i).getFoul()));

            String totalPoints = String.valueOf((playersList.get(i).getPm2() * 2)
                    + (playersList.get(i).getPm3() * 3) + playersList.get(i).getPm1());
            pts.setText(totalPoints);

            String index = String.valueOf(((playersList.get(i).getPm2() * 2)
                    + (playersList.get(i).getPm3() * 3)
                    + playersList.get(i).getPm1())
                    + (playersList.get(i).getOffReb() + playersList.get(i).getDefReb())
                    + playersList.get(i).getAsist() + playersList.get(i).getBlock()
                    + playersList.get(i).getSteal() - playersList.get(i).getTurnover()
                    - playersList.get(i).getFoul() - (playersList.get(i).getPa2() - playersList.get(i).getPm2())
                    - (playersList.get(i).getPa3() - playersList.get(i).getPm3())
                    - (playersList.get(i).getPa1() - playersList.get(i).getPm1()) );
            eff.setText(index);

            teamTime += playersList.get(i).getMinutes();
            teamFGM += (playersList.get(i).getPm2() + playersList.get(i).getPm3());
            teamFGA += (playersList.get(i).getPa2() + playersList.get(i).getPa3());
            team2PTSpm += playersList.get(i).getPm2();
            team2PTSpa += playersList.get(i).getPa2();
            team3PTSpm += playersList.get(i).getPm3();
            team3PTSpa += playersList.get(i).getPa3();
            team1PTSpm += playersList.get(i).getPm1();
            team1PTSpa += playersList.get(i).getPa1();
            teamOffReb += playersList.get(i).getOffReb();
            teamDefReb += playersList.get(i).getDefReb();
            teamReb += (playersList.get(i).getOffReb() + playersList.get(i).getDefReb());
            teamAssist += playersList.get(i).getAsist();
            teamBlocks += playersList.get(i).getBlock();
            teamSteals += playersList.get(i).getSteal();
            teamTurnovers += playersList.get(i).getTurnover();
            teamFouls += playersList.get(i).getFoul();
            teamPoints += ((playersList.get(i).getPm2() * 2)
                    + (playersList.get(i).getPm3() * 3) + playersList.get(i).getPm1());
            teamEff += ((playersList.get(i).getPm2() * 2)
                    + (playersList.get(i).getPm3() * 3)
                    + playersList.get(i).getPm1())
                    + (playersList.get(i).getOffReb() + playersList.get(i).getDefReb())
                    + playersList.get(i).getAsist() + playersList.get(i).getBlock()
                    + playersList.get(i).getSteal() - playersList.get(i).getTurnover()
                    - playersList.get(i).getFoul() - (playersList.get(i).getPa2() - playersList.get(i).getPm2())
                    - (playersList.get(i).getPa3() - playersList.get(i).getPm3())
                    - (playersList.get(i).getPa1() - playersList.get(i).getPm1());

            row2.addView(number);
            row2.addView(name);
            row2.addView(time);
            row2.addView(fg);
            row2.addView(pts2);
            row2.addView(pts3);
            row2.addView(pts1);
            row2.addView(offReb);
            row2.addView(defReb);
            row2.addView(totalReb);
            row2.addView(assist);
            row2.addView(block);
            row2.addView(steal);
            row2.addView(turnov);
            row2.addView(foul);
            row2.addView(pts);
            row2.addView(eff);

            table.addView(row2, i+1);
        }

        TableRow row3 = new TableRow(this);
        row3.setLayoutParams(lp);
        row3.setBackgroundResource(R.drawable.table_border);

        teamTV = new TextView(this);
        teamTV.setBackgroundResource(R.drawable.table_border);
        teamTV.setPadding(15,15,15,15);

        teamTitle = new TextView(this);
        teamTitle.setBackgroundResource(R.drawable.table_border);
        teamTitle.setPadding(15,15,15,15);

        teamTimeTV = new TextView(this);
        teamTimeTV.setBackgroundResource(R.drawable.table_border);
        teamTimeTV.setPadding(15,15,15,15);

        teamFGtv = new TextView(this);
        teamFGtv.setBackgroundResource(R.drawable.table_border);
        teamFGtv.setPadding(15,15,15,15);

        team2ptsTV = new TextView(this);
        team2ptsTV.setBackgroundResource(R.drawable.table_border);
        team2ptsTV.setPadding(15,15,15,15);

        team3ptsTV = new TextView(this);
        team3ptsTV.setBackgroundResource(R.drawable.table_border);
        team3ptsTV.setPadding(15,15,15,15);

        team1ptsTV = new TextView(this);
        team1ptsTV.setBackgroundResource(R.drawable.table_border);
        team1ptsTV.setPadding(15,15,15,15);

        teamOffRebTV = new TextView(this);
        teamOffRebTV.setBackgroundResource(R.drawable.table_border);
        teamOffRebTV.setPadding(15,15,15,15);

        teamDefRebTV = new TextView(this);
        teamDefRebTV.setBackgroundResource(R.drawable.table_border);
        teamDefRebTV.setPadding(15,15,15,15);

        teamRebTV = new TextView(this);
        teamRebTV.setBackgroundResource(R.drawable.table_border);
        teamRebTV.setPadding(15,15,15,15);

        teamAssistTV = new TextView(this);
        teamAssistTV.setBackgroundResource(R.drawable.table_border);
        teamAssistTV.setPadding(15,15,15,15);

        teamBlocksTV = new TextView(this);
        teamBlocksTV.setBackgroundResource(R.drawable.table_border);
        teamBlocksTV.setPadding(15,15,15,15);

        teamStealsTV = new TextView(this);
        teamStealsTV.setBackgroundResource(R.drawable.table_border);
        teamStealsTV.setPadding(15,15,15,15);

        teamTurnTV = new TextView(this);
        teamTurnTV.setBackgroundResource(R.drawable.table_border);
        teamTurnTV.setPadding(15,15,15,15);

        teamFoulsTV = new TextView(this);
        teamFoulsTV.setBackgroundResource(R.drawable.table_border);
        teamFoulsTV.setPadding(15,15,15,15);

        teamPointsTV = new TextView(this);
        teamPointsTV.setBackgroundResource(R.drawable.table_border);
        teamPointsTV.setPadding(15,15,15,15);

        teamEffTV = new TextView(this);
        teamEffTV.setBackgroundResource(R.drawable.table_border);
        teamEffTV.setPadding(15,15,15,15);

        teamTV.setText(R.string.taraba);
        teamTitle.setText(R.string.total);
        teamTimeTV.setText(String.valueOf(teamTime));
        teamFGtv.setText(teamFGM + R.string.kosa_crta + teamFGA);
        team2ptsTV.setText(team2PTSpm + R.string.kosa_crta + team2PTSpa);
        team3ptsTV.setText(team3PTSpm + R.string.kosa_crta + team3PTSpa);
        team1ptsTV.setText(team1PTSpm + R.string.kosa_crta + team1PTSpa);
        teamOffRebTV.setText(String.valueOf(teamOffReb));
        teamDefRebTV.setText(String.valueOf(teamDefReb));
        teamRebTV.setText(String.valueOf(teamReb));
        teamAssistTV.setText(String.valueOf(teamAssist));
        teamBlocksTV.setText(String.valueOf(teamBlocks));
        teamStealsTV.setText(String.valueOf(teamSteals));
        teamTurnTV.setText(String.valueOf(teamTurnovers));
        teamFoulsTV.setText(String.valueOf(teamFouls));
        teamPointsTV.setText(String.valueOf(teamPoints));
        teamEffTV.setText(String.valueOf(teamEff));

        row3.addView(teamTV);
        row3.addView(teamTitle);
        row3.addView(teamTimeTV);
        row3.addView(teamFGtv);
        row3.addView(team2ptsTV);
        row3.addView(team3ptsTV);
        row3.addView(team1ptsTV);
        row3.addView(teamOffRebTV);
        row3.addView(teamDefRebTV);
        row3.addView(teamRebTV);
        row3.addView(teamAssistTV);
        row3.addView(teamBlocksTV);
        row3.addView(teamStealsTV);
        row3.addView(teamTurnTV);
        row3.addView(teamFoulsTV);
        row3.addView(teamPointsTV);
        row3.addView(teamEffTV);

        table.addView(row3);
    }
}