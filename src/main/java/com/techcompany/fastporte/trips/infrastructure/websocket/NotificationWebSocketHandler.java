package com.techcompany.fastporte.trips.infrastructure.websocket;

import com.techcompany.fastporte.users.domain.model.aggregates.enums.RoleName;
import com.techcompany.fastporte.users.infrastructure.auth.jwt.JwtUtil;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.DriverRepository;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.ClientRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class NotificationWebSocketHandler extends TextWebSocketHandler {

    // Mapa de sesiones activas por usuario
    private final ConcurrentHashMap<Long, CopyOnWriteArrayList<WebSocketSession>> userSessions = new ConcurrentHashMap<>();
    private final DriverRepository driverRepository;
    private final ClientRepository clientRepository;
    private final JwtUtil jwtUtil;

    public NotificationWebSocketHandler(DriverRepository driverRepository, ClientRepository clientRepository, JwtUtil jwtUtil) {
        this.driverRepository = driverRepository;
        this.clientRepository = clientRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long userId = getUserIdFromToken(session);
        System.out.println("Estableciendo conexión para userId: " + userId);

        userSessions.putIfAbsent(userId, new CopyOnWriteArrayList<>());
        userSessions.get(userId).add(session);

        // Imprimir el estado de las sesiones después de agregar
        System.out.println("Sesiones después de agregar: " + userSessions);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long userId = getUserIdFromToken(session);
        System.out.println("Cerrando conexión para userId: " + userId);

        if (userSessions.containsKey(userId)) {
            userSessions.get(userId).remove(session);
            // Elimina la lista si está vacía para limpiar el mapa
            if (userSessions.get(userId).isEmpty()) {
                userSessions.remove(userId);
            }
        }

        // Imprimir el estado de las sesiones después de cerrar
        System.out.println("Sesiones después de cerrar: " + userSessions);
    }


    public void sendNotification(String notificationJson, Long userId) {
        if (userSessions.containsKey(userId)) {
            for (WebSocketSession session : userSessions.get(userId)) {
                try {
                    session.sendMessage(new TextMessage(notificationJson));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Long getUserIdFromToken(WebSocketSession session) {
        // Extrae el token JWT desde los headers de la sesión
        String token = Objects.requireNonNull(session.getHandshakeHeaders().getFirst("Authorization")).replace("Bearer ", "");

        // Decodifica el token para obtener el userId y el rol

        Long userId = jwtUtil.extractClaim(token, claims -> claims.get("id", Long.class));
        if (userId == null) {
            throw new RuntimeException("Error: No se pudo obtener el userId del token");
        }

        RoleName role = RoleName.valueOf(jwtUtil.extractClaim(token, claims -> claims.get("role", String.class)));
        if (role == null) {
            throw new RuntimeException("Error: No se pudo obtener el rol del token");
        }

        if (role.equals(RoleName.ROLE_DRIVER)) {
            return driverRepository.findById(userId).get().getUser().getId();

        } else if (role.equals(RoleName.ROLE_CLIENT)) {
            return clientRepository.findById(userId).get().getUser().getId();
        } else {
            throw new RuntimeException("Error: El rol del usuario no es válido");

        }

    }

    public boolean isUserConnected(Long userId, RoleName role) {

        return userSessions.containsKey(userId) && !userSessions.get(userId).isEmpty();
    }
}