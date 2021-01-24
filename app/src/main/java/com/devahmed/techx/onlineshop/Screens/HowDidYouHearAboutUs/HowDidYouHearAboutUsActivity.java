package com.devahmed.techx.onlineshop.Screens.HowDidYouHearAboutUs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.devahmed.techx.onlineshop.MainActivity;
import com.devahmed.techx.onlineshop.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HowDidYouHearAboutUsActivity extends AppCompatActivity {
    EditText howdidEditText;
    Button okButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_did_you_hear_about_us);
        howdidEditText = findViewById(R.id.howdidText);

        okButton = findViewById(R.id.okBtn);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String response = howdidEditText.getText().toString();
                if(response.isEmpty()){
                    Toast.makeText(HowDidYouHearAboutUsActivity.this, "Give us a response please", Toast.LENGTH_SHORT).show();
                    return;
                }
                addHowDidYouFindUsResponseToFirebase(response);
            }
        });

    }

    private void addHowDidYouFindUsResponseToFirebase(String product) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("HowDidYouHearAboutUs").push();
        //get post unique ID & update post key
        String key = myRef.getKey();
        //add post data to firebase database
        myRef.setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                startActivity(new Intent(HowDidYouHearAboutUsActivity.this , MainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
