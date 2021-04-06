/**
 * Mitchell Ibarra
 * Southern New Hampshire University
 * CS 350
 *
 * @description Model Class Subject.java stores Subject text and time when Subject was last updated.
 */
package com.cs360.studyhelper;

public class Subject {

    private String mText;
    private long mUpdateTime;

    public Subject() {
        //intentionally left blank
    }

    /**
     * Creates new Instance of <code>Subject</code>
     * Sets the subject's name and the updated time to current system time.
     * @param text String The text of the subject.
     */
    public Subject(String text) {
        mText = text;
        mUpdateTime = System.currentTimeMillis();
    }

    /**
     * Retrieve subject name.
     * @return String mText Name of subject.
     */
    public String getText() {
        return mText;
    }

    /**
     * Sets the name of the subject.
     * @param text String Text to set for subject name.
     */
    public void setText(String text) {
        mText = text;
    }

    /**
     * Get the time when the subject was last updated.
     * @return long mUpdateTime Time in milliseconds when subject was last updated.
     */
    public long getUpdateTime() {
        return mUpdateTime;
    }

    /**
     * Set the time when subject was updated in ms.
     * @param updateTime long Time when subject was updated in ms.
     */
    public void setUpdateTime(long updateTime) {
        mUpdateTime = updateTime;
    }
}