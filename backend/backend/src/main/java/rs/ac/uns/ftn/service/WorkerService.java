package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.User;
import rs.ac.uns.ftn.model.Worker;

import java.util.List;
import java.util.Optional;

public interface WorkerService {

    Worker findByEmail(String email);

    Worker createWorker(Worker worker);

    Optional<Worker> getById(Long id);

    List<Worker> getAll();

}
