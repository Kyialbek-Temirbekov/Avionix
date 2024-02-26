package avia.cloud.client.service;

import avia.cloud.client.dto.AirlineDTO;
import avia.cloud.client.dto.ClientCredentials;

import java.io.IOException;

public interface IAirlineService {
    ClientCredentials createClient(String name);
    void createAirline(AirlineDTO airlineDTO) throws IOException;
}
