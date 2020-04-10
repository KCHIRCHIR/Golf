package strathmore.university.gym;

public class GolfLocation {
    public String name;
 //   public String OpenTime;
 //   public String CloseTime;
 //   public String Details;
    public double latitude;
    public double longitude;

    public GolfLocation() {
    }

    public GolfLocation(String name, double latitude, double longitude) {
        this.name = name;
    //    OpenTime = openTime;
      //  CloseTime = closeTime;
        // Details = details;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
// String openTime, String closeTime, String details,