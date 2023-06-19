package apap.tugas.SISDM.service;

import apap.tugas.SISDM.model.KaryawanModel;

import java.util.List;
import java.util.Optional;

public interface KaryawanService {

    List<KaryawanModel> getDaftarKaryawan();

    void addKaryawan(KaryawanModel karyawan);

    KaryawanModel getKaryawanByIdKaryawan(Long idKaryawan);

    KaryawanModel updateKaryawan(KaryawanModel karyawan);

    void deleteKaryawan(KaryawanModel karyawan);

//    Optional<KaryawanModel> getFilteredKaryawan(Long idSertifikasi);
}
