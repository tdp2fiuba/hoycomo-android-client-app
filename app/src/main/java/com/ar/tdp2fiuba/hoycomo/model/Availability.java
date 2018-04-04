package com.ar.tdp2fiuba.hoycomo.model;

public class Availability {
    private DailyTimeWindow sunday;
    private DailyTimeWindow monday;
    private DailyTimeWindow tuesday;
    private DailyTimeWindow wednesday;
    private DailyTimeWindow thursday;
    private DailyTimeWindow friday;
    private DailyTimeWindow saturday;

    public Availability(DailyTimeWindow sunday, DailyTimeWindow monday, DailyTimeWindow tuesday,
                        DailyTimeWindow wednesday, DailyTimeWindow thursday, DailyTimeWindow friday,
                        DailyTimeWindow saturday) {
        this.sunday = sunday;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
    }

    public DailyTimeWindow getSunday() {
        return sunday;
    }

    public DailyTimeWindow getMonday() {
        return monday;
    }

    public DailyTimeWindow getTuesday() {
        return tuesday;
    }

    public DailyTimeWindow getWednesday() {
        return wednesday;
    }

    public DailyTimeWindow getThursday() {
        return thursday;
    }

    public DailyTimeWindow getFriday() {
        return friday;
    }

    public DailyTimeWindow getSaturday() {
        return saturday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Availability that = (Availability) o;

        if (sunday != null ? !sunday.equals(that.sunday) : that.sunday != null) return false;
        if (monday != null ? !monday.equals(that.monday) : that.monday != null) return false;
        if (tuesday != null ? !tuesday.equals(that.tuesday) : that.tuesday != null) return false;
        if (wednesday != null ? !wednesday.equals(that.wednesday) : that.wednesday != null)
            return false;
        if (thursday != null ? !thursday.equals(that.thursday) : that.thursday != null)
            return false;
        if (friday != null ? !friday.equals(that.friday) : that.friday != null) return false;
        return saturday != null ? saturday.equals(that.saturday) : that.saturday == null;
    }

    @Override
    public int hashCode() {
        int result = sunday != null ? sunday.hashCode() : 0;
        result = 31 * result + (monday != null ? monday.hashCode() : 0);
        result = 31 * result + (tuesday != null ? tuesday.hashCode() : 0);
        result = 31 * result + (wednesday != null ? wednesday.hashCode() : 0);
        result = 31 * result + (thursday != null ? thursday.hashCode() : 0);
        result = 31 * result + (friday != null ? friday.hashCode() : 0);
        result = 31 * result + (saturday != null ? saturday.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Availability{" +
                "sunday=" + sunday +
                ", monday=" + monday +
                ", tuesday=" + tuesday +
                ", wednesday=" + wednesday +
                ", thursday=" + thursday +
                ", friday=" + friday +
                ", saturday=" + saturday +
                '}';
    }
}
