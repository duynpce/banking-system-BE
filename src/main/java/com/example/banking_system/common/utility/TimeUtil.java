package com.example.banking_system.common.utility;

import com.example.banking_system.common.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;

@Component
public class TimeUtil {

	public LocalDate getDayStartDate(int year, int month, int day) {
		try {
			return LocalDate.of(year, month, day);
		} catch (RuntimeException exception) {
			throw new ValidationException("invalid year, month, or day");
		}
	}

	public LocalDate getDayEndDate(int year, int month, int day) {
		return getDayStartDate(year, month, day);
	}

	public LocalDate getWeekStartDate(int year, int month, int week) {
		YearMonth yearMonth = getYearMonth(year, month);
		LocalDate firstDayOfMonth = yearMonth.atDay(1);
		LocalDate startDate = firstDayOfMonth.plusDays((long) (week - 1) * 7);

		if (startDate.getMonthValue() != month) {
			throw new ValidationException("week is out of range for the selected month");
		}

		return startDate;
	}

	public LocalDate getWeekEndDate(int year, int month, int week) {
		LocalDate weekStartDate = getWeekStartDate(year, month, week);
		LocalDate monthEndDate = getMonthEndDate(year, month);
		LocalDate weekEndDate = weekStartDate.plusDays(6);

		return weekEndDate.isAfter(monthEndDate) ? monthEndDate : weekEndDate;
	}

	public LocalDate getMonthStartDate(int year, int month) {
		return getYearMonth(year, month).atDay(1);
	}

	public LocalDate getMonthEndDate(int year, int month) {
		return getYearMonth(year, month).atEndOfMonth();
	}

	public LocalDate getYearStartDate(int year) {
		return LocalDate.of(year, 1, 1);
	}

	public LocalDate getYearEndDate(int year) {
		return LocalDate.of(year, 12, 31);
	}

	private YearMonth getYearMonth(int year, int month) {
		try {
			return YearMonth.of(year, month);
		} catch (RuntimeException exception) {
			throw new ValidationException("invalid year or month");
		}
	}
}
