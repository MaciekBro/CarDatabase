package com.example.maciekBro.cardatabase.listing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.maciekBro.cardatabase.MotoDatabaseOpenHelper;
import com.example.maciekBro.cardatabase.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by RENT on 2017-03-27.
 */

public class ListingFragment extends Fragment {

    private static final String QUERY_KEY = "query_key";

    private Unbinder unbinder; //do ButterKnife, akrtywnośc sama go unbinduje, fragment nie.

    private MotoDatabaseOpenHelper openHelper;  //do pobierania danych z bazy

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;



    public static Fragment getInstance(String query) {   //zeby wiedziec ze trzeba przekazac jakies atrybuty do fragmentu, jak przy intencie

        ListingFragment fragment = new ListingFragment();
        Bundle arguments = new Bundle();
        arguments.putString(QUERY_KEY, query);
        fragment.setArguments(arguments);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openHelper = new MotoDatabaseOpenHelper(getActivity());


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        String query = getArguments().getString(QUERY_KEY);
        RecyclerViewCursorAdapter adapter = new RecyclerViewCursorAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
       adapter.setOnCarItemClickListener((OnCarItemClickListener) getActivity()); //aktywnosc bedzie implementowala interface, zebybylo mniej operacji tak ustawiamy
        adapter.setCursor(openHelper.searchQuery(query));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
