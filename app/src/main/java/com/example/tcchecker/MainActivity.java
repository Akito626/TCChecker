package com.example.tcchecker;

import static android.widget.ImageView.ScaleType.FIT_START;
import static android.widget.ImageView.ScaleType.FIT_XY;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.Icon;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
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
import com.google.android.gms.ads.AdSize;
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
            {0, -1, -1, 1, 1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0},        //みず
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
    private LinearLayout lay8;
    private LinearLayout adlayout;

    private static final String TAG = "MainActivity";
    private AdView adView;

    private int n = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //広告の初期化
        MobileAds.initialize(this,
                initializationStatus -> {
                });
        adlayout = findViewById(R.id.adlayout);
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.ads_UnitId_Banner));
        adlayout.addView(adView);
        adlayout.setGravity(Gravity.BOTTOM);
        loadBanner();

        initSpinner();      // スピナーを生成

        // レイアウトの準備
        lay1 = findViewById(R.id.layout1);
        lay2 = findViewById(R.id.layout2);
        lay3 = findViewById(R.id.layout3);
        lay4 = findViewById(R.id.layout4);
        lay5 = findViewById(R.id.layout5);
        lay6 = findViewById(R.id.layout6);
        lay7 = findViewById(R.id.layout7);
        lay8 = findViewById(R.id.layout8);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(n == 0) {
            n++;
            initSpinner();
        }
    }

    //広告の準備
    private void loadBanner() {
        AdRequest adRequest = new AdRequest.Builder().build();

        AdSize adSize = getAdSize();
        // Step 4 - Set the adaptive ad size on the ad view.
        adView.setAdSize(adSize);


        // Step 5 - Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    //スピナーの準備
    private void initSpinner(){
        String [] colors = {
                "BLACK","#b3b3b5","#e8554d","#579bf3","#f1c525","#57a747", "#68c5df",
                "#e8a33b","#8357d0","#a6783a","#add5ea", "#ed6c94", "#a5ab39","#b2b194",
                "#8d658e","#7482e9","#706261","#94b1c2","#e48fe3"
        };

        Spinner spinner1 = findViewById(R.id.spinner1);
        Spinner spinner2 = findViewById(R.id.spinner2);

        int size = spinner1.getHeight()/5;

        int sh = spinner1.getHeight();
        Log.d("log","sh="+sh);

        custom_adapter adapter =
                new custom_adapter(
                        this,
                        R.layout.custom_spinner,
                        getResources().getStringArray(R.array.list),
                        colors,
                        size
                );

        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);

        AdapterView.OnItemSelectedListener MyListener = new AdapterView.OnItemSelectedListener(){
            //　アイテムが選択された時
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                Cheker();
            }

            //　アイテムが選択されなかった時
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        spinner1.setOnItemSelectedListener(MyListener);
        spinner2.setOnItemSelectedListener(MyListener);
    }

    //レイアウトを空にする
    private void ResetLayout(){
        lay1.removeAllViews();
        lay2.removeAllViews();
        lay3.removeAllViews();
        lay4.removeAllViews();
        lay5.removeAllViews();
        lay6.removeAllViews();
        lay7.removeAllViews();
        lay8.removeAllViews();
    }

    //選択された時の処理
    public void Cheker() {
        Spinner spinner1 = findViewById(R.id.spinner1);
        Spinner spinner2 = findViewById(R.id.spinner2);

        int idx1 = spinner1.getSelectedItemPosition();
        int idx2 = spinner2.getSelectedItemPosition();

        // 画像の数のカウント用
        int num = 0;
        int num2 = 0;

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
                if (comp[i] == 2) {     // こうかばつぐん(×4)
                    ImageView imageview = new ImageView(this);
                    imageview.setImageResource(icon[i]);
                    LinearLayout.LayoutParams layoutParams =
                            new LinearLayout.LayoutParams(lay1.getHeight()+2, lay1.getHeight()-2);
                    imageview.setLayoutParams(layoutParams);
                    lay1.addView(imageview);
                } else if (comp[i] == 1) {      // こうかばつぐん(×2)
                    ImageView imageview = new ImageView(this);
                    imageview.setImageResource(icon[i]);
                    LinearLayout.LayoutParams layoutParams =
                            new LinearLayout.LayoutParams(lay2.getHeight()+2, lay2.getHeight()-2);
                    imageview.setLayoutParams(layoutParams);
                    lay2.addView(imageview);
                } else if (comp[i] == 0) {      // こうかあり
                    num++;
                    ImageView imageview = new ImageView(this);
                    imageview.setImageResource(icon[i]);
                    LinearLayout.LayoutParams layoutParams =
                            new LinearLayout.LayoutParams(lay3.getHeight()+2, lay3.getHeight()-2);
                    imageview.setLayoutParams(layoutParams);
                    if((num+1)*(lay3.getHeight()+2) < lay3.getWidth()) {
                        lay3.addView(imageview);
                    }else {     // 横幅いっぱいになると次の段に
                        lay4.addView(imageview);
                    }
                } else if (comp[i] == -1) {     // いまひとつ(1/2)
                    num2++;
                    ImageView imageview = new ImageView(this);
                    imageview.setImageResource(icon[i]);
                    LinearLayout.LayoutParams layoutParams =
                            new LinearLayout.LayoutParams(lay5.getHeight()+2, lay5.getHeight()-2);
                    imageview.setLayoutParams(layoutParams);
                    if((num2+1)*(lay5.getHeight()+2) < lay5.getWidth()) {
                        lay5.addView(imageview);
                    }else {     // 横幅いっぱいになると次の段に
                        lay8.addView(imageview);
                    }
                } else if (comp[i] == -2) {     // いまひとつ(1/4)
                    ImageView imageview = new ImageView(this);
                    imageview.setImageResource(icon[i]);
                    LinearLayout.LayoutParams layoutParams =
                            new LinearLayout.LayoutParams(lay6.getHeight()+2, lay6.getHeight()-2);
                    imageview.setLayoutParams(layoutParams);
                    lay6.addView(imageview);
                } else {        // こうかなし
                    ImageView imageview = new ImageView(this);
                    imageview.setImageResource(icon[i]);
                    LinearLayout.LayoutParams layoutParams =
                            new LinearLayout.LayoutParams(lay7.getHeight()+2, lay7.getHeight()-2);
                    imageview.setLayoutParams(layoutParams);
                    lay7.addView(imageview);
                }
            }

            //相性計算用の配列をリセット
            for (int i = 0; i < 18; i++) comp[i] = 0;
        }
    }
}