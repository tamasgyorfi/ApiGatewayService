package hu.bets.apigateway.service;

import hu.bets.common.services.Services;
import hu.bets.common.util.servicediscovery.EurekaFacade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ServiceResolverServiceTest {

    @Mock
    private EurekaFacade eurekaFacade;

    private ServiceResolverService sut;

    @Before
    public void before() {
        sut = new ServiceResolverService(eurekaFacade);
    }

    @Test
    public void shouldCallEurekaFacadeForResolve() {
        sut.resolve(Services.MATCHES);

        verify(eurekaFacade).resolveEndpoint(Services.MATCHES.getServiceName());
    }
}
