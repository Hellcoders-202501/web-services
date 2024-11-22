package com.techcompany.fastporte.users.interfaces.rest;

import com.techcompany.fastporte.users.domain.model.commands.auth.AuthenticateAccountCommand;
import com.techcompany.fastporte.users.domain.services.auth.AuthenticationCommandService;
import com.techcompany.fastporte.users.interfaces.rest.resources.LoginResource;
import com.techcompany.fastporte.users.interfaces.rest.resources.TokenResource;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Operations related to user authentication and authorization")
public class AuthenticationController {

    private final AuthenticationCommandService authenticationService;

    public AuthenticationController(AuthenticationCommandService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping() //@PostMapping("/authenticate")
    public ResponseEntity<TokenResource> createAuthenticationToken(@RequestBody LoginResource resource) {

        String username = resource.email();
        String password = resource.password();

        String token = authenticationService.handle(new AuthenticateAccountCommand(username, password));

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResource(null));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new TokenResource(token));
        }

    }
}