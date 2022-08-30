package cl.sitrack.sopaDeLetras.services;


import cl.sitrack.sopaDeLetras.model.SopaDeLetras;
import cl.sitrack.sopaDeLetras.repositories.SopaDeLetrasRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SopaDeLetrasServiceImpl implements SopaDeLetrasService {
    SopaDeLetrasRepository sopaDeLetrasRepository;
    public SopaDeLetrasServiceImpl(SopaDeLetrasRepository sopaDeLetrasRepository) {
        this.sopaDeLetrasRepository = sopaDeLetrasRepository;
    }

    @Override
    public List<SopaDeLetras> getSopaDeLetras() {
        List<SopaDeLetras> sopaDeLetras = new ArrayList<>();
        sopaDeLetrasRepository.findAll().forEach(sopaDeLetras::add);
        return sopaDeLetras;
    }

    @Override
    public Optional<SopaDeLetras> getSopaDeLetrasById(UUID id) {
        return sopaDeLetrasRepository.findById(id);
    }

    @Override
    public SopaDeLetras insert(SopaDeLetras sopaDeLetras) {
        return sopaDeLetrasRepository.save(sopaDeLetras);
    }

    @Override
    public void updateSopaDeLetras(UUID id, SopaDeLetras sopaDeLetras) {
        SopaDeLetras sopaDeLetrasFromDb = sopaDeLetrasRepository.findById(id).get();
        System.out.println(sopaDeLetrasFromDb.toString());
        sopaDeLetrasFromDb.setNombre(sopaDeLetras.getNombre());
        sopaDeLetrasRepository.save(sopaDeLetrasFromDb);
    }

    @Override
    public void deleteSopaDeLetras(UUID id) {
        sopaDeLetrasRepository.deleteById(id);
    }
}

