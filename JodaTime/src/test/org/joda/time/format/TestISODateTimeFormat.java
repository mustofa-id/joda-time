/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2004 Stephen Colebourne.  
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by the
 *        Joda project (http://www.joda.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The name "Joda" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact licence@joda.org.
 *
 * 5. Products derived from this software may not be called "Joda",
 *    nor may "Joda" appear in their name, without prior written
 *    permission of the Joda project.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE JODA AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Joda project and was originally 
 * created by Stephen Colebourne <scolebourne@joda.org>. For more
 * information on the Joda project, please see <http://www.joda.org/>.
 */
package org.joda.time.format;

import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;

/**
 * This class is a Junit unit test for ISODateTimeFormat.
 *
 * @author Stephen Colebourne
 */
public class TestISODateTimeFormat extends TestCase {

    private static final DateTimeZone UTC = DateTimeZone.UTC;
    private static final DateTimeZone PARIS = DateTimeZone.getInstance("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.getInstance("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.getInstance("Asia/Tokyo");

    long y2002days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 
                     366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 
                     365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
                     366 + 365;
    // 2002-06-09
    private long TEST_TIME_NOW =
            (y2002days + 31L + 28L + 31L + 30L + 31L + 9L -1L) * DateTimeConstants.MILLIS_PER_DAY;

    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestISODateTimeFormat.class);
    }

    public TestISODateTimeFormat(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
        originalDateTimeZone = null;
        originalTimeZone = null;
        originalLocale = null;
    }

    //-----------------------------------------------------------------------
    public void testFormat_date() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("2004-06-09", ISODateTimeFormat.getInstance().date().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("2004-06-09", ISODateTimeFormat.getInstance().date().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("2004-06-09", ISODateTimeFormat.getInstance().date().print(dt));
    }

    public void testFormat_time() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("10:20:30.040Z", ISODateTimeFormat.getInstance().time().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("11:20:30.040+01:00", ISODateTimeFormat.getInstance().time().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("12:20:30.040+02:00", ISODateTimeFormat.getInstance().time().print(dt));
    }

    public void testFormat_timeNoMillis() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("10:20:30Z", ISODateTimeFormat.getInstance().timeNoMillis().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("11:20:30+01:00", ISODateTimeFormat.getInstance().timeNoMillis().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("12:20:30+02:00", ISODateTimeFormat.getInstance().timeNoMillis().print(dt));
    }

    public void testFormat_tTime() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("T10:20:30.040Z", ISODateTimeFormat.getInstance().tTime().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("T11:20:30.040+01:00", ISODateTimeFormat.getInstance().tTime().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("T12:20:30.040+02:00", ISODateTimeFormat.getInstance().tTime().print(dt));
    }

    public void testFormat_tTimeNoMillis() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("T10:20:30Z", ISODateTimeFormat.getInstance().tTimeNoMillis().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("T11:20:30+01:00", ISODateTimeFormat.getInstance().tTimeNoMillis().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("T12:20:30+02:00", ISODateTimeFormat.getInstance().tTimeNoMillis().print(dt));
    }

    public void testFormat_dateTime() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("2004-06-09T10:20:30.040Z", ISODateTimeFormat.getInstance().dateTime().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("2004-06-09T11:20:30.040+01:00", ISODateTimeFormat.getInstance().dateTime().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("2004-06-09T12:20:30.040+02:00", ISODateTimeFormat.getInstance().dateTime().print(dt));
        
//        dt = dt.withZone(LONDON);
//        assertEquals("2004-06-09T11:20:30.040+01:00", ISODateTimeFormat.getInstance(PARIS).dateTime().print(dt));
//        
//        dt = dt.withZone(LONDON);
//        assertEquals("2004-06-09T12:20:30.040+02:00", ISODateTimeFormat.getInstance().dateTime().print(dt.getMillis(), PARIS));
//        
//        dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, Chronology.getCoptic());
//        assertEquals("2288-02-19T10:20:30.040Z", ISODateTimeFormat.getInstance().dateTime().print(dt));
//        
//        dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, Chronology.getCoptic());
//        assertEquals("2004-06-09T10:20:30.040Z", ISODateTimeFormat.getInstance(Chronology.getCoptic()).dateTime().print(dt));
    }

    public void testFormat_dateTimeNoMillis() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("2004-06-09T10:20:30Z", ISODateTimeFormat.getInstance().dateTimeNoMillis().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("2004-06-09T11:20:30+01:00", ISODateTimeFormat.getInstance().dateTimeNoMillis().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("2004-06-09T12:20:30+02:00", ISODateTimeFormat.getInstance().dateTimeNoMillis().print(dt));
    }

    public void testFormat_weekDate() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("2004-W24-3", ISODateTimeFormat.getInstance().weekDate().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("2004-W24-3", ISODateTimeFormat.getInstance().weekDate().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("2004-W24-3", ISODateTimeFormat.getInstance().weekDate().print(dt));
    }

    public void testFormat_weekDateTime() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("2004-W24-3T10:20:30.040Z", ISODateTimeFormat.getInstance().weekDateTime().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("2004-W24-3T11:20:30.040+01:00", ISODateTimeFormat.getInstance().weekDateTime().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("2004-W24-3T12:20:30.040+02:00", ISODateTimeFormat.getInstance().weekDateTime().print(dt));
    }

    public void testFormat_weekDateTimeNoMillis() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("2004-W24-3T10:20:30Z", ISODateTimeFormat.getInstance().weekDateTimeNoMillis().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("2004-W24-3T11:20:30+01:00", ISODateTimeFormat.getInstance().weekDateTimeNoMillis().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("2004-W24-3T12:20:30+02:00", ISODateTimeFormat.getInstance().weekDateTimeNoMillis().print(dt));
    }

    //-----------------------------------------------------------------------
    public void testFormat_basicDate() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("20040609", ISODateTimeFormat.getInstance().basicDate().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("20040609", ISODateTimeFormat.getInstance().basicDate().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("20040609", ISODateTimeFormat.getInstance().basicDate().print(dt));
    }

    public void testFormat_basicTime() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("102030.040Z", ISODateTimeFormat.getInstance().basicTime().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("112030.040+0100", ISODateTimeFormat.getInstance().basicTime().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("122030.040+0200", ISODateTimeFormat.getInstance().basicTime().print(dt));
    }

    public void testFormat_basicTimeNoMillis() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("102030Z", ISODateTimeFormat.getInstance().basicTimeNoMillis().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("112030+0100", ISODateTimeFormat.getInstance().basicTimeNoMillis().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("122030+0200", ISODateTimeFormat.getInstance().basicTimeNoMillis().print(dt));
    }

    public void testFormat_basicTTime() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("T102030.040Z", ISODateTimeFormat.getInstance().basicTTime().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("T112030.040+0100", ISODateTimeFormat.getInstance().basicTTime().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("T122030.040+0200", ISODateTimeFormat.getInstance().basicTTime().print(dt));
    }

    public void testFormat_basicTTimeNoMillis() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("T102030Z", ISODateTimeFormat.getInstance().basicTTimeNoMillis().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("T112030+0100", ISODateTimeFormat.getInstance().basicTTimeNoMillis().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("T122030+0200", ISODateTimeFormat.getInstance().basicTTimeNoMillis().print(dt));
    }

    public void testFormat_basicDateTime() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("20040609T102030.040Z", ISODateTimeFormat.getInstance().basicDateTime().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("20040609T112030.040+0100", ISODateTimeFormat.getInstance().basicDateTime().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("20040609T122030.040+0200", ISODateTimeFormat.getInstance().basicDateTime().print(dt));
    }

    public void testFormat_basicDateTimeNoMillis() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("20040609T102030Z", ISODateTimeFormat.getInstance().basicDateTimeNoMillis().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("20040609T112030+0100", ISODateTimeFormat.getInstance().basicDateTimeNoMillis().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("20040609T122030+0200", ISODateTimeFormat.getInstance().basicDateTimeNoMillis().print(dt));
    }

    public void testFormat_basicWeekDate() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("2004W243", ISODateTimeFormat.getInstance().basicWeekDate().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("2004W243", ISODateTimeFormat.getInstance().basicWeekDate().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("2004W243", ISODateTimeFormat.getInstance().basicWeekDate().print(dt));
    }

    public void testFormat_basicWeekDateTime() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("2004W243T102030.040Z", ISODateTimeFormat.getInstance().basicWeekDateTime().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("2004W243T112030.040+0100", ISODateTimeFormat.getInstance().basicWeekDateTime().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("2004W243T122030.040+0200", ISODateTimeFormat.getInstance().basicWeekDateTime().print(dt));
    }

    public void testFormat_basicWeekDateTimeNoMillis() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("2004W243T102030Z", ISODateTimeFormat.getInstance().basicWeekDateTimeNoMillis().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("2004W243T112030+0100", ISODateTimeFormat.getInstance().basicWeekDateTimeNoMillis().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("2004W243T122030+0200", ISODateTimeFormat.getInstance().basicWeekDateTimeNoMillis().print(dt));
    }

    //-----------------------------------------------------------------------
    public void testFormat_year() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("2004", ISODateTimeFormat.getInstance().year().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("2004", ISODateTimeFormat.getInstance().year().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("2004", ISODateTimeFormat.getInstance().year().print(dt));
    }

    public void testFormat_yearMonth() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("2004-06", ISODateTimeFormat.getInstance().yearMonth().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("2004-06", ISODateTimeFormat.getInstance().yearMonth().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("2004-06", ISODateTimeFormat.getInstance().yearMonth().print(dt));
    }

    public void testFormat_yearMonthDay() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("2004-06-09", ISODateTimeFormat.getInstance().yearMonthDay().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("2004-06-09", ISODateTimeFormat.getInstance().yearMonthDay().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("2004-06-09", ISODateTimeFormat.getInstance().yearMonthDay().print(dt));
    }

    public void testFormat_weekyear() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("2004", ISODateTimeFormat.getInstance().weekyear().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("2004", ISODateTimeFormat.getInstance().weekyear().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("2004", ISODateTimeFormat.getInstance().weekyear().print(dt));
    }

    public void testFormat_weekyearWeek() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("2004-W24", ISODateTimeFormat.getInstance().weekyearWeek().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("2004-W24", ISODateTimeFormat.getInstance().weekyearWeek().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("2004-W24", ISODateTimeFormat.getInstance().weekyearWeek().print(dt));
    }

    public void testFormat_weekyearWeekDay() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("2004-W24-3", ISODateTimeFormat.getInstance().weekyearWeekDay().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("2004-W24-3", ISODateTimeFormat.getInstance().weekyearWeekDay().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("2004-W24-3", ISODateTimeFormat.getInstance().weekyearWeekDay().print(dt));
    }

    //-----------------------------------------------------------------------
    public void testFormat_hour() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("10", ISODateTimeFormat.getInstance().hour().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("11", ISODateTimeFormat.getInstance().hour().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("12", ISODateTimeFormat.getInstance().hour().print(dt));
    }

    public void testFormat_hourMinute() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("10:20", ISODateTimeFormat.getInstance().hourMinute().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("11:20", ISODateTimeFormat.getInstance().hourMinute().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("12:20", ISODateTimeFormat.getInstance().hourMinute().print(dt));
    }

    public void testFormat_hourMinuteSecond() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("10:20:30", ISODateTimeFormat.getInstance().hourMinuteSecond().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("11:20:30", ISODateTimeFormat.getInstance().hourMinuteSecond().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("12:20:30", ISODateTimeFormat.getInstance().hourMinuteSecond().print(dt));
    }

    public void testFormat_hourMinuteSecondMillis() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("10:20:30.040", ISODateTimeFormat.getInstance().hourMinuteSecondMillis().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("11:20:30.040", ISODateTimeFormat.getInstance().hourMinuteSecondMillis().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("12:20:30.040", ISODateTimeFormat.getInstance().hourMinuteSecondMillis().print(dt));
    }

    public void testFormat_hourMinuteSecondFraction() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("10:20:30.040", ISODateTimeFormat.getInstance().hourMinuteSecondFraction().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("11:20:30.040", ISODateTimeFormat.getInstance().hourMinuteSecondFraction().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("12:20:30.040", ISODateTimeFormat.getInstance().hourMinuteSecondFraction().print(dt));
    }

    //-----------------------------------------------------------------------
    public void testFormat_dateHour() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("2004-06-09T10", ISODateTimeFormat.getInstance().dateHour().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("2004-06-09T11", ISODateTimeFormat.getInstance().dateHour().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("2004-06-09T12", ISODateTimeFormat.getInstance().dateHour().print(dt));
    }

    public void testFormat_dateHourMinute() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("2004-06-09T10:20", ISODateTimeFormat.getInstance().dateHourMinute().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("2004-06-09T11:20", ISODateTimeFormat.getInstance().dateHourMinute().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("2004-06-09T12:20", ISODateTimeFormat.getInstance().dateHourMinute().print(dt));
    }

    public void testFormat_dateHourMinuteSecond() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("2004-06-09T10:20:30", ISODateTimeFormat.getInstance().dateHourMinuteSecond().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("2004-06-09T11:20:30", ISODateTimeFormat.getInstance().dateHourMinuteSecond().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("2004-06-09T12:20:30", ISODateTimeFormat.getInstance().dateHourMinuteSecond().print(dt));
    }

    public void testFormat_dateHourMinuteSecondMillis() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("2004-06-09T10:20:30.040", ISODateTimeFormat.getInstance().dateHourMinuteSecondMillis().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("2004-06-09T11:20:30.040", ISODateTimeFormat.getInstance().dateHourMinuteSecondMillis().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("2004-06-09T12:20:30.040", ISODateTimeFormat.getInstance().dateHourMinuteSecondMillis().print(dt));
    }

    public void testFormat_dateHourMinuteSecondFraction() {
        DateTime dt = new DateTime(2004, 6, 9, 10, 20, 30, 40, UTC);
        assertEquals("2004-06-09T10:20:30.040", ISODateTimeFormat.getInstance().dateHourMinuteSecondFraction().print(dt));
        
        dt = dt.withZone(LONDON);
        assertEquals("2004-06-09T11:20:30.040", ISODateTimeFormat.getInstance().dateHourMinuteSecondFraction().print(dt));
        
        dt = dt.withZone(PARIS);
        assertEquals("2004-06-09T12:20:30.040", ISODateTimeFormat.getInstance().dateHourMinuteSecondFraction().print(dt));
    }

}