package baajna.scroll.owner.mobioapp.utils;

/**
 * Created by Jewel on 1/3/2016.
 */
public class TimeConverter {


    public static String getTime(long mSec){
        String s="",m="";
        long inSec=mSec/1000;
        int min=(int)inSec/60;

        m=(min<10)?"0"+min:min+"";
        int sec=(int)inSec%60;

        s=(sec<10)?"0"+sec:sec+"";

        return m+":"+s;
    }
}
