package br.com.food.pagamentos.controller;


import br.com.food.pagamentos.dto.PagamentoDto;
import br.com.food.pagamentos.dto.PagamentoItensDto;
import br.com.food.pagamentos.service.PagamentoService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService service;

    @GetMapping
    public ResponseEntity<Page<PagamentoDto>> listar(@PageableDefault(size = 10) Pageable paginacao) {
        return ResponseEntity.ok(service.obterTodos(paginacao));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoItensDto> detalhar(@PathVariable @NotNull Long id) {
        PagamentoItensDto dto = service.obterPedidoPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<PagamentoDto> cadastrar(@RequestBody @Valid PagamentoDto dto, UriComponentsBuilder uriBuilder){
        var pagamento = service.criarPagamento(dto);
        var url = uriBuilder.path("pagamentos/{id}").buildAndExpand(pagamento.id()).toUri();

        return ResponseEntity.created(url).body(pagamento);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<PagamentoDto> atualizar(@PathVariable @NotNull Long id, @RequestBody @Valid PagamentoDto dto){
        var atualizado = service.atualizarPagamento(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity remover(@PathVariable Long id){
        service.excluirPagamento(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/confirmar")
    @CircuitBreaker(name = "atualizaPedido", fallbackMethod = "pagamentoAutorizadoComIntegracaoPendente")
    public void confirmarPagamento(@PathVariable @NotNull Long id){
        service.atualizaPagamento(id);
    }

    public void pagamentoAutorizadoComIntegracaoPendente(@PathVariable @NotNull Long id, Exception e){
        service.alteraStatus(id);
    }

}
