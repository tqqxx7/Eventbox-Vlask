package eventbox;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import model.Profile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.APIUtils;
import services.DataClient;

public class ScanCodeActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    TextView tvResult, tvFaculty, tvStatus, tvTitleScan, tvScanTicket, tvScanCard;
    LinearLayout lnScanTicket, lnScanCard, lnAddHandmade;
    CameraSource cameraSource;
    BarcodeDetector barcodeDetector;
    ImageView imgFlash, imgScanner, imgBack, imgScanTicket, imgScanCard;
    private int STORAGE_PERMISSION_CODE = 1;
    private int FLASH_CODE = 0;
    String event_id;
    Camera.Parameters params;
    Camera camera;
    boolean isFlash = false;
    boolean mCheckOk = false;
    Dialog dialogFail;
    EditText edtEmailHand;
    Integer mMode = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);
        if (ContextCompat.checkSelfPermission(ScanCodeActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        } else {
            requestStoragePermission();
        }

        Intent intent = getIntent();
        event_id = intent.getStringExtra("event_id");
        dialogFail = new Dialog(ScanCodeActivity.this);

        initView();
        setUpCamera();
        eventClick();

    }

    private void eventClick() {
        imgFlash.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if (FLASH_CODE == 0) {
                    imgFlash.setImageResource(R.drawable.flash_on_icon);
                    FLASH_CODE = 1;
                    changeFlashStatus();
                } else {
                    imgFlash.setImageResource(R.drawable.flash_off_icon);
                    FLASH_CODE = 0;
                    changeFlashStatus();
                }

            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        lnScanTicket.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                mMode = 1;
                tvTitleScan.setText(R.string.scanmode);
                tvScanTicket.setTextColor(getResources().getColor(R.color.pinky));
                imgScanTicket.setColorFilter(ContextCompat.getColor(ScanCodeActivity.this, R.color.pinky), android.graphics.PorterDuff.Mode.SRC_IN);
                tvScanCard.setTextColor(R.color.black);
                imgScanCard.setColorFilter(ContextCompat.getColor(ScanCodeActivity.this, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);

            }
        });
        lnScanCard.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                mMode = 2;
                tvTitleScan.setText(R.string.scan_card_mode);
                tvScanCard.setTextColor(getResources().getColor(R.color.pinky));
                imgScanCard.setColorFilter(ContextCompat.getColor(ScanCodeActivity.this, R.color.pinky), android.graphics.PorterDuff.Mode.SRC_IN);
                tvScanTicket.setTextColor(R.color.black);
                imgScanTicket.setColorFilter(ContextCompat.getColor(ScanCodeActivity.this, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        });

        lnAddHandmade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogHandAdd();
            }
        });
    }

    private void setUpCamera() {


        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.CODE_128 | Barcode.QR_CODE).build();

