package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {
    // Структура: День недели -> (Время -> Список занятий)
    // HashMap для быстрого доступа к дню (O(1))
    // TreeMap для автоматической сортировки по времени
    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    /**
     * Добавляет новое занятие в расписание
     */
    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        // Получаем или создаем карту для этого дня
        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.computeIfAbsent(day,
                k -> new TreeMap<>());

        // Получаем или создаем список для этого времени
        List<TrainingSession> sessionsAtTime = dayMap.computeIfAbsent(time,
                k -> new ArrayList<>());

        sessionsAtTime.add(trainingSession);
    }

    /**
     * Возвращает все занятия за указанный день, отсортированные по времени
     * Сложность: O(1) на получение дня, O(n) на сборку списка (n - количество занятий в день)
     */
    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(dayOfWeek);

        if (dayMap == null) {
            return Collections.emptyList();
        }

        List<TrainingSession> result = new ArrayList<>();
        for (List<TrainingSession> sessions : dayMap.values()) {
            result.addAll(sessions);
        }
        return result;
    }

    /**
     * Возвращает все занятия за указанный день и время
     * Сложность: O(log n) где n - количество разных времен в этот день
     */
    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> dayMap = timetable.get(dayOfWeek);

        if (dayMap == null) {
            return Collections.emptyList();
        }

        List<TrainingSession> sessions = dayMap.get(timeOfDay);
        return sessions != null ? sessions : Collections.emptyList();
    }

    /**
     * Возвращает статистику по тренерам: количество тренировок в неделю
     * Результат отсортирован по убыванию количества тренировок
     */
    public List<CoachStats> getCountByCoaches() {
        Map<Coach, Integer> counts = new HashMap<>();

        // Подсчитываем тренировки для каждого тренера
        for (TreeMap<TimeOfDay, List<TrainingSession>> dayMap : timetable.values()) {
            for (List<TrainingSession> sessions : dayMap.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    counts.put(coach, counts.getOrDefault(coach, 0) + 1);
                }
            }
        }

        // Преобразуем в список статистики
        List<CoachStats> stats = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : counts.entrySet()) {
            stats.add(new CoachStats(entry.getKey(), entry.getValue()));
        }

        // Сортируем по убыванию количества тренировок
        stats.sort((a, b) -> Integer.compare(b.getCount(), a.getCount()));
        return stats;
    }

    /**
     * Вспомогательный класс для хранения статистики по тренеру
     */
    public static class CoachStats {
        private final Coach coach;
        private final int count;

        public CoachStats(Coach coach, int count) {
            this.coach = coach;
            this.count = count;
        }

        public Coach getCoach() {
            return coach;
        }

        public int getCount() {
            return count;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CoachStats that = (CoachStats) o;
            return count == that.count && Objects.equals(coach, that.coach);
        }

        @Override
        public int hashCode() {
            return Objects.hash(coach, count);
        }
    }
}