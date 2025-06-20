package com.example.kpu.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.kpu.bean.Attendance;
import com.example.kpu.bean.AttendanceItem;
import com.example.kpu.bean.AttendanceSubItem;
import com.example.kpu.bean.CalendarSchedule;
import com.example.kpu.bean.FreeRecord;
import com.example.kpu.bean.Schedule;
import com.example.kpu.bean.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDbHelper extends SQLiteOpenHelper {
    // 数据库信息
    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 2;  // 升级版本
    // 表名
    private static final String TABLE_NAME = "users";
    private static final String TABLE_ATTENDANCE = "attendance";

    // 用户表列名
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ACCOUNT = "account";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_CLAZZ = "clazz";

    // 出勤表列名
    private static final String COLUMN_ATTENDANCE_ID = "attendance_id";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_DATE = "date";  // 记录具体的日期
    private static final String COLUMN_STATUS = "status";  // 'present', 'absent', 'leave'

    // 创建用户表的SQL语句
    private static final String SQL_CREATE_USER_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_ACCOUNT + " TEXT UNIQUE, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_CLAZZ + " TEXT)";

    // 创建考勤表的SQL语句
    private static final String SQL_CREATE_ATTENDANCE_TABLE =
            "CREATE TABLE " + TABLE_ATTENDANCE + " (" +
                    COLUMN_ATTENDANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_ID + " INTEGER, " +
                    COLUMN_DATE + " TEXT, " +  // 以YYYY-MM-DD格式存储
                    COLUMN_STATUS + " INTEGER)";


    // 表名
    private static final String TABLE_FREE = "free";
    // free表列名
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_TIME_DATE = "timeDate";
    private static final String COLUMN_TOTAL_FEE = "Total_Fee";
    private static final String COLUMN_EXTRA_FEE = "Extra_Fee";
    private static final String COLUMN_LATE_CHARGES = "Late_Charges";
    private static final String COLUMN_DISCOUNT = "Discount";
    private static final String COLUMN_DISCOUNT_FEE = "Discount_Fee";
    private static final String COLUMN_PAID_FEE = "Paid_Fee";
    private static final String COLUMN_TITLE = "title";


    // 创建free表的SQL语句
    private static final String SQL_CREATE_FREE_TABLE =
            "CREATE TABLE " + TABLE_FREE + " (" +
                    COLUMN_TYPE + " INTEGER, " +
                    COLUMN_TIME_DATE + " TEXT, " +
                    COLUMN_TOTAL_FEE + " TEXT, " +
                    COLUMN_EXTRA_FEE + " TEXT, " +
                    COLUMN_LATE_CHARGES + " TEXT, " +
                    COLUMN_DISCOUNT + " TEXT, " +
                    COLUMN_DISCOUNT_FEE + " TEXT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_PAID_FEE + " TEXT)";


    // 日程表名
    private static final String TABLE_SCHEDULE = "schedule";

    // 日程表列名
    private static final String COLUMN_SCHEDULE_ID = "schedule_id";
    private static final String COLUMN_SCHEDULE_TITLE = "title";
    private static final String COLUMN_SCHEDULE_CONTENT = "content";
    private static final String COLUMN_SCHEDULE_CREATE_TIME = "create_time";
    private static final String COLUMN_SCHEDULE_USER_ID = "user_id";  // 登录用户ID

    // 创建日程表的SQL语句
    private static final String SQL_CREATE_SCHEDULE_TABLE =
            "CREATE TABLE " + TABLE_SCHEDULE + " (" +
                    COLUMN_SCHEDULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_SCHEDULE_TITLE + " TEXT, " +
                    COLUMN_SCHEDULE_CONTENT + " TEXT, " +
                    COLUMN_SCHEDULE_CREATE_TIME + " TEXT, " +
                    COLUMN_SCHEDULE_USER_ID + " INTEGER)";

    public UserDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USER_TABLE);
        db.execSQL(SQL_CREATE_ATTENDANCE_TABLE);
        db.execSQL(SQL_CREATE_FREE_TABLE);
        db.execSQL(SQL_CREATE_SCHEDULE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 如果数据库版本号更新，执行删除和重建表操作

    }

    // 注册方法
    public long registerUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, user.id);
        values.put(COLUMN_ACCOUNT, user.account);
        values.put(COLUMN_NAME, user.name);
        values.put(COLUMN_CLAZZ, user.clazz);

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result;
    }

    // 登录方法
    @SuppressLint("Range")
    public User loginUser(String account) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_ACCOUNT + " = ?", new String[]{account}, null, null, null);

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
            user.account = cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT));
            user.name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            user.clazz = cursor.getString(cursor.getColumnIndex(COLUMN_CLAZZ));
            cursor.close();
        }
        db.close();
        return user;
    }

    // add Attendance
    public long recordAttendance(long userId, String date, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_STATUS, status);

        // 插入数据
        long result = db.insert(TABLE_ATTENDANCE, null, values);
        db.close();
        return result;
    }




    // Get the attendance data for the current user for the previous year and current year, grouped by month
    public List<AttendanceItem> getAttendanceForLastAndCurrentYear(long userId) {
        List<AttendanceItem> attendanceItems = new ArrayList<>();

        // Get the current year and the previous year
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int previousYear = currentYear - 1;

        // Get the monthly attendance statistics for the current year and the previous year
        List<AttendanceSubItem> subItemsCurrentYear = getMonthlyAttendance(userId, currentYear);
        List<AttendanceSubItem> subItemsPreviousYear = getMonthlyAttendance(userId, previousYear);

        // Add the attendance data for both years to the list as AttendanceItem objects
        attendanceItems.add(new AttendanceItem(String.valueOf(previousYear), subItemsPreviousYear));
        attendanceItems.add(new AttendanceItem(String.valueOf(currentYear), subItemsCurrentYear));

        return attendanceItems;  // Return the list of attendance items for both years
    }

    // Get the monthly attendance statistics for a specified year
    private List<AttendanceSubItem> getMonthlyAttendance(long userId, int year) {
        List<AttendanceSubItem> subItems = new ArrayList<>();

        // Get attendance records for each month of the specified year
        for (int month = 1; month <= 12; month++) {
            // Get the attendance data for the current month
            List<Attendance> attendanceList = getAttendanceForMonth(userId, year + "-" + String.format("%02d", month));
            int presentCount = 0;  // Count of present days
            int absentCount = 0;   // Count of absent days
            int leaveCount = 0;    // Count of leave days

            // Count the attendance status for each day in the month
            for (Attendance attendance : attendanceList) {
                int status = attendance.getStatus();  // 1: Present, 2: Absent, 3: Leave
                if (status == 1) {
                    presentCount++;  // Increment present count
                } else if (status == 2) {
                    absentCount++;   // Increment absent count
                } else if (status == 3) {
                    leaveCount++;    // Increment leave count
                }
            }

            // Convert the month number to its English abbreviation (e.g., "Jan" for January)
            String monthName = getMonthName(month);
            // Add the monthly statistics as an AttendanceSubItem object
            subItems.add(new AttendanceSubItem(monthName, presentCount, absentCount, leaveCount));
        }

        return subItems;  // Return the list of monthly attendance sub-items
    }

    private String getMonthName(int month) {
        switch (month) {
            case 1: return "JAN";
            case 2: return "FEB";
            case 3: return "MAR";
            case 4: return "APR";
            case 5: return "MAY";
            case 6: return "JUN";
            case 7: return "JUL";
            case 8: return "AUG";
            case 9: return "SEP";
            case 10: return "OCT";
            case 11: return "NOV";
            case 12: return "DEC";
            default: return "";
        }
    }

    // Retrieves all attendance records for a specific user in a given month, and returns the corresponding date and attendance status.
    public List<Attendance> getAttendanceForMonth(long  userId, String yearMonth) {
        List<Attendance> result = new ArrayList<>();
        // Get all the dates for the specified year and month
        String[] yearMonthParts = yearMonth.split("-");
        int year = Integer.parseInt(yearMonthParts[0]);
        int month = Integer.parseInt(yearMonthParts[1]);
        List<String> monthDates = getMonthDates(year, month);
        SQLiteDatabase db = this.getReadableDatabase();
        // Split the provided year and month (e.g., 2025-01)
        String currentYear = yearMonthParts[0];  // Get the year
        String currentMonth = yearMonthParts[1]; // Get the month
        // SQL query: Retrieve all attendance records for the specified year and month
        String query = "SELECT * FROM " + TABLE_ATTENDANCE + " WHERE " + COLUMN_USER_ID + " = ? AND " + COLUMN_DATE + " LIKE ? ORDER BY " + COLUMN_DATE + " DESC";
        String yearMonthPattern = currentYear + "-" + currentMonth + "-%"; // Construct query pattern to match year and month

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), yearMonthPattern});

        // Store attendance records by date
        Map<String, Integer> attendanceMap = new HashMap<>();
        while (cursor != null && cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
            int status = cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS));
            attendanceMap.put(date, status);
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();

        // Map dates to attendance status
        for (String date : monthDates) {
            // Process only if the date is not empty
            if (!date.isEmpty()) {
                String monthString = month < 10 ? "0" + month : month + "";
                String dateString = date.length() < 2 ? "0" + date : date + "";
                Integer status = attendanceMap.get(currentYear + "-" + monthString + "-" + dateString);
                // If attendance record exists for the date, return corresponding status
                if (status != null) {
                    result.add(new Attendance(0, userId, date, status)); // Generate Attendance object with existing status
                } else {
                    result.add(new Attendance(0, userId, date, -1)); // If no record, set status to -1
                }
            } else {
                // If the date is empty, return an empty attendance record
                result.add(new Attendance(0, userId, "", -1)); // Empty date, status is -1
            }
        }

        return result;
    }

    // Retrieves all the dates in a specified year and month
    private List<String> getMonthDates(int year, int month) {
        List<String> dateList = new ArrayList<>();

        // Get the first day and total number of days in the month
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1); // Months start from 0
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // Get the weekday of the first day of the month
        int totalDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // Get the total days in the month

        // Adjust firstDayOfWeek to represent Monday-Sunday sequence
        int emptyDays = (firstDayOfWeek == 1) ? 6 : firstDayOfWeek - 2; // If Sunday is 1, add 6 empty days before
        for (int i = 0; i < emptyDays; i++) {
            dateList.add("");  // Fill with empty entries
        }

        // Fill with actual dates of the month
        for (int day = 1; day <= totalDaysInMonth; day++) {
            dateList.add(String.valueOf(day));
        }

        // Return the list of dates
        return dateList;
    }

    // Inserts a new free record
    public long insertFreeRecord(int type, String title, String timeDate, String totalFee, String extraFee,
                                 String lateCharges, String discount, String discountFee, String paidFee) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_TIME_DATE, timeDate);
        values.put(COLUMN_TOTAL_FEE, totalFee);
        values.put(COLUMN_EXTRA_FEE, extraFee);
        values.put(COLUMN_LATE_CHARGES, lateCharges);
        values.put(COLUMN_DISCOUNT, discount);
        values.put(COLUMN_DISCOUNT_FEE, discountFee);
        values.put(COLUMN_PAID_FEE, paidFee);
        values.put(COLUMN_TITLE, title);

        long result = db.insert(TABLE_FREE, null, values);
        db.close();
        return result;
    }

    // Get free records based on type
    public List<FreeRecord> getFreeRecordsByType(int type) {
        List<FreeRecord> freeRecords = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // SQL query to fetch records with the specified type
        Cursor cursor = db.query(TABLE_FREE, null, COLUMN_TYPE + " = ?", new String[]{String.valueOf(type)},
                null, null, null);

        while (cursor != null && cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
            String timeDate = cursor.getString(cursor.getColumnIndex(COLUMN_TIME_DATE));
            String totalFee = cursor.getString(cursor.getColumnIndex(COLUMN_TOTAL_FEE));
            String extraFee = cursor.getString(cursor.getColumnIndex(COLUMN_EXTRA_FEE));
            String lateCharges = cursor.getString(cursor.getColumnIndex(COLUMN_LATE_CHARGES));
            String discount = cursor.getString(cursor.getColumnIndex(COLUMN_DISCOUNT));
            String discountFee = cursor.getString(cursor.getColumnIndex(COLUMN_DISCOUNT_FEE));
            String paidFee = cursor.getString(cursor.getColumnIndex(COLUMN_PAID_FEE));

            // Create a new FreeRecord object and add it to the list
            FreeRecord freeRecord = new FreeRecord(type, title, timeDate, totalFee, extraFee, lateCharges, discount, discountFee, paidFee);
            freeRecords.add(freeRecord);
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return freeRecords;
    }

    // Add a new schedule to the database
    public long addSchedule(Schedule schedule) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SCHEDULE_TITLE, schedule.getTitle());
        values.put(COLUMN_SCHEDULE_CONTENT, schedule.getContent());
        values.put(COLUMN_SCHEDULE_CREATE_TIME, schedule.getCreateTime());
        values.put(COLUMN_SCHEDULE_USER_ID, schedule.getUserId());

        long result = db.insert(TABLE_SCHEDULE, null, values);
        db.close();
        return result;
    }

    // Update an existing schedule
    public int updateSchedule(Schedule schedule) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SCHEDULE_TITLE, schedule.getTitle());
        values.put(COLUMN_SCHEDULE_CONTENT, schedule.getContent());
        values.put(COLUMN_SCHEDULE_CREATE_TIME, schedule.getCreateTime());

        return db.update(TABLE_SCHEDULE, values, COLUMN_SCHEDULE_ID + " = ?",
                new String[]{String.valueOf(schedule.getScheduleId())});
    }

    // Delete a schedule from the database
    public int deleteSchedule(long scheduleId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_SCHEDULE, COLUMN_SCHEDULE_ID + " = ?",
                new String[]{String.valueOf(scheduleId)});
    }

    // Get all schedules for a specific user in a given month, grouped by date
    public List<CalendarSchedule> getSchedulesForMonth(long userId, String yearMonth) {
        List<CalendarSchedule> result = new ArrayList<>();

        // Get all dates for the given month
        String[] yearMonthParts = yearMonth.split("-");
        int year = Integer.parseInt(yearMonthParts[0]);
        int month = Integer.parseInt(yearMonthParts[1]);
        List<String> monthDates = getMonthDates(year, month);

        SQLiteDatabase db = this.getReadableDatabase();

        // SQL query to get all schedules for the specified user and month
        String query = "SELECT * FROM " + TABLE_SCHEDULE + " WHERE " + COLUMN_SCHEDULE_USER_ID + " = ? AND " + COLUMN_SCHEDULE_CREATE_TIME + " LIKE ? ORDER BY " + COLUMN_SCHEDULE_CREATE_TIME + " DESC";
        String yearMonthPattern = yearMonthParts[0] + "-" + yearMonthParts[1] + "-%"; // Construct pattern to match year-month

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), yearMonthPattern});

        // Group the schedule records by date
        Map<String, List<Schedule>> scheduleMap = new HashMap<>();
        while (cursor != null && cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndex(COLUMN_SCHEDULE_CREATE_TIME)).split(" ")[0]; // Get the date part
            String title = cursor.getString(cursor.getColumnIndex(COLUMN_SCHEDULE_TITLE));
            String content = cursor.getString(cursor.getColumnIndex(COLUMN_SCHEDULE_CONTENT));
            long scheduleId = cursor.getLong(cursor.getColumnIndex(COLUMN_SCHEDULE_ID));

            Schedule schedule = new Schedule(scheduleId, title, content, cursor.getString(cursor.getColumnIndex(COLUMN_SCHEDULE_CREATE_TIME)), userId);

            // Add the schedule to the list for the corresponding date
            if (!scheduleMap.containsKey(date)) {
                scheduleMap.put(date, new ArrayList<>());
            }
            scheduleMap.get(date).add(schedule);
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();

        // Create CalendarSchedule objects and group them by date
        for (String date : monthDates) {
            if (!date.isEmpty()) {
                String monthString = month < 10 ? "0" + month : String.valueOf(month);
                String dateString = date.length() < 2 ? "0" + date : date;
                String formattedDate = yearMonthParts[0] + "-" + monthString + "-" + dateString;
                CalendarSchedule calendarSchedule = new CalendarSchedule();
                calendarSchedule.setDate(formattedDate);

                // Get all schedules for the specific date
                List<Schedule> schedules = scheduleMap.get(formattedDate);
                if (schedules != null && !schedules.isEmpty()) {
                    calendarSchedule.setScheduleList(schedules); // Set the schedule list for that date
                }

                result.add(calendarSchedule); // Add the calendarSchedule to the result list
            } else {
                result.add(new CalendarSchedule("", null)); // Add an empty CalendarSchedule for empty dates
            }
        }

        return result;
    }





}
