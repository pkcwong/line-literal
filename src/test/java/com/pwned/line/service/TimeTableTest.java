package com.pwned.line.service;

import com.pwned.line.web.MongoDB;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimeTableTest {
    public final static String testClassSchedule = "GO!\n" +
            "\n" +
            "KWAN, Wai Hin Eric 關偉軒\n" +
            " \t\t\t\t\t\t\t\t\t\t\t\n" +
            "\t\n" +
            "Search\n" +
            "\t\t\n" +
            "Plan\n" +
            "\t\t\n" +
            "Enroll\n" +
            "\t\t\n" +
            "My Academics\n" +
            "\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\n" +
            " \t\t\t\t\t\t\t\t\t\t\t\t\t\t\n" +
            "\t\n" +
            "my class schedule\n" +
            "\t\t\n" +
            "add\n" +
            "\t\t\n" +
            "drop\n" +
            "\t\t\n" +
            "swap\n" +
            "\t\t\n" +
            "term information\n" +
            "\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\n" +
            "My Class Schedule\n" +
            "List View\tWeekly Calendar View\n" +
            "Select Display Option\n" +
            "2017-18 Fall | Undergraduate | HKUST\n" +
            "Collapse section Class Schedule Filter Options \n" +
            "filter\n" +
            "Show Enrolled Classes\tShow Dropped Classes\tShow Waitlisted Classes\n" +
            "COMP 1991 - Industrial Experience\n" +
            "Status\tUnits\tGrading\tGrade\tDeadlines\n" +
            "Enrolled\t0.00\tGraded P, F or PP\t \tAcademic Calendar Deadlines\n" +
            "Class Nbr\tSection\tComponent\tDays & Times\tRoom\tInstructor\tStart/End Date\n" +
            "1789\tR1\tOthers\t \tTBA\tKWOK, James Tin Yau\t01/09/2017 - 30/11/2017\n" +
            "COMP 3021 - Java Programming\n" +
            "Status\tUnits\tGrading\tGrade\tDeadlines\n" +
            "Enrolled\t3.00\tGraded A+ to F\t \tAcademic Calendar Deadlines\n" +
            "Class Nbr\tSection\tComponent\tDays & Times\tRoom\tInstructor\tStart/End Date\n" +
            "1844\tL1\tLecture\tTuTh 10:30AM - 11:50AM\tRm 4619, Lift 31-32 (126)\tTSOI, Yau Chat\t01/09/2017 - 30/11/2017\n" +
            "1845\tLA1\tLaboratory\tWe 9:00AM - 9:50AM\tRm 4210, Lift 19 (67)\tTSOI, Yau Chat\t01/09/2017 - 30/11/2017\n" +
            "COMP 3111 - Software Engineering\n" +
            "Status\tUnits\tGrading\tGrade\tDeadlines\n" +
            "Enrolled\t4.00\tGraded A+ to F\t \tAcademic Calendar Deadlines\n" +
            "Class Nbr\tSection\tComponent\tDays & Times\tRoom\tInstructor\tStart/End Date\n" +
            "3767\tL2\tLecture\tTuTh 12:00PM - 1:20PM\tLecture Theater D (216)\tKIM, Sung Hun\t01/09/2017 - 30/11/2017\n" +
            "3768\tLA1\tLaboratory\tFr 11:00AM - 11:50AM\tRm 4210, Lift 19 (67)\tKIM, Sung Hun\t01/09/2017 - 30/11/2017\n" +
            "3772\tT2\tTutorial\tTh 5:00PM - 5:50PM\tRm 1104, Acad Concourse (120)\tKIM, Sung Hun\t01/09/2017 - 30/11/2017\n" +
            "COMP 3511 - Operating Systems\n" +
            "Status\tUnits\tGrading\tGrade\tDeadlines\n" +
            "Enrolled\t3.00\tGraded A+ to F\t \tAcademic Calendar Deadlines\n" +
            "Class Nbr\tSection\tComponent\tDays & Times\tRoom\tInstructor\tStart/End Date\n" +
            "1875\tL2\tLecture\tMoWe 10:30AM - 11:50AM\tRm 4619, Lift 31-32 (126)\tLI, Bo\t01/09/2017 - 30/11/2017\n" +
            "1883\tLA4\tLaboratory\tFr 1:00PM - 2:50PM\tRm 4214, Lift 19 (52)\tCHEN, Kai, \n" +
            "LI, Bo\t01/09/2017 - 30/11/2017\n" +
            "COMP 4900 - Academic & Prof Dev\n" +
            "Status\tUnits\tGrading\tGrade\tDeadlines\n" +
            "Enrolled\t0.00\tGraded P, F or PP\t \tAcademic Calendar Deadlines\n" +
            "Class Nbr\tSection\tComponent\tDays & Times\tRoom\tInstructor\tStart/End Date\n" +
            "1951\tT26\tTutorial\tWe 6:00PM - 6:50PM\tTBA\tMAK, Brian Kan Wing\t01/09/2017 - 30/11/2017\n" +
            "HUMA 1001A - Aristotle x Mencius\n" +
            "Status\tUnits\tGrading\tGrade\tDeadlines\n" +
            "Enrolled\t3.00\tGraded A+ to F\t \tAcademic Calendar Deadlines\n" +
            "Class Nbr\tSection\tComponent\tDays & Times\tRoom\tInstructor\tStart/End Date\n" +
            "2331\tL1\tLecture\tMoWe 12:00PM - 12:50PM\tLecture Theater A (400)\tYIP, Kam Ming\t01/09/2017 - 30/11/2017\n" +
            "2332\tT1\tTutorial\tMoWe 1:00PM - 1:20PM\tLecture Theater A (400)\tYIP, Kam Ming\t01/09/2017 - 30/11/2017\n" +
            "MATH 2011 - Intro to Multivar Calculu\n" +
            "Status\tUnits\tGrading\tGrade\tDeadlines\n" +
            "Enrolled\t3.00\tGraded A+ to F\t \tAcademic Calendar Deadlines\n" +
            "Class Nbr\tSection\tComponent\tDays & Times\tRoom\tInstructor\tStart/End Date\n" +
            "3162\tL2\tLecture\tMo 1:30PM - 2:50PM\tLecture Theater C (213)\tHO, Hon Ming\t01/09/2017 - 30/11/2017\n" +
            " \t \t \tFr 9:00AM - 10:20AM\tLecture Theater C (213)\tHO, Hon Ming\t01/09/2017 - 30/11/2017\n" +
            "3176\tT2D\tTutorial\tMo 9:30AM - 10:20AM\tRm 1505, Lift 25-26 (61)\tHO, Hon Ming\t01/09/2017 - 30/11/2017\n" +
            "Printer Friendly Page\n" +
            " \tSearch\t \t \t \tPlan\t \t \t \tEnroll\t \t \t \tMy Academics\t \t \n" +
            " \tMy Class Schedule\t \t \t \tAdd\t \t \t \tDrop\t \t \t \tSwap\t \t \t \tTerm Information\t \t \n" +
            " \n" +
            "GO!";
    public static final String expectedTimetable = "Here is your timetable:\n" +
            "\n" +
            "COMP 3021\n" +
            "Venue: Rm 4619, Lift 31-32 (126)\n" +
            "Day: Tuesday\n" +
            "Start Time: 10:30AM\n" +
            "End Time: 11:50AM\n" +
            "\n" +
            "COMP 3021\n" +
            "Venue: Rm 4619, Lift 31-32 (126)\n" +
            "Day: Thursday\n" +
            "Start Time: 10:30AM\n" +
            "End Time: 11:50AM\n" +
            "\n" +
            "COMP 3021\n" +
            "Venue: Rm 4210, Lift 19 (67)\n" +
            "Day: Wednesday\n" +
            "Start Time: 09:00AM\n" +
            "End Time: 09:50AM\n" +
            "\n" +
            "COMP 3111\n" +
            "Venue: Lecture Theater D (216)\n" +
            "Day: Tuesday\n" +
            "Start Time: 12:00PM\n" +
            "End Time: 01:20PM\n" +
            "\n" +
            "COMP 3111\n" +
            "Venue: Lecture Theater D (216)\n" +
            "Day: Thursday\n" +
            "Start Time: 12:00PM\n" +
            "End Time: 01:20PM\n" +
            "\n" +
            "COMP 3111\n" +
            "Venue: Rm 1104, Acad Concourse (120)\n" +
            "Day: Thursday\n" +
            "Start Time: 05:00PM\n" +
            "End Time: 05:50PM\n" +
            "\n" +
            "COMP 3111\n" +
            "Venue: Rm 4210, Lift 19 (67)\n" +
            "Day: Friday\n" +
            "Start Time: 11:00AM\n" +
            "End Time: 11:50AM\n" +
            "\n" +
            "COMP 3511\n" +
            "Venue: Rm 4619, Lift 31-32 (126)\n" +
            "Day: Monday\n" +
            "Start Time: 10:30AM\n" +
            "End Time: 11:50AM\n" +
            "\n" +
            "COMP 3511\n" +
            "Venue: Rm 4619, Lift 31-32 (126)\n" +
            "Day: Wednesday\n" +
            "Start Time: 10:30AM\n" +
            "End Time: 11:50AM\n" +
            "\n" +
            "COMP 3511\n" +
            "Venue: Rm 4214, Lift 19 (52)\n" +
            "Day: Friday\n" +
            "Start Time: 01:00PM\n" +
            "End Time: 02:50PM\n" +
            "\n" +
            "COMP 4900\n" +
            "Venue: TBA\n" +
            "Day: Wednesday\n" +
            "Start Time: 06:00PM\n" +
            "End Time: 06:50PM\n" +
            "\n" +
            "HUMA 1001A\n" +
            "Venue: Lecture Theater A (400)\n" +
            "Day: Monday\n" +
            "Start Time: 12:00PM\n" +
            "End Time: 12:50PM\n" +
            "\n" +
            "HUMA 1001A\n" +
            "Venue: Lecture Theater A (400)\n" +
            "Day: Wednesday\n" +
            "Start Time: 12:00PM\n" +
            "End Time: 12:50PM\n" +
            "\n" +
            "HUMA 1001A\n" +
            "Venue: Lecture Theater A (400)\n" +
            "Day: Monday\n" +
            "Start Time: 01:00PM\n" +
            "End Time: 01:20PM\n" +
            "\n" +
            "HUMA 1001A\n" +
            "Venue: Lecture Theater A (400)\n" +
            "Day: Wednesday\n" +
            "Start Time: 01:00PM\n" +
            "End Time: 01:20PM\n" +
            "\n" +
            "MATH 2011\n" +
            "Venue: Lecture Theater C (213)\n" +
            "Day: Monday\n" +
            "Start Time: 01:30PM\n" +
            "End Time: 02:50PM";
    @Before
    public void setUp() throws Exception {
        new MongoDB(System.getenv("MONGODB_URI")).drop("Timetable");
        Service service = new MasterController(new DefaultService("hi"));
        service.setParam("uid", "junit");
        service.setParam("replyToken", "junit");
        service.setParam("timestamp", "junit");
        service.resolve().get();
    }
    @Test
    public void DialogFlowTimetablePayload() throws Exception {
        Service service = new DialogFlowTimetable(new DefaultService("Show my Timetable"));
        service.setParam("uid", "junit");
        service.setParam("replyToken", "junit");
        service.setParam("timestamp", "junit");
        service.payload();
        assertEquals ("", new JSONObject(service.getParam("parameters").toString()).getString("oneByOneTrigger"));
        assertEquals ("timetable", new JSONObject(service.getParam("parameters").toString()).getString("timetableTrigger"));
        assertEquals ("", new JSONObject(service.getParam("parameters").toString()).getString("ReviewAdd"));

    }
    @Test
    public void TimeTableAddPayload() throws Exception {
        {
            Service service = new DialogFlowTimetable(new DefaultService("Add Timetable"));
            service.setParam("uid", "junit");
            service.setParam("replyToken", "junit");
            service.setParam("timestamp", "junit");
            service.payload();
            service = new TimeTable(service);
            service.payload();
            assertEquals("You can copy your timetable here: ", service.getFulfillment());
        }
        {
            Service service = new MasterController(new DefaultService(testClassSchedule));
            service.setParam("uid", "junit");
            service.setParam("replyToken", "junit");
            service.setParam("timestamp", "junit");
            Service result = service.resolve().get();
            assertEquals("Saved your Timetable", result.getFulfillment());
        }
        {
            Service service = new DialogFlowTimetable(new DefaultService("Timetable"));
            service.setParam("uid", "junit");
            service.setParam("replyToken", "junit");
            service.setParam("timestamp", "junit");
            service.payload();
            service = new TimeTable(service);
            service.payload();
            assertEquals(expectedTimetable, service.getFulfillment());
        }

    }
    @Test
    public void TimeTableAddOneByOnePayload() throws Exception {
        {
            Service service = new DialogFlowTimetable(new DefaultService("Add Timetable one by one"));
            service.setParam("uid", "junit");
            service.setParam("replyToken", "junit");
            service.setParam("timestamp", "junit");
            service.payload();
            service = new TimeTable(service);
            service.payload();
            assertEquals("You can add your timetable here one by one: \n" +
                    "***Please follow the following format:\n" +
                    "course code(e.g COMP3111),section number1(e.g. 2632),section number 2,...", service.getFulfillment());
        }
        {
            Service service = new MasterController(new DefaultService("COMP3111,3767,3768,3772"));
            service.setParam("uid", "junit");
            service.setParam("replyToken", "junit");
            service.setParam("timestamp", "junit");
            Service result = service.resolve().get();
            assertEquals("Saved the course to your timetable, if you want to insert others, please do it again. If not, then type addTimetable::end ", result.getFulfillment());
        }
        {
            Service service = new MasterController(new DefaultService("HUMA1001A,2331,2332"));
            service.setParam("uid", "junit");
            service.setParam("replyToken", "junit");
            service.setParam("timestamp", "junit");
            Service result = service.resolve().get();
            assertEquals("Saved the course to your timetable, if you want to insert others, please do it again. If not, then type addTimetable::end ", result.getFulfillment());
        }
        {
            Service service = new MasterController(new DefaultService("addtimetable::end"));
            service.setParam("uid", "junit");
            service.setParam("replyToken", "junit");
            service.setParam("timestamp", "junit");
            service.payload();
            service = new TimeTableAddOneByOne(service);
            service.payload();
            assertEquals("Finish adding course to your Timetable :)", service.getFulfillment());
        }

    }

    @Test
    public void TimeTableAddChain() throws Exception {
        Service service = new TimeTableAdd(new DefaultService(""));
        assertEquals(service, service.chain());
    }
    @Test
    public void TimetableChain() throws Exception {
        Service service = new TimeTable(new DefaultService(""));
        assertEquals(service, service.chain());
    }

}