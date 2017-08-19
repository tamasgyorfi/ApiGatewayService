package hu.bets.apigateway.service;


import hu.bets.servicediscovery.EurekaFacade;
import hu.bets.services.Services;

public class ServiceResolverService {

    private EurekaFacade eurekaFacade;

    public ServiceResolverService(EurekaFacade eurekaFacade) {
        this.eurekaFacade = eurekaFacade;
    }

    public String resolve(Services service) {
        return eurekaFacade.resolveEndpoint(service.getServiceName());
    }
}
