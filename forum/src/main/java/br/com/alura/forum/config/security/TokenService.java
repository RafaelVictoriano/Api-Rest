package br.com.alura.forum.config.security;

import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class TokenService {

    @Value("${forum.jwt.expiration}")
    private String expiration;

    @Value("${forum.jwt.secret}")
    private String secret;


    public String gerarToken(Authentication authenticate) {
        Usuario logado = (Usuario)authenticate.getPrincipal();
        Date data = new Date();
        Date exp = new Date(data.getTime()+Long.parseLong(expiration));
        return Jwts.builder()
                .setIssuer("Foruma alura")
                .setSubject(logado.getId().toString()) //identifica quem esta logado  Tostring trasnforma em string
                .setIssuedAt(data) //data
                .setExpiration(exp)//experição quando tempo pode ficar logado
                .signWith(SignatureAlgorithm.HS256, secret) //criptografia de senha cria uma assinatura
                .compact(); //compactando para string
    }

    public boolean isTokenValido(String token) {
        try{
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).toString();
            return true;
        }catch(Exception e ){
            return false;
        }

    }

    //Recuperando is do usuario
    public Long getIdUsuario(String token) {
        Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody(); //Pega as informações do token

        return Long.parseLong(claims.getSubject());
    }
}
