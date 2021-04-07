package br.com.alura.forum.config.security;

import br.com.alura.forum.modelo.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

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
}