//        barcodeDetector1 = new BarcodeDetector.Builder(this)
//                .setBarcodeFormats(Barcode.CODABAR).build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .build();


        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    cameraSource.start(surfaceHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });

        surfaceView.setFocusableInTouchMode(true);
        surfaceView.setFocusable(true);
        surfaceView.requestFocus();
        setCamFocusMode();
        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraFocus(cameraSource, Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);

            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                if (qrCodes.size() != 0) {
//                    cameraSource.stop();
                    tvResult.post(new Runnable() {
                        @Override
                        public void run() {
                            if(mMode==1){
                                CheckCode();
                            }else{
                                String codeStu;
                                if(qrCodes.valueAt(0).displayValue.length()>32){
                                    int start = qrCodes.valueAt(0).displayValue.indexOf("MÃ SINH VIÊN:") + 14;
                                    int end = qrCodes.valueAt(0).displayValue.indexOf("ĐỊNH DANH:") - 1;
                                    codeStu = qrCodes.valueAt(0).displayValue.substring(start,end);
                                    UpdateByCard(codeStu);
                                }else{
                                    if(qrCodes.valueAt(0).displayValue.length()<26){
                                        codeStu = qrCodes.valueAt(0).displayValue;
                                        UpdateByCard(codeStu);
                                    }else{
                                        cameraSource.stop();
                                        showWarningDialog();
                                    }

                                }

                            }
//                            Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
//                            vibrator.vibrate(1000);
//                            tvResult.setText(qrCodes.valueAt(0).displayValue);


                        }

                        private void CheckCode() {
                            cameraSource.stop();
                            DataClient dataClient = APIUtils.getData();
                            Call<String> callback = dataClient.Update_Checkin(qrCodes.valueAt(0).displayValue, event_id);
                            callback.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if(dialogFail.isShowing()){
                                        dialogFail.dismiss();
                                    }
                                    if (response.body().equals("success")) {
                                        mCheckOk = true;
                                        showDialogSuccess();
                                        showProfile();
                                    }else{
                                        if(response.body().equals("1")){
                                            if(mCheckOk==false){

                                                showDialogFail(dialogFail, R.string.scan_code_1);
                                            }
                                            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                                return;
                                            }
                                            try {
                                                cameraSource.start(surfaceView.getHolder());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }else{
                                            if(mCheckOk==false){
                                                showDialogFail(dialogFail, R.string.scan_code_2);
                                            }
                                            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                                return;
                                            }
                                            try {
                                                cameraSource.start(surfaceView.getHolder());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }


                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.d("fail", t.getMessage());

                                }

                                private void showProfile() {

                                    DataClient dataClient1 = APIUtils.getData();
                                    Call<List<Profile>> callback1 = dataClient1.Get_Profile_By_Code(qrCodes.valueAt(0).displayValue);
                                    callback1.enqueue(new Callback<List<Profile>>() {
                                        @Override
                                        public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
                                            ArrayList<Profile> profileList = (ArrayList<Profile>) response.body();

                                            for (int i = 0; i < profileList.size(); i++) {
                                                Profile profile = new Profile(profileList.get(i).getName(), profileList.get(i).getFaculty(), profileList.get(i).getEmail());
                                                tvResult.setText(profile.getName());
                                                tvFaculty.setText(profile.getFaculty());
                                                tvStatus.setText(R.string.scan_success_message);

                                            }


                                        }

                                        @Override
                                        public void onFailure(Call<List<Profile>> call, Throwable t) {

                                        }


                                    });


                                }
                            });
                        }

                        /* Update checkin info by card to DB */
                        private void UpdateByCard(final String stuCode) {
                            cameraSource.stop();
                            DataClient dataClient = APIUtils.getData();
                            Call<String> callback = dataClient.Update_Card(stuCode, event_id);
                            callback.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if(response.body().equals("success")){
                                        mCheckOk = true;
                                        showDialogSuccess();
                                    }else{
                                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                            return;
                                        }
                                        try {
                                            cameraSource.start(surfaceView.getHolder());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        if(response.body().equals("already")){
                                            Toast.makeText(ScanCodeActivity.this, "Thẻ đã quét rồi!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.d("fail", t.getMessage());

                                }
                            });
                        }
                    });
                }
            }
        });
    }


    /* Show Dialog when Checkin Success */
    private void showDialogSuccess() {
        final Dialog dialog = new Dialog(ScanCodeActivity.this);
        dialog.setContentView(R.layout.checkin_success_dialog);
//        tvName = dialog.findViewById(R.id.TextViewName);
//        tvName.setText("t160913@vanlanguni.vn");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button btnOk = dialog.findViewById(R.id.ButtonOkCheckinSuccess);
        dialog.show();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                mCheckOk=false;
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try {
                    cameraSource.start(surfaceView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /* Show Dialog when checkin fail */
    private void showDialogFail(final Dialog dialog, int message) {
        dialog.setContentView(R.layout.fail_checkin_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView tvMess = dialog.findViewById(R.id.TextViewFailScan);
        tvMess.setText(message);
        dialog.show();
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();

                }
            }
        };

        handler.postDelayed(runnable, 3000);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try {
                    cameraSource.start(surfaceView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /* Show Warning dialog when Check Ticket on Scan Card Mode */
    private void showWarningDialog() {
        final Dialog dialog = new Dialog(ScanCodeActivity.this);
        dialog.setContentView(R.layout.warning_checkin_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();

                }
            }
        };

        handler.postDelayed(runnable, 3000);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try {
                    cameraSource.start(surfaceView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /* Show Dialog to Input Email (Handmade Checkin */
    private void showDialogHandAdd() {
        final Dialog dialog = new Dialog(ScanCodeActivity.this);
        dialog.setContentView(R.layout.hand_add_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button btnOkHandAdd = dialog.findViewById(R.id.ButtonOkHandAdd);
        edtEmailHand = dialog.findViewById(R.id.EditTextEmailHand);
        dialog.show();
        btnOkHandAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailHand = edtEmailHand.getText().toString().trim();
                DataClient dataClient = APIUtils.getData();
                Call<String> callback = dataClient.Update_Hand(emailHand, event_id);
                callback.enqueue(new Callback<String>() {
                                     @Override
                                     public void onResponse(Call<String> call, Response<String> response) {
                                        if(response.body().equals("success")){
                                            mCheckOk = true;
                                            showDialogSuccess();
                                        }else{
                                            Toast.makeText(ScanCodeActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                                        }

                                     }

                                     @Override
                                     public void onFailure(Call<String> call, Throwable t) {
                                         Log.d("fail", t.getMessage());

                                     }
                                 });
                dialog.dismiss();
            }
        });
    }

    private void initView() {
        surfaceView = findViewById(R.id.cameraPreview);
        tvResult = findViewById(R.id.TextViewNameAcoount);
        tvFaculty = findViewById(R.id.TextViewFaculty);
        tvStatus = findViewById(R.id.TextViewStatus);
        imgFlash = findViewById(R.id.ImageViewFlash);
        imgScanner = findViewById(R.id.ImageScanner);
        Animation anim_move;
        anim_move = AnimationUtils.loadAnimation(ScanCodeActivity.this, R.anim.anim_scanner);
        imgScanner.startAnimation(anim_move);
        imgBack = findViewById(R.id.ImageViewBackScan);
        lnScanTicket = findViewById(R.id.LayoutScanTicket);
        lnScanCard = findViewById(R.id.LayoutScanCard);
        lnAddHandmade = findViewById(R.id.LayoutAddHandmade);
        tvTitleScan = findViewById(R.id.TextViewTitleScanCode);
        tvScanTicket = findViewById(R.id.TextViewScanTicket);
        tvScanCard = findViewById(R.id.TextViewScanCard);
        imgScanTicket = findViewById(R.id.ImgScanTicket);
        imgScanCard = findViewById(R.id.ImgScanCard);
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(ScanCodeActivity.this,
                                    new String[] {Manifest.permission.CAMERA}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.CAMERA}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /* Turn Flash for Camera */
    public void changeFlashStatus() {
        Field[] declaredFields = CameraSource.class.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    camera = (Camera) field.get(cameraSource);
                    if (camera != null) {
                        params = camera.getParameters();
                        if (!isFlash) {
                            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                            imgFlash.setImageResource(R.drawable.flash_on_icon);
                            FLASH_CODE = 1;
                            isFlash = true;
                        } else {
                            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                            imgFlash.setImageResource(R.drawable.flash_off_icon);
                            FLASH_CODE = 1;
                            isFlash = false;
                        }
                        camera.setParameters(params);
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                break;
            }
        }
    }

    private void setCamFocusMode(){

        if(null == camera) {
            return;
        }

        /* Set Auto focus */
        Camera.Parameters parameters = camera.getParameters();
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }

        camera.setParameters(parameters);
    }

    /* Make Camera Focus when touch on screen */
    private static boolean cameraFocus(@NonNull CameraSource cameraSource, @NonNull String focusMode) {
        Field[] declaredFields = CameraSource.class.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    Camera camera = (Camera) field.get(cameraSource);
                    if (camera != null) {
                        Camera.Parameters params = camera.getParameters();
                        params.setFocusMode(focusMode);
                        camera.setParameters(params);
                        return true;
                    }

                    return false;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                break;
            }
        }

        return false;
    }


}
