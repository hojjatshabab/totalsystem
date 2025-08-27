package fava.betaja.erp.payload.request;

import fava.betaja.erp.validation.StrongPassword;
import fava.betaja.erp.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "firstname is required")
    private String firstname;
    @NotBlank(message = "lastname is required")
    private String lastname;
    @NotBlank(message = "username is required")

    private String username;
    @NotBlank(message = "password is required")
    @StrongPassword
    private String password;

}
