package com.example.chords;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.chords.adapter.FavoriteAdapter;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<String> list = new ArrayList<>();

    SharedPreferences sp;
    Button btnClearAll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerView = findViewById(R.id.recyclerView);

        sp = getSharedPreferences("favorites", MODE_PRIVATE);

        String saved = sp.getString("list", "");

        String[] arr = saved.split(";");

        for (String s : arr) {
            if (!s.isEmpty()) {
                list.add(s);
            }
        }

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerView.setAdapter(

                new FavoriteAdapter(

                        list,

                        progression -> {

                            Intent intent =
                                    new Intent(
                                            FavoritesActivity.this,
                                            ProgressionEditorActivity.class
                                    );

                            String[] a =
                                    progression.split("\\|");

                            if (a.length >= 2) {

                                intent.putExtra(
                                        "progression",
                                        a[1]   // 只传和弦部分
                                );

                            } else {

                                intent.putExtra(
                                        "progression",
                                        progression
                                );
                            }

                            startActivity(intent);
                        },
                        position -> {
                            showDeleteDialog(position);
                        }
                )
        );
        btnClearAll =
                findViewById(
                        R.id.btnClearAll
                );
        btnClearAll.setOnClickListener(v -> {

            new AlertDialog.Builder(this)

                    .setTitle(
                            "Clear Favorites"
                    )

                    .setMessage(
                            "Delete all favorites?"
                    )

                    .setPositiveButton(
                            "Delete",

                            (d, w) -> {

                                list.clear();

                                saveList();

                                recyclerView
                                        .getAdapter()
                                        .notifyDataSetChanged();
                            })

                    .setNegativeButton(
                            "Cancel",
                            null)

                    .show();

        });
    }
    private void showDeleteDialog(int position) {

        AlertDialog dialog =
                new AlertDialog.Builder(this)

                        .setTitle("Delete")

                        .setMessage(
                                "Delete this progression?"
                        )

                        .setPositiveButton(
                                "Delete",
                                (d, w) -> {

                                }
                        )

                        .setNegativeButton(
                                "Cancel",
                                null
                        )

                        .create();

        dialog.show();

// 白底
        dialog.getWindow()
                .setBackgroundDrawableResource(
                        android.R.color.white
                );

// 黑字
        dialog.getButton(
                AlertDialog.BUTTON_POSITIVE
        ).setTextColor(Color.BLACK);

        dialog.getButton(
                AlertDialog.BUTTON_NEGATIVE
        ).setTextColor(Color.BLACK);
    }
    private void saveList() {

        StringBuilder builder =
                new StringBuilder();

        for (String s : list) {

            builder.append(s);

            builder.append(";");
        }

        getSharedPreferences(
                "favorites",
                MODE_PRIVATE
        )

                .edit()

                .putString(
                        "list",
                        builder.toString()
                )

                .apply();
    }
}