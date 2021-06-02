package com.example.spacexcrew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.example.spacexcrew.Adapter.RecyclerAdapter;
import com.example.spacexcrew.DAO.CrewDAO;
import com.example.spacexcrew.Database.CrewDatabase;
import com.example.spacexcrew.Model.CrewDetails;
import com.example.spacexcrew.NetworkCall.APIService;
import com.example.spacexcrew.Repository.CrewRepository;
import com.example.spacexcrew.ViewModel.CrewViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String BASE_URL="https://api.spacexdata.com/v4/";
    private RecyclerView recyclerView;
    private CrewRepository crewRepository;
    private RecyclerAdapter adapter;
    private List<CrewDetails> crewDetailsList;
    private CrewViewModel crewViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CrewDatabase crewDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        crewRepository=new CrewRepository(getApplication());
        crewDetailsList=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerView);
        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        crewDatabase=CrewDatabase.getCrewDatabaseInstance(getApplication());

        adapter=new RecyclerAdapter(this,crewDetailsList,getApplication());

        crewViewModel= new ViewModelProvider(this).get(CrewViewModel.class);
        crewViewModel.getAllData().observe(this, new Observer<List<CrewDetails>>() {
            @Override
            public void onChanged(List<CrewDetails> crewDetailsList) {
                adapter.setAllData(crewDetailsList);
                recyclerView.setAdapter(adapter);

            }
        });
        networkCall();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                System.out.println("Swipe");

                networkCall();
                crewViewModel.getAllData().observe(MainActivity.this, new Observer<List<CrewDetails>>() {
                    @Override
                    public void onChanged(List<CrewDetails> crewDetailsList) {

                        adapter.setAllData(crewDetailsList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }
                });
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void networkCall() {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService=retrofit.create(APIService.class);
        Call<List<CrewDetails>> call=apiService.getAllItemsFromAPI();
        call.enqueue(new Callback<List<CrewDetails>>() {
            @Override
            public void onResponse(Call<List<CrewDetails>> call, Response<List<CrewDetails>> response) {
                if(response.isSuccessful()){
                    CrewDAO crewDAO=crewDatabase.crewDao();
                    new DeleteAllAsyncTask(crewDatabase).execute();
                    crewRepository.insert(response.body());
                }

            }

            @Override
            public void onFailure(Call<List<CrewDetails>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Turn on internet or displayed data is from database", Toast.LENGTH_SHORT).show();
                System.out.println(t.getMessage());

            }
        });
    }
    static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void> {
        private CrewDAO crewDao;

        public DeleteAllAsyncTask(CrewDatabase itemDatabase) {
            this.crewDao = itemDatabase.crewDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            crewDao.deleteAll();
            return null;

        }
    }
}