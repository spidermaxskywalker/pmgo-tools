package br.com.maxgontijo.pmgo.planilhasveiculos.model;

import br.com.maxgontijo.pmgo.planilhasveiculos.model.enums.Acesso;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "usuario", schema = Model.SCHEMA_AUTENTICACAO)
public class Usuario extends Model<Integer> {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private boolean ativo;
    private List<Acesso> acessos;

    @Override
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(sequenceName = "usuario_seq", name = "seq", schema = Model.SCHEMA_AUTENTICACAO, allocationSize = 1)
    public Integer getId() {
        return this.id;
    }

    @Column(name = "username", nullable = false, length = 32)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password", nullable = false, length = 256)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "ativo", nullable = false)
    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @ElementCollection(targetClass = Acesso.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            schema = SCHEMA_AUTENTICACAO,
            name = "usuario_acesso",
            joinColumns = {@JoinColumn(name = "fk_usuario", referencedColumnName = "id")},
            uniqueConstraints = {@UniqueConstraint(columnNames = {"fk_usuario", "acesso"})})
    @Column(name = "acesso")
    public List<Acesso> getAcessos() {
        return acessos;
    }

    public void setAcessos(List<Acesso> acessos) {
        this.acessos = acessos;
    }
}
