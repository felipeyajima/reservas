package br.com.fiap.aluno.reservas.reservas.service;


// service classe logica onde vai ter a regra de negocio onde vai receber os dados da controller e vai inserir atraves da repository
//
// controller apenas para receber os dados e retornar ao client
// service: toda logica nas operaçoes de negócio na arquitetura de camadas
// repository: faz abstracao do banco de dados para salvar e persistir os dados

import br.com.fiap.aluno.reservas.reservas.controller.exception.ControllerNotFoundException;
import br.com.fiap.aluno.reservas.reservas.dto.ReservaDTO;
import br.com.fiap.aluno.reservas.reservas.entities.Reserva;
import br.com.fiap.aluno.reservas.reservas.repository.ReservaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;




// @service faz com que o spring coloque esta classe no pull de injeção de dependencia
@Service
public class ReservaService {

    //fazendo a injeção da repository. declarando o atributo final
    private final ReservaRepository reservaRepository;

    //escrevendo o construtor, recebera o repository como parametro, nova forma de fazer injecao de dep.
    // autowired colocara no pull de injecao de dependencia, fazendo com o que o spring tome conta
    @Autowired
    public ReservaService(ReservaRepository reservaRepository){
        this.reservaRepository = reservaRepository;
    }

    public Page<ReservaDTO> findAll(Pageable pageable){
        Page<Reserva> reservas = reservaRepository.findAll(pageable);
        return reservas.map(this::toDTO);
    }

    public ReservaDTO findById(Long id){
        Reserva reserva = reservaRepository.findById(id).orElseThrow(() -> new ControllerNotFoundException("reserva não encontrada"));
        return toDTO(reserva);
    }


    public ReservaDTO save(ReservaDTO reservaDTO){
        Reserva reserva = toEntity(reservaDTO);
        reserva = reservaRepository.save(reserva);
        return toDTO(reserva);
    }

    public ReservaDTO update(Long id, ReservaDTO reservaDTO){
        try {
            Reserva reserva = reservaRepository.getReferenceById(id);
            reserva.setResponsavel(reservaDTO.responsavel());
            reserva.setEmail(reservaDTO.email());
            reserva.setTelefone(reservaDTO.telefone());
            reserva.setInicioDaReserva(reservaDTO.inicioDaReserva());
            reserva.setStatus(reservaDTO.status());

            reserva = reservaRepository.save(reserva);

            return toDTO(reserva);

        } catch(EntityNotFoundException e) {
            throw new ControllerNotFoundException("reserva nao encontrada");
        }
    }


    public ReservaDTO efetiva(Long id, ReservaDTO reservaDTO){
        try {
            Reserva reserva = reservaRepository.getReferenceById(id);
            reserva.setResponsavel(reservaDTO.responsavel());
            reserva.setEmail(reservaDTO.email());
            reserva.setTelefone(reservaDTO.telefone());
            reserva.setInicioDaReserva(reservaDTO.inicioDaReserva());
            reserva.setStatus("atendido");

            reserva = reservaRepository.save(reserva);

            return toDTO(reserva);

        } catch(EntityNotFoundException e) {
            throw new ControllerNotFoundException("reserva nao encontrada");
        }

    }

    public void delete(Long id){
        reservaRepository.deleteById(id);
    }

    private ReservaDTO toDTO(Reserva reserva) {
        return new ReservaDTO(
                reserva.getId(),
                reserva.getResponsavel(),
                reserva.getEmail(),
                reserva.getTelefone(),
                reserva.getInicioDaReserva(),
                reserva.getStatus()
        );
    }

    private Reserva toEntity(ReservaDTO reservaDTO){
        return new Reserva(
                reservaDTO.id(),
                reservaDTO.responsavel(),
                reservaDTO.email(),
                reservaDTO.telefone(),
                reservaDTO.inicioDaReserva(),
                reservaDTO.status()
        );
    }


}
