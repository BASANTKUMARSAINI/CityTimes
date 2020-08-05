package users;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.mycity.R;
import com.squareup.picasso.Picasso;

import model.ApplicationClass;

public class SeeFullImageActivity extends AppCompatActivity {
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationClass.loadLocale(this);
        setContentView(R.layout.activity_see_full_image);
        imageView=findViewById(R.id.img_view);
        String url=getIntent().getStringExtra("uri");
        Picasso.get().load(url).into(imageView);
    }
}
