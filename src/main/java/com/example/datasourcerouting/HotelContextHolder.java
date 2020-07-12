package com.example.datasourcerouting;

public class HotelContextHolder {
    private static final ThreadLocal<String> databaseName = new ThreadLocal<>();

    public static String getDatabase() {
        return databaseName.get();
    }

    public static void setDatabase(String name) {
        databaseName.set(name);
    }

    public static void clearDatabaseName() {
        databaseName.remove();
    }

}
