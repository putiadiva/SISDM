package apap.tugas.SISDM.repository;

import apap.tugas.SISDM.model.SertifikasiKaryawanModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SertifikasiKaryawanDb extends JpaRepository<SertifikasiKaryawanModel, Long> {


}
