package eventbox;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import model.Event;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.APIUtils;
import services.DataClient;



public class EventDetailActivity extends AppCompatActivity {

    Button btnCheckIn;
    LinearLayout layoutAddMod;
    TextView tvTitle, tvFaculty, tvStartDate, tvEndDate, tvPlace, tvTotalCheckin, tvSumTicket, tvMissEvent;
    ImageView imgRefesh;
    int mSumTicket = 0;
    Event event;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        Intent intent = getIntent();
        event = (Event) intent.getSerializableExtra("eventdetail");
        initView();
        totalTicket();
        totalCheckin();
        setDetailView();
        clickEvent();

    }


    /* Count Total Checkin */
    private void totalCheckin() {

        DataClient dataClient = APIUtils.getData();
        Call<String> callback = dataClient.CountCheckin(event.getId());
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                Toast.makeText(EventDetailActivity.this, "total:" + response.body(), Toast.LENGTH_SHORT).show();
//                ArrayList<Event> eventList = (ArrayList<Event>) response.body();
//                for(int i=0; i < eventList.size(); i++){
//                   totalCheckin = eventList.get(i).get
              tvTotalCheckin.setText(response.body());
              int missTicket = mSumTicket - Integer.parseInt(response.body());
              tvMissEvent.setText(String.valueOf(missTicket));
//
//                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("fail", t.getMessage());

            }
        });
    }

    /* Count Total Ticket */
    private void totalTicket() {

        DataClient dataClient = APIUtils.getData();
        Call<String> callback = dataClient.CountTicket(event.getId());
        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                tvSumTicket.setText(response.body());
                mSumTicket = Integer.parseInt(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("fail", t.getMessage());

            }
        });
    }

    private void setDetailView() {
        tvTitle.setText(event.getTitle());
        tvStartDate.setText(event.getStartDate());
        tvEndDate.setText(event.getEndDate());
        tvPlace.setText(event.getPlace());
        tvFaculty.setText("Khoa " + event.getFaculty());
    }

    private void clickEvent() {
        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentScanCode = new Intent(EventDetailActivity.this, ScanCodeActivity.class);
                intentScanCode.putExtra("event_id", event.getId());
                startActivity(intentScanCode);
            }
        });

        layoutAddMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAddMod = new Intent(EventDetailActivity.this, AddModActivity.class);
                intentAddMod.putExtra("event_id", event.getId());
                startActivity(intentAddMod);
            }
        });
    }

    private void initView() {
        btnCheckIn = findViewById(R.id.ButtonCheckIn);
        layoutAddMod = findViewById(R.id.Layout_Addmod);
        tvTitle = findViewById(R.id.TextViewTitle);
//        tvTicket = findViewById(R.id.TextViewTicket);
        tvStartDate = findViewById(R.id.TextViewStartDate);
        tvEndDate = findViewById(R.id.TextViewEndDate);
        tvPlace = findViewById(R.id.TextViewAddress);
        tvFaculty = findViewById(R.id.TextViewFaculty);
        tvTotalCheckin = findViewById(R.id.TextViewTotalCheckin);
        tvSumTicket = findViewById(R.id.TextViewSumTicket);
        tvMissEvent = findViewById(R.id.TextViewMissEvent);
        imgRefesh = findViewById(R.id.ImageViewRefeshTicket);
        final Animation anim_rotate;
        anim_rotate = AnimationUtils.loadAnimation(EventDetailActivity.this, R.anim.anim_rotate);
        imgRefesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgRefesh.startAnimation(anim_rotate);
                totalCheckin();
            }
        });
    }


}
