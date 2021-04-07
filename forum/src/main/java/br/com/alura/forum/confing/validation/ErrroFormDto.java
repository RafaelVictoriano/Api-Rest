package br.com.alura.forum.confing.validation;

public class ErrroFormDto {

    private String campo;
    private String erro;

    public ErrroFormDto(String campo, String erro) {
        this.campo = campo;
        this.erro = erro;
    }

    public String getCampo() {
        return campo;
    }

    public String getErro() {
        return erro;
    }


}
