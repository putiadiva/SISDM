package apap.tugas.SISDM.service;

import apap.tugas.SISDM.model.SertifikasiKaryawanModel;
import apap.tugas.SISDM.repository.SertifikasiKaryawanDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class SertifikasiKaryawanServiceImpl implements SertifikasiKaryawanService {

    @Autowired
    SertifikasiKaryawanDb sertifikasiKaryawanDb;

    @Override
    public void tambahSertifikasiKaryawan(SertifikasiKaryawanModel sertifikasiKaryawan) {
        sertifikasiKaryawanDb.save(sertifikasiKaryawan);
    }

    @Override
    public void hapusSertifikasiKaryawan(SertifikasiKaryawanModel sertifikasiKaryawan) {
        sertifikasiKaryawanDb.delete(sertifikasiKaryawan);
    }

    @Override
    public SertifikasiKaryawanModel getSertifikasiKarywanById(Long idSertifikasiKaryawan) {
        Optional<SertifikasiKaryawanModel> sertifikasiKaryawan =  sertifikasiKaryawanDb.findById(idSertifikasiKaryawan);
        if (sertifikasiKaryawan.isPresent()) {
            return sertifikasiKaryawan.get();
        } else {
            return null;
        }
    }
}
