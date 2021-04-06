/**
 * Mitchell Ibarra
 * Southern New Hampshire University
 * CS 350
 *
 * @description Class QuestionEditActivity is started by QuestionActivity intent which may or may
 * not contain a question ID. If question ID is present, the user wants to edit an existing question
 * and if it is not present then the user wants to add a new question.
 */
package com.cs360.studyhelper;

import android.content.Intent;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class QuestionEditActivity extends AppCompatActivity {

    public static final String EXTRA_QUESTION_ID = "com.cs360.studyhelper.question_id";
    public static final String EXTRA_SUBJECT = "com.cs360.studyhelper.subject";

    private EditText mQuestionText;
    private EditText mAnswerText;

    private StudyDatabase mStudyDb;
    private long mQuestionId;
    private Question mQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_edit);

        mQuestionText = findViewById(R.id.questionText);
        mAnswerText = findViewById(R.id.answerText);

        mStudyDb = StudyDatabase.getInstance(getApplicationContext());

        // Get question ID from QuestionActivity
        Intent intent = getIntent();
        mQuestionId = intent.getLongExtra(EXTRA_QUESTION_ID, -1);

        ActionBar actionBar = getSupportActionBar();

        if (mQuestionId == -1) {
            // Add new question
            mQuestion = new Question();
            setTitle(R.string.add_question);
        }
        else {
            // Update existing question
            mQuestion = mStudyDb.getQuestion(mQuestionId);
            mQuestionText.setText(mQuestion.getText());
            mAnswerText.setText(mQuestion.getAnswer());
            setTitle(R.string.update_question);
        }

        String subject = intent.getStringExtra(EXTRA_SUBJECT);
        mQuestion.setSubject(subject);
    }

    public void saveButtonClick(View view) {

        mQuestion.setText(mQuestionText.getText().toString());
        mQuestion.setAnswer(mAnswerText.getText().toString());

        if (mQuestionId == -1) {
            // New question
            mStudyDb.addQuestion(mQuestion);
        } else {
            // Existing question
            mStudyDb.updateQuestion(mQuestion);
        }

        // Send back question ID
        Intent intent = new Intent();
        intent.putExtra(EXTRA_QUESTION_ID, mQuestion.getId());
        setResult(RESULT_OK, intent);
        finish();
    }
}