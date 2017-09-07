package io.github.abhirojp.myfeed_android.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.github.abhirojp.myfeed_android.API.MyBackendAPI;
import io.github.abhirojp.myfeed_android.R;
import io.github.abhirojp.myfeed_android.adapter.FeedListAdapter;
import io.github.abhirojp.myfeed_android.data.DataModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static io.github.abhirojp.myfeed_android.data.Constants.baseUrl;
import static io.github.abhirojp.myfeed_android.data.Constants.fragtag;

/**
 * Created by abhiroj on 6/9/17.
 */

public class FeedListFragment extends Fragment {

     public static final String TAG=FeedListFragment.class.getSimpleName();
    private FeedListAdapter listAdapter;
    private Bundle instanceState;

    public static FeedListFragment newInstance() {
        FeedListFragment listFragment = new FeedListFragment();
        fragtag.put(TAG, listFragment);
        return listFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.instanceState = savedInstanceState;

    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO: Make a fresh query if instance State is empty
        Retrofit retrofit=new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();

        MyBackendAPI myBackendAPI=retrofit.create(MyBackendAPI.class);
        Call<ArrayList<DataModel>> call=myBackendAPI.loadData();
        call.enqueue(new Callback<ArrayList<DataModel>>() {
            @Override
            public void onResponse(Call<ArrayList<DataModel>> call, Response<ArrayList<DataModel>> response) {
                Log.d(TAG,"Retrofit on Response : -- > "+response.message()+" size is "+response.body().size());
                listAdapter.addAPIData(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<DataModel>> call, Throwable t) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_feedlist,container,false);
        RecyclerView recyclerView=view.findViewById(R.id.recyclable_list);
        listAdapter=new FeedListAdapter(view.getContext());
        recyclerView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        return view;
    }

    private ArrayList<DataModel> getFakeData() {
        Log.d(TAG,"creating list");
    // TODO: parse Fake Data as a JSON
        ArrayList<DataModel> parsedList=new ArrayList<>();
        for (int i=1;i<=20;i++){
            DataModel model=new DataModel();
            model.setTitle("title "+i);
            model.setName("name "+i);
            model.setDescription("description "+i);
            model.setText("text "+i);
            parsedList.add(model);
        }
        return parsedList;
    }
}
