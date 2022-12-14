package com.example.tcchecker;

import static android.widget.ImageView.ScaleType.FIT_START;
import static android.widget.ImageView.ScaleType.FIT_XY;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.Icon;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowMetrics;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {

    //タイプ相性
    private int [][] typeComp = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0},            //ノーマル
            {0, -1, 1, 0, -1, -1, 0, 0, 1, 0, 0, -1, 1, 0, 0, 0, -1, -1},       //ほのお
            {0, -1, -1, 1, 1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1},        //みず
            {0, 0, 0, -1, 0, 0, 0, 0, 1, -1, 0, 0, 0, 0, 0, 0, -1, 0},          //でんき
            {0, 1, -1, -1, -1, 1, 0, 1, -1, 1, 0, 1, 0, 0, 0, 0, 0, 0},         //くさ
            {0, 1, 0, 0, 0, -1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0},            //こおり
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, -1, -1, 0, 0, -1, 0, 1},          //かくとう
            {0, 0, 0, 0, -1, 0, -1, -1, 1, 0, 1, -1, 0, 0, 0, 0, 0, -1},        //どく
            {0, 0, 1, 10, 1, 1, 0, -1, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0},          //じめん
            {0, 0, 0, 1, -1, 1, -1, 0, 10, 0, 0, -1, 1, 0, 0, 0, 0, 0},         //ひこう
            {0, 0, 0, 0, 0, 0, -1, 0, 0, 0, -1, 1, 0, 1, 0, 1, 0, 0},           //エスパー
            {0, 1, 0, 0, -1, 0, -1, 0, -1, 1, 0, 0, 1, 0, 0, 0, 0, 0},          //むし
            {-1, -1, 1, 0, 1, 0, 1, -1, 1, -1, 0, 0, 0, 0, 0, 0, 1, 0},         //いわ
            {10, 0, 0, 0, 0, 0, 10, -1, 0, 0, 0, -1, 0, 1, 0, 1, 0, 0},         //ゴースト
            {0, -1, -1, -1, -1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1},         //ドラゴン
            {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 10, 1, 0, -1, 0, -1, 0, 1},          //あく
            {-1, 1, 0, 0, -1, -1, 1, 10, 1, -1, -1, -1, -1, 0, -1, 0, -1, -1},  //はがね
            {0, 0, 0, 0, 0, 0, -1, 1, 0, 0, 0, -1, 0, 0, 10, -1, 1, 0}          //フェアリー
    };

    //相性計算用
    private int [] comp = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    //画像の設定
    int icon[] = {
            R.drawable.normal, R.drawable.fire, R.drawable.water, R.drawable.electric, R.drawable.grass,
            R.drawable.ice, R.drawable.fighting, R.drawable.poison, R.drawable.ground, R.drawable.flying,
            R.drawable.psychic, R.drawable.bug, R.drawable.rock, R.drawable.ghost, R.drawable.dragon,
            R.drawable.dark, R.drawable.steel, R.drawable.fairy,
    };

    private LinearLayout lay1;
    private LinearLayout lay2;
    private LinearLayout lay3;
    private LinearLayout lay4;
    private LinearLayout lay5;
    private LinearLayout lay6;
    private LinearLayout lay7;

    private static final String TAG = "MainActivity";
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //広告の初期化
        MobileAds.initialize(this,
                initializationStatus -> {
                });
        adView = findViewById(R.id.adv);
        AdRequest adq = new AdRequest.Builder()
                .build();
        adView.loadAd(adq);

        initSpinner();

        lay1 = findViewById(R.id.layout1);
        lay2 = findViewById(R.id.layout2);
        lay3 = findViewById(R.id.layout3);
        lay4 = findViewById(R.id.layout4);
        lay5 = findViewById(R.id.layout5);
        lay6 = findViewById(R.id.layout6);
        lay7 = findViewById(R.id.layout7);
    }

    private void initSpinner(){
        Spinner spinner1 = findViewById(R.id.spinner1);
        Spinner spinner2 = findViewById(R.id.spinner2);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner,
                getResources().getStringArray(R.array.list)
        );

        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);

        // リスナーを登録
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //　アイテムが選択された時
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                
            }

            //　アイテムが選択されなかった
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });
    }

    private void ResetLayout(){
        lay1.removeAllViews();
        lay2.removeAllViews();
        lay3.removeAllViews();
        lay4.removeAllViews();
        lay5.removeAllViews();
        lay6.removeAllViews();
        lay7.removeAllViews();
    }

    public void onButton(View view) {
        Spinner spinner1 = findViewById(R.id.spinner1);
        Spinner spinner2 = findViewById(R.id.spinner2);

        int idx1 = spinner1.getSelectedItemPosition();
        int idx2 = spinner2.getSelectedItemPosition();



        int num = 0;

        ResetLayout();

        //両方空の場合は計算しない
        if (!(idx1 == 0 & idx2 == 0)) {
            //タイプごとに相性計算
            for (int i = 0; i < 18; i++) {
                comp[i] += typeComp[idx1][i];
                if (idx1 != idx2) {  //両方同じ場合は片方のみ計算
                    comp[i] += typeComp[idx2][i];
                }
            }


            //相性によって画像を配置
            for (int i = 0; i < 18; i++) {
                if (comp[i] == 2) {
                    ImageView imageview = new ImageView(this);
                    imageview.setImageResource(icon[i]);
                    LinearLayout.LayoutParams layoutParams =
                            new LinearLayout.LayoutParams(lay1.getHeight()+2, lay1.getHeight()-2);
                    imageview.setLayoutParams(layoutParams);
                    lay1.addView(imageview);
                } else if (comp[i] == 1) {
                    ImageView imageview = new ImageView(this);
                    imageview.setImageResource(icon[i]);
                    LinearLayout.LayoutParams layoutParams =
                            new LinearLayout.LayoutParams(lay2.getHeight()+2, lay2.getHeight()-2);
                    imageview.setLayoutParams(layoutParams);
                    lay2.addView(imageview);
                } else if (comp[i] == 0) {
                    num++;
                    ImageView imageview = new ImageView(this);
                    imageview.setImageResource(icon[i]);
                    LinearLayout.LayoutParams layoutParams =
                            new LinearLayout.LayoutParams(lay3.getHeight()+2, lay3.getHeight()-2);
                    imageview.setLayoutParams(layoutParams);
                    if((num+1)*(lay3.getHeight()+2) < lay3.getWidth()) {
                        lay3.addView(imageview);
                    }else {
                        lay4.addView(imageview);
                    }
                } else if (comp[i] == -1) {
                    ImageView imageview = new ImageView(this);
                    imageview.setImageResource(icon[i]);
                    LinearLayout.LayoutParams layoutParams =
                            new LinearLayout.LayoutParams(lay5.getHeight()+2, lay5.getHeight()-2);
                    imageview.setLayoutParams(layoutParams);
                    lay5.addView(imageview);
                } else if (comp[i] == -2) {
                    ImageView imageview = new ImageView(this);
                    imageview.setImageResource(icon[i]);
                    LinearLayout.LayoutParams layoutParams =
                            new LinearLayout.LayoutParams(lay6.getHeight()+2, lay6.getHeight()-2);
                    imageview.setLayoutParams(layoutParams);
                    lay6.addView(imageview);
                } else {
                    ImageView imageview = new ImageView(this);
                    imageview.setImageResource(icon[i]);
                    LinearLayout.LayoutParams layoutParams =
                            new LinearLayout.LayoutParams(lay7.getHeight()+2, lay7.getHeight()-2);
                    imageview.setLayoutParams(layoutParams);
                    lay7.addView(imageview);
                }
            }

            //リセット
            for (int i = 0; i < 18; i++) comp[i] = 0;
        }
    }
}