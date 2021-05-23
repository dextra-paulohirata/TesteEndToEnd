package org.example.TesteEndToEnd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.TesteEndToEnd.dto.UsuarioDTO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private String cidade;
    private String estado;

    public UsuarioDTO toDTO() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(this.getId());
        dto.setNome(this.getNome());
        dto.setCep(this.getCep());
        dto.setLogradouro(this.getLogradouro());
        dto.setNumero(this.getNumero());
        dto.setComplemento(this.getComplemento());
        dto.setCidade(this.getCidade());
        dto.setEstado(this.getEstado());
        return dto;
    }
}