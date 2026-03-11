package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size());
        assertEquals(singleTrainingSession, mondaySessions.get(0));

        // Проверить, что за вторник не вернулось занятий
        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, mondaySessions.size());
        assertEquals(mondayChildTrainingSession, mondaySessions.get(0));

        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        List<TrainingSession> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, thursdaySessions.size());
        assertEquals(thursdayChildTrainingSession, thursdaySessions.get(0)); // 13:00
        assertEquals(thursdayAdultTrainingSession, thursdaySessions.get(1)); // 20:00

        // Проверить, что за вторник не вернулось занятий
        List<TrainingSession> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        // Проверить, что за понедельник в 13:00 вернулось одно занятие
        List<TrainingSession> monday13 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        assertEquals(1, monday13.size());
        assertEquals(singleTrainingSession, monday13.get(0));

        // Проверить, что за понедельник в 14:00 не вернулось занятий
        List<TrainingSession> monday14 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        assertTrue(monday14.isEmpty());
    }

    // Новые тесты для метода getCountByCoaches

    @Test
    void testGetCountByCoachesEmpty() {
        Timetable timetable = new Timetable();
        List<Timetable.CoachStats> stats = timetable.getCountByCoaches();
        assertTrue(stats.isEmpty());
    }

    @Test
    void testGetCountByCoachesSingleCoach() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Иванов", "Иван", "Иванович");
        Group group = new Group("Йога", Age.ADULT, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(10, 0)));

        List<Timetable.CoachStats> stats = timetable.getCountByCoaches();
        assertEquals(1, stats.size());
        assertEquals(coach, stats.get(0).getCoach());
        assertEquals(3, stats.get(0).getCount());
    }

    @Test
    void testGetCountByCoachesMultipleCoaches() {
        Timetable timetable = new Timetable();
        Coach coach1 = new Coach("Петров", "Петр", "Петрович");
        Coach coach2 = new Coach("Сидоров", "Сидор", "Сидорович");
        Group group = new Group("Аэробика", Age.ADULT, 45);

        // У тренера Петрова 2 тренировки
        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(18, 0)));

        // У тренера Сидорова 3 тренировки
        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.TUESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.THURSDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.SATURDAY, new TimeOfDay(12, 0)));

        List<Timetable.CoachStats> stats = timetable.getCountByCoaches();
        assertEquals(2, stats.size());

        // Должен быть отсортирован по убыванию: сначала Сидоров (3), потом Петров (2)
        assertEquals(coach2, stats.get(0).getCoach());
        assertEquals(3, stats.get(0).getCount());
        assertEquals(coach1, stats.get(1).getCoach());
        assertEquals(2, stats.get(1).getCount());
    }

    @Test
    void testGetCountByCoachesSameCoachDifferentGroups() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Васильева", "Анна", "Игоревна");
        Group group1 = new Group("Детская гимнастика", Age.CHILD, 60);
        Group group2 = new Group("Взрослая гимнастика", Age.ADULT, 90);

        timetable.addNewTrainingSession(new TrainingSession(group1, coach,
                DayOfWeek.MONDAY, new TimeOfDay(15, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group2, coach,
                DayOfWeek.MONDAY, new TimeOfDay(17, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group1, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(15, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group2, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(19, 0)));

        List<Timetable.CoachStats> stats = timetable.getCountByCoaches();
        assertEquals(1, stats.size());
        assertEquals(4, stats.get(0).getCount());
    }

    @Test
    void testMultipleSessionsAtSameTime() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Тренер", "Первый", "");
        Coach coach2 = new Coach("Тренер", "Второй", "");
        Group group1 = new Group("Группа 1", Age.ADULT, 60);
        Group group2 = new Group("Группа 2", Age.ADULT, 60);

        // Два занятия в одно и то же время в понедельник
        TrainingSession session1 = new TrainingSession(group1, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(18, 0));
        TrainingSession session2 = new TrainingSession(group2, coach2,
                DayOfWeek.MONDAY, new TimeOfDay(18, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        // Проверяем, что оба занятия вернулись для этого времени
        List<TrainingSession> monday18 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(18, 0));
        assertEquals(2, monday18.size());
        assertTrue(monday18.contains(session1));
        assertTrue(monday18.contains(session2));

        // Проверяем, что в списке за день тоже оба занятия
        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(2, mondaySessions.size());
    }
}