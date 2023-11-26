package com.example.nigel;

import org.junit.Test;

import static org.junit.Assert.*;

import com.github.mikephil.charting.data.Entry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TestBaby {

    @Test
    public void checkGetId(){
        List<Entry> list = new ArrayList<Entry>();
        Baby baby = new Baby(0, 0L, 40.5, "Group A",list);
        assertEquals(0, baby.getId());
    }
    @Test
    public void checkGetBirthDate(){
        List<Entry> list = new ArrayList<Entry>();
        Baby baby = new Baby(0, 0L, 40.5, "Group A",list);
        assertEquals(0L, baby.getBirthDate());
    }
    @Test
    public void checkGetWeight(){
        List<Entry> list = new ArrayList<Entry>();
        Baby baby = new Baby(0, 0L, 40.5, "Group A",list);
        assertEquals(40.5, baby.getWeight(),0.0);
    }
    @Test
    public void checkGetGroup(){
        List<Entry> list = new ArrayList<Entry>();
        Baby baby = new Baby(0, 0L, 40.5, "Group A",list);
        assertEquals("Group A", baby.getGroup());
    }
    @Test
    public void checkGetList(){
        List<Entry> list = new ArrayList<Entry>();
        List<Entry> list2 = new ArrayList<Entry>();
        Baby baby = new Baby(0, 0L, 40.5, "Group A",list);
        assertTrue(baby.getTimeSeriesData().equals(list2));
    }
    @Test
    public void checkGetBirthDateString(){
        List<Entry> list = new ArrayList<Entry>();
        long unix_seconds = 1372339860L;
        Baby baby = new Baby(0, 1372339860L, 40.5, "Group A",list);
        Date date = new Date(unix_seconds*1000L);
        SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        jdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        String java_date = jdf.format(date);
        System.out.println("\n"+java_date+"\n");
        assertEquals(baby.getBirthDateString(),"2013-06-27 09:31:00 GMT-04:00");
    }

    @Test
    public void checkSetId(){
        List<Entry> list = new ArrayList<Entry>();
        Baby baby = new Baby(0, 0L, 40.5, "Group A",list);
        baby.setId(4);
        assertEquals(4, baby.getId());
    }
    @Test
    public void checkSetBirthDate(){
        List<Entry> list = new ArrayList<Entry>();
        Baby baby = new Baby(0, 0L, 40.5, "Group A",list);
        baby.setBirthDate(4L);
        assertEquals(4L, baby.getBirthDate());
    }
    @Test
    public void checkSetWeight(){
        List<Entry> list = new ArrayList<Entry>();
        Baby baby = new Baby(0, 0L, 40.5, "Group A",list);
        baby.setWeight(60.5);
        assertEquals(60.5, baby.getWeight(),0.0);
    }
    @Test
    public void checkSetGroup(){
        List<Entry> list = new ArrayList<Entry>();
        Baby baby = new Baby(0, 0L, 40.5, "Group A",list);
        baby.setGroup("Group B");
        assertEquals("Group B", baby.getGroup());
    }
    @Test
    public void checkSetList(){
        List<Entry> list = new ArrayList<Entry>();
        List<Entry> list2 = new ArrayList<Entry>();
        List<Entry> list3 = new ArrayList<Entry>();
        Baby baby = new Baby(0, 0L, 40.5, "Group A",list);
        baby.setTimeSeriesData(list2);
        assertTrue(baby.getTimeSeriesData().equals(list3));
    }


}
