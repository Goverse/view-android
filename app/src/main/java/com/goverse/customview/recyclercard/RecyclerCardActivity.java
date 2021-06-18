package com.goverse.customview.recyclercard;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.goverse.customview.R;

public class RecyclerCardActivity extends AppCompatActivity {

    public static final String TAG = "RecyclerCardActivity";
    private RecyclerCardLayout recyclerCardLayout;
    private RecyclerCardController recyclerCardController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_card);
        recyclerCardLayout = findViewById(R.id.layout_recyclerCard);
        recyclerCardController = new RecyclerCardController(this, recyclerCardLayout);
        recyclerCardController.addCard(new ImageCard(Color.GRAY));
        recyclerCardController.addCard(new ImageCard(Color.BLUE));
        recyclerCardController.addCard(new ImageCard(Color.BLACK));
        recyclerCardController.addCard(new ImageCard(Color.YELLOW));
        recyclerCardController.addCard(new ImageCard(Color.GREEN));
        recyclerCardController.addCard(new LinearComboCard());
        recyclerCardController.addCard(new ImageCard(Color.GRAY));
        recyclerCardController.addCard(new ImageCard(Color.BLUE));
        recyclerCardController.addCard(new ImageCard(Color.BLACK));
        recyclerCardController.addCard(new ImageCard(Color.YELLOW));
        recyclerCardController.addCard(new ImageCard(Color.GREEN));
        recyclerCardController.addCard(new ImageCard(Color.GRAY));
        recyclerCardController.addCard(new ImageCard(Color.BLUE));
        recyclerCardController.addCard(new ImageCard(Color.BLACK));
        recyclerCardController.addCard(new ImageCard(Color.YELLOW));
        recyclerCardController.addCard(new ImageCard(Color.GREEN));
    }

}