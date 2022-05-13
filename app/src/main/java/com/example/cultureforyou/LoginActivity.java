package com.example.cultureforyou;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private EditText putid;
    private EditText putpw;
    private Button btn_find_id_pw;
    private Button btn_register;
    private ImageButton btn_login;
    private FirebaseAuth firebaseAuth;
    String user_id = null;

    // firebase-auth google sign in
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private SignInButton GoogleSignBtn;

    FirebaseDatabase database;

    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        putid = findViewById(R.id.putid);
        putpw = findViewById(R.id.putpw);
        btn_login = findViewById(R.id.btn_login);
        btn_find_id_pw = findViewById(R.id.btn_find_id_pw);
        btn_register = findViewById(R.id.btn_register);
        GoogleSignBtn = findViewById(R.id.GoogleSignBtn);
        firebaseAuth = FirebaseAuth.getInstance();

        // 파이어베이스 정의
        database = FirebaseDatabase.getInstance();

        // google sign in 구성 코드
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("857631073690-h5o13un5njui036is7u3978l8hnu9ir9.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        /*
        // 자동로그인 기능
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
            finish();
        }
         */

        // 로그인 버튼 클릭 시
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sign_email(putid.getText().toString().trim(), putpw.getText().toString().trim());
            }
        });

        // 회원가입 버튼 클릭 시
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);
            }
        });

        GoogleSignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }

    /*
    // 자동 로그인
    public void onStart() {
        // Check if user is signed in (non-null) and update UI accordingly.
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }
     */

    // 이메일로 로그인
    private void sign_email(String email, String password) {
        //ID, PW 공란 검사
        if (!validateForm()) {
            return;
        }
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { // 로그인 성공 시
                            Toast.makeText(LoginActivity.this, "로그인 성공",
                                    Toast.LENGTH_SHORT).show();
                            // MainActivity로 이동
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            // 현재 화면에 '로그인 실패' 토스트 문구 노출
                            Toast.makeText(LoginActivity.this, "로그인 실패 " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    // 이메일로 로그인 - 아이디 비밀번호 공란 확인
    private boolean validateForm() {
        boolean valid = true;

        String email = putid.getText().toString();
        if (TextUtils.isEmpty(email)) { // 아이디 putid가 공란이면
            putid.setError("아이디를 입력해주세요.");
            valid = false;
        } else {
            putid.setError(null);
        }

        String password = putpw.getText().toString();
        if (TextUtils.isEmpty(password)) { //비밀번호 putpw 공란이면
            putpw.setError("비밀번호를 입력해주세요.");
            valid = false;
        } else {
            putpw.setError(null);
        }
        return valid;
    }

    // 구글로 로그인
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle: " + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(LoginActivity.this, "로그인을 완료했습니다.", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String uid = user.getUid();
                            String email = user.getEmail();
                            String nickname = "";
                            String anniversary = "";
                            String anni_mood = "";

                            Log.i("VALUE_UID", uid);
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("Users");
                            DatabaseReference reference_uid = reference.child(uid);
                            // uid가 db-user에 있다면 메인 화면으로 이동
                            // uid가 db-user에 있다면 회원등록 이전이므로 초기 설정 페이지로 이동


                            ValueEventListener eventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    // realtime-db에 uid가 존재하지 않는다면 > 새로 생성
                                    if(!snapshot.exists()){
                                        Toast.makeText(LoginActivity.this, "로그인을 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();

                                        // 해쉬맵 테이블 > 파이어베이스 DB에 저장 (Users)
                                        HashMap<Object, String> hashMaps = new HashMap<>();
                                        hashMaps.put("uid", uid);
                                        hashMaps.put("email", email);
                                        hashMaps.put("nickname", nickname);
                                        hashMaps.put("anniversary", anniversary);
                                        hashMaps.put("anni_mood", anni_mood);

                                        reference.child(uid).setValue(hashMaps);
                                        Intent intent_start = new Intent(getApplicationContext(), FirstSettingActivity.class);
                                        startActivity(intent_start);
                                        finish();
                                    }
                                    else {
                                        // Sign in success, update UI with the signed-in user's information
                                        // Toast.makeText(LoginActivity.this, "성공", Toast.LENGTH_SHORT).show();
                                        updateUI(user);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.d("TAG", error.getMessage());
                                }
                            };
                            reference_uid.addListenerForSingleValueEvent(eventListener);


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "실패", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void updateUI(FirebaseUser user) { //update ui code here
        if (user != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
