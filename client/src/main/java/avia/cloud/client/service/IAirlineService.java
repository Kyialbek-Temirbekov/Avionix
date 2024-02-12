package avia.cloud.client.service;

import avia.cloud.client.dto.AirlineDTO;
import avia.cloud.client.dto.ClientCredentials;

public interface IAirlineService extends IClientDetailsService{
    ClientCredentials createClient(String name);
    void createAirline(AirlineDTO airlineDTO);
}
