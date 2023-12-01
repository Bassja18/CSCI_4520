package com.example.csci_4540_a3_bassja18_101;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

public class Screen_A extends AppCompatActivity {

    private Button submit_btn;
    private EditText enterNameEditTextArea, enterAgeEditTextArea;
    public static String newName, newAge, gender, hobbies = "";
    private RadioButton maleBtn, femaleBtn;
    private CheckBox hobby1, hobby2, hobby3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_a_view);

        submit_btn = findViewById(R.id.submitButton);
        enterNameEditTextArea = findViewById(R.id.enterNameEditTexts);
        enterAgeEditTextArea = findViewById(R.id.enterAgeEditTexts);
        maleBtn = findViewById(R.id.maleRadioButton);
        femaleBtn = findViewById(R.id.femaleRadioButton);
        hobby1 = findViewById(R.id.hobby1Checkbox);
        hobby2 = findViewById(R.id.hobby2Checkbox);
        hobby3 = findViewById(R.id.hobby3Checkbox);


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newName = enterNameEditTextArea.getText().toString();
                newAge = enterAgeEditTextArea.getText().toString();

                if (maleBtn.isChecked()) {
                    gender = "Male";
                }
                else if (femaleBtn.isChecked()) {
                    gender = "Female";
                }
                else
                {
                    gender = "Unknown";
                }

                if (hobby1.isChecked()) {
                    hobbies += hobby1.getText().toString() + "\n";
                }

                if (hobby2.isChecked()) {
                    hobbies += hobby2.getText().toString() + "\n";
                }

                if (hobby3.isChecked()) {
                    hobbies += hobby3.getText().toString() + "\n";
                }

                if (hobbies.isEmpty()) {
                    hobbies = "You have no hobbies...";
                }

                Intent intent = new Intent(Screen_A.this, Screen_B.class);
                startActivity(intent);
            }
        });
    }
}