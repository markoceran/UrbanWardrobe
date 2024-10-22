package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.Worker;
import rs.ac.uns.ftn.model.dto.UserDTO;

public interface WorkerService {

    Worker createWorker(UserDTO worker);

}
