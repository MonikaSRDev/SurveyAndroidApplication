package com.example.mithraapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {

    private TextView participantName;
    private TextView questionNumber, totalQuestions, ph9Question;
//    private RadioGroup optionsRadioGroup;
//    private RadioButton selectedOption;
    private Button nextButton;
    private int questionIndex = 0;
    private int selectedOptionValue;
    private ArrayList<String> questionArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_card_screen);
        RegisterViews();
        setTestArrayData();
        setCardData();
        getSelectedOption();
        onClickNextButton();
    }

    private void setTestArrayData() {
        questionArray.add("Feeling tired or having little energy.");
        questionArray.add("Question 2");
        questionArray.add("Question 3");
        questionArray.add("Question 4");
    }

    /**
     * Description : This method is used to register the views
     */
    private void RegisterViews() {
        participantName = findViewById(R.id.participantNameTV);
        questionNumber = findViewById(R.id.questionNumbers);
        totalQuestions = findViewById(R.id.totalQuestions);
        ph9Question = findViewById(R.id.questionsTV);
//        optionsRadioGroup = findViewById(R.id.questionOptions);
        nextButton = findViewById(R.id.nextButton);
        enableDisableButton(false);
    }

    private void getSelectedOption(){
//        optionsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                selectedOption = group.findViewById(checkedId);
//                switch(selectedOption.getId()){
//                    case R.id.optionRadioBtn1 : selectedOptionValue = 1;
//                                                break;
//                    case R.id.optionRadioBtn2 : selectedOptionValue = 2;
//                                                break;
//                    case R.id.optionRadioBtn3 : selectedOptionValue = 3;
//                                                break;
//                    case R.id.optionRadioBtn4 : selectedOptionValue = 4;
//                                                break;
//                }
//                if(selectedOption.isChecked()){
//                    enableDisableButton(true);
//                }
//            }
//        });
    }

    /**
     * Description : Move to the next question on clicking the next button
     */
    private void onClickNextButton() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(QuestionActivity.this,
                        "Selected Value : " + selectedOptionValue,
                        Toast.LENGTH_LONG).show();
                questionIndex++;
                setCardData();
                enableDisableButton(false);
            }
        });
    }

    /**
     * Description : Sets the questions in the card view
     */
    private void setCardData() {
        if(questionIndex < questionArray.size()){
            ph9Question.setText(questionArray.get(questionIndex));
            totalQuestions.setText("Of " + questionArray.size() +"");
            questionNumber.setText(questionIndex + 1 + "");
        }
        else{
            Toast.makeText(QuestionActivity.this, "All Questions are answered", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Description : Enable and disable the button
     */
    private void enableDisableButton(boolean isEnabled){
        if(isEnabled){
            nextButton.setEnabled(true);
            nextButton.setBackgroundResource(R.drawable.linear_gradient_color);
            nextButton.setTextColor(Color.parseColor("#ffffff"));
        }else{
            nextButton.setEnabled(false);
            nextButton.setBackgroundResource(R.color.text_color);
            nextButton.setTextColor(Color.parseColor("#000000"));
        }
    }

}
