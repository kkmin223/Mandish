package com.example.mandish_lilac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity
{
    private FirebaseAuth mFirebaseAuth; //파이어베이스 인증
    private DatabaseReference mDatabaseRef; //실시간 데이터베이스
    private EditText mEtEmail,mEtPwd; //회원가입 입력필드
    private Button mBtnRegister; //회원가입 버튼


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth=FirebaseAuth.getInstance();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("UserAccount");

        mEtEmail=findViewById(R.id.et_email);
        mEtPwd=findViewById(R.id.et_pwd);
        mBtnRegister=findViewById(R.id.btn_register);

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 처리 시작
                String strEmail=mEtEmail.getText().toString();
                String strPwd=mEtPwd.getText().toString();

                //Firebase Auth 진행
                mFirebaseAuth.createUserWithEmailAndPassword(strEmail,strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>()
                {
                    @Override//인증처리가 되었을때
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseUser firebaseUser= mFirebaseAuth.getCurrentUser();
                        UserAccount account=new UserAccount();
                        account.setIdToken(firebaseUser.getUid());
                        account.setEmailId(firebaseUser.getEmail());
                        account.setPassword(strPwd);
                        //setValue: database insert(삽입) 행위
                        mDatabaseRef.child(firebaseUser.getUid()).setValue(account);

                        Toast.makeText(RegisterActivity.this,"회원가입에 성공",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                        startActivity(intent);

                    }
                    else{
                        Toast.makeText(RegisterActivity.this,"회원가입에 실패하셨습니다",Toast.LENGTH_SHORT).show();
                    }
                    }
                });

            }
        });
    }

}