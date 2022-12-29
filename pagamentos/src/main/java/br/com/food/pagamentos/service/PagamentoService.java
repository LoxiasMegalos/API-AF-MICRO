package br.com.food.pagamentos.service;

import br.com.food.pagamentos.dto.PagamentoDto;
import br.com.food.pagamentos.dto.PagamentoItensDto;
import br.com.food.pagamentos.http.PedidoClient;
import br.com.food.pagamentos.model.Pagamento;
import br.com.food.pagamentos.model.Status;
import br.com.food.pagamentos.repository.PagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PedidoClient pedido;

    public Page<PagamentoDto> obterTodos(Pageable paginacao){
        return repository.findAll(paginacao).map(PagamentoDto::new);
    }

    public PagamentoItensDto obterPedidoPorId(Long id){
        var pagamento = repository.getReferenceById(id);
        var request = pedido.getItensPedidoByPedidoId(pagamento.getPedidoId());

        return new PagamentoItensDto(pagamento, request);
    }

    public PagamentoDto criarPagamento(PagamentoDto dto) {
        Pagamento pagamento = new Pagamento(dto);
        pagamento.setStatus(Status.CRIADO);
        repository.save(pagamento);

        return new PagamentoDto(pagamento);
    }

    public PagamentoDto atualizarPagamento(Long id, PagamentoDto dto){
        Pagamento pagamento = new Pagamento(dto);
        pagamento.setId(id);
        pagamento = repository.save(pagamento);
        return new PagamentoDto(pagamento);
    }

    public void excluirPagamento(Long id){
        repository.deleteById(id);
    }


    public void atualizaPagamento(Long id) {
        var pagamento = repository.findById(id);

        if(!pagamento.isPresent()){
            throw new EntityNotFoundException();
        }

        pagamento.get().setStatus(Status.CONFIRMADO);
        repository.save(pagamento.get());
        pedido.atualizaPagamento(pagamento.get().getPedidoId());

    }

    public void alteraStatus(Long id) {
        var pagamento = repository.findById(id);

        if(pagamento.isEmpty()){
            throw new EntityNotFoundException();
        }

        pagamento.get().setStatus(Status.CONFIRMADO_SEM_INTEGRACAO);
        repository.save(pagamento.get());
    }
}
