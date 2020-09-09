package ar.edu.itba.pod.g3.enums;

/**
 * enum that determines which remote service to call from the Server.
 */
public enum ServiceName {
    MANAGEMENT ("managementService"),
    VOTE ("voteService"),
    QUERY("queryService"),
    FISCALIZATION("fiscalizationService"),
    ;

    final String name;
    ServiceName(String name){
        this.name = name;
    }

    public String getServiceName(){
        return name;
    }
}
