package hu.bets.apigateway.service;

import hu.bets.common.services.Services;
import hu.bets.common.util.servicediscovery.EurekaFacade;

public class ServiceResolverService {

    private EurekaFacade eurekaFacade;

    public ServiceResolverService(EurekaFacade eurekaFacade) {
        this.eurekaFacade = eurekaFacade;
    }

    public String resolve(Services service) {
        return eurekaFacade.resolveEndpoint(service.getServiceName());
    }
}
