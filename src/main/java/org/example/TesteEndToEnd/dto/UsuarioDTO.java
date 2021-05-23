package org.example.TesteEndToEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.TesteEndToEnd.model.Usuario;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioDTO {
    private Long id;
    private String nome;
    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private String cidade;
    private String estado;

    public Usuario toModel() {
        Usuario usuario = new Usuario();
        usuario.setId(this.getId());
        usuario.setNome(this.getNome());
        usuario.setCep(this.getCep());
        usuario.setLogradouro(this.getLogradouro());
        usuario.setNumero(this.getNumero());
        usuario.setComplemento(this.getComplemento());
        usuario.setNumero(this.getNumero());
        usuario.setCidade(this.getCidade());
        usuario.setEstado(this.getEstado());
        return usuario;
    }
}
