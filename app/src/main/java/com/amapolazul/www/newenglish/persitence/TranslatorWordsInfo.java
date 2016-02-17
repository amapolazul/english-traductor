package com.amapolazul.www.newenglish.persitence;

/**
 * Created by jsmartinez on 17/03/2015.
 */
public class TranslatorWordsInfo {

    private Long _id;
    private String espanol_def;
    private String english_def;
    private String english_audio_path;
    private String espanol_audio_path;

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getEspanol_def() {
        return espanol_def;
    }

    public void setEspanol_def(String espanol_def) {
        this.espanol_def = espanol_def;
    }

    public String getEnglish_def() {
        return english_def;
    }

    public void setEnglish_def(String english_def) {
        this.english_def = english_def;
    }

    public String getEnglish_audio_path() {
        return english_audio_path;
    }

    public void setEnglish_audio_path(String english_audio_path) {
        this.english_audio_path = english_audio_path;
    }

    public String getEspanol_audio_path() {
        return espanol_audio_path;
    }

    public void setEspanol_audio_path(String espanol_audio_path) {
        this.espanol_audio_path = espanol_audio_path;
    }

    @Override
    public String toString() {
        return "TransatorWordsInfo{" +
                "_id='" + _id + '\'' +
                ", espanol_def='" + espanol_def + '\'' +
                ", english_def='" + english_def + '\'' +
                ", english_audio_path='" + english_audio_path + '\'' +
                ", espanol_audio_path='" + espanol_audio_path + '\'' +
                '}';
    }
}