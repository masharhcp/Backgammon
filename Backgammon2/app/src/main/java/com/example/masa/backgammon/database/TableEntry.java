package com.example.masa.backgammon.database;

import android.provider.BaseColumns;

public abstract class TableEntry implements BaseColumns{
    public static final String TABLE_NAME = "scores";
    public static final String PLAYER_ONE = "player_one";
    public static final String PLAYER_TWO = "player_two";
    public static final String PLAYER_ONE_POINTS = "player_one_points";
    public static final String PLAYER_TWO_POINTS = "player_two_points";
    public static final String TIME = "time";


}
