import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DEIF {
	
	private Long timestamp;
	private JSONArray measurements;
	private JSONObject location;
	private String id;
	private String name;


public DEIF(Long timestamp, JSONArray measurements, JSONObject location, String id, String name) {
    this.timestamp = timestamp;
    this.measurements = measurements;
    this.location = location;
    this.id = id;
    this.name = name;

 }

public Long getTimestamp() { return timestamp; }
public JSONArray getMeasurements() { return measurements; }
public JSONObject getLocation() { return location; }
public String getID() { return id; }
public String getName() { return name; }

}