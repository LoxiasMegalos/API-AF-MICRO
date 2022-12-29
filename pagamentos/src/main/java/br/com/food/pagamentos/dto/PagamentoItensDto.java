package br.com.food.pagamentos.dto;

import br.com.food.pagamentos.model.Pagamento;
import br.com.food.pagamentos.model.Status;

import java.math.BigDecimal;
import java.util.List;

public record PagamentoItensDto(
        Long id,
        BigDecimal valor,
        String nome,
        String numero,
        String expiracao,
        String codigo,
        Status status,
        Long pedidoId,
        Long formaDePagamentoId,
        List<ItensDto> itens
) {
    public PagamentoItensDto(Pagamento pagamento, PedidoDto request) {
        this(pagamento.getId(), pagamento.getValor(), pagamento.getNome(), pagamento.getNumero()
        , pagamento.getExpiracao(), pagamento.getCodigo(), pagamento.getStatus(), pagamento.getFormaDePagamentoId()
        ,pagamento.getPedidoId(), request.itens());
    }
}
