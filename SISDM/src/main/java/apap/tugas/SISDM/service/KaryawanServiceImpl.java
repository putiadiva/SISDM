package apap.tugas.SISDM.service;

import apap.tugas.SISDM.model.KaryawanModel;
import apap.tugas.SISDM.repository.KaryawanDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class KaryawanServiceImpl implements KaryawanService{

    @Autowired
    KaryawanDb karyawanDb;

    @Override
    public List<KaryawanModel> getDaftarKaryawan() {
        return karyawanDb.findAll();
    }

    @Override
    public void addKaryawan(KaryawanModel karyawan) {
        karyawanDb.save(karyawan);
    }

    @Override
    public KaryawanModel getKaryawanByIdKaryawan(Long idKaryawan) {
        Optional<KaryawanModel> karyawan = karyawanDb.findById(idKaryawan);
        if (karyawan.isPresent()) {
            return karyawan.get();
        } else {
            return null;
        }
    }

    @Override
    public KaryawanModel updateKaryawan(KaryawanModel karyawan) {
        karyawanDb.save(karyawan);
        return karyawan;
    }

    @Override
    public void deleteKaryawan(KaryawanModel karyawan) {
        karyawanDb.delete(karyawan);
    }

//    @Override
//    public Optional<KaryawanModel> getFilteredKaryawan(Long idSertifikasi) {
//        return null;
//    }
}
