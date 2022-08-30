package cl.sitrack.sopaDeLetras.services;

import cl.sitrack.sopaDeLetras.model.SopaDeLetras;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SopaDeLetrasService {

    List<SopaDeLetras> getSopaDeLetras();

    Optional<SopaDeLetras> getSopaDeLetrasById(UUID id);

    SopaDeLetras insert(SopaDeLetras sopaDeLetras);

    void updateSopaDeLetras(UUID id, SopaDeLetras sopaDeLetras);

    void deleteSopaDeLetras(UUID id);
}
