package apap.tugas.SISDM.service;

import apap.tugas.SISDM.model.TugasModel;
import apap.tugas.SISDM.repository.TugasDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TugasServiceImpl implements TugasService {

    @Autowired
    TugasDb tugasDb;

    public List<Integer> status = Arrays.asList(0, 1, 2);

    @Override
    public void addTugas(TugasModel tugas) {
        tugasDb.save(tugas);
    }

    @Override
    public List<TugasModel> getDaftarTugas() {
        return tugasDb.findAll();
    }

    @Override
    public List<Integer> getStatus() {
        return status;
    }

    @Override
    public TugasModel updateTugas(TugasModel tugas) {
        return tugasDb.save(tugas);
    }

    @Override
    public Optional<TugasModel> getTugasByIdTugas(Long idTugas) {
        return tugasDb.findById(idTugas);
    }
}
