import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

beans = {
    passwordEncoder(BCryptPasswordEncoder)
}
