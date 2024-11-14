import com.app.transport.businfo.api.BusResponse
import retrofit2.http.GET

//class TrackerResponse {
//    final int id;
//    final String imei;
//    final String name;
//    final String stateCode;
//    final String stateName;
//    final double lat;
//    final double lng;
//    final String speed;
//    final String orientation;
//    @JsonKey(name: "gpstime")
//    final String gpsTime;
//    final int routeId;
//    final String routeName;
//    @JsonKey(name: "routeColour")
//    final String routeColor;
//    final bool inDepo;
//    final String busNumber;
//    final int perevId;
//    final String perevName;
//    final String remark;
//    final bool online;
//    final int idBusTypes;
//
//
//    @GET("/map/tracker/")
//    Future<Map<String, TrackerResponse>> fetchBusesLocation(@Query("selectedRoutesStr") String selectedRoutesStr);
//
interface BusClient {

    @GET("/map/routes/1")
    suspend fun fetchBusesInfo(): Map<String, BusResponse>
}