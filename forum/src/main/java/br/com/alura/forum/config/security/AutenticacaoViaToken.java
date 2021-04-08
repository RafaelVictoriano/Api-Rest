package br.com.alura.forum.config.security;

import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AutenticacaoViaToken extends OncePerRequestFilter {

    private TokenService tokenService;
    private UsuarioRepository usuarioRepository;

    public AutenticacaoViaToken(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String token = recuperaToken(httpServletRequest);
        boolean valido = tokenService.isTokenValido(token);
        if(valido){
            autenticarCliente(token);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse); //Barra a requisição

    }

    private void autenticarCliente(String token) {
        Long idUsuario = tokenService.getIdUsuario(token);
        Usuario usuario = usuarioRepository.findById(idUsuario).get(); //recuperando usuario

        UsernamePasswordAuthenticationToken authentication = new  UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities()); //Criando autenticacao de token | Não passa a senha porque já está autenticado, Authorities pega seus perfis de autorização

        SecurityContextHolder.getContext().setAuthentication(authentication); //Força autenticação e indica que o cliente esta autenticado
    }

    private String recuperaToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization"); //Qual cabeçalho quer recuperar

        if(token == null || token.isEmpty() || !token.startsWith("Bearer ")){ //verificando se o cabeçalho esta vazio
            return null;
        }


        return token.substring(7, token.length()); //Seperando o token do Bearer

    }

}