package at.htl.busmanagement.control;

import javax.enterprise.context.ApplicationScoped;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {
    public LocalDateTime unmarshal(String localDateString) {
        return LocalDateTime.parse(localDateString);
    }

    public String marshal(LocalDateTime localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDate.format(formatter);
    }
}
