package volleyappsetup.com.theapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import volleyappsetup.com.theapp.Model.Chat;
import volleyappsetup.com.theapp.ViewHolder.ChatViewHolder;
import volleyappsetup.com.theapp.commen.Common;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ChatRoom extends AppCompatActivity {

    EditText userMessage;
    ImageButton SendButton,addbutton,recordvoicebtn,camerabutton;
    ScrollView mScrollView;
    RecyclerView chatRecycler;
    RecyclerView.LayoutManager layoutManager;

    private MediaRecorder recorder;

    private String fileName = null ;
    private static final String LOG_TAG ="Record_log";


    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;


    private Uri filePath;

    FirebaseRecyclerAdapter<Chat,ChatViewHolder> adapters;

    MediaPlayer mediaPlayer = new MediaPlayer();

    StorageReference storage;


    FirebaseDatabase database;
    DatabaseReference chats;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);


        IntitializedFields();

        ShowMessage();

        SendMessage();

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 438);
            }
        });

        recordvoicebtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    startRecording();
                }
                else if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    stopRecording();
                }
                return false;
            }
        });

        camerabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==438 && resultCode == RESULT_OK && data!=null && data.getData()!=null)

        {
            filePath = data.getData();
            uploadFile();

        }
        else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            uploadFile();

        }

    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {

        //Firebase

        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference riversRef = storage.child("Images").child(Common.currentuser.getPhone()).child(String.valueOf(System.currentTimeMillis())+
                                                                                                         "."+getFileExtension(filePath));
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog

                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri uri = urlTask.getResult();

                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-YY");
                            final String formattedDate = df.format(c);
                            final String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

                            //and displaying a success toast
                            Chat chat = new Chat(uri.toString(),currentTime + "    " +
                                    formattedDate,Common.currentuser.getPhone(),"Image");
                            String uploadid = chats.push().getKey();
                            chats.child(uploadid).setValue(chat);

                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
            //you can display an error toast
        }
    }

    private void SendMessage() {


        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-YY");
        final String formattedDate = df.format(c);
        final String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());


        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (userMessage.getText().toString().equals(""))
                {
                    Toast.makeText(ChatRoom.this, "Please enter TEXT", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    chats.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            Chat message = new Chat( userMessage.getText().toString(), currentTime + "    " + formattedDate,Common.currentuser.getPhone(),"Text");
                            String uploadid = chats.push().getKey();
                            chats.child(uploadid).setValue(message);
                            userMessage.setText("");
                            InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(ChatRoom.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }


    private void IntitializedFields() {

        userMessage = (EditText) findViewById(R.id.input_group_message);
        recordvoicebtn = (ImageButton) findViewById(R.id.record_btn);
        SendButton = (ImageButton) findViewById(R.id.send_button);
        mScrollView = (ScrollView) findViewById(R.id.scroll);
        addbutton = (ImageButton) findViewById(R.id.addbutton);
        camerabutton = (ImageButton) findViewById(R.id.camerabutton);

        chatRecycler = (RecyclerView)findViewById(R.id.chatrec);
        chatRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        chatRecycler.setLayoutManager(layoutManager);

        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest.3gp";

        database = FirebaseDatabase.getInstance();
        chats = database.getReference("ChatRoom").child(Common.currentuser.getPhone());

        storage = FirebaseStorage.getInstance().getReference();



    }


    private void ShowMessage()
    {
        adapters = new FirebaseRecyclerAdapter<Chat, ChatViewHolder>(Chat.class,
                                                                     R.layout.messagelayout,
                                                                     ChatViewHolder.class,
                                                                     chats) {
            @Override
            protected void populateViewHolder(final ChatViewHolder chatViewHolder, final Chat chat, int i) {


                if (chat.getType().equals("Text"))
                {
                    chatViewHolder.messagedate.setText(chat.getDate());
                    chatViewHolder.playbtn.setVisibility(View.INVISIBLE);
                    chatViewHolder.stopbutton.setVisibility(View.INVISIBLE);

                    if (chat.getName().equals(Common.currentuser.getPhone()))

                    {
                        chatViewHolder.usermessage.setText(chat.getMessage());
                        chatViewHolder.recivermessage.setVisibility(View.INVISIBLE);


                    } else
                    {
                        chatViewHolder.recivermessage.setText(chat.getMessage());
                        chatViewHolder.usermessage.setVisibility(View.INVISIBLE);

                    }
                }
                else if (chat.getType().equals("Image"))
                {
                    Picasso.with(getApplicationContext()).load(chat.getMessage()).into(chatViewHolder.messageimage);
                    chatViewHolder.recivermessage.setVisibility(View.INVISIBLE);
                    chatViewHolder.usermessage.setVisibility(View.INVISIBLE);
                    chatViewHolder.playbtn.setVisibility(View.INVISIBLE);
                    chatViewHolder.stopbutton.setVisibility(View.INVISIBLE);

                    chatViewHolder.messagedate.setText(chat.getDate());


                }
                else if (chat.getType().equals("Audio"))
                {
                    chatViewHolder.messagedate.setText(chat.getDate());
                    chatViewHolder.stopbutton.setVisibility(View.INVISIBLE);
                    chatViewHolder.recivermessage.setVisibility(View.INVISIBLE);
                    chatViewHolder.usermessage.setVisibility(View.INVISIBLE);

                    chatViewHolder.playbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chatViewHolder.stopbutton.setVisibility(View.VISIBLE);
                            chatViewHolder.playbtn.setVisibility(View.INVISIBLE);

                            try {
                                mediaPlayer = new MediaPlayer();
                                mediaPlayer.setDataSource(chat.getMessage());
                                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mp) {
                                        mp.start();
                                    }
                                });

                                mediaPlayer.prepare();
                            }catch (IOException e)
                            {
                                e.printStackTrace();
                            }

                        }
                    });
                    chatViewHolder.stopbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            chatViewHolder.stopbutton.setVisibility(View.INVISIBLE);
                            chatViewHolder.playbtn.setVisibility(View.VISIBLE);

                            mediaPlayer.release();
                        }
                    });
                }


            }
        };
        chatRecycler.setAdapter(adapters);
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;

        UploadAudio();
        Toast.makeText(ChatRoom.this, "Start Recording", Toast.LENGTH_SHORT).show();

    }

    private void UploadAudio() {

        StorageReference filepath = storage.child("Audio").child(Common.currentuser.getPhone()).child(String.valueOf(System.currentTimeMillis())+".3gp");

        Uri uri = Uri.fromFile(new File(fileName));

        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener <UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());
                Uri uri = urlTask.getResult();

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-YY");
                final String formattedDate = df.format(c);
                final String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

                //and displaying a success toast
                Chat chat = new Chat(uri.toString(),currentTime + "    " +
                        formattedDate,Common.currentuser.getPhone(),"Audio");
                Toast.makeText(ChatRoom.this, "Upload Audio Successful", Toast.LENGTH_SHORT).show();
                String uploadid = chats.push().getKey();
                chats.child(uploadid).setValue(chat);

            }
        });
    }



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                                                          "com.example.android.fileprovider",
                                                          photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
}
