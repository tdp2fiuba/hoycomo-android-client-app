package com.ar.tdp2fiuba.hoycomo.model;

import android.support.annotation.Nullable;

public class DelayTime {
    @Nullable private Integer min;
    private Integer max;

    public DelayTime(Integer maxDelayTime) {
        this.min = null;
        this.max = maxDelayTime;
    }

    public DelayTime(@Nullable Integer min, Integer max) {
        this.min = min;
        this.max = max;
    }

    public Integer getMin() {
        return min;
    }

    public Integer getMax() {
        return max;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DelayTime delayTime = (DelayTime) o;

        if (min != null ? !min.equals(delayTime.min) : delayTime.min != null) return false;
        return max != null ? max.equals(delayTime.max) : delayTime.max == null;
    }

    @Override
    public int hashCode() {
        int result = min != null ? min.hashCode() : 0;
        result = 31 * result + (max != null ? max.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DelayTime{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}
