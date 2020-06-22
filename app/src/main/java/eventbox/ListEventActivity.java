package eventbox;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import adapter.List_event_adapter;
import model.Event;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.APIUtils;
import services.DataClient;

public class ListEventActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List_event_adapter result_page_travelum_adapter;
    private ArrayList<Event> result_event_load;
    private String test;
    private SwipeRefreshLayout swipeRefreshLayout;
    TextView tvWelcome;
    TextView tvSignOut;
    TextView tvNoEvent;
    TextView tvRefesh;
    ImageView imgLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_event);
        initView();
        loadAllEvent();
        recyclerView = findViewById(R.id.rc1);
    }

    /* Init View*/
    private void initView() {
        Intent intent = getIntent();
        test = intent.getStringExtra("mail");
        tvRefesh = findViewById(R.id.tv_refesh);
        tvNoEvent = findViewById(R.id.tv_no_event);
        tvWelcome = findViewById(R.id.TextViewWelcome);
        tvWelcome.setText(test);
        imgLoading = findViewById(R.id.img_loading_list);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_list_event);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        tvSignOut = findViewById(R.id.TextViewSignOut);
        tvSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("user_email", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                finish();
                Intent intent1 = new Intent(ListEventActivity.this, SigninActivity.class);
                intent1.putExtra("out", "out");
                intent1.putExtra("email_sign_out", "unknown");
                startActivity(intent1);
            }
        });

        tvRefesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvNoEvent.setVisibility(View.GONE);
                tvRefesh.setVisibility(View.GONE);
                imgLoading.setVisibility(View.VISIBLE);
                loadAllEvent();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadAllEvent();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1500);
            }
        });
    }

    /* Load All Event to UI by Account  */
    private void loadAllEvent() {
        result_event_load = new ArrayList<>();
        DataClient dataClient = APIUtils.getData();
        Call<List<Event>> callback = dataClient.LoadEvent(test);
        callback.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(@NotNull Call<List<Event>> call, @NotNull Response<List<Event>> response) {
                ArrayList<Event> eventList = (ArrayList<Event>) response.body();
                if (eventList != null) {
                    if(eventList.size() == 0){
                        imgLoading.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        tvNoEvent.setVisibility(View.VISIBLE);
                        tvRefesh.setVisibility(View.VISIBLE);
                    }else{
                        for(int i=0; i < eventList.size(); i++){
                            Event event = new Event(eventList.get(i).getId(),eventList.get(i).getAccountId(),eventList.get(i).getCategoryId(),eventList.get(i).getTitle(),eventList.get(i).getCode(),eventList.get(i).getPlace(),eventList.get(i).getAvatar(),eventList.get(i).getTicketNumber(),eventList.get(i).getStartDate(),eventList.get(i).getEndDate(),eventList.get(i).getExpiredDate(),eventList.get(i).getShortDesc(),eventList.get(i).getEmail(),eventList.get(i).getStatus(), eventList.get(i).getFaculty());
                            result_event_load.add(event);
                        }
                        imgLoading.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ListEventActivity.this);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        result_page_travelum_adapter = new List_event_adapter(ListEventActivity.this,result_event_load);
                        recyclerView.setAdapter(result_page_travelum_adapter);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Event>> call, @NotNull Throwable t) {
                Log.d("fail", t.getMessage());
            }
        });
    }
}
