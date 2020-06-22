package eventbox;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import adapter.ListModAdapter;
import model.Profile;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.APIUtils;
import services.DataClient;

public class AddModActivity extends AppCompatActivity {
    private RecyclerView rcListMod;
    private ListModAdapter listModAdapter;
    private ArrayList<Profile> result_mod_load;
    EditText edtInputMail;
    TextView tvAddMod, tvEmail;
    String event_id;
    String email;
    ImageView imgBack;
    GifImageView imgLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mod);
        Intent intent = getIntent();
        event_id = intent.getStringExtra("event_id");
        initView();
        eventClick();
        loadAllMod();
    }

    private void loadAllMod() {
        Log.d("event_id", event_id);
        result_mod_load = new ArrayList<>();
        DataClient dataClient = APIUtils.getData();
        Call<List<Profile>> callback = dataClient.LoadAllMod(event_id);
        callback.enqueue(new Callback<List<Profile>>() {
            @Override
            public void onResponse(@NotNull Call<List<Profile>> call, @NotNull Response<List<Profile>> response) {
                ArrayList<Profile> modList = (ArrayList<Profile>) response.body();
                Log.d("OK", "asdasd");
                if (modList != null) {
                    if(modList.size() == 0){
//                        imgLoading.setVisibility(View.GONE);
//                        recyclerView.setVisibility(View.GONE);
//                        tvNoEvent.setVisibility(View.VISIBLE);
//                        tvRefesh.setVisibility(View.VISIBLE);
                    }else{
                        for(int i=0; i < modList.size(); i++){
                            Profile profile = new Profile(modList.get(i).getName(),modList.get(i).getFaculty(),modList.get(i).getEmail(),modList.get(i).getAvatar());
                            result_mod_load.add(profile);
                        }
//                        imgLoading.setVisibility(View.GONE);
                        rcListMod.setVisibility(View.VISIBLE);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AddModActivity.this);
                        rcListMod.setLayoutManager(layoutManager);
                        rcListMod.setItemAnimator(new DefaultItemAnimator());
                        listModAdapter = new ListModAdapter(AddModActivity.this,result_mod_load);
                        rcListMod.setAdapter(listModAdapter);
                        imgLoading.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Profile>> call, @NotNull Throwable t) {
                Log.d("fail", t.getMessage());
            }
        });
    }

    private void eventClick() {
        tvAddMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMod();
            }
            private void addMod() {
                email = edtInputMail.getText().toString();
                if(email.isEmpty()){
                    Toast.makeText(AddModActivity.this, R.string.pleasefillemail, Toast.LENGTH_SHORT).show();
                }else{
                    final Dialog dialog = new Dialog(AddModActivity.this);
                    showDialogLoading(dialog);
                    DataClient dataClient = APIUtils.getData();
                    Call<String> callback = dataClient.AddMod(event_id, email);
                    callback.enqueue(new Callback<String>() {

                        @Override
                        public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                            if (response.body() != null && response.body().equals("success")) {
                                showDialogSuccess();
                                dismissDialogLoading(dialog);
                                loadAllMod();

                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                            Log.d("fail", t.getMessage());

                        }
                    });
                }

            }
        });


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showDialogSuccess() {
        final Dialog dialog = new Dialog(AddModActivity.this);
        dialog.setContentView(R.layout.addmod_success_dialog);
        tvEmail = dialog.findViewById(R.id.TextViewEmail);
        tvEmail.setText(email + " \n đã trở thành moderator.");
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        };

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 3000);
    }

    private void initView() {
        edtInputMail = findViewById(R.id.EditTextInputMail);
        tvAddMod = findViewById(R.id.TextViewAddMod);
        rcListMod = findViewById(R.id.rc_list_mod);
        imgBack = findViewById(R.id.img_back_add_mod);
        imgLoading = findViewById(R.id.img_loading_mod);
    }

    private void showDialogLoading(Dialog dialog){
        dialog.setContentView(R.layout.add_mod_loading_dialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    private void dismissDialogLoading(Dialog dialog){
        dialog.dismiss();

    }
}
