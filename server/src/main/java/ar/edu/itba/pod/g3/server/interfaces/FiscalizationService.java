package ar.edu.itba.pod.g3.server.interfaces;

import ar.edu.itba.pod.g3.interfaces.NotificationConsumer;

public interface FiscalizationService {
    boolean registerFiscal(NotificationConsumer notificationConsumer) throws Exception;
}
