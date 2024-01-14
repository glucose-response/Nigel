package com.example.nigel.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import static java.lang.invoke.MethodHandles.catchException;

import com.example.nigel.dataclasses.DebugTimestampConverter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;

import java.text.ParseException;

public class DebugTimestampConverterTest {

    long timestamp;
    long timestamp2;

    @Rule
    public final ExpectedException exception = ExpectedException.none();
    @BeforeEach
    void setUp() {
        timestamp = DebugTimestampConverter.convertToUnixTimestamp("01/01/2020, 00:00");
        timestamp2 = DebugTimestampConverter.convertToUnixTimestamp("01/06/2022, 17:23");
    }

    @Test
    void checkNotNull() {
        assertNotEquals((float) timestamp, (Float) null);
    }

    @Test
    void checkTrue(){
        assertEquals(timestamp, 1577836800*1000L);
    }

    @Test
    void checkTrue2(){
        assertEquals(timestamp2, 1654104180*1000L);
    }

    /**
     * Sourced from stackoverflow
     * https://stackoverflow.com/questions/156503/how-do-you-assert-that-a-certain-exception-is-thrown-in-junit-tests
     */
    @Test
    void catchAnException(){
        exception.expect(ParseException.class);
        exception.expectMessage("Unparseable date: \"01/06/2022, 173\"");
        DebugTimestampConverter.convertToUnixTimestamp("01/06/2022, 173");

    }
    @Test
    void expectMinusWithinException(){
        assertEquals(DebugTimestampConverter.convertToUnixTimestamp("01/06/2022, 173"),-1);

    }
}
