package avia.cloud.client.service;

import avia.cloud.client.dto.AirlineDTO;
import avia.cloud.client.dto.ClientCredentials;
import avia.cloud.client.dto.management.AirlineMDTO;
import avia.cloud.client.dto.records.AirlineName;
import avia.cloud.client.dto.records.AirlineRatingRecord;

import java.io.IOException;
import java.util.List;

public interface IAirlineService {
    ClientCredentials createClient(String name);
    void createAirline(AirlineDTO airlineDTO) throws IOException;
    AirlineDTO fetchAirline(String email);
    List<AirlineName> findAirlineNames();
    List<AirlineRatingRecord> findAirlineRatings();
    AirlineDTO findAirlineById(String id);
    List<String> findIdsByText(String text);
    AirlineMDTO fetchAirlineM(String id);
}
