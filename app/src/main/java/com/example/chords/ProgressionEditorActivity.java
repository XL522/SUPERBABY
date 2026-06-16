package com.example.chords;

import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chords.adapter.ChordAdapter;
import com.example.chords.model.Chord;
import com.example.chords.model.ChordProgression;

//import com.example.chords.music.Transposer;
import com.example.chords.player.ChordPlayer;
import com.example.chords.player.LoopPlayer;
import com.example.chords.player.Metronome;

import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import android.widget.EditText;

import java.util.Collections;

public class ProgressionEditorActivity
        extends AppCompatActivity {
    private TextView tvProgression;

    private ChordProgression progression =
            new ChordProgression();

    private Button btnC;
    private Button btnG;
    private Button btnAm;
    private Button btnF;

    private ChordPlayer chordPlayer;
    private LoopPlayer loopPlayer;
    private Metronome metronome;

    private Button btnPlay;
    private Button btnClear;

    private Button btnTransposeUp;
    private Button btnTransposeDown;


    private Button btnSave;

    private boolean isPlaying = false;

    private RecyclerView rvProgression;

    private ChordAdapter chordAdapter;
    @Override
    protected void onCreate(
            Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_progression_editor
        );
        rvProgression =
                findViewById(
                        R.id.rvProgression
                );

        chordAdapter =
                new ChordAdapter(
                        progression.getChords()
                );

        rvProgression.setLayoutManager(
                new LinearLayoutManager(
                        this
                )
        );

        rvProgression.setAdapter(
                chordAdapter
        );
        chordAdapter.notifyDataSetChanged();

        btnC =
                findViewById(R.id.btnC);

        btnG =
                findViewById(R.id.btnG);

        btnAm =
                findViewById(R.id.btnAm);

        btnF =
                findViewById(R.id.btnF);

        btnPlay = findViewById(R.id.btnPlay);
        btnClear = findViewById(R.id.btnClear);

        btnTransposeUp =
                findViewById(
                        R.id.btnTransposeUp);

        btnTransposeDown =
                findViewById(
                        R.id.btnTransposeDown);

        btnSave = findViewById(R.id.btnSave);

        chordPlayer = new ChordPlayer(this);

        loopPlayer = new LoopPlayer(chordPlayer);

        metronome = new Metronome(this);

        loopPlayer.setMetronome(metronome);

        loopPlayer.setMetronomeEnabled(true);

        loopPlayer.setBpm(120);

        btnC.setOnClickListener(v -> {

            progression.addChord(
                    new Chord(
                            "C",
                            "maj"
                    )
            );

            updateProgressionText();
        });
        btnG.setOnClickListener(v -> {
            progression.addChord(
                    new Chord("G","maj"));
            updateProgressionText();
        });

        btnAm.setOnClickListener(v -> {
            progression.addChord(
                    new Chord("A","min"));
            updateProgressionText();
        });

        btnF.setOnClickListener(v -> {
            progression.addChord(
                    new Chord("F","maj"));
            updateProgressionText();
        });

        btnPlay.setOnClickListener(v -> {

            if (progression.getChords().isEmpty())
                return;

            if (!isPlaying) {

                loopPlayer.start(progression);

                btnPlay.setText("STOP");

                isPlaying = true;

            } else {

                loopPlayer.stop();

                btnPlay.setText("PLAY");

                isPlaying = false;
            }
        });

        btnClear.setOnClickListener(v -> {

            stopPlayback();

            progression.getChords().clear();

            updateProgressionText();
        });

//        btnTransposeUp
//                .setOnClickListener(v -> {
//
//                    transpose(1);
//
//                });
//
//        btnTransposeDown
//                .setOnClickListener(v -> {
//
//                    transpose(-1);
//
//                });

        btnSave.setOnClickListener(v -> {

            if (progression.getChords().isEmpty())
                return;

            showSaveDialog();

        });
        String saved =
                getIntent().getStringExtra(
                        "progression"
                );

        if (saved != null) {

            loadProgression(saved);
        }
        ItemTouchHelper helper =
                new ItemTouchHelper(

                        new ItemTouchHelper.SimpleCallback(

                                ItemTouchHelper.UP
                                        |
                                        ItemTouchHelper.DOWN,

                                ItemTouchHelper.LEFT
                        ) {

                            @Override
                            public boolean onMove(
                                    RecyclerView recyclerView,

                                    RecyclerView.ViewHolder viewHolder,

                                    RecyclerView.ViewHolder target) {

                                int from =
                                        viewHolder
                                                .getAdapterPosition();

                                int to =
                                        target
                                                .getAdapterPosition();

                                Collections.swap(
                                        progression.getChords(),
                                        from,
                                        to
                                );

                                chordAdapter.notifyItemMoved(
                                        from,
                                        to
                                );

                                return true;
                            }


                            @Override
                            public void onSwiped(
                                    RecyclerView.ViewHolder viewHolder,
                                    int direction) {

                                stopPlayback();

                                int position =
                                        viewHolder
                                                .getAdapterPosition();

                                progression
                                        .getChords()
                                        .remove(position);

                                chordAdapter.notifyItemRemoved(
                                        position
                                );
                            }
                        });

        helper.attachToRecyclerView(
                rvProgression
        );
    }

    private void stopPlayback() {

        loopPlayer.stop();

        isPlaying = false;

        btnPlay.setText("PLAY");
    }

    private void updateProgressionText() {

        if (progression.getChords()
                .isEmpty()) {

            chordAdapter.notifyDataSetChanged();

            return;
        }

        StringBuilder builder =
                new StringBuilder();

        for (Chord chord :
                progression.getChords()) {

            builder.append(
                    chord.getDisplayName()
            );

            builder.append(" - ");
        }

        builder.delete(
                builder.length() - 3,
                builder.length()
        );

        chordAdapter.notifyDataSetChanged();
    }

