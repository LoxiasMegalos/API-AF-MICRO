package br.com.food.pagamentos.model;

import br.com.food.pagamentos.dto.PagamentoDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "pagamentos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Positive
    private BigDecimal valor;


    @Size(max = 100)
    private String nome;


    @Size(max = 19)
    private String numero;


    @Size(max = 7)
    private String expiracao;


    @Size(max = 3, min = 3)
    private String codigo;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull
    private Long pedidoId;

    @NotNull
    private Long formaDePagamentoId;

    public Pagamento(PagamentoDto dto) {
        this.id = dto.id();
        this.valor = dto.valor();
        this.formaDePagamentoId = dto.formaDePagamentoId();
        this.nome = dto.nome();
        this.numero = dto.numero();
        this.expiracao = dto.expiracao();
        this.codigo = dto.codigo();
        this.setStatus(Status.CRIADO);
        this.pedidoId = dto.pedidoId();
    }
}
