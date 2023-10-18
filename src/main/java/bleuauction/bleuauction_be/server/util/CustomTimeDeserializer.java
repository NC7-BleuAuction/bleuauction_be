package bleuauction.bleuauction_be.server.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;


@JsonComponent
public class CustomTimeDeserializer extends JsonDeserializer<Time> {
  @Override
  public Time deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    String timeStr = p.getValueAsString();
    try {
      java.util.Date date = sdf.parse(timeStr);
      return new Time(date.getTime());
    } catch (ParseException e) {
      throw new IOException("Error parsing time", e);
    }
  }
}