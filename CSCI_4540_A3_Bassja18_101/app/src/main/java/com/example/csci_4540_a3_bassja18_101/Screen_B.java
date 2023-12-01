package com.example.csci_4540_a3_bassja18_101;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Screen_B extends AppCompatActivity {

    private TextView nameTxt, ageTxt, genderTxt, hobbiesTxt;
    private String name, age, genderId, hobbiesSelected;
    private Switch profileSwt;
    private ImageView myImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_b_view);

        nameTxt = findViewById(R.id.nameTextView);
        ageTxt = findViewById(R.id.ageTextView);
        genderTxt = findViewById(R.id.genderTextView);
        hobbiesTxt = findViewById(R.id.interestsTextView);

        profileSwt = findViewById(R.id.switch1);
        myImage = findViewById(R.id.imageView_screenB);

        myImage.setImageResource(R.drawable.pikablr);

        profileSwt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    myImage.setImageResource(R.drawable.pika);
                }
                else
                {
                    myImage.setImageResource(R.drawable.pikablr);
                }
            }
        });

        name = Screen_A.newName;
        if (name.equalsIgnoreCase("Enter Name: ")) {
            nameTxt.setText("Name: Unknown");
        }
        else
        {
            nameTxt.setText("Name:\t" + name.replace("Enter Name: ", ""));
        }


        age = Screen_A.newAge;
        if (age.equalsIgnoreCase("Enter Age: ")) {
            ageTxt.setText("Age: Unknown");
        }
        else
        {
            ageTxt.setText("Age:\t" + age.replace("Enter Age: ", ""));
        }

        genderId = Screen_A.gender;
        genderId = genderId.replace("Gender: ", "");
        genderTxt.setText("Gender:\t" + genderId);

        hobbiesSelected = Screen_A.hobbies;
        hobbiesTxt.setText("Intrests:\t " + hobbiesSelected);
    }
}