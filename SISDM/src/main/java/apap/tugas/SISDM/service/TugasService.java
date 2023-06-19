package apap.tugas.SISDM.service;

import apap.tugas.SISDM.model.TugasModel;

import java.util.List;
import java.util.Optional;

public interface TugasService {

    void addTugas(TugasModel tugas);

    List<TugasModel> getDaftarTugas();

    List<Integer> getStatus();

    TugasModel updateTugas(TugasModel tugas);

    Optional<TugasModel> getTugasByIdTugas(Long idTugas);

}
