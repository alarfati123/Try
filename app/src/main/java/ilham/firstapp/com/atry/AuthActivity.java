package ilham.firstapp.com.atry;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class AuthActivity extends AppCompatActivity {
    private LinearLayout linearLayout_phone;
    private LinearLayout linearLayout_verify;

    private EditText phonenumber;
    private EditText verify;
    Button send;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mscalls;
    private FirebaseAuth mAuth;
    private AuthCredential credential;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private int btnType=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        linearLayout_phone=(LinearLayout)findViewById(R.id.one);
        linearLayout_verify=(LinearLayout)findViewById(R.id.two);

       phonenumber=(EditText) findViewById(R.id.phone_number);
        verify=(EditText) findViewById(R.id.verify);

        send=(Button)findViewById(R.id.login);
        mAuth= FirebaseAuth.getInstance();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(btnType==0) {
                    phonenumber.setEnabled(false);
                    send.setEnabled(false);

                    String phone = phonenumber.getText().toString();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(String.valueOf(phonenumber), 60, TimeUnit.SECONDS, AuthActivity.this, mscalls);
                }else{
                    send.setEnabled(false);
                    String verifycode=phonenumber.getText().toString();
                    PhoneAuthCredential phoneAuthCredential =PhoneAuthProvider.getCredential(mVerificationId,verifycode);
                    signInWithPhoneAuthCredential(phoneAuthCredential);

                }
            }
        });
        mscalls=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {


                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                btnType=1;
                send.setEnabled(true);

                // ...
            }
        };
        };


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information


                            FirebaseUser user = task.getResult().getUser();
                            Intent mainintent=new Intent(AuthActivity.this,MainActivity.class);
                            startActivity(mainintent);
                            finish();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
}
