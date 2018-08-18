package pinerria.business.pinerrianew.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pinerria.business.pinerrianew.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddBusiness extends Fragment {


    public AddBusiness() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_business, container, false);

        return view;
    }




    public static Fragment NewInstance(String id) {
        Bundle args = new Bundle();
        args.putString("id", id);

        AddBusiness fragment = new AddBusiness();
        fragment.setArguments(args);

        return fragment;
    }



}
