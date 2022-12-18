package com.example.try1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.try1.adapter.ShortcutAdapter;
import com.example.try1.model.Shortcut;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView shortcutRecycler;
    ShortcutAdapter shortcutAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_home, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        List<Shortcut> shortcutList = new ArrayList<>();
        shortcutList.add(new Shortcut("Turn on camera", "Action 1"));
        shortcutList.add(new Shortcut("Open android store", "Store opener"));
        shortcutList.add(new Shortcut("Order Chicago pizza","Chicago Pizza"));
        shortcutList.add(new Shortcut("Back to main page", "Main"));
        shortcutList.add(new Shortcut("Turn up volume","Volume up"));
        shortcutList.add(new Shortcut("Call mum", "Action 3"));
        shortcutList.add(new Shortcut("Call dad", "Action 4"));


        shortcutRecycler = view.findViewById(R.id.shortcut_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        shortcutRecycler.setLayoutManager(layoutManager);
        shortcutAdapter = new ShortcutAdapter(getContext(), shortcutList);
        shortcutRecycler.setAdapter(shortcutAdapter);
    }


}