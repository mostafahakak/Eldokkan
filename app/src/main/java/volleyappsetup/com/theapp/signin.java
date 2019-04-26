package volleyappsetup.com.theapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.paperdb.Paper;
import volleyappsetup.com.theapp.Model.User;
import volleyappsetup.com.theapp.commen.Common;

public class signin extends AppCompatActivity {
    private MaterialEditText edtphone,password;
    private Button sign;
    private CheckBox chkRemember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        edtphone = (MaterialEditText) findViewById(R.id.edtphone);
        password = (MaterialEditText) findViewById(R.id.edtpassword);
        sign = (Button) findViewById(R.id.btnSignIn);
        chkRemember = (CheckBox) findViewById(R.id.check);

        Paper.init(this);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("user");

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectingToNet(getBaseContext())) {
                    if (chkRemember.isChecked()) {
                        Paper.book().write(Common.USER_KEY, edtphone.getText().toString());
                        Paper.book().write(Common.PWD_KEY, password.getText().toString());

                    }

                    final ProgressDialog mDialog = new ProgressDialog(signin.this);
                    mDialog.setMessage("Please Wait....");
                    mDialog.show();
                   /* table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.child(edtphone.getText().toString()).exists()) {
                                mDialog.dismiss();
                                User user = dataSnapshot.child(edtphone.getText().toString()).getValue(User.class);
                                if (user.getPassword().equals(password.getText().toString())) {
                                    {
                                        Common.currentuser = user;
                                        Intent SignIn = new Intent(signin.this, Home.class);
                                        SignIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(SignIn);

                                    }
                                    Toast.makeText(signin.this, "Sign in Successfuly", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(signin.this, "Sign in Failled", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                mDialog.dismiss();
                                Toast.makeText(signin.this, "User Not Exist", Toast.LENGTH_LONG).show();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/

                } else {
                    Toast.makeText(signin.this, "Check InterNet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }
}
