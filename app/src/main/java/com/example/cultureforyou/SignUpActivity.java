package com.example.cultureforyou;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private EditText input_email;
    private EditText input_password;
    private ImageButton login_button;
    private FirebaseAuth firebaseAuth;

    FirebaseDatabase database;
    DatabaseReference dref;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        // 파이어베이스 정의
        database = FirebaseDatabase.getInstance();
        dref = FirebaseDatabase.getInstance().getReference();
        firebaseAuth =  FirebaseAuth.getInstance();

        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        login_button = findViewById(R.id.login_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(input_email.getText().toString().trim(), input_password.getText().toString().trim());
            }
        });

    }


    // 회원가입
    private void createAccount(String email, String password) {
        if (!validateForm()) {
            return;
        }

        Log.i("VALUE_PASS_SIGN", password);
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // 가입 성공
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String email = user.getEmail();
                            String uid = user.getUid();
                            String nickname = null;
                            String anniversary = null;
                            String anni_mood = null;

                            // 해쉬맵 테이블 > 파이어베이스 DB에 저장 (Users)
                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("uid", uid);
                            hashMap.put("email", email);
                            hashMap.put("nickname", nickname);
                            hashMap.put("anniversary", anniversary);
                            hashMap.put("anni_mood", anni_mood);

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");
                            reference.child(uid).setValue(hashMap);

                            // 가입 성공 → 가입 화면을 빠져나감 (프로필 설정 페이지로 이동)
                            updateUI(user);
                            Intent intent = new Intent(SignUpActivity.this, FirstSettingActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(SignUpActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "이미 존재하는 계정입니다.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    // 이메일 비밀번호 공란 확인
    private boolean validateForm() {
        boolean valid = true;

        String email = input_email.getText().toString();
        if (TextUtils.isEmpty(email)) { // 아이디 putid가 공란이면
            input_email.setError("이메일을 입력해주세요.");
            valid = false;
        } else {
            input_email.setError(null);
        }

        String password = input_password.getText().toString();
        if (TextUtils.isEmpty(password)) { //비밀번호 putpw 공란이면
            input_password.setError("비밀번호를 입력해주세요.");
            valid = false;
        } else {
            input_password.setError(null);
        }
        return valid;
    }

    // 이메일 인증
    private void sendEmailVerification() {
        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Email sent
                    }
                });
        // [END send_email_verification]
    }

    private void updateUI(FirebaseUser user) {

    }
}