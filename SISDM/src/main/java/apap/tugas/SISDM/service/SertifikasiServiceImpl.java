package apap.tugas.SISDM.service;

import apap.tugas.SISDM.model.SertifikasiModel;
import apap.tugas.SISDM.repository.SertifikasiDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SertifikasiServiceImpl implements SertifikasiService {

    @Autowired
    SertifikasiDb sertifikasiDb;

    @Override
    public List<SertifikasiModel> getDaftarSertifikasi() {
        return sertifikasiDb.findAll();
    }

    @Override
    public SertifikasiModel getSertifikasiByIdSertifikasi(Long idSertifikasi) {
        Optional<SertifikasiModel> sertifikasi = sertifikasiDb.findById(idSertifikasi);
        if (sertifikasi.isPresent()) {
            return sertifikasi.get();
        } else {
            return null;
        }
    }
}
