package ar.edu.itba.pod.g3.client.interfaces;

import ar.edu.itba.pod.g3.api.models.Fiscal;

public interface FiscalizationClient {
    boolean registerFiscal(Fiscal fiscal);

}