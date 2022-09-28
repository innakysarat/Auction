package auction.repository;

import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class LocationDao {
    private final Set<String> locations;

    public LocationDao() {
        locations = new LinkedHashSet<>();
    }

    public Set<String> getLocations() {
        return locations;
    }

    public void addLocation(String location) {
        locations.add(location);
    }
}
