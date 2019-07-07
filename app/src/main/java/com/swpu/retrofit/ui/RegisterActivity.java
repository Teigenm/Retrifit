package com.swpu.retrofit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.swpu.retrofit.R;
import com.swpu.retrofit.model.ResponseMessageEntity;
import com.swpu.retrofit.net.ApiRetrofit;
import com.swpu.retrofit.net.UserApiService;
import com.swpu.retrofit.util.Validator;

import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText mUsernameText;
    private EditText mPasswordText;
    private EditText mConfirmPasswordText;

    private UserApiService mUserApiService ;
    public static void go(Context context) {
        Intent intent = new Intent();
        intent.setClass(context,RegisterActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUserApiService = ApiRetrofit.getInstance().getService(UserApiService.class);

        View register = findViewById(R.id.register_button);
        register.setOnClickListener(this);

        mUsernameText = findViewById(R.id.register_username);
        mPasswordText = findViewById(R.id.register_password);
        mConfirmPasswordText = findViewById(R.id.register_password_confirm);
    }

    @Override
    public void onClick(View v) {
        attemptRegister();
    }

    private void attemptRegister() {
        String username = mUsernameText.getText().toString();
        if (Validator.isEmpty(username)) {
            mUsernameText.setError(getString(R.string.error_username_empty));
            mUsernameText.requestFocus();
            return;
        }

        String password = mPasswordText.getText().toString();
        if (Validator.isEmpty(password)) {
            mPasswordText.setError(getString(R.string.error_password_empty));
            mPasswordText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            mPasswordText.setError(getString(R.string.error_password_too_short));
            mPasswordText.requestFocus();
            return;
        }

        String confirmPassword = mConfirmPasswordText.getText().toString();
        if (!password.equals(confirmPassword)) {
            mConfirmPasswordText.setError(getString(R.string.error_password_inconsistent));
            mConfirmPasswordText.requestFocus();
            return;
        }

        register(username, password);
    }
    public void register(String username, String password) {
        Flowable<ResponseMessageEntity> register = mUserApiService.register(username, password);
        register.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FlowableSubscriber<ResponseMessageEntity>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(ResponseMessageEntity msg) {
                        Toast.makeText(getApplicationContext(), msg.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onComplete() {
                    }
                });
    }
}
