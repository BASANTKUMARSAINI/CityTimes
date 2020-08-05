package dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mycity.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import model.ApplicationClass;
import users.fragments.StoresFragment;

public class SortStoreDialog extends Dialog {
    public Context context;
    SeekBar seekBar;
    CheckBox checkBox;
    private Button btnApply;
    ImageView imgCross;
    TextView tvDistance;
    RatingBar ratingBar;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    public  static String PREF_NAME="MyData";

    public SortStoreDialog(@NonNull Context context) {
        super(context);
        this.context=context;


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sort_store_dialog);

        seekBar=findViewById(R.id.seekbar_distance);
        tvDistance=findViewById(R.id.tv_distance);
        btnApply=findViewById(R.id.btn_apply);
        imgCross=findViewById(R.id.img_cross);
        ratingBar=findViewById(R.id.rating_bar);
        checkBox=findViewById(R.id.checkbox);

        final SharedPreferences sharedPreferences=context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        final int distance=sharedPreferences.getInt("distance",0);
        final float rating=sharedPreferences.getFloat("rating",0);
        boolean isChecked=sharedPreferences.getBoolean("checkbox",true);

        seekBar.setProgress(distance);
        tvDistance.setText(distance+context.getString(R.string.km));
        ratingBar.setRating(rating);
        checkBox.setChecked(isChecked);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    seekBar.setProgress(0);
                    tvDistance.setText(context.getString(R.string.city));
                    ratingBar.setRating(0);
                }
                else
                {
                    seekBar.setProgress(distance);
                    tvDistance.setText(distance+context.getString(R.string.km));
                    ratingBar.setRating(rating);
                }
            }
        });
        imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        tvDistance.setText(progress+context.getString(R.string.km));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
Log.v("PRO","start"+seekBar.getProgress());
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
Log.v("PRO",seekBar.getProgress()+"km");
    }
});


        Log.v("TAG",seekBar.getMax()+"kk");

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences1=context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences1.edit();
                if(checkBox.isChecked())
                {
                    editor.remove("distance");
                    editor.remove("rating");
                    editor.putBoolean("checkbox",true);
                    editor.apply();
                }
                else
                {
                    editor.putInt("distance",seekBar.getProgress());
                    editor.putFloat("rating",ratingBar.getRating());
                    editor.putBoolean("checkbox",false);
                    editor.apply();
                }
                StoresFragment.setData("");
                dismiss();
            }
        });

    }
}
