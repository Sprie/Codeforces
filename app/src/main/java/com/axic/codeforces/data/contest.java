package com.axic.codeforces.data;

import java.util.List;

/**
 * Created by 59786 on 2016/3/21.
 */
public class contest {


    /**
     *
     */

    private String status;
    /**
     * id : 100001
     * name : 2010 Codeforces Beta Round #1 (as training)
     * type : ICPC
     * phase : FINISHED
     * frozen : false
     * durationSeconds : 7200
     * description : This is the only contest for testing Codeforces::Gym. As you participate in any other training, you guarantee that you solve problems without assistance and that you do not send other people's solutions.
     * difficulty : 3
     * kind : Official International Personal Contest
     * country : Russia
     * city : Saratov
     * season : 2010-2011
     */

    private List<ResultBean> result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        private int id;
        private String name;
        private String type;
        private String phase;
        private boolean frozen;
        private int durationSeconds;
        private String description;
        private int difficulty;
        private String kind;
        private String country;
        private String city;
        private String season;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPhase() {
            return phase;
        }

        public void setPhase(String phase) {
            this.phase = phase;
        }

        public boolean isFrozen() {
            return frozen;
        }

        public void setFrozen(boolean frozen) {
            this.frozen = frozen;
        }

        public int getDurationSeconds() {
            return durationSeconds;
        }

        public void setDurationSeconds(int durationSeconds) {
            this.durationSeconds = durationSeconds;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getDifficulty() {
            return difficulty;
        }

        public void setDifficulty(int difficulty) {
            this.difficulty = difficulty;
        }

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getSeason() {
            return season;
        }

        public void setSeason(String season) {
            this.season = season;
        }
    }
}
