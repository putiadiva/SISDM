package apap.tugas.SISDM.service;

import apap.tugas.SISDM.model.PresensiModel;
import apap.tugas.SISDM.repository.PresensiDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PresensiServiceImpl implements PresensiService {

    @Autowired
    PresensiDb presensiDb;

    @Override
    public List<PresensiModel> getDaftarPresensi() {
        return presensiDb.findAll();
    }

    @Override
    public void addPresensi(PresensiModel presensi) {
        presensiDb.save(presensi);
    }

    @Override
    public Optional<PresensiModel> getPresensiByIdPresensi(Long idPresensi) {
        return presensiDb.findById(idPresensi);
    }

    @Override
    public PresensiModel updatePresensi(PresensiModel presensi) {
        return presensiDb.save(presensi);
    }
}
