package Model;

import Helper.Helper;

import java.util.Calendar;
import java.util.Date;

public class DateInterval {

    private Date start;
    private Date end;
    private int price;
    private int id;


    public DateInterval(Calendar start, Calendar end, int price, int id) {
        this.start = Helper.getDateWithoutTimeUsingCalendar(start);
        this.end = Helper.getDateWithoutTimeUsingCalendar(end);
        this.price = Math.max(0, price);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = Math.max(0, price);
    }


}
