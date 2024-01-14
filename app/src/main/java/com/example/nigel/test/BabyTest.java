package com.example.nigel.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.nigel.Baby;
import com.example.nigel.dataclasses.BloodSample;
import com.example.nigel.dataclasses.DataSample;
import com.example.nigel.dataclasses.SweatSample;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BabyTest {

    private Baby baby;
    private List<DataSample> dataset;
    private Baby babyForDatabase;
    private Baby babyForDataset;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Before
    public void setUp() {
        dataset = new ArrayList<>();
        dataset.add(new BloodSample(1l, 1, 1.4f, 1.7f));

        baby = new Baby(1, 30.5, LocalDate.of(2022, 1, 1), 3.2, "Notes", dataset);
        babyForDatabase = new Baby(2, 28.0, "2022-02-15", 2.8, "Database Notes");
        babyForDataset = new Baby(3, 32.0, LocalDate.of(2022, 3, 1), 3.5, "Dataset Notes", null);

    }

    @Test
    public void testConstructorWithGraphingNotNull() {
        assertNotNull(baby);
    }

    @Test
    public void testConstructorWithGraphingId(){
        assertEquals(1, baby.getId());
    }
    @Test
    public void testConstructorWithGraphingGAge(){
        assertEquals(30.5, baby.getGestationalAge());
    }
    @Test
    public void testConstructorWithGraphingDateOfBirth(){
        assertNotNull(baby.getDateOfBirth());
    }
    @Test
    public void testConstructorWithGraphingWeight(){
        assertEquals(3.2, baby.getWeight(), 0.01);
    }
    @Test
    public void testConstructorWithGraphingNotes(){
        assertEquals("Notes", baby.getNotes());
    }
    @Test
    public void testConstructorWithGraphingTimeSeriesDataNotNull(){
        assertNotNull(baby.getTimeSeriesData());
    }
    @Test
    public void testConstructorWithGraphingTimeSeriesDataSize(){
        assertEquals(1, baby.getTimeSeriesData().size());
    }

    @Test
    public void testConstructorForDatabase() {
        assertNotNull(babyForDatabase);
    }
    @Test
    public void testConstructorForDatabaseId() {
        assertEquals(2, babyForDatabase.getId());
    }
    @Test
    public void testConstructorForDatabaseGAge() {
        assertEquals(28, babyForDatabase.getGestationalAge());
    }
    @Test
    public void testConstructorForDatabaseBirthday() {
        assertEquals("2022-02-15", babyForDatabase.getBirthday());
    }
    @Test
    public void testConstructorForDatabaseWeight() {
        assertEquals(2.8, babyForDatabase.getWeight(), 0.01);
    }
    @Test
    public void testConstructorForDatabaseNotes() {
        assertEquals("Database Notes", babyForDatabase.getNotes());
    }
    @Test
    public void testConstructorForDatabaseListNotNull() {
        assertNotNull(babyForDatabase.getTimeSeriesData());
    }
    @Test
    public void testConstructorForDatabaseListEmpty() {
        assertEquals(0, babyForDatabase.getTimeSeriesData().size());

    }
    @Test
    public void testConstructorForDataset() {
        assertNotNull(babyForDataset);
    }
    @Test
    public void testConstructorForDatasetId() {
        assertEquals(3, babyForDataset.getId());
    }
    @Test
    public void testConstructorForDatasetGAge() {
        assertEquals(32.0, babyForDataset.getGestationalAge(), 0.01);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Test
    public void testConstructorForDatasetDateofBirth() {
        assertEquals(LocalDate.of(2022, 3, 1), babyForDataset.getDateOfBirth());
    }
    @Test
    public void testConstructorForDatasetWeight() {
        assertEquals(3.5, babyForDataset.getWeight(), 0.01);
    }
    @Test
    public void testConstructorForDatasetNotes() {
        assertEquals("Dataset Notes", babyForDataset.getNotes());
    }
    @Test
    public void testConstructorForDatasetTimeSeriesDataNotNull() {
        assertNotNull(babyForDataset.getTimeSeriesData());
    }
    @Test
    public void testConstructorForDatasetTimeSeriesDataSize() {
        assertEquals(0, babyForDataset.getTimeSeriesData().size());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Test
    public void testGettersAndSettersId() {
        baby.setId(4);
        assertEquals(4, baby.getId());
    }
    @Test
    public void testGettersAndSettersAge() {
        baby.setGestationalAge(33.5);
        assertEquals(33.5, baby.getGestationalAge(), 0.01);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Test
    public void testGettersAndSettersLocalDate() {
        LocalDate dateOfBirth = LocalDate.of(2022, 4, 1);
        baby.setDateOfBirth(dateOfBirth);
        assertEquals(dateOfBirth, baby.getDateOfBirth());
    }
    @Test
    public void testGettersAndSettersBirthday() {
        baby.setBirthday("2022-04-01");
        assertEquals("2022-04-01", baby.getBirthday());
    }
    @Test
    public void testGettersAndSettersWeight() {
        baby.setWeight(4.0);
        assertEquals(4.0, baby.getWeight(), 0.01);
    }
    @Test
    public void testGettersAndSettersNotes() {
        baby.setNotes("Updated Notes");
        assertEquals("Updated Notes", baby.getNotes());
    }
    @Test
    public void testGettersAndSettersTimeSeriesData() {
        babyForDataset.setTimeSeriesData(new ArrayList<>());
        assertNotNull(babyForDataset.getTimeSeriesData());
        assertEquals(0, babyForDataset.getTimeSeriesData().size());
    }

    @Test
    public void testInsertEvent() {
        SweatSample newDataSample = new SweatSample(4L, 5,5.4f, 2.3f, 5f, 7.5f, 8.44f, 9.2f );
        baby.insertEvent(newDataSample);

        assertNotNull(baby.getTimeSeriesData());
        assertEquals(2, baby.getTimeSeriesData().size());
        assertEquals(newDataSample, baby.getTimeSeriesData().get(1));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Test
    public void testGetAge() {
        assertEquals("2 years, 1 months, 8 days", baby.getAgeForTest());
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Test
    public void testGetAge2() {
        assertEquals("1 years, 11 months, 25 days", babyForDatabase.getAgeForTest());
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Test
    public void testGetAge3() {
        assertEquals("1 years, 11 months, 8 days", babyForDataset.getAgeForTest());
    }

    @Test
    public void testDateOfBirthToString() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            assertEquals("2022-01-01", baby.dateOfBirthToString());
        }
    }
}
