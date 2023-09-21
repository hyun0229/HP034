package com.ict.test;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.TextView;


import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lexruntimev2.LexRuntimeV2Client;
import software.amazon.awssdk.services.lexruntimev2.model.RecognizeTextRequest;
import software.amazon.awssdk.services.lexruntimev2.model.RecognizeTextResponse;
//import retrofit2.converter.scalars.ScalarsConverterFactory;



public class ChatFragment extends Fragment {
    private String uid = "1234";
    private String dialog = "벽에 금이 갔어요.";

    private ApiService2 apiService2;

    private static final int PICK_IMAGE_REQUEST = 1;
    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    ImageButton sendButton;
    List<Message> messageList;
    MessageAdapter messageAdapter;

    private ImageButton btn_back, btn_test;

    public interface ApiService {
        @POST("yyy.php")
        Call<String> sendMessage(@Body RequestBody body); // 변경된 파라미터 타입
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        btn_back = view.findViewById(R.id.btn_back_chat);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.commit_MenuFragment();
                closeFragment();
            }
        });
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://3.34.50.23:80/") // 서버 URL을 입력하세요.
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        apiService2 = retrofit.create(ApiService2.class);


        btn_test = view.findViewById(R.id.btn_image_test);
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChooseImageClick(v);
            }
        });


        messageList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view);
        welcomeTextView = view.findViewById(R.id.welcome_text);
        messageEditText = view.findViewById(R.id.message_edit_text);
        sendButton = view.findViewById(R.id.send_btn);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
            }
        });
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        sendButton.setOnClickListener((v) -> {
            String question = messageEditText.getText().toString().trim();
            addToChat(question, Message.SENT_BY_ME);
            messageEditText.setText("");
            test_answe(question);



            //sendMessageToLex(question);

            System.out.println("질문" + question);
            welcomeTextView.setVisibility(View.GONE);

        });


        return view;
    }

    void addToChat(String message, String sentBy) {
        requireActivity().runOnUiThread(() -> {
            messageList.add(new Message(message, sentBy, null));
            messageAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        });

    }

    public void test_answe(String send) {
        messageList.add(new Message("입력중... ", Message.SENT_BY_BOT, null));    new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                addResponse("제보가 완료되었습니다.");
            }
        }, 1000); // 1000 밀리초(1초) 후에 실행


    }

    public void sendMessageToLex(String question) {
        messageList.add(new Message("입력중... ", Message.SENT_BY_BOT, null));

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("message", question);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonBody.toString());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://18.188.172.219")
                .addConverterFactory(ScalarsConverterFactory.create()) // ScalarsConverter 추가
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<String> call = apiService.sendMessage(requestBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String lexResponse = response.body();
                    // lexResponse를 처리하고 Amazon Lex로 전송
                    Log.d("Retrofit", "Response: " + lexResponse);
                    addResponse(lexResponse);
                } else {
                    // 전송 실패 시 처리
                    Log.e("Retrofit", "Request failed: " + response.message());
                    addResponse("전송실패 " + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // 통신 실패 처리
                Log.e("Retrofit", "Communication failure: " + t.getMessage());
                addResponse("Communication failure: " + t.getMessage());
            }
        });

    }


    void addResponse(String response) {
        messageList.remove(messageList.size() - 1);
        addToChat(response, Message.SENT_BY_BOT);
    }


    private void closeFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(this);
        fragmentTransaction.commit();
    }

    public void onChooseImageClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    // 이미지 선택 버튼 클릭 시 실행되는 메서드

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String a = "0";
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            welcomeTextView.setVisibility(View.GONE);
            Uri selectedImageUri = data.getData();
            addToImage(selectedImageUri, Message.SENT_BY_URI);
            Bitmap bitmap = getBitmapFromUri(selectedImageUri);
            uploadImage(bitmap);
        }

    }

    void addToImage(Uri uri, String sentBy) {
        requireActivity().runOnUiThread(() -> {
            messageList.add(new Message("image", sentBy, uri));
            messageAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        });

    }
    public interface ApiService2 {
        @Multipart
        @POST("save_report_data.php") // 업로드를 처리할 PHP 스크립트 경로
        Call<String> uploadImage(@Part("uid") RequestBody uid,
                                 @Part("dialog") RequestBody dialog,
                                 @Part MultipartBody.Part reportImage);
    }


    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    private void uploadImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageBytes);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("report_image", "report_image.jpg", requestBody);

        Call<String> call = apiService2.uploadImage(
                RequestBody.create(MediaType.parse("text/plain"), uid),
                RequestBody.create(MediaType.parse("text/plain"), dialog),
                imagePart);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // 업로드 성공 처리
                    String message = response.body();
                    // 서버에서 반환한 메시지 처리
                } else {
                    // 업로드 실패 처리
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // 통신 실패 처리
            }
        });
    }

}