/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces.defaultmethods;

import java.time.ZonedDateTime;

public interface AbstractZoneTimeClient extends TimeClient {

    @Override
    public ZonedDateTime getZonedDateTime(String zoneString);
}
