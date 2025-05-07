package com.jug.demo.templates;

import com.jug.demo.exceptions.ClientNotFoundException;
import com.jug.demo.generated.models.ClientResponse;
import com.jug.demo.mappers.ClientMapper;
import com.jug.demo.repositories.ClientRepository;

import java.util.Optional;

public class GenerateClientReport extends ReportGenerator {

    private final ClientRepository clientRepository;
    private final Integer id;
    private String dataProcessed;

    public GenerateClientReport(Integer id, ClientRepository clientRepository) {
        super(id, clientRepository);
        this.id = id;
        this.clientRepository = clientRepository;
    }

    @Override
    protected Optional<ClientResponse> loadData() {
        return Optional.of(clientRepository.findById(Long.valueOf(id))
                .map(ClientMapper::toResponse).orElseThrow(ClientNotFoundException::new));
    }

    @Override
    protected void process(Object data) {
        ClientResponse clientResponse = ((Optional<ClientResponse>) data).get();
        dataProcessed = clientResponse.getName() + ", Email: " + clientResponse.getEmail();
    }

    @Override
    protected String export() {
        return"Client Report: "+ this.dataProcessed;
    }
}
