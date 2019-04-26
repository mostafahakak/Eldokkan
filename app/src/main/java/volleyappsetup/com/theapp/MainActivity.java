package volleyappsetup.com.theapp;

import android.accounts.Account;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.instabug.library.Instabug;
import com.instabug.library.invocation.InstabugInvocationEvent;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import volleyappsetup.com.theapp.Model.User;
import volleyappsetup.com.theapp.commen.Common;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 99;
    Button fb, in, up;
    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        FacebookSdk.sdkInitialize(getApplicationContext());
        AccountKit.initialize(this);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("user");

        Paper.init(this);

        fb = (Button) findViewById(R.id.FBSign);
        //in = (Button) findViewById(R.id.sin);
        //up = (Button) findViewById(R.id.sup);

        /*in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, signin.class);
                startActivity(intent);
            }
        });
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, signup.class);
                startActivity(intent);
            }
        });*/
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startLoginSystem();

            }
        });



        // checkRemember
        String pho = Paper.book().read(Common.USER_KEY);
        String pwd = Paper.book().read(Common.PWD_KEY);
        if (pho != null && pwd != null) {
            if (!pho.isEmpty() && !pwd.isEmpty())
                login(pho, pwd);
        }

        // Check FB Session
        AccessToken accessToken = AccountKit.getCurrentAccessToken();

        if (accessToken != null) {
            final AlertDialog waitingDialog = new SpotsDialog(this);
            waitingDialog.show();
            waitingDialog.setMessage("Please Wait");
            waitingDialog.setCancelable(false);

            AccountKit.getCurrentAccount(new AccountKitCallback <com.facebook.accountkit.Account>() {
                @Override
                public void onSuccess(com.facebook.accountkit.Account account) {
                    users.child(account.getPhoneNumber().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User localuser = dataSnapshot.getValue(User.class);
                            // Copy From Login
                            Common.currentuser = localuser;
                            Intent SignIn = new Intent(MainActivity.this, Main2Activity.class);
                            SignIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(SignIn);
                            waitingDialog.dismiss();
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

                @Override
                public void onError(AccountKitError accountKitError) {

                }
            });


        } else {
            //Handle new or logged out user

        }
    }

    private void login(final String pho, final String pwd) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("user");
            // if there Internet Connection
        if (Common.isConnectingToNet(getBaseContext())) {
                // set PD
            final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Please Wait....");
            mDialog.show();
/*
            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child(pho).exists()) {
                        mDialog.dismiss();
                        User user = dataSnapshot.child(pho).getValue(User.class);
                        user.setPhone(pho);
                        if (user.getPassword().equals(pwd)) {
                            {
                                Common.currentuser = user;
                                Intent SignIn = new Intent(MainActivity.this, Main2Activity.class);
                                SignIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(SignIn);

                            }
                            Toast.makeText(MainActivity.this, "Sign in Successfuly", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Sign in Failled", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        mDialog.dismiss();
                        Toast.makeText(MainActivity.this, "User Not Exist", Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/

        } else {
            Toast.makeText(MainActivity.this, "Check InterNet Connection", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private void startLoginSystem() {
        final Intent intent = new Intent(MainActivity.this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.CODE
        // ... perform additional configuration ...
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build());
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    toastMessage = "Success:";
                } else {
                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0, 10));
                }

                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.

                // Success! Start your next activity...
                xxxx();
            }

            // Surface the result to your user in an appropriate way.
            Toast.makeText(
                    this,
                    toastMessage,
                    Toast.LENGTH_LONG)
                    .show();
        }

    }
    private void xxxx () {

        final AlertDialog waitingDialog = new SpotsDialog(this);
        waitingDialog.show();
        waitingDialog.setMessage("Please Wait");
        waitingDialog.setCancelable(false);

        AccountKit.getCurrentAccount(new AccountKitCallback <com.facebook.accountkit.Account>() {
            @Override
            public void onSuccess(com.facebook.accountkit.Account account) {
                final String userPhone = account.getPhoneNumber().toString();

                // Check if Existed
                users.orderByKey().equalTo(userPhone).
                        addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.child(userPhone).exists()) {
                                    User newUser = new User();
                                    newUser.setPhone(userPhone);
                                    newUser.setName("NEW USER");

                                    // Add To FireBase
                                    users.child(userPhone).setValue(newUser).
                                            addOnCompleteListener(new OnCompleteListener <Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task <Void> task) {
                                                    if (task.isSuccessful())
                                                        Toast.makeText(MainActivity.this, "User Register Successful", Toast.LENGTH_SHORT).show();
                                                    // Login
                                                    users.child(userPhone).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            User localuser = dataSnapshot.getValue(User.class);
                                                            // Copy From Login
                                                            Common.currentuser = localuser;
                                                            Intent SignIn = new Intent(MainActivity.this, Main2Activity.class);
                                                            startActivity(SignIn);
                                                            waitingDialog.dismiss();
                                                            SignIn.putExtra("phone", userPhone);

                                                            finish();
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            });
                                } else {
                                    // Login
                                    users.child(userPhone).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            User localuser = dataSnapshot.getValue(User.class);
                                            // Copy From Login
                                            Common.currentuser = localuser;
                                            Intent SignIn = new Intent(MainActivity.this, Main2Activity.class);
                                            SignIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(SignIn);
                                            waitingDialog.dismiss();

                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

            }

            @Override
            public void onError(AccountKitError accountKitError) {

                Toast.makeText(MainActivity.this, "" + accountKitError.getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

