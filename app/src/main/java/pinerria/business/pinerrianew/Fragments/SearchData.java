package pinerria.business.pinerrianew.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pinerria.business.pinerrianew.R;
import pinerria.business.pinerrianew.SqliteDatabase.DatabaseHelper;
import pinerria.business.pinerrianew.SqliteDatabase.Note;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchData extends Fragment {


    public SearchData() {
        // Required empty public constructor
    }

    private ListView lv;

    // Listview Adapter
    ArrayAdapter<String> adapter;

    // Search EditText



    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;


    private DatabaseHelper db;
    private List<Note> notesList = new ArrayList<>();
    android.support.v7.widget.SearchView search;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_data, container, false);


//        String products[] = {"Dell Inspiron", "HTC One X", "HTC Wildfire S", "HTC Sense", "HTC Sensation XE",
//                "iPhone 4S", "Samsung Galaxy Note 800",
//                "Samsung Galaxy S3", "MacBook Air", "Mac Mini", "MacBook Pro"};

        List<String> products=new ArrayList<>();
        lv = (ListView) view.findViewById(R.id.list_view);
        search = (android.support.v7.widget.SearchView) view.findViewById(R.id.search);

        db = new DatabaseHelper(getActivity());


        Log.d("fdssdfsdgdgdfg", String.valueOf(db.getNotesCount()));

        for (int i=0;i<db.getNotesCount();i++){
            notesList.addAll(db.getAllNotes());
            Log.d("fsdfsdfsdfsdf",notesList.get(i).getNote());
            products.add(notesList.get(i).getNote());

        }


      //  Log.d("sdfsdfsdfsdfsfs", String.valueOf(db.getNotesCount()));

        // Adding items to listview
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.product_name, products);
        lv.setAdapter(adapter);


        search.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(getActivity(), ""+query, Toast.LENGTH_SHORT).show();

                Fragment fragment = new Listing();
                Bundle bundle = new Bundle();
                bundle.putString("id", query);
                bundle.putString("subcategory", query);
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                fragment.setArguments(bundle);
                ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout, R.anim.frag_fade_right, R.anim.frag_fad_left);
                ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();

                createNote(query.toString());

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
             //   Toast.makeText(getActivity(), ""+adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();


               // Log.d("dfsdfsdfs", AllProducts.get(position).get("id0"));
                Fragment fragment = new Listing();
                Bundle bundle = new Bundle();
                bundle.putString("id", String.valueOf(adapterView.getItemAtPosition(i)));
                bundle.putString("subcategory", String.valueOf(adapterView.getItemAtPosition(i)));
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = manager.beginTransaction();
                fragment.setArguments(bundle);
                ft.setCustomAnimations(R.anim.frag_fadein, R.anim.frag_fadeout, R.anim.frag_fade_right, R.anim.frag_fad_left);
                ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
            }
        });



     //   Log.d("fdgdfgdfgdfg",notesList.get(0).getNote());



        return view;
    }



    private void createNote(String note) {
        // inserting note in db and getting
        // newly inserted note id
        long id = db.insertNote(note);

        // get the newly inserted note from db
        Note n = db.getNote(id);

        if (n != null) {
            // adding new note to array list at 0 position
            notesList.add(0, n);

            // refreshing the list
            adapter.notifyDataSetChanged();

           // toggleEmptyNotes();
        }
    }

//    private void toggleEmptyNotes() {
//        // you can check notesList.size() > 0
//
//        if (db.getNotesCount() > 0) {
//            noNotesView.setVisibility(View.GONE);
//        } else {
//            noNotesView.setVisibility(View.VISIBLE);
//        }
//    }


}
