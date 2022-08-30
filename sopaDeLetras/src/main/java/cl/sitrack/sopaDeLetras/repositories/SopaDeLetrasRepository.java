package cl.sitrack.sopaDeLetras.repositories;

import cl.sitrack.sopaDeLetras.model.SopaDeLetras;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface SopaDeLetrasRepository    extends CrudRepository<SopaDeLetras, UUID> {

}
