package users;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mycity.R;

import java.io.File;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
    public void goToBack(View view)
    {finish();}
    public void clearCache(View view)
    {
        try {
            File dir = getCacheDir();
           if(deleteDir(dir))
               Toast.makeText(SettingsActivity.this,"cleared",Toast.LENGTH_LONG).show();
           else
               Toast.makeText(SettingsActivity.this,"not cleared",Toast.LENGTH_LONG).show();
        } catch (Exception e) { e.printStackTrace();}
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}
