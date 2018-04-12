package pl.dominisz.salaries;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * http://dominisz.pl
 * 11.04.2018
 */
public class HourlyEmployee extends Employee {

    private static final int STANDARD_HOURS = 8;
    private static final BigDecimal OVERHOUR_RATE = new BigDecimal("1.5");

    private BigDecimal hourlyRate;
    private List<WorkingDay> workingDays;

    public void setWorkingDays(List<WorkingDay> workingDays) {
        this.workingDays = workingDays;
    }

    public void addWorkingDay(WorkingDay workingDay) {
        workingDays.add(workingDay);
    }

    @Override
    protected BigDecimal computeSalary(LocalDate date) {
        LocalDate firstDay = getFirstDayOfWorkingPeriod(date);
        List<WorkingDay> salariedDays = findWorkingDays(firstDay, date);
        return computeSalary(salariedDays);
    }

    private List<WorkingDay> findWorkingDays(LocalDate firstDay, LocalDate lastDay) {
        return workingDays.stream()
                .filter(workingDay -> workingDay.between(firstDay, lastDay))
                .collect(Collectors.toList());
    }

    private BigDecimal computeSalary(List<WorkingDay> salariedDays) {
        return salariedDays.stream()
                .map(day -> computeSalary(day))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal computeSalary(WorkingDay workingDay) {
        return BigDecimal.ZERO;
    }

    @Override
    protected LocalDate getFirstDayOfWorkingPeriod(LocalDate date) {
        return date.minusDays(4);
    }

    @Override
    protected boolean isPayDay(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.FRIDAY;
    }

}
