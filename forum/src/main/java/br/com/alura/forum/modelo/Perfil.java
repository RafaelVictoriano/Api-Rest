package br.com.alura.forum.modelo;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Perfil implements GrantedAuthority {

    //Classe de indentificação do usuario

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;


    @Override //Metodo para saber o nome do perfil
    public String getAuthority() {
        return nome;
    }
}

