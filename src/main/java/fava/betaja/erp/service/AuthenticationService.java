package fava.betaja.erp.service;

import fava.betaja.erp.payload.request.AuthenticationRequest;
import fava.betaja.erp.payload.request.RegisterRequest;
import fava.betaja.erp.payload.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
