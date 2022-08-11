package com.example.mithraapplication.Adapters;

import static com.example.mithraapplication.PHQScreeningScreen.isLanguageSelected;

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
import com.example.mithraapplication.ModelClasses.QuestionOptions;
import com.example.mithraapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PHQQuestionnaireAdapter extends RecyclerView.Adapter<PHQQuestionnaireAdapter.ViewHolder>{
    private Context context;
    private final ArrayList<QuestionAnswers> questionAnswersArrayList;
    private final ArrayList<QuestionOptions> questionOptionsArrayList;
    private ArrayList<QuestionOptions> filteredQuestionOptionsArrayList = new ArrayList<>();
    private ArrayList<ParticipantAnswers>  phqParticipantAnswers = new ArrayList<>();

    @NonNull
    @Override
    public PHQQuestionnaireAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.phq_questionnaire_row, parent, false);
        return new PHQQuestionnaireAdapter.ViewHolder(view);
    }

    public PHQQuestionnaireAdapter(Context context, ArrayList<QuestionAnswers> questionAnswersArrayList, ArrayList<QuestionOptions> questionOptionsArrayList, ArrayList<ParticipantAnswers> phqParticipantAnswers){
        this.context = context;
        this.questionAnswersArrayList = questionAnswersArrayList;
        this.questionOptionsArrayList = questionOptionsArrayList;
        this.phqParticipantAnswers = phqParticipantAnswers;
    }

    @Override
    public void onBindViewHolder(@NonNull PHQQuestionnaireAdapter.ViewHolder holder, int position) {

        filteredQuestionOptionsArrayList.clear();

        QuestionAnswers questionAnswers = questionAnswersArrayList.get(position);
        String questionID = questionAnswers.getName();

        filteredQuestionOptionsArrayList = questionOptionsArrayList.stream()
                .filter(questionOptions -> questionOptions.getQuestion_id().equalsIgnoreCase(questionID)).collect(Collectors.toCollection(ArrayList::new));

        if (phqParticipantAnswers != null && phqParticipantAnswers.size() == questionAnswersArrayList.size()) {
            setExistingData(holder, phqParticipantAnswers.get(position));
        }else{
            ParticipantAnswers participantAnswers = new ParticipantAnswers();
            phqParticipantAnswers.add(participantAnswers);
        }

        holder.questionNumber.setText(position + 1 +".");
        if(isLanguageSelected.equals("en")){
            holder.ph9Question.setText(questionAnswers.getQuestion_e());
            holder.option_oneTV.setText(filteredQuestionOptionsArrayList.get(0).getOption_e());
            holder.option_twoTV.setText(filteredQuestionOptionsArrayList.get(1).getOption_e());
            holder.option_threeTV.setText(filteredQuestionOptionsArrayList.get(2).getOption_e());
            holder.option_fourTV.setText(filteredQuestionOptionsArrayList.get(3).getOption_e());
        }else{
            holder.ph9Question.setText(questionAnswers.getQuestion_k());
            holder.option_oneTV.setText(filteredQuestionOptionsArrayList.get(0).getOption_k());
            holder.option_twoTV.setText(filteredQuestionOptionsArrayList.get(1).getOption_k());
            holder.option_threeTV.setText(filteredQuestionOptionsArrayList.get(2).getOption_k());
            holder.option_fourTV.setText(filteredQuestionOptionsArrayList.get(3).getOption_k());
        }
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

    private void setExistingData(ViewHolder holder, ParticipantAnswers participantAnswers){
        String selected_option_id = participantAnswers.getOption_id();

        if(selected_option_id!=null && !selected_option_id.equalsIgnoreCase("null")){
            if(selected_option_id.equals(filteredQuestionOptionsArrayList.get(0).getId())){
                holder.option_view_one.setVisibility(View.VISIBLE);
                holder.option_view_two.setVisibility(View.INVISIBLE);
                holder.option_view_three.setVisibility(View.INVISIBLE);
                holder.option_view_four.setVisibility(View.INVISIBLE);

                holder.option_oneTV.setTextColor(context.getResources().getColor(R.color.options_color));
                holder.option_twoTV.setTextColor(context.getResources().getColor(R.color.text_color));
                holder.option_threeTV.setTextColor(context.getResources().getColor(R.color.text_color));
                holder.option_fourTV.setTextColor(context.getResources().getColor(R.color.text_color));
            } else if(selected_option_id.equals(filteredQuestionOptionsArrayList.get(1).getId())){
                holder.option_view_one.setVisibility(View.INVISIBLE);
                holder.option_view_two.setVisibility(View.VISIBLE);
                holder.option_view_three.setVisibility(View.INVISIBLE);
                holder.option_view_four.setVisibility(View.INVISIBLE);

                holder.option_oneTV.setTextColor(context.getResources().getColor(R.color.text_color));
                holder.option_twoTV.setTextColor(context.getResources().getColor(R.color.options_color));
                holder.option_threeTV.setTextColor(context.getResources().getColor(R.color.text_color));
                holder.option_fourTV.setTextColor(context.getResources().getColor(R.color.text_color));
            } else if(selected_option_id.equals(filteredQuestionOptionsArrayList.get(2).getId())){
                holder.option_view_one.setVisibility(View.INVISIBLE);
                holder.option_view_two.setVisibility(View.INVISIBLE);
                holder.option_view_three.setVisibility(View.VISIBLE);
                holder.option_view_four.setVisibility(View.INVISIBLE);

                holder.option_oneTV.setTextColor(context.getResources().getColor(R.color.text_color));
                holder.option_twoTV.setTextColor(context.getResources().getColor(R.color.text_color));
                holder.option_threeTV.setTextColor(context.getResources().getColor(R.color.options_color));
                holder.option_fourTV.setTextColor(context.getResources().getColor(R.color.text_color));
            } else if(selected_option_id.equals(filteredQuestionOptionsArrayList.get(3).getId())){
                holder.option_view_one.setVisibility(View.INVISIBLE);
                holder.option_view_two.setVisibility(View.INVISIBLE);
                holder.option_view_three.setVisibility(View.INVISIBLE);
                holder.option_view_four.setVisibility(View.VISIBLE);

                holder.option_oneTV.setTextColor(context.getResources().getColor(R.color.text_color));
                holder.option_twoTV.setTextColor(context.getResources().getColor(R.color.text_color));
                holder.option_threeTV.setTextColor(context.getResources().getColor(R.color.text_color));
                holder.option_fourTV.setTextColor(context.getResources().getColor(R.color.options_color));
            }
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
        filteredQuestionOptionsArrayList.clear();
        String questionID = questionAnswers.getName();
        filteredQuestionOptionsArrayList = questionOptionsArrayList.stream()
                .filter(questionOptions -> questionOptions.getQuestion_id().equalsIgnoreCase(questionID)).collect(Collectors.toCollection(ArrayList::new));
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setQuestion_id(questionAnswers.getName());
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setQuestion(questionAnswers.getQuestion_e());
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setSelected_answer(filteredQuestionOptionsArrayList.get(0).getOption_e());
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setSelected_answer_weightage(String.valueOf(filteredQuestionOptionsArrayList.get(0).getOption_w()));
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setQuestion_no(questionAnswers.getQn_number());
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setOption_id(filteredQuestionOptionsArrayList.get(0).getId());

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
        filteredQuestionOptionsArrayList.clear();
        String questionID = questionAnswers.getName();
        filteredQuestionOptionsArrayList = questionOptionsArrayList.stream()
                .filter(questionOptions -> questionOptions.getQuestion_id().equalsIgnoreCase(questionID)).collect(Collectors.toCollection(ArrayList::new));
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setQuestion_id(questionAnswers.getName());
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setQuestion(questionAnswers.getQuestion_e());
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setSelected_answer(filteredQuestionOptionsArrayList.get(1).getOption_e());
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setSelected_answer_weightage(String.valueOf(filteredQuestionOptionsArrayList.get(1).getOption_w()));
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setQuestion_no(questionAnswers.getQn_number());
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setOption_id(filteredQuestionOptionsArrayList.get(1).getId());

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
        filteredQuestionOptionsArrayList.clear();
        String questionID = questionAnswers.getName();
        filteredQuestionOptionsArrayList = questionOptionsArrayList.stream()
                .filter(questionOptions -> questionOptions.getQuestion_id().equalsIgnoreCase(questionID)).collect(Collectors.toCollection(ArrayList::new));
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setQuestion_id(questionAnswers.getName());
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setQuestion(questionAnswers.getQuestion_e());
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setSelected_answer(filteredQuestionOptionsArrayList.get(2).getOption_e());
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setSelected_answer_weightage(String.valueOf(filteredQuestionOptionsArrayList.get(2).getOption_w()));
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setQuestion_no(questionAnswers.getQn_number());
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setOption_id(filteredQuestionOptionsArrayList.get(2).getId());

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
        filteredQuestionOptionsArrayList.clear();
        String questionID = questionAnswers.getName();
        filteredQuestionOptionsArrayList = questionOptionsArrayList.stream()
                .filter(questionOptions -> questionOptions.getQuestion_id().equalsIgnoreCase(questionID)).collect(Collectors.toCollection(ArrayList::new));
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setQuestion_id(questionAnswers.getName());
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setQuestion(questionAnswers.getQuestion_e());
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setSelected_answer(filteredQuestionOptionsArrayList.get(3).getOption_e());
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setSelected_answer_weightage(String.valueOf(filteredQuestionOptionsArrayList.get(3).getOption_w()));
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setQuestion_no(questionAnswers.getQn_number());
        phqParticipantAnswers.get(holder.getAbsoluteAdapterPosition()).setOption_id(filteredQuestionOptionsArrayList.get(3).getId());

        holder.option_view_one.setVisibility(View.INVISIBLE);
        holder.option_view_two.setVisibility(View.INVISIBLE);
        holder.option_view_three.setVisibility(View.INVISIBLE);
        holder.option_view_four.setVisibility(View.VISIBLE);

        holder.option_oneTV.setTextColor(context.getResources().getColor(R.color.text_color));
        holder.option_twoTV.setTextColor(context.getResources().getColor(R.color.text_color));
        holder.option_threeTV.setTextColor(context.getResources().getColor(R.color.text_color));
        holder.option_fourTV.setTextColor(context.getResources().getColor(R.color.options_color));
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
        Bundle args = new Bundle();
        args.putSerializable("PHQScreeningSurveyList", phqParticipantAnswers);
        intent.putExtra("PHQScreeningSurveyListData",args);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