//    private void transpose(
//            int semitone) {
//
//        progression =
//                Transposer.transpose(
//                        progression,
//                        semitone
//                );
//
//        updateProgressionText();
//    }

    private String progressionToString() {

        StringBuilder builder =
                new StringBuilder();

        for (Chord chord :
                progression.getChords()) {

            builder.append(
                    chord.getDisplayName()
            );

            builder.append(",");
        }

        return builder.toString();
    }

    private void saveProgression(
            String name) {

        SharedPreferences sp =
                getSharedPreferences(
                        "favorites",
                        MODE_PRIVATE
                );

        String old =
                sp.getString(
                        "list",
                        ""
                );

        String current =
                progressionToString();

        old +=
                name
                        + "|"
                        + current
                        + ";";

        sp.edit()
                .putString(
                        "list",
                        old
                )
                .apply();

        Toast.makeText(
                this,
                "Saved!",
                Toast.LENGTH_SHORT
        ).show();
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();

        stopPlayback();

        if (chordPlayer != null)
            chordPlayer.release();
    }
    private void loadProgression(
            String saved) {
        stopPlayback();
        progression.getChords().clear();

        String[] arr =
                saved.split(",");

        for (String s : arr) {

            if (s.isEmpty())
                continue;

            if (s.endsWith("m")) {

                progression.addChord(

                        new Chord(
                                s.substring(
                                        0,
                                        s.length() - 1
                                ),
                                "min"
                        )
                );

            } else {

                progression.addChord(

                        new Chord(
                                s,
                                "maj"
                        )
                );
            }
        }

        updateProgressionText();
    }
    private void showSaveDialog() {

        EditText editText =
                new EditText(this);

        editText.setHint(
                "Progression Name"
        );

        new AlertDialog.Builder(this)

                .setTitle("Save As")

                .setView(editText)

                .setNegativeButton(
                        "Cancel",
                        null
                )

                .setPositiveButton(
                        "Save",

                        (dialog, which) -> {

                            String name =
                                    editText
                                            .getText()
                                            .toString()
                                            .trim();

                            if (name.isEmpty()) {

                                name =
                                        "Untitled";
                            }

                            saveProgression(
                                    name
                            );
                        })

                .show();

    }
}