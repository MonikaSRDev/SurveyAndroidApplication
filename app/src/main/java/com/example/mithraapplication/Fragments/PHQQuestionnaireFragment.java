package com.example.mithraapplication.Fragments;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mithraapplication.Adapters.PHQQuestionnaireAdapter;
import com.example.mithraapplication.HandleServerResponse;
import com.example.mithraapplication.LoginScreen;
import com.example.mithraapplication.MithraUtility;
import com.example.mithraapplication.ModelClasses.DiseasesProfile;
import com.example.mithraapplication.ModelClasses.FrappeResponse;
import com.example.mithraapplication.ModelClasses.PHQLocations;
import com.example.mithraapplication.ModelClasses.PHQSurveyPostAnswers;
import com.example.mithraapplication.ModelClasses.PostSurveyQuestions;
import com.example.mithraapplication.ModelClasses.QuestionAnswers;
import com.example.mithraapplication.ModelClasses.SurveyQuestions;
import com.example.mithraapplication.PHQParticipantsScreen;
import com.example.mithraapplication.ParticipantLandingScreen;
import com.example.mithraapplication.R;
import com.example.mithraapplication.ServerRequestAndResponse;
import com.example.mithraapplication.SurveyScreen;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;

public class PHQQuestionnaireFragment extends Fragment implements HandleServerResponse {

    private EditText phqSHGName, phqID, phqParticipantName;
    private Button saveButton;
    private ArrayList<QuestionAnswers> questionArray = new ArrayList<>();
    private final MithraUtility mithraUtility = new MithraUtility();
    private String surveyStartDateTime, surveyEndDateTime, totalSurveyTime, questionStartTime, questionEndTime, totalQuestionTime;
    private int questionIndex = 0, selectedOptionValue = 0, totalScore = 0;
    private Dialog dialog;
    private PHQLocations phqLocations;
    private PHQQuestionnaireAdapter phqQuestionnaireAdapter;
    private RecyclerView questionsRecyclerView;
    private ArrayList<PHQSurveyPostAnswers> phqSurveyPostAnswersArrayList = new ArrayList<>();
    private String postAnswers = null;
    private Context context;

