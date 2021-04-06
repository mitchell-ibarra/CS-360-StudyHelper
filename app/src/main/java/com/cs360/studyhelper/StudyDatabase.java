/**
 * Mitchell Ibarra
 * Southern New Hampshire University
 * CS 350
 *
 * @description Singleton Class StudyDatabase.java creates a subjects table with columns that match
 * the Subject class's properties and creates a questions table with columns that match the question
 * class's properties. This class also sets up the initial database for subjects and questions.
 */
package com.cs360.studyhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import java.util.ArrayList;
import java.util.List;

public class StudyDatabase extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "study.db";

    private static StudyDatabase mStudyDb;

    public enum SubjectSortOrder {
        eALPHABETIC, eUPDATE_DESC, eUPDATE_ASC
    };

    // Retrieve the singleton instance of this class. Creates a new instance if one does not exist.
    public static StudyDatabase getInstance(Context context) {
        if (mStudyDb == null) {
            mStudyDb = new StudyDatabase(context);
        }
        return mStudyDb;
    }


    private StudyDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    // Setting up Subjects table format
    private static final class SubjectTable {
        private static final String TABLE = "subjects";
        private static final String COL_TEXT = "text";
        private static final String COL_UPDATE_TIME = "updated";
    }

    // Setting up questions table format
    private static final class QuestionTable {
        private static final String TABLE = "questions";
        private static final String COL_ID = "_id";
        private static final String COL_TEXT = "text";
        private static final String COL_ANSWER = "answer";
        private static final String COL_SUBJECT = "subject";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create subjects table
        db.execSQL("create table " + SubjectTable.TABLE + " (" +
                SubjectTable.COL_TEXT + " primary key, " +
                SubjectTable.COL_UPDATE_TIME + " int)");

        // Create questions table with foreign key that cascade deletes
        db.execSQL("create table " + QuestionTable.TABLE + " (" +
                QuestionTable.COL_ID + " integer primary key autoincrement, " +
                QuestionTable.COL_TEXT + ", " +
                QuestionTable.COL_ANSWER + ", " +
                QuestionTable.COL_SUBJECT + ", " +
                "foreign key(" + QuestionTable.COL_SUBJECT + ") references " +
                SubjectTable.TABLE + "(" + SubjectTable.COL_TEXT + ") on delete cascade)");

        // Add some subjects
        String[] subjects = { "History", "Math", "Computing" };
        for (String sub: subjects) {
            Subject subject = new Subject(sub);
            ContentValues values = new ContentValues();
            values.put(SubjectTable.COL_TEXT, subject.getText());
            values.put(SubjectTable.COL_UPDATE_TIME, subject.getUpdateTime());
            db.insert(SubjectTable.TABLE, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + SubjectTable.TABLE);
        db.execSQL("drop table if exists " + QuestionTable.TABLE);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                db.execSQL("pragma foreign_keys = on;");
            } else {
                db.setForeignKeyConstraintsEnabled(true);
            }
        }
    }

    /**
     * Return a list of subjects by the specified subject order enum.
     * @param order SubjectSortOrder Enum that specifies what order to sort the subjects list.
     * @return List<Subject> subjects The ordered list of subjects.
     */
    public List<Subject> getSubjects(SubjectSortOrder order) {
        List<Subject> subjects = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String orderBy;
        switch (order) {
            case eALPHABETIC:
                orderBy = SubjectTable.COL_TEXT + " collate nocase";
                break;
            case eUPDATE_DESC:
                orderBy = SubjectTable.COL_UPDATE_TIME + " desc";
                break;
            default:
                orderBy = SubjectTable.COL_UPDATE_TIME + " asc";
                break;
        }

        // Accessing the database to retrieve Subject data from SubjectTable
        // Creates new Subject objects and adds them to a list to be returned
        String sql = "select * from " + SubjectTable.TABLE + " order by " + orderBy;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                Subject subject = new Subject();
                subject.setText(cursor.getString(0));
                subject.setUpdateTime(cursor.getLong(1));
                subjects.add(subject);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return subjects;
    }

    /**
     * Adds a new subject to the database.
     * @param subject Subject The subject to be added.
     * @return boolean <code>true</code> if successfully added Subject to database
     *                 <code>false</code> if unsuccessful attempt to add Subject to database.
     */
    public boolean addSubject(Subject subject) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SubjectTable.COL_TEXT, subject.getText());
        values.put(SubjectTable.COL_UPDATE_TIME, subject.getUpdateTime());
        long id = db.insert(SubjectTable.TABLE, null, values);
        return id != -1;
    }

    /**
     * Update a specific Subject with the text and update time.
     * @param subject Subject The subject to be updated.
     */
    public void updateSubject(Subject subject) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SubjectTable.COL_TEXT, subject.getText());
        values.put(SubjectTable.COL_UPDATE_TIME, subject.getUpdateTime());
        db.update(SubjectTable.TABLE, values,
                SubjectTable.COL_TEXT + " = ?", new String[] { subject.getText() });
    }

    /**
     * Delete a specific Subject.
     * @param subject Subject The subject to be deleted from database.
     */
    public void deleteSubject(Subject subject) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(SubjectTable.TABLE,
                SubjectTable.COL_TEXT + " = ?", new String[] { subject.getText() });
    }

    /**
     * Return a list of questions by the subject.
     * @param subject String The subject where questions should be pulled from.
     * @return List<Question> questins The ordered list of questions.
     */
    public List<Question> getQuestions(String subject) {
        List<Question> questions = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + QuestionTable.TABLE +
                " where " + QuestionTable.COL_SUBJECT + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[] { subject });
        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(cursor.getInt(0));
                question.setText(cursor.getString(1));
                question.setAnswer(cursor.getString(2));
                question.setSubject(cursor.getString(3));
                questions.add(question);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return questions;
    }

    /**
     * Return a specific question by the question ID.
     * @param questionId long The id for which question to retrieve from database.
     * @return Question question The Question returned from the database.
     */
    public Question getQuestion(long questionId) {
        Question question = null;

        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from " + QuestionTable.TABLE +
                " where " + QuestionTable.COL_ID + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[] { Float.toString(questionId) });

        if (cursor.moveToFirst()) {
            question = new Question();
            question.setId(cursor.getInt(0));
            question.setText(cursor.getString(1));
            question.setAnswer(cursor.getString(2));
            question.setSubject(cursor.getString(3));
        }

        return question;
    }

    /**
     * Add a new question to the database.
     * @param question Question The question to be added to the database.
     */
    public void addQuestion(Question question) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(QuestionTable.COL_TEXT, question.getText());
        values.put(QuestionTable.COL_ANSWER, question.getAnswer());
        values.put(QuestionTable.COL_SUBJECT, question.getSubject());
        long questionId = db.insert(QuestionTable.TABLE, null, values);
        question.setId(questionId);

        // Change update time in subjects table
        updateSubject(new Subject(question.getSubject()));
    }

    /**
     * Update a speific question in the database.
     * @param question Question The updated question.
     */
    public void updateQuestion(Question question) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(QuestionTable.COL_ID, question.getId());
        values.put(QuestionTable.COL_TEXT, question.getText());
        values.put(QuestionTable.COL_ANSWER, question.getAnswer());
        values.put(QuestionTable.COL_SUBJECT, question.getSubject());
        db.update(QuestionTable.TABLE, values,
                QuestionTable.COL_ID + " = " + question.getId(), null);

        // Change update time in subjects table
        updateSubject(new Subject(question.getSubject()));
    }

    /**
     * Delete a specific question from database.
     * @param questionId long The id to search for which question to delete.
     */
    public void deleteQuestion(long questionId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(QuestionTable.TABLE,
                QuestionTable.COL_ID + " = " + questionId, null);
    }
}
