package project.ecommerce.dto;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import project.ecommerce.domain.enums.Role;

public class MemberDto {

    @Getter
    public static class SignupDto {
        private String email;
        private String nickname;
        private String password;
    }

    @Getter
    public static class LoginDto {
        private String email;
        private String password;

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(email, password);
        }
    }


}
