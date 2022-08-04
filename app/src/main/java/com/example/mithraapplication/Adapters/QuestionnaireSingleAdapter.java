package com.example.mithraapplication.Adapters;

import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mithraapplication.ModelClasses.SurveyQuestionAnswers;
import com.example.mithraapplication.R;

import java.util.ArrayList;

public class QuestionnaireSingleAdapter extends RecyclerView.Adapter<QuestionnaireSingleAdapter.ViewHolder>{
    private Context context;
    private ArrayList<SurveyQuestionAnswers> surveyQuestionAnswersArrayList;
    @NonNull
    @Override
    public QuestionnaireSingleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_questionnaire_row, parent, false);

        return new QuestionnaireSingleAdapter.ViewHolder(view);
    }

    public QuestionnaireSingleAdapter(Context context, ArrayList<SurveyQuestionAnswers> surveyQuestionAnswersArrayList){
        this.surveyQuestionAnswersArrayList = surveyQuestionAnswersArrayList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView questionTV, questionNumber;
        ImageButton speakerButton;
        RadioGroup answersRadioGroup;
        EditText answerEditText;
        LinearLayout answersCheckBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTV = itemView.findViewById(R.id.questionsTVQS);
            questionNumber = itemView.findViewById(R.id.questionNumbersQS);
            speakerButton = itemView.findViewById(R.id.imageButtonSpeakerQS);
            answersRadioGroup = itemView.findViewById(R.id.answerradioGroup);
            answersRadioGroup.setVisibility(View.GONE);
            answerEditText = itemView.findViewById(R.id.answerEditText);
            answerEditText.setVisibility(View.INVISIBLE);
            answersCheckBox = itemView.findViewById(R.id.answerCheckBox);
            answersCheckBox.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionnaireSingleAdapter.ViewHolder holder, int position) {

        SurveyQuestionAnswers surveyQuestionAnswers = surveyQuestionAnswersArrayList.get(position);

        holder.questionNumber.setText(surveyQuestionAnswers.getQuestionNumber());
        holder.questionTV.setText(surveyQuestionAnswers.getE_Question());

        if(surveyQuestionAnswers.getInputType()!=null && surveyQuestionAnswers.getInputType().equalsIgnoreCase("numeric")){
            holder.answerEditText.setVisibility(View.VISIBLE);
            holder.answerEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            holder.answersRadioGroup.setVisibility(View.GONE);
            holder.answersCheckBox.setVisibility(View.GONE);
        }else if(surveyQuestionAnswers.getInputType()!=null && surveyQuestionAnswers.getInputType().equalsIgnoreCase("date")){
            holder.answerEditText.setVisibility(View.VISIBLE);
            holder.answerEditText.setInputType(InputType.TYPE_CLASS_DATETIME);
            holder.answersRadioGroup.setVisibility(View.GONE);
            holder.answersCheckBox.setVisibility(View.GONE);
        }else if(surveyQuestionAnswers.getInputType()!=null && surveyQuestionAnswers.getInputType().equalsIgnoreCase("single")){
            holder.answersRadioGroup.setVisibility(View.VISIBLE);
            holder.answerEditText.setVisibility(View.GONE);
            holder.answersCheckBox.setVisibility(View.GONE);

            for(int i = 1; i < 4; i++) {
                RadioButton rdbtn = new RadioButton(context);
                rdbtn.setId(i);
                rdbtn.setText("Radio " + rdbtn.getId());
                rdbtn.setTextColor(context.getResources().getColor(R.color.text_color));
                holder.answersRadioGroup.addView(rdbtn);
            }

        }else if(surveyQuestionAnswers.getInputType()!=null && surveyQuestionAnswers.getInputType().equalsIgnoreCase("multi_choice")){
            holder.answersCheckBox.setVisibility(View.VISIBLE);
            holder.answerEditText.setVisibility(View.GONE);
            holder.answersRadioGroup.setVisibility(View.GONE);
            for(int i = 0; i < 4; i++) {
                CheckBox cb = new CheckBox(context);
                cb.setText("I'm dynamic!");
                cb.setTextColor(context.getResources().getColor(R.color.text_color));
                holder.answersCheckBox.addView(cb);
            }
        }else if(surveyQuestionAnswers.getInputType()!=null && surveyQuestionAnswers.getInputType().equalsIgnoreCase("text")){
            holder.answerEditText.setVisibility(View.VISIBLE);
            holder.answerEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            holder.answersRadioGroup.setVisibility(View.GONE);
            holder.answersCheckBox.setVisibility(View.GONE);
        }else{
            holder.answerEditText.setVisibility(View.VISIBLE);
            holder.answerEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            holder.answersRadioGroup.setVisibility(View.GONE);
            holder.answersCheckBox.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return surveyQuestionAnswersArrayList.size();
    }
}
