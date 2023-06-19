package apap.tugas.SISDM.repository;

import apap.tugas.SISDM.model.TugasModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TugasDb extends JpaRepository<TugasModel, Long> {

}
