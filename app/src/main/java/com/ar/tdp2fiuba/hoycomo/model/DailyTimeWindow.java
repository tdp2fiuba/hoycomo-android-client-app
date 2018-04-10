package com.ar.tdp2fiuba.hoycomo.model;

import com.ar.tdp2fiuba.hoycomo.utils.DateUtils;

public class DailyTimeWindow {
    private String startTime;
    private String endTime;

    public DailyTimeWindow(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getStartTime() {
        return DateUtils.formatTime(startTime);
    }

    public String getEndTime() {
        return DateUtils.formatTime(endTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DailyTimeWindow that = (DailyTimeWindow) o;

        if (startTime != null ? !startTime.equals(that.startTime) : that.startTime != null)
            return false;
        return endTime != null ? endTime.equals(that.endTime) : that.endTime == null;
    }

    @Override
    public int hashCode() {
        int result = startTime != null ? startTime.hashCode() : 0;
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DailyTimeWindow{" +
                "startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
