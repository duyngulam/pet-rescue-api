package com.uit.petrescueapi.infrastructure.geo;

import com.uit.petrescueapi.application.port.command.GeoCommandPort;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GeoPresenceEventListener {

    private final GeoCommandPort geoCommandPort;

    @EventListener
    public void onDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal principal = accessor.getUser();
        if (principal == null) {
            return;
        }
        try {
            UUID userId = UUID.fromString(principal.getName());
            geoCommandPort.markInactive(userId);
        } catch (IllegalArgumentException ignored) {
            // Non-UUID principal names are ignored for geo presence.
        }
    }
}
