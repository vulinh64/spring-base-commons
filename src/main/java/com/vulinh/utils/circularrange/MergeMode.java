package com.vulinh.utils.circularrange;

/** Defines how adjacent circular ranges should be merged. */
public enum MergeMode {
  /**
   * Adjacent ranges will NOT merge automatically. Only overlapping ranges will be merged. Example:
   * MON-FRI, SAT-SUN → "MONDAY-FRIDAY,SATURDAY-SUNDAY" Suitable for custom numeric ranges where
   * gaps are meaningful.
   */
  ALLOW_GAPS,

  /**
   * Adjacent ranges will merge automatically (no gaps). Example: MON-FRI, SAT-SUN → "MONDAY-SUNDAY"
   * Suitable for DayOfWeek, Month in cron expressions where continuity matters.
   */
  NO_GAPS
}
