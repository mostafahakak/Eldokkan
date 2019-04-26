package volleyappsetup.com.theapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import volleyappsetup.com.theapp.Model.User;
import volleyappsetup.com.theapp.commen.Common;

public class signup extends AppCompatActivity {
    private MaterialEditText edtname,edtphone,password;
    private Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtname = (MaterialEditText) findViewById(R.id.edtname);
        edtphone = (MaterialEditText) findViewById(R.id.edtphone);
        password = (MaterialEditText) findViewById(R.id.edtpassword);

        signup =(Button) findViewById(R.id.btnSignUp);

        Intent intent = getIntent();
        String easyPuzzle = intent.getExtras().getString("phone");
        edtphone.setText(easyPuzzle);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("user");

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectingToNet(getBaseContext())) {

                    final ProgressDialog mDialog = new ProgressDialog(signup.this);
                    mDialog.setMessage("Please Wait....");
                    mDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(edtphone.getText().toString()).exists()) {
                                Toast.makeText(signup.this, "Phone number already Exist", Toast.LENGTH_LONG).show();
                                mDialog.dismiss();
                            } else {
                                Intent intent = getIntent();
                                String number = intent.getStringExtra("phone");
                                edtphone.setText(number);

                                mDialog.dismiss();
                                User user = new User(edtname.getText().toString(), password.getText().toString(), edtphone.getText().toString());
                                table_user.child(edtphone.getText().toString()).setValue(user);
                                Toast.makeText(signup.this, "Register Complete", Toast.LENGTH_LONG).show();


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                else {
                    Toast.makeText(signup.this, "Check InterNet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
