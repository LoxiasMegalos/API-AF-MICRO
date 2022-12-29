package br.com.food.pagamentos.dto;

import br.com.food.pagamentos.model.Pagamento;
import br.com.food.pagamentos.model.Status;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


public record PagamentoDto(
        Long id,
        BigDecimal valor,
        String nome,
        String numero,
        String expiracao,
        String codigo,
        Status status,
        Long pedidoId,
        Long formaDePagamentoId
) {
    public PagamentoDto(Pagamento pagamento){
        this(pagamento.getId(), pagamento.getValor(), pagamento.getNome(), pagamento.getNumero(), pagamento.getExpiracao(),
                pagamento.getCodigo(), pagamento.getStatus(), pagamento.getPedidoId(), pagamento.getFormaDePagamentoId());
    }
}
