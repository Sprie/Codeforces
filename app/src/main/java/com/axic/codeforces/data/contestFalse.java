package com.axic.codeforces.data;

import java.util.List;

/**
 * Created by 59786 on 2016/3/23.
 */
public class contestFalse {

    /**

     */

    private String status;
    /**
     * id : 643
     * name : VK Cup 2016 - Round 3
     * type : CF
     * phase : BEFORE
     * frozen : false
     * durationSeconds : 7200
     * startTimeSeconds : 1462638900
     * relativeTimeSeconds : -4505146
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
        private String id;
        private String name;
        private String type;

        public String getPhase() {
            return phase;
        }

        public void setPhase(String phase) {
            this.phase = phase;
        }

        private String phase;

        public String getId() {
            return id;
        }

        public void setId(String id) {
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
    }
}
