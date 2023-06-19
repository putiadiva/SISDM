package apap.tugas.SISDM.service;

import apap.tugas.SISDM.model.SertifikasiKaryawanModel;

public interface SertifikasiKaryawanService {

    void tambahSertifikasiKaryawan(SertifikasiKaryawanModel sertifikasiKaryawan);

    void hapusSertifikasiKaryawan (SertifikasiKaryawanModel sertifikasiKaryawan);

    SertifikasiKaryawanModel getSertifikasiKarywanById(Long idSertifikasiKaryawan);
}
