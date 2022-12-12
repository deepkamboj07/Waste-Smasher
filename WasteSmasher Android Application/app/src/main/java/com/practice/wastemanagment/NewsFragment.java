package com.practice.wastemanagment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import com.practice.wastemanagment.Adapter.NewsAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    String api="pub_702823a6ea14c58f95eb474d903f7da42901";
    ArrayList<ModelClassNews> arrayList;
    NewsAdapter adapter;
    String language="english";
    private RecyclerView recyclerView;

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
        View fragView=inflater.inflate(R.layout.fragment_news,container,false);
        arrayList= new ArrayList<>();
        recyclerView= fragView.findViewById(R.id.newsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        //Toast.makeText(getContext(),"call",Toast.LENGTH_SHORT).show();
        swipeRefreshLayout=fragView.findViewById(R.id.swiper);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                swipeRefreshLayout.setRefreshing(false);

            }
        });
        findNews();

        adapter= new NewsAdapter(getContext(),arrayList);

        recyclerView.setAdapter(adapter);


        return fragView;
    }

    private void findNews() {

        ApiUtilities.getApiInterface().getNews(api,"in,us,cn,ca","environment","en",1).enqueue(new Callback<ModelNews2>() {
            @Override
            public void onResponse(Call<ModelNews2> call, Response<ModelNews2> response) {
                if(response.isSuccessful())
                {
                   // arrayList.add(new ModelClassNews("deepansahu","this is heading","","","",""));
                    arrayList.addAll(response.body().getResults());
                    adapter.notifyDataSetChanged();
                   // Toast.makeText(getContext(),"work",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ModelNews2> call, Throwable t) {
                //arrayList.add(new ModelClassNews("deepansahu","this is heading","","","",""));
                Toast.makeText(getContext(),t.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}