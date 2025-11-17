package utils;

public class DateHelper {

    public static long dateAsInt(String date) {

        String[] p = date.split("-"); // DD-MM-YY

        String day = p[0].length() == 1 ? "0" + p[0] : p[0];
        String month = p[1].length() == 1 ? "0" + p[1] : p[1];
        String year = p[2].length() == 1 ? "0" + p[2] : p[2];
        System.out.println(day);
        System.out.println(month);
        System.out.println(year);

        // Convert to YYMMDD
        return Long.parseLong(year + month + day);
    }



    public static void main(String[] args){
       System.out.println( DateHelper.dateAsInt("23-11-25"));
    }
}
