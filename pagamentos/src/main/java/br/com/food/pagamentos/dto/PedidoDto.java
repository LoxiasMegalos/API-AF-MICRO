package br.com.food.pagamentos.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record PedidoDto(
        Long id,
        LocalDateTime dataHora,
        Status status,
        List<ItensDto> itens
) {
}