    public PHQQuestionnaireFragment(Context context, PHQLocations phqLocations) {
        this.context = context;
        this.phqLocations = phqLocations;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phq_questionnaire, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RegisterViews(view);
        setOnClickForSaveButton();
//        getIntentData();
//        initializeData();
        setOnClickForSaveButton();
        callServerToGetPHQ9Questions();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("PHQQuestionnaire"));
    }

    private void initializeData() {
        String message = "{\"message\":\n" +
                "\t[\n" +
                "\t\t{\"qn_number\":\"9\",\n" +
                "\t\t\"question_e\":\"Thoughts that you would be better off dead, or of hurting yourself. \",\n" +
                "\t\t\"option_1_e\":\"Not at all\",\n" +
                "\t\t\"option_2_e\":\"Several Days\",\n" +
                "\t\t\"option_3_e\":\"More than half the days\",\n" +
                "\t\t\"option_4_e\":\"Nearly every day\",\n" +
                "\t\t\"qn_numberKannada\":\"೯\",\n" +
                "\t\t\"questionKannada\":\"ನೀವು ಸತ್ತರೆ ಚೆನ್ನಾಗಿರುತ್ತದೆ ಎಂಬ ಅಥವಾ ಯಾವುದಾದರೂ ರೀತಿಯಲ್ಲಿ ನಿಮ್ಮನ್ನು ಹಾನಿಪಡಿಸಿಕೊಳ್ಳುವ ಯೋಚನೆಗಳು.\",\n" +
                "\t\t\"option_1Kannada\":\"ಇಲ್ಲವೇ ಇಲ್ಲ\",\n" +
                "\t\t\"option_2Kannada\":\"ಹಲವು ದಿನಗಳು\",\n" +
                "\t\t\"option_3Kannada\":\"ಅರ್ಧಕ್ಕಿಂತ ಹೆಚ್ಚು ದಿನಗಳು\",\n" +
                "\t\t\"option_4Kannada\":\"ಬಹುತೇಕ ಪ್ರತಿದಿನ\"},\n" +
                "\t\t\n" +
                "\t\t{\"qn_number\":\"8\",\n" +
                "\t\t\"question_e\":\"Moving or speaking so slowly that other people could have noticed. Or the opposite being so fidgety or restless that you have been moving around a lot more than usual. \\n\",\n" +
                "\t\t\"option_1_e\":\"Not at all\",\n" +
                "\t\t\"option_2_e\":\"Several Days\",\n" +
                "\t\t\"option_3_e\":\"More than half the days\",\n" +
                "\t\t\"option_4_e\":\"nearly every day\",\n" +
                "\t\t\"qn_numberKannada\":\"೮\",\n" +
                "\t\t\"questionKannada\":\"ಇತರರ ಗಮನಕ್ಕೆ ಬರುವಷ್ಟು ನಿದಾನವಾಗಿ ನಡೆದಾಡುವುದು ಅಥವಾ ಮಾತನಾಡುವುದು? ಅಥವಾ ತದ್ವಿರುದ್ಧವಾಗಿ - ಸಾಮಾನ್ಯಕ್ಕಿಂತ ಹೆಚ್ಚು ಅತ್ತಿಂದಿತ್ತ ಓಡಾಡುವಷ್ಟು ಚಡಪಡಿಕೆ ಅಥವಾ ಅಶಾಂತಿ.\",\n" +
                "\t\t\"option_1Kannada\":\"ಇಲ್ಲವೇ ಇಲ್ಲ\",\n" +
                "\t\t\"option_2Kannada\":\"ಹಲವು ದಿನಗಳು\",\n" +
                "\t\t\"option_3Kannada\":\"ಅರ್ಧಕ್ಕಿಂತ ಹೆಚ್ಚು ದಿನಗಳು\",\n" +
                "\t\t\"option_4Kannada\":\"ಬಹುತೇಕ ಪ್ರತಿದಿನ\"},\n" +
                "\t\t\n" +
                "\t\t{\"qn_number\":\"7\",\n" +
                "\t\t\"question_e\":\"Trouble concentrating on things, such as reading the newspaper or watching television.  \",\n" +
                "\t\t\"option_1_e\":\"Not at all\",\n" +
                "\t\t\"option_2_e\":\"Several Days \",\n" +
                "\t\t\"option_3_e\":\"More than half the days\",\n" +
                "\t\t\"option_4_e\":\"Nearly every day\",\n" +
                "\t\t\"qn_numberKannada\":\"೭\",\n" +
                "\t\t\"questionKannada\":\"ಪತ್ರಿಕೆಯನ್ನ ಓದಲು ಅಥವಾ ಟೆಲಿವಿಷನ್ ನೋಡುವುದು ಇತ್ಯಾದಿ ವಿಷಯಗಳಲ್ಲಿ ಗಮನ ಕೇಂದ್ರೀಕರಿಸಲು ತೊಂದರೆ.\",\n" +
                "\t\t\"option_1Kannada\":\"ಇಲ್ಲವೇ ಇಲ್ಲ\",\n" +
                "\t\t\"option_2Kannada\":\"ಹಲವು ದಿನಗಳು\",\n" +
                "\t\t\"option_3Kannada\":\"ಅರ್ಧಕ್ಕಿಂತ ಹೆಚ್ಚು ದಿನಗಳು\",\n" +
                "\t\t\"option_4Kannada\":\"ಬಹುತೇಕ ಪ್ರತಿದಿನ\"},\n" +
                "\n" +
                "\t\t{\"qn_number\":\"6\",\n" +
                "\t\t\"question_e\":\"Feeling bad about yourself or that you are a failure or have let yourself or your family down.\",\n" +
                "\t\t\"option_1_e\":\"Not at all\",\n" +
                "\t\t\"option_2_e\":\"Several Days\",\n" +
                "\t\t\"option_3_e\":\"More than half the days\",\n" +
                "\t\t\"option_4_e\":\"Nearly every day\",\n" +
                "\t\t\"qn_numberKannada\":\"೬\",\n" +
                "\t\t\"questionKannada\":\"ನಿಮ್ಮ ಬಗ್ಗೆ ನಿಮಗೆ ಕೆಟ್ಟ ಭಾವನೆ - ಅಥವಾ ನೀವು ವಿಫಲರು ಅಥವಾ ನೀವು ನಿಮ್ಮ ಹಾಗು ಕುಟುಂಬದವರ ನಿರೀಕ್ಷೆಗಿಂತ ಕೆಳಮಟ್ಟದಲ್ಲಿದ್ದೀರಿ ಎಂಬ ಭಾವನೆ.\",\n" +
                "\t\t\"option_1Kannada\":\"ಇಲ್ಲವೇ ಇಲ್ಲ\",\n" +
                "\t\t\"option_2Kannada\":\"ಹಲವು ದಿನಗಳು\",\n" +
                "\t\t\"option_3Kannada\":\"ಅರ್ಧಕ್ಕಿಂತ ಹೆಚ್ಚು ದಿನಗಳು\",\n" +
                "\t\t\"option_4Kannada\":\"ಬಹುತೇಕ ಪ್ರತಿದಿನ\"},\n" +
                "\n" +
                "\t\t{\"qn_number\":\"5\",\n" +
                "\t\t\"question_e\":\"Poor appetite or over eating\",\n" +
                "\t\t\"option_1_e\":\"Not at all\",\n" +
                "\t\t\"option_2_e\":\"Several Days\",\n" +
                "\t\t\"option_3_e\":\"More than half the days\",\n" +
                "\t\t\"option_4_e\":\"Nearly every day\",\n" +
                "\t\t\"qn_numberKannada\":\"೫\",\n" +
                "\t\t\"questionKannada\":\"ಕಡಿಮೆ ಹಸಿವು ಅಥವಾ ಅತಿಯಾಗಿ ತಿನ್ನುವುದು.\",\n" +
                "\t\t\"option_1Kannada\":\"ಇಲ್ಲವೇ ಇಲ್ಲ\",\n" +
                "\t\t\"option_2Kannada\":\"ಹಲವು ದಿನಗಳು\",\n" +
                "\t\t\"option_3Kannada\":\"ಅರ್ಧಕ್ಕಿಂತ ಹೆಚ್ಚು ದಿನಗಳು\",\n" +
                "\t\t\"option_4Kannada\":\"ಬಹುತೇಕ ಪ್ರತಿದಿನ\"},\n" +
                "\n" +
                "\t\t{\"qn_number\":\"4\",\n" +
                "\t\t\"question_e\":\"Feeling tired or having little energy.\",\n" +
                "\t\t\"option_1_e\":\"Not at all\",\n" +
                "\t\t\"option_2_e\":\"Several Days\",\n" +
                "\t\t\"option_3_e\":\"More than half the days\",\n" +
                "\t\t\"option_4_e\":\"Nearly every day\",\n" +
                "\t\t\"qn_numberKannada\":\"೪\",\n" +
                "\t\t\"questionKannada\":\"ಆಯಾಸಗೊಳ್ಳುವುದು ಅಥವಾ ಚೈತನ್ಯ ಇಲ್ಲದಿರುವುದು.\",\n" +
                "\t\t\"option_1Kannada\":\"ಇಲ್ಲವೇ ಇಲ್ಲ\",\n" +
                "\t\t\"option_2Kannada\":\"ಹಲವು ದಿನಗಳು\",\n" +
                "\t\t\"option_3Kannada\":\"ಅರ್ಧಕ್ಕಿಂತ ಹೆಚ್ಚು ದಿನಗಳು\",\n" +
                "\t\t\"option_4Kannada\":\"ಬಹುತೇಕ ಪ್ರತಿದಿನ\"},\n" +
                "\n" +
                "\t\t{\"qn_number\":\"3\",\n" +
                "\t\t\"question_e\":\"Trouble falling or staying asleep, or sleeping too much\",\n" +
                "\t\t\"option_1_e\":\"Not at all\",\n" +
                "\t\t\"option_2_e\":\"Several Days\",\n" +
                "\t\t\"option_3_e\":\"More than half the days\",\n" +
                "\t\t\"option_4_e\":\"Nearly every day\",\n" +
                "\t\t\"qn_numberKannada\":\"೩\",\n" +
                "\t\t\"questionKannada\":\"ನಿದ್ರೆ ಬರುವದು ಅಥವಾ ನಿದ್ರೆಯಲ್ಲಿರುವುದಕ್ಕೆ ತೊಂದರೆ ಅಥವಾ ಅತಿಯಾಗಿ ನಿದ್ರೆ ಮಾಡುವುದು.\",\n" +
                "\t\t\"option_1Kannada\":\"ಇಲ್ಲವೇ ಇಲ್ಲ\",\n" +
                "\t\t\"option_2Kannada\":\"ಹಲವು ದಿನಗಳು\",\n" +
                "\t\t\"option_3Kannada\":\"ಅರ್ಧಕ್ಕಿಂತ ಹೆಚ್ಚು ದಿನಗಳು\",\n" +
                "\t\t\"option_4Kannada\":\"ಬಹುತೇಕ ಪ್ರತಿದಿನ\"},\n" +
                "\n" +
                "\t\t{\"qn_number\":\"1\",\n" +
                "\t\t\"question_e\":\"Little interest or pleasure in doing things.\",\n" +
                "\t\t\"option_1_e\":\"Not at all\",\n" +
                "\t\t\"option_2_e\":\"Several Days\",\n" +
                "\t\t\"option_3_e\":\"More than half the days\",\n" +
                "\t\t\"option_4_e\":\"Nearly every day\",\n" +
                "\t\t\"qn_numberKannada\":\"೧\",\n" +
                "\t\t\"questionKannada\":\"ಕೆಲಸಗಳನ್ನು ಮಾಡುವುದರಲ್ಲಿ ಕಡಿಮೆ ಆಸಕ್ತಿ ಅಥವಾ ಆನಂದ.\",\n" +
                "\t\t\"option_1Kannada\":\"ಇಲ್ಲವೇ ಇಲ್ಲ\",\n" +
                "\t\t\"option_2Kannada\":\"ಹಲವು ದಿನಗಳು\",\n" +
                "\t\t\"option_3Kannada\":\"ಅರ್ಧಕ್ಕಿಂತ ಹೆಚ್ಚು ದಿನಗಳು\",\n" +
                "\t\t\"option_4Kannada\":\"ಬಹುತೇಕ ಪ್ರತಿದಿನ\"},\n" +
                "\n" +
                "\t\t{\"qn_number\":\"2\",\n" +
                "\t\t\"question_e\":\"Feeling down, depressed, or hopeless.\",\n" +
                "\t\t\"option_1_e\":\"Not at all\",\n" +
                "\t\t\"option_2_e\":\"Several days\",\n" +
                "\t\t\"option_3_e\":\"More than half the days\",\n" +
                "\t\t\"option_4_e\":\"Nearly every day\",\n" +
                "\t\t\"qn_numberKannada\":\"೨\",\n" +
                "\t\t\"questionKannada\":\"ಬೇಸರ, ಖಿನ್ನತೆ ಅಥವಾ ಹತಾಶೆ.\",\n" +
                "\t\t\"option_1Kannada\":\"ಇಲ್ಲವೇ ಇಲ್ಲ\",\n" +
                "\t\t\"option_2Kannada\":\"ಹಲವು ದಿನಗಳು\",\n" +
                "\t\t\"option_3Kannada\":\"ಅರ್ಧಕ್ಕಿಂತ ಹೆಚ್ಚು ದಿನಗಳು\",\n" +
                "\t\t\"option_4Kannada\":\"ಬಹುತೇಕ ಪ್ರತಿದಿನ\"}]}";
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<QuestionAnswers>>(){}.getType();
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            ArrayList<QuestionAnswers> questionAnswersArrayList = new ArrayList<>();

            try{
                questionAnswersArrayList = gson.fromJson(jsonObject.get("message"), type);
                if(questionAnswersArrayList.size() > 1){
                    questionAnswersArrayList.sort(Comparator.comparingInt(question -> Integer.parseInt(question.getQn_number())));
                    Log.i("SurveyScreen", "responseReceivedSuccessfully : " +questionAnswersArrayList);
                    questionArray = questionAnswersArrayList;
                    surveyStartDateTime = mithraUtility.getCurrentTime();
                    setRecyclerView();
                }
            }catch(Exception e){
                Toast.makeText(getActivity(), jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
            }
    }


    private void RegisterViews(View view) {
        saveButton = view.findViewById(R.id.PHQQuestionnaireSaveButton);
        phqSHGName = view.findViewById(R.id.PHQQuestionnaireSHGTV);
        phqSHGName.setText(phqLocations.getSHGName());
        phqID = view.findViewById(R.id.PHQQuestionnaireID);
        phqParticipantName = view.findViewById(R.id.PHQQuestionnaireName);
        questionsRecyclerView = view.findViewById(R.id.PHQQuestionnaireRV);

    }

    private void setOnClickForSaveButton(){
        saveButton.setOnClickListener(v -> {
            if(phqQuestionnaireAdapter != null) {
                surveyEndDateTime = mithraUtility.getCurrentTime();
                phqQuestionnaireAdapter.sendDataToActivity();
            }
        });
    }

    /**
     * Description : This broadcast receiver is used get the data from the adapter
     */
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            postAnswers = intent.getStringExtra("PHQScreeningSurveyData");
            totalScore = intent.getIntExtra("TotalScore", 0);
            if(postAnswers.length() > 0){
                callServerForPostPHQAnswers();
            }
        }
    };

    private void showDialogForPHQScreeningID(String PHQID){
        View customLayout = getLayoutInflater().inflate(R.layout.activity_phq_screening_popup, null);

        TextView description = customLayout.findViewById(R.id.PHQScreeningIDTVPopup);
        TextView phqScreeningID = customLayout.findViewById(R.id.PHQScreeningIDPopup);
        phqScreeningID.setText(PHQID);
        Button okButton = customLayout.findViewById(R.id.OKScreeningIDButton);

        dialog  = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(customLayout);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.dimAmount = 0.5f;
        dialog.getWindow().setAttributes(wmlp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();

        okButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PHQParticipantsScreen.class);
            startActivity(intent);
            getActivity().finish();
        });
    }

    private void setRecyclerView(){
        phqQuestionnaireAdapter = new PHQQuestionnaireAdapter(getActivity(), questionArray);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        questionsRecyclerView.setLayoutManager(linearLayoutManager);
        questionsRecyclerView.setAdapter(phqQuestionnaireAdapter);
    }

    /**
     * Description : Call the server to get the PHQ9 questions for the participant
     */
    private void callServerToGetPHQ9Questions(){
        String url = "http://"+ context.getString(R.string.base_url)+ "/api/method/mithra.mithra.doctype.survey_questions.api.questions";
        SurveyQuestions surveyQuestions = new SurveyQuestions();
        surveyQuestions.setType("SUR0001");
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.getPHQ9Questions(context, surveyQuestions, url);
    }

    /**
     * Description : Update the server with the data entered by the user
     */
    private void callServerForPostPHQAnswers(){
        String url = "http://"+ context.getString(R.string.base_url)+ "/api/resource/phq9_scr_sub";
        totalSurveyTime = mithraUtility.getTimeDifferenceMinutes(surveyStartDateTime, surveyEndDateTime);
        PHQSurveyPostAnswers phqSurveyPostAnswers = new PHQSurveyPostAnswers();
        phqSurveyPostAnswers.setCreated_user(mithraUtility.getSharedPreferencesData(context, context.getString(R.string.primaryID), context.getString(R.string.coordinatorPrimaryID)));
        phqSurveyPostAnswers.setType("SUR0001");
        phqSurveyPostAnswers.setScore(String.valueOf(totalScore));
        phqSurveyPostAnswers.setMinutes(totalSurveyTime);
        phqSurveyPostAnswers.setAnswer(postAnswers.substring(0, postAnswers.length()-1));
        phqSurveyPostAnswers.setSurvey_start_time(surveyStartDateTime);
        phqSurveyPostAnswers.setSurvey_end_time(surveyEndDateTime);
        phqSurveyPostAnswers.setMan_id(phqID.getText().toString());
        phqSurveyPostAnswers.setParticipantName(phqParticipantName.getText().toString());
        phqSurveyPostAnswers.setScreening_id("Pending");
        phqSurveyPostAnswers.setShg(phqLocations.getName());
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.postPHQScreeningAnswers(context, phqSurveyPostAnswers, url);
    }


    @Override
    public void responseReceivedSuccessfully(String message) {
        Log.i("SurveyScreen", "responseReceivedSuccessfully");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<QuestionAnswers>>(){}.getType();
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        if(jsonObject.get("message")!=null) {
            ArrayList<QuestionAnswers> questionAnswersArrayList = new ArrayList<>();

            try {
                questionAnswersArrayList = gson.fromJson(jsonObject.get("message"), type);
                if (questionAnswersArrayList.size() > 1) {
                    questionAnswersArrayList.sort(Comparator.comparingInt(question -> Integer.parseInt(question.getQn_number())));
                    Log.i("SurveyScreen", "responseReceivedSuccessfully : " + questionAnswersArrayList);
                    questionArray = questionAnswersArrayList;
                    surveyStartDateTime = mithraUtility.getCurrentTime();
                    setRecyclerView();
                }
            } catch (Exception e) {
                Toast.makeText(context, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
            }
        }else{
            JsonObject jsonObjectRegistration = JsonParser.parseString(message).getAsJsonObject();
            Type typeFrappe = new TypeToken<FrappeResponse>(){}.getType();
            if(jsonObjectRegistration.get("data")!=null) {
                FrappeResponse frappeResponse;
                frappeResponse = gson.fromJson(jsonObjectRegistration.get("data"), typeFrappe);
                showDialogForPHQScreeningID(frappeResponse.getName());
            }
        }
    }

    @Override
    public void responseReceivedFailure(String message) {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
