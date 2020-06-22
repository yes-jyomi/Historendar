package kr.hs.emirim.sagittta.historendar.DB;

import android.provider.BaseColumns;

public  final class mypage implements BaseColumns {
    public static  String NUM = "num";
    public static  String LIKE = "like";
    public static  String TABLENAME0 = "LIKEY";

    public static  String _CREATE0 = "create table if not exists "+TABLENAME0+"("
            +NUM+" integer primary key autoincrement, "
            +LIKE+" integer );";


}
