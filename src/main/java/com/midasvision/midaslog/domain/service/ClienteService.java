package com.midasvision.midaslog.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.midasvision.midaslog.domain.exception.ServiceException;
import com.midasvision.midaslog.domain.model.Cliente;
import com.midasvision.midaslog.model.repository.ClienteRepository;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Transactional
	public List<Cliente> listar(){
		
		return clienteRepository.findAll();
	}
	
	@Transactional
	public ResponseEntity<Cliente> buscar(Long clienteId) {
		
		return clienteRepository.findById(clienteId)
				.map(cliente -> ResponseEntity.ok(cliente))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@Transactional
	public Cliente criar(Cliente cliente){
		
		boolean consultaEmail = clienteRepository.findByEmail(cliente.getEmail())
				.stream()
				.anyMatch(clienteExistente -> !clienteExistente.equals(cliente));
		
		if(consultaEmail) {
			throw new ServiceException("Email ja cadastrado");
		}
		
		return clienteRepository.save(cliente);
	}
	
	@Transactional
	public ResponseEntity<Cliente> atualizar(Long clienteId, Cliente cliente){
		
		if(!clienteRepository.existsById(clienteId)) {
			return ResponseEntity.notFound().build();
		}
		
		cliente.setId(clienteId);
		cliente = clienteRepository.save(cliente);
		
		return ResponseEntity.ok(cliente);
	}
	
	@Transactional
	public ResponseEntity<Void> excluir(Long clienteId){
		
		if(!clienteRepository.existsById(clienteId)) {
			return ResponseEntity.notFound().build();
		}
		
		clienteRepository.deleteById(clienteId);
		
		return ResponseEntity.noContent().build();
	}
}
