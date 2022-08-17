package com.example.mithraapplication.Fragments;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.LocaleList;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.mithraapplication.MithraAppServerEvents.HandleServerResponse;
import com.example.mithraapplication.MithraAppServerEventsListeners.PHQQuestionnaireServerEvents;
import com.example.mithraapplication.MithraUtility;
import com.example.mithraapplication.ModelClasses.DiseasesProfile;
import com.example.mithraapplication.ModelClasses.FrappeResponse;
import com.example.mithraapplication.ModelClasses.OptionsRequest;
import com.example.mithraapplication.ModelClasses.PHQLocations;
import com.example.mithraapplication.ModelClasses.PHQSurveyPostAnswers;
import com.example.mithraapplication.ModelClasses.ParticipantAnswers;
import com.example.mithraapplication.ModelClasses.QuestionAnswers;
import com.example.mithraapplication.ModelClasses.QuestionOptions;
import com.example.mithraapplication.ModelClasses.RegisterParticipant;
import com.example.mithraapplication.ModelClasses.SurveyQuestions;
import com.example.mithraapplication.PHQParticipantsScreen;
import com.example.mithraapplication.PHQScreeningScreen;
import com.example.mithraapplication.R;
import com.example.mithraapplication.MithraAppServerEvents.ServerRequestAndResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PHQQuestionnaireFragment extends Fragment implements HandleServerResponse, PHQQuestionnaireServerEvents {

    private EditText phqID, phqParticipantName;
    private TextView phqSHGName;
    private Button saveButton;
    private ArrayList<QuestionAnswers> questionArray = new ArrayList<>();
    private ArrayList<QuestionOptions> optionsArray = new ArrayList<>();
    private final MithraUtility mithraUtility = new MithraUtility();
    private String surveyStartDateTime, surveyEndDateTime, totalSurveyTime, questionStartTime, questionEndTime, totalQuestionTime;
    private int questionIndex = 0, selectedOptionValue = 0, totalScore = 0;
    private Dialog dialog;
    private PHQLocations phqLocations;
    private PHQQuestionnaireAdapter phqQuestionnaireAdapter;
    private RecyclerView questionsRecyclerView;
    private ArrayList<PHQSurveyPostAnswers> phqSurveyPostAnswersArrayList = new ArrayList<>();
    private String postAnswers = null, ManualID, participantName;
    private Context context;
    private boolean isConfigChanged = false;
    private ArrayList<QuestionOptions> filteredQuestionOptionsArrayList = new ArrayList<>();
    private ArrayList<ParticipantAnswers>  phqParticipantAnswersArrayList = new ArrayList<>();

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
        return inflater.inflate(R.layout.fragment_phq_questionnaire, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RegisterViews(view);
        setOnClickForSaveButton();
        editTextChangeListeners();
        callServerToGetPHQ9Questions();
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
                Toast.makeText(context, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
            }
    }


    private void RegisterViews(View view) {
        saveButton = view.findViewById(R.id.PHQQuestionnaireSaveButton);
        saveButton.setEnabled(false);
        saveButton.setBackgroundResource(R.drawable.yes_no_button);
        saveButton.setTextColor(getResources().getColor(R.color.text_color, context.getTheme()));
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

    private void editTextChangeListeners(){
        phqID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean valid = getUserEnteredData();
                if(valid){
                    saveButton.setEnabled(true);
                    saveButton.setBackgroundResource(R.drawable.status_button);
                    saveButton.setTextColor(getResources().getColor(R.color.white, context.getTheme()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        phqParticipantName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean valid = getUserEnteredData();
                if(valid){
                    saveButton.setEnabled(true);
                    saveButton.setBackgroundResource(R.drawable.status_button);
                    saveButton.setTextColor(getResources().getColor(R.color.white, context.getTheme()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//                String name = phqParticipantName.getText().toString();
//                if(!name.equals("") && name.matches("^[a-zA-Z0-9]*$")){
//                    participantName = name;
//                    saveButton.setEnabled(true);
//                    saveButton.setBackgroundResource(R.drawable.status_button);
//                    saveButton.setTextColor(getResources().getColor(R.color.white, context.getTheme()));
//                }else{
//                    phqParticipantName.setError("Name should contain both alphabets and numbers.");
//                    saveButton.setEnabled(false);
//                    saveButton.setBackgroundResource(R.drawable.yes_no_button);
//                    saveButton.setTextColor(getResources().getColor(R.color.text_color, context.getTheme()));
//                }
            }
        });
    }

    /**
     * Description : This broadcast receiver is used get the data from the adapter
     */
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle args = intent.getBundleExtra("PHQScreeningSurveyListData");
            phqParticipantAnswersArrayList = (ArrayList<ParticipantAnswers>) args.getSerializable("PHQScreeningSurveyList");
            if(isConfigChanged){
                if(phqQuestionnaireAdapter!=null){
                    setRecyclerView();
                    isConfigChanged = false;
                }
            }else{
                boolean isValid = getPostAnswersData();
                if(isValid){
                    callServerForPostPHQAnswers();
                }
            }
        }
    };

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver, new IntentFilter("PHQQuestionnaire"));
        super.onResume();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    private void showDialogForPHQScreeningID(String PHQID){
        View customLayout = getLayoutInflater().inflate(R.layout.activity_phq_screening_popup, null);

        TextView description = customLayout.findViewById(R.id.PHQScreeningIDTVPopup);
        TextView phqScreeningID = customLayout.findViewById(R.id.PHQScreeningIDPopup);
        phqScreeningID.setText(PHQID);
        Button okButton = customLayout.findViewById(R.id.OKScreeningIDButton);

        dialog  = new Dialog(context);
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
            Intent intent = new Intent(context, PHQParticipantsScreen.class);
            intent.putExtra("PHQLocations", (Serializable) phqLocations);
            startActivity(intent);
        });
    }

    private void setRecyclerView(){
        phqQuestionnaireAdapter = new PHQQuestionnaireAdapter(context, questionArray, optionsArray, phqParticipantAnswersArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        questionsRecyclerView.setLayoutManager(linearLayoutManager);
        questionsRecyclerView.setAdapter(phqQuestionnaireAdapter);
    }

    private boolean getUserEnteredData(){
        ManualID = phqID.getText().toString();
        participantName = phqParticipantName.getText().toString();

        if(ManualID!=null && ManualID.isEmpty()){
            phqID.setError(context.getString(R.string.enter_man_id));
            saveButton.setEnabled(false);
            saveButton.setBackgroundResource(R.drawable.yes_no_button);
            saveButton.setTextColor(getResources().getColor(R.color.text_color, context.getTheme()));
            return false;
        }else if(ManualID!=null && ManualID.length() < 5){
            phqID.setError(context.getString(R.string.man_min_5));
            saveButton.setEnabled(false);
            saveButton.setBackgroundResource(R.drawable.yes_no_button);
            saveButton.setTextColor(getResources().getColor(R.color.text_color, context.getTheme()));
            return false;
        }

        if(participantName!=null && participantName.isEmpty()){
            phqParticipantName.setError(context.getString(R.string.enter_name));
            saveButton.setEnabled(false);
            saveButton.setBackgroundResource(R.drawable.yes_no_button);
            saveButton.setTextColor(getResources().getColor(R.color.text_color, context.getTheme()));
            return false;
        }else if(participantName!=null && participantName.length() < 5){
            phqParticipantName.setError(context.getString(R.string.name_min_5));
            saveButton.setEnabled(false);
            saveButton.setBackgroundResource(R.drawable.yes_no_button);
            saveButton.setTextColor(getResources().getColor(R.color.text_color, context.getTheme()));
            return false;
        }
        return true;
    }

    private boolean getPostAnswersData(){
        totalScore = 0;
        postAnswers = "";
        int i = 0;
        for (ParticipantAnswers participantAnswers: phqParticipantAnswersArrayList) {
            if(participantAnswers.getOption_id()!=null && !participantAnswers.getOption_id().equalsIgnoreCase("null")){
                if(participantAnswers.getSelected_answer_weightage()!=null && !participantAnswers.getSelected_answer_weightage().equalsIgnoreCase("null")){
                    totalScore = totalScore + Integer.parseInt(participantAnswers.getSelected_answer_weightage());
                    postAnswers = postAnswers + getAnswersList(i, phqParticipantAnswersArrayList) + ",";
                    i++;
                }
            }else{
                Toast.makeText(context, context.getString(R.string.answer_all_questions), Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    private String getAnswersList(int position, ArrayList<ParticipantAnswers> surveyAnswersArrayList){
        List<String> surveyAnswer = new ArrayList<>();
        surveyAnswer.add("'qn_id'" + ":'" + surveyAnswersArrayList.get(position).getQuestion_id() +"'");
        surveyAnswer.add("'qn_no'" + ":'" + surveyAnswersArrayList.get(position).getQuestion_no()+"'");
        surveyAnswer.add("'question'" + ":'" + surveyAnswersArrayList.get(position).getQuestion()+"'");
        surveyAnswer.add("'option_id'" + ":'" + surveyAnswersArrayList.get(position).getOption_id()+"'");
        surveyAnswer.add("'ans'" + ":'" + surveyAnswersArrayList.get(position).getSelected_answer()+"'");
        surveyAnswer.add("'w'" + ":'" + surveyAnswersArrayList.get(position).getSelected_answer_weightage()+"'");
        String surveyAnswerStr = String.join(",", surveyAnswer );
        surveyAnswerStr = "{" + surveyAnswerStr + "}";

        return surveyAnswerStr;
    }

    /**
     * Description : Call the server to get the PHQ9 questions for the participant
     */
    private void callServerToGetPHQ9Questions(){
        String url = "http://"+ context.getString(R.string.base_url)+ "/api/method/mithra.mithra.doctype.survey_questions.api.questions";
        SurveyQuestions surveyQuestions = new SurveyQuestions();
        surveyQuestions.setFilter_data("{'sur_pri_id':'SUR0001'}");
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.setPhqQuestionnaireServerEvents(this);
        requestObject.getPHQ9Questions(context, surveyQuestions, url);
    }

    /**
     * Description : Call the server to get the PHQ9 questions for the participant
     */
    private void callServerToGetPHQ9Options(){
        String url = "http://"+ context.getString(R.string.base_url)+ "/api/method/mithra.mithra.doctype.survey_question_options.api.options";
        OptionsRequest optionsRequest = new OptionsRequest();
        optionsRequest.setFilter_data("{'sur_pri_id':'SUR0001'}");
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.setPhqQuestionnaireServerEvents(this);
        requestObject.getPHQ9Options(context, optionsRequest, url);
    }

    /**
     * Description : Update the server with the data entered by the user
     */
    private void callServerForPostPHQAnswers(){
        String url = "http://"+ context.getString(R.string.base_url)+ "/api/method/mithra.mithra.doctype.phq9_scr_sub.api.phq9_scr_sub";
        totalSurveyTime = mithraUtility.getTimeDifferenceMinutes(surveyStartDateTime, surveyEndDateTime);
        PHQSurveyPostAnswers phqSurveyPostAnswers = new PHQSurveyPostAnswers();
        phqSurveyPostAnswers.setCreated_user(mithraUtility.getSharedPreferencesData(context, context.getString(R.string.primaryID), context.getString(R.string.coordinatorPrimaryID)));
        phqSurveyPostAnswers.setType("SUR0001");
        phqSurveyPostAnswers.setScore(String.valueOf(totalScore));
        phqSurveyPostAnswers.setMinutes(totalSurveyTime);
        phqSurveyPostAnswers.setAnswer(postAnswers.substring(0, postAnswers.length() - 1)); //.substring(0, postAnswers.length()-1)
        phqSurveyPostAnswers.setSurvey_start_time(surveyStartDateTime);
        phqSurveyPostAnswers.setSurvey_end_time(surveyEndDateTime);
        phqSurveyPostAnswers.setMan_id(ManualID);
        phqSurveyPostAnswers.setParticipantName(participantName);
        phqSurveyPostAnswers.setScreening_id("S0001"); //S0001, S0068 - For test server
        phqSurveyPostAnswers.setShg(phqLocations.getName());
        phqSurveyPostAnswers.setRegister("no");
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.setPhqQuestionnaireServerEvents(this);
        requestObject.postPHQScreeningAnswers(context, phqSurveyPostAnswers, url);
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

    /**
     * @param newConfig
     * Description : This method is used to update the views on change of language
     */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

        saveButton.setText(R.string.save);

        Resources res = getResources();
        Configuration conf = res.getConfiguration();
        LocaleList lang = conf.getLocales();
        if(lang.get(0).getLanguage().equals("kn")){
            PHQScreeningScreen.isLanguageSelected = "kn";
        }else{
            PHQScreeningScreen.isLanguageSelected = "en";
        }

        isConfigChanged = true;
        if(phqQuestionnaireAdapter!=null){
            phqQuestionnaireAdapter.sendDataToActivity();
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void getPHQ9Questions(String message) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<QuestionAnswers>>(){}.getType();
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        if(jsonObject.get("message")!=null) {
            ArrayList<QuestionAnswers> questionAnswersArrayList;
            try {
                questionAnswersArrayList = gson.fromJson(jsonObject.get("message"), type);
                if (questionAnswersArrayList.size() > 0) {
                    questionAnswersArrayList.sort(Comparator.comparingInt(question -> Integer.parseInt(question.getQn_number())));
                    Log.i("SurveyScreen", "responseReceivedSuccessfully : " + questionAnswersArrayList);
                    questionArray = questionAnswersArrayList;
                    callServerToGetPHQ9Options();
                }
            } catch (Exception e) {
                Toast.makeText(context, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(context, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void getPHQ9Options(String message) {
        Gson gson = new Gson();
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        Type typeOptions = new TypeToken<ArrayList<QuestionOptions>>(){}.getType();
        if(jsonObject.get("message")!=null) {
            ArrayList<QuestionOptions> questionOptionsArrayList;
            try {
                questionOptionsArrayList = gson.fromJson(jsonObject.get("message"), typeOptions);
                if (questionOptionsArrayList.size() > 0) {
                    optionsArray = questionOptionsArrayList;
                    surveyStartDateTime = mithraUtility.getCurrentTime();
                    setRecyclerView();
                }
            } catch (Exception ex) {
                Toast.makeText(context, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(context, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void postPHQAnswers(String message) {
        Gson gson = new Gson();
        JsonObject jsonObjectRegistration = JsonParser.parseString(message).getAsJsonObject();
        Type typeFrappe = new TypeToken<ArrayList<RegisterParticipant>>(){}.getType();
        if(jsonObjectRegistration.get("message")!=null) {
            ArrayList<RegisterParticipant> frappeResponse;
            try{
                frappeResponse = gson.fromJson(jsonObjectRegistration.get("message"), typeFrappe);
                showDialogForPHQScreeningID(frappeResponse.get(0).getPhq_scr_id());
            }catch(Exception e){
                Toast.makeText(context, jsonObjectRegistration.get("message").toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void responseReceivedFailure(String message) {
        if(message!=null){
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            String serverErrorResponse = jsonObject.get("exception").toString();
            mithraUtility.showAppropriateMessages(context, serverErrorResponse);
        }else{
            Toast.makeText(context, context.getString(R.string.something_wrong), Toast.LENGTH_LONG).show();
        }
    }
}
