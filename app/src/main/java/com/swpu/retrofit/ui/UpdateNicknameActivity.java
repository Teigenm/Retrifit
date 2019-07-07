package com.swpu.retrofit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.swpu.retrofit.R;
import com.swpu.retrofit.net.ApiRetrofit;
import com.swpu.retrofit.net.UserApiService;
import com.swpu.retrofit.model.ResponseMessageEntity;
import com.swpu.retrofit.util.Validator;

import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class UpdateNicknameActivity extends AppCompatActivity implements View.OnClickListener {

    private UserApiService mUserApiService ;
    private static long sid;
    private EditText mNickName;
    public static void go(Context context,long id) {
        Intent intent = new Intent();
        intent.setClass(context, UpdateNicknameActivity.class);
        context.startActivity(intent);
        sid = id;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nickname);

        mUserApiService = ApiRetrofit.getInstance().getService(UserApiService.class);

        View button = findViewById(R.id.update_save_button);
        button.setOnClickListener(this);

        mNickName = findViewById(R.id.update_nickname);

        mNickName.setText(HomeActivity.sUserEntity.getNickname());
    }

    public void save(String nickname) {
        Flowable<ResponseMessageEntity> update = mUserApiService.updateNickname(sid, nickname);
        update.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FlowableSubscriber<ResponseMessageEntity>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(ResponseMessageEntity msg) {
                        Toast.makeText(getApplicationContext(), msg.getMsg(), Toast.LENGTH_SHORT).show();
                        HomeActivity.sUserEntity.setNickname(nickname);
                    }
                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onComplete() {
                        HomeActivity.getInstance().initDate();
                        finish();
                    }
                });
    }

    public void attemptSave() {
        String nickname = mNickName.getText().toString();
        if(Validator.isEmpty(nickname)) {
            mNickName.setError("不能为空");
            mNickName.requestFocus();
            return;
        }
        save(nickname);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_save_button:
                attemptSave();
            default:
                break;
        }
    }

}
