package users.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mycity.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GovernmentFragment extends Fragment {

    public GovernmentFragment() {
        Log.v("TAG","gover");
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_government, container, false);
    }
}
