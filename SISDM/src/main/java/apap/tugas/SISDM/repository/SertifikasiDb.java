package apap.tugas.SISDM.repository;

import apap.tugas.SISDM.model.SertifikasiModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SertifikasiDb extends JpaRepository<SertifikasiModel, Long> {

}
