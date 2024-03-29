package avia.cloud.client.service;

import avia.cloud.client.dto.AirlineDTO;
import avia.cloud.client.dto.ClientCredentials;
import avia.cloud.client.dto.records.AirlineName;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface IAirlineService {
    ClientCredentials createClient(String name);
    void createAirline(AirlineDTO airlineDTO) throws IOException;
    List<AirlineName> findAirlineNames();
}
