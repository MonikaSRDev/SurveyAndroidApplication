package com.example.mithraapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mithraapplication.ModelClasses.DiseasesProfile;
import com.example.mithraapplication.ModelClasses.PHQParticipantDetails;
import com.example.mithraapplication.ModelClasses.PHQSurveyPostAnswers;
import com.example.mithraapplication.ModelClasses.ParticipantAnswers;
import com.example.mithraapplication.ModelClasses.ParticipantDetails;
import com.example.mithraapplication.ModelClasses.QuestionAnswers;
import com.example.mithraapplication.R;

import java.util.ArrayList;
import java.util.List;

public class PHQQuestionnaireAdapter extends RecyclerView.Adapter<PHQQuestionnaireAdapter.ViewHolder>{
    private Context context;
    private ArrayList<QuestionAnswers> questionAnswersArrayList;
    private ArrayList<ParticipantAnswers>  phqParticipantAnswers = new ArrayList<>();
    private int totalScore = 0;
    private String postAnswers = "";


    @NonNull
    @Override
    public PHQQuestionnaireAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.phq_questionnaire_row, parent, false);
        return new PHQQuestionnaireAdapter.ViewHolder(view);
    }

    public PHQQuestionnaireAdapter(Context context, ArrayList<QuestionAnswers> questionAnswersArrayList){
        this.context = context;
        this.questionAnswersArrayList = questionAnswersArrayList;
    }

    @Override
    public void onBindViewHolder(@NonNull PHQQuestionnaireAdapter.ViewHolder holder, int position) {

        ParticipantAnswers participantAnswers = new ParticipantAnswers();

        phqParticipantAnswers.add(participantAnswers);

        QuestionAnswers questionAnswers = questionAnswersArrayList.get(position);

        holder.questionNumber.setText(position + 1 +".");
        holder.ph9Question.setText(questionAnswers.getQuestion_e());
        holder.option_oneTV.setText(questionAnswers.getOption_1_e());
        holder.option_twoTV.setText(String.valueOf(questionAnswers.getOption_2_e()));
        holder.option_threeTV.setText(questionAnswers.getOption_3_e());
        holder.option_fourTV.setText(questionAnswers.getOption_4_e());
        getSelectedOption(holder, questionAnswers);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView questionNumber, ph9Question, option_oneTV, option_twoTV, option_threeTV, option_fourTV;
        private ImageButton optionImageButtonOne, optionImageButtonTwo, optionImageButtonThree, optionImageButtonFour;
        private View option_view_one, option_view_two, option_view_three, option_view_four;
        private LinearLayout option1LinearLayout, option2LinearLayout, option3LinearLayout, option4LinearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            questionNumber = itemView.findViewById(R.id.PHQ_questionNumbers);
            ph9Question = itemView.findViewById(R.id.PHQ_questionsTV);

            option_oneTV = itemView.findViewById(R.id.PHQ_option_oneTV);
            option_twoTV = itemView.findViewById(R.id.PHQ_option_twoTV);
            option_threeTV = itemView.findViewById(R.id.PHQ_option_threeTV);
            option_fourTV = itemView.findViewById(R.id.PHQ_option_fourTV);

            optionImageButtonOne = itemView.findViewById(R.id.PHQ_optionImageBtn1);
            optionImageButtonTwo = itemView.findViewById(R.id.PHQ_optionImageBtn2);
            optionImageButtonThree = itemView.findViewById(R.id.PHQ_optionImageBtn3);
            optionImageButtonFour = itemView.findViewById(R.id.PHQ_optionImageBtn4);

            option_view_one = itemView.findViewById(R.id.PHQ_option_one_view);
            option_view_two = itemView.findViewById(R.id.PHQ_option_two_view);
            option_view_three = itemView.findViewById(R.id.PHQ_option_three_view);
            option_view_four = itemView.findViewById(R.id.PHQ_option_four_view);

            option1LinearLayout = itemView.findViewById(R.id.PHQ_option_one_linearLayout);
            option2LinearLayout = itemView.findViewById(R.id.PHQ_option_two_linearLayout);
            option3LinearLayout = itemView.findViewById(R.id.PHQ_option_three_linearLayout);
            option4LinearLayout = itemView.findViewById(R.id.PHQ_option_four_linearLayout);
        }
    }
    /**
     * Description : Get the option selected by the participant and assign weightage to it.
     */
    private void getSelectedOption(ViewHolder holder, QuestionAnswers questionAnswers){
        holder.option1LinearLayout.setOnClickListener(v -> onClickOfOptionOne(holder, questionAnswers));

        holder.option2LinearLayout.setOnClickListener(v -> onClickOfOptionTwo(holder, questionAnswers));

        holder.option3LinearLayout.setOnClickListener(v -> onClickOfOptionThree(holder, questionAnswers));

        holder.option4LinearLayout.setOnClickListener(v -> onClickOfOptionFour(holder, questionAnswers));

        holder.optionImageButtonOne.setOnClickListener(v -> onClickOfOptionOne(holder, questionAnswers));

        holder.optionImageButtonTwo.setOnClickListener(v -> onClickOfOptionTwo(holder, questionAnswers));

        holder.optionImageButtonThree.setOnClickListener(v -> onClickOfOptionThree(holder, questionAnswers));

        holder.optionImageButtonFour.setOnClickListener(v -> onClickOfOptionFour(holder, questionAnswers));
    }

    private void onClickOfOptionOne(ViewHolder holder, QuestionAnswers questionAnswers) {
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setQuestion(questionAnswers.getQuestion_e());
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setSelected_answer(questionAnswers.getOption_1_e());
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setSelected_answer_weightage(String.valueOf(questionAnswers.getOption_1_weightage()));
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setQuestion_no(questionAnswers.getQn_number());
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setQuestion_id(questionAnswers.getType());

        totalScore +=  questionAnswers.getOption_1_weightage();

        postAnswers = postAnswers + getAnswersList(holder.getAbsoluteAdapterPosition(), phqParticipantAnswers) + ",";

        holder.option_view_one.setVisibility(View.VISIBLE);
        holder.option_view_two.setVisibility(View.INVISIBLE);
        holder.option_view_three.setVisibility(View.INVISIBLE);
        holder.option_view_four.setVisibility(View.INVISIBLE);

        holder.option_oneTV.setTextColor(context.getResources().getColor(R.color.options_color));
        holder.option_twoTV.setTextColor(context.getResources().getColor(R.color.text_color));
        holder.option_threeTV.setTextColor(context.getResources().getColor(R.color.text_color));
        holder.option_fourTV.setTextColor(context.getResources().getColor(R.color.text_color));

    }

    private void onClickOfOptionTwo(ViewHolder holder, QuestionAnswers questionAnswers) {
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setQuestion(questionAnswers.getQuestion_e());
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setSelected_answer(questionAnswers.getOption_2_e());
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setSelected_answer_weightage(String.valueOf(questionAnswers.getOption_2_weightage()));
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setQuestion_no(questionAnswers.getQn_number());
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setQuestion_id(questionAnswers.getType());

        totalScore +=  questionAnswers.getOption_2_weightage();

        postAnswers = postAnswers + getAnswersList(holder.getAbsoluteAdapterPosition(), phqParticipantAnswers) + ",";

        holder.option_view_one.setVisibility(View.INVISIBLE);
        holder.option_view_two.setVisibility(View.VISIBLE);
        holder.option_view_three.setVisibility(View.INVISIBLE);
        holder.option_view_four.setVisibility(View.INVISIBLE);

        holder.option_oneTV.setTextColor(context.getResources().getColor(R.color.text_color));
        holder.option_twoTV.setTextColor(context.getResources().getColor(R.color.options_color));
        holder.option_threeTV.setTextColor(context.getResources().getColor(R.color.text_color));
        holder.option_fourTV.setTextColor(context.getResources().getColor(R.color.text_color));
    }

    private void onClickOfOptionThree(ViewHolder holder, QuestionAnswers questionAnswers) {

        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setQuestion(questionAnswers.getQuestion_e());
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setSelected_answer(questionAnswers.getOption_3_e());
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setSelected_answer_weightage(String.valueOf(questionAnswers.getOption_3_weightage()));
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setQuestion_no(questionAnswers.getQn_number());
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setQuestion_id(questionAnswers.getType());

        totalScore +=  questionAnswers.getOption_3_weightage();

        postAnswers = postAnswers + getAnswersList(holder.getAbsoluteAdapterPosition(), phqParticipantAnswers) + ",";

        holder.option_view_one.setVisibility(View.INVISIBLE);
        holder.option_view_two.setVisibility(View.INVISIBLE);
        holder.option_view_three.setVisibility(View.VISIBLE);
        holder.option_view_four.setVisibility(View.INVISIBLE);

        holder.option_oneTV.setTextColor(context.getResources().getColor(R.color.text_color));
        holder.option_twoTV.setTextColor(context.getResources().getColor(R.color.text_color));
        holder.option_threeTV.setTextColor(context.getResources().getColor(R.color.options_color));
        holder.option_fourTV.setTextColor(context.getResources().getColor(R.color.text_color));
    }

    private void onClickOfOptionFour(ViewHolder holder, QuestionAnswers questionAnswers) {

        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setQuestion(questionAnswers.getQuestion_e());
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setSelected_answer(questionAnswers.getOption_4_e());
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setSelected_answer_weightage(String.valueOf(questionAnswers.getOption_4_weightage()));
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setQuestion_no(questionAnswers.getQn_number());
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setQuestion_id(questionAnswers.getType());

        totalScore +=  questionAnswers.getOption_4_weightage();

        postAnswers = postAnswers + getAnswersList(holder.getAbsoluteAdapterPosition(), phqParticipantAnswers) + ",";

        holder.option_view_one.setVisibility(View.INVISIBLE);
        holder.option_view_two.setVisibility(View.INVISIBLE);
        holder.option_view_three.setVisibility(View.INVISIBLE);
        holder.option_view_four.setVisibility(View.VISIBLE);

        holder.option_oneTV.setTextColor(context.getResources().getColor(R.color.text_color));
        holder.option_twoTV.setTextColor(context.getResources().getColor(R.color.text_color));
        holder.option_threeTV.setTextColor(context.getResources().getColor(R.color.text_color));
        holder.option_fourTV.setTextColor(context.getResources().getColor(R.color.options_color));
    }

    private String getAnswersList(int position, ArrayList<ParticipantAnswers> surveyAnswersArrayList){
        List<String> surveyAnswer = new ArrayList<>();
        surveyAnswer.add("'qn_id'" + ":'" + surveyAnswersArrayList.get(position).getQuestion_id() +"'");
        surveyAnswer.add("'qn_no'" + ":'" + surveyAnswersArrayList.get(position).getQuestion_no()+"'");
        surveyAnswer.add("'question'" + ":'" + surveyAnswersArrayList.get(position).getQuestion()+"'");
        surveyAnswer.add("'ans'" + ":'" + surveyAnswersArrayList.get(position).getSelected_answer()+"'");
        surveyAnswer.add("'w'" + ":'" + surveyAnswersArrayList.get(position).getSelected_answer_weightage()+"'");
        surveyAnswer.add("'qn_start'" + ":'" + surveyAnswersArrayList.get(position).getQuestion_start_time()+"'");
        surveyAnswer.add("'qn_stop'" + ":'" + surveyAnswersArrayList.get(position).getQuestion_stop_time()+"'");
        surveyAnswer.add("'seconds'" + ":'" + surveyAnswersArrayList.get(position).getSeconds_taken()+"'");
        String surveyAnswerStr = String.join(",", surveyAnswer );
        surveyAnswerStr = "{" + surveyAnswerStr + "}";

        return surveyAnswerStr;
    }

    @Override
    public int getItemCount() {
        return questionAnswersArrayList.size();
    }

    /**
     * Description : This method is used to get the data entered by the user and send it to main activity on click of the save button
     */
    public void sendDataToActivity(){
        Intent intent = new Intent("PHQQuestionnaire");
        intent.putExtra("PHQScreeningSurveyData",postAnswers);
        intent.putExtra("TotalScore",totalScore);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
