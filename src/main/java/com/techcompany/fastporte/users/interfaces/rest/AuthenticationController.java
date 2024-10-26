package com.techcompany.fastporte.users.interfaces.rest;

import com.techcompany.fastporte.users.domain.model.commands.auth.AuthenticateAccountCommand;
import com.techcompany.fastporte.users.domain.services.auth.AuthenticationCommandService;
import com.techcompany.fastporte.users.interfaces.rest.resources.LoginResource;
import com.techcompany.fastporte.users.interfaces.rest.resources.TokenResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class AuthenticationController {

    private final AuthenticationCommandService authenticationService;

    public AuthenticationController(AuthenticationCommandService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<TokenResource> createAuthenticationToken(@RequestBody LoginResource resource) {
        try {
            String username = resource.username();
            String password = resource.password();

            String token = authenticationService.handle(new AuthenticateAccountCommand(username, password));

            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResource(token));
            } else {
                return ResponseEntity.ok(new TokenResource(token));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TokenResource(null));
        }
    }
}