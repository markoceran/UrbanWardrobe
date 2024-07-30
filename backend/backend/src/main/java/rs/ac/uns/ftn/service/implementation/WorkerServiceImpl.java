package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.model.BasicUser;
import rs.ac.uns.ftn.model.Worker;
import rs.ac.uns.ftn.model.dto.UserDTO;
import rs.ac.uns.ftn.repository.WorkerRepository;
import rs.ac.uns.ftn.service.BasicUserService;
import rs.ac.uns.ftn.service.WorkerService;
import java.util.List;
import java.util.Optional;

@Service
public class WorkerServiceImpl implements WorkerService {

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private BasicUserService basicUserService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public WorkerServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Worker> getAll() {
        return workerRepository.findAll();
    }

    @Override
    public Optional<Worker> getById(Long id) {
        return workerRepository.findById(id);
    }


    @Override
    public Worker findByEmail(String email) {
        return workerRepository.findByEmail(email).orElse(null);
    }
    @Override
    public Worker createWorker(UserDTO worker) {

        BasicUser user = basicUserService.findByEmail(worker.getEmail());

        if(user != null){
            return null;
        }

        Worker newWorker = new Worker();
        newWorker.setEmail(worker.getEmail());
        newWorker.setPassword(passwordEncoder.encode(worker.getPassword()));
        newWorker.setFirstName(worker.getFirstName());
        newWorker.setLastName(worker.getLastName());
        newWorker.setPhoneNumber(worker.getPhoneNumber());
        newWorker = workerRepository.save(newWorker);

        return newWorker;
    }
}
