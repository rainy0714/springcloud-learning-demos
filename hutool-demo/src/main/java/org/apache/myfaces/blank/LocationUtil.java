package org.apache.myfaces.blank;

/**
 * @Author tongzhenke
 * @Date 2020/12/28 10:50
 */
public class LocationUtil {

    private static final double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static double GetDistance(double lng1, double lat1, double lng2, double lat2) {
        double radlng1 = rad(lng1);
        double radLat1 = rad(lat1);
        double radlng2 = rad(lng2);
        double radLat2 = rad(lat2);
        double distanceAdress = Math.acos(Math.sin(radlng1) * Math.sin(radlng2) + Math.cos(radlng1) * Math.cos(radlng2) * Math.cos(radLat2 - radLat1)) * EARTH_RADIUS;
        return distanceAdress;
    }

    private static final Double PI = Math.PI;
    private static final Double PK = 180 / PI;
    public static String getDistance2(double lat_a, double lng_a, double lat_b, double lng_b) {
        double t1 =
                Math.cos(lat_a / PK) * Math.cos(lng_a / PK) * Math.cos(lat_b / PK) * Math.cos(lng_b / PK);
        double t2 =
                Math.cos(lat_a / PK) * Math.sin(lng_a / PK) * Math.cos(lat_b / PK) * Math.sin(lng_b / PK);
        double t3 = Math.sin(lat_a / PK) * Math.sin(lat_b / PK);

        double tt = Math.acos(t1 + t2 + t3);
        String res = (EARTH_RADIUS * tt) + "";
        return res.substring(0, res.indexOf("."));
    }

    public static void main(String[] args) {
        //珠江国际大厦 113.267621,23.129623
        double lng1 = 113.267621;
        double lat1 = 23.129623;

        //广东省中医院 113.256554,23.118118
        double lng2 = 113.256554;
        double lat2 = 23.118118;

        double distance = GetDistance(lng1, lat1, lng2, lat2);
        System.out.println(distance);

        //此种方法是错的，完全不准
        String distance2 = getDistance2(lng1, lat1, lng2, lat2);
        System.out.println(distance2);
    }

}
