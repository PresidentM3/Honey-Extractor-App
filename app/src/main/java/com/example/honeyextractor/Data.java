package com.example.honeyextractor;

public class Data {

    public static String source = "Phone app";
    public static int motor_id = 1;
    public static int number_of_pole_pairs = 1;
    public static int max_velocity = 3000;
    public static int can_baud_rate = 5;

    public static boolean master_enable;
    public static int control_word;
    public static int target_velocity;
    public static int target_frequency = 0;
    public static int operation_mode = 1;

    public static int status_word;
    public static double actual_velocity;
    public static double actual_frequency;
    public static double DC_link_voltage;
    public static int Output_voltage;
}
