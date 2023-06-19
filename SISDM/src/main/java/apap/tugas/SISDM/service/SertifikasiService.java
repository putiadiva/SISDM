package apap.tugas.SISDM.service;

import apap.tugas.SISDM.model.SertifikasiModel;

import java.util.List;

public interface SertifikasiService {

    List<SertifikasiModel> getDaftarSertifikasi();

    SertifikasiModel getSertifikasiByIdSertifikasi(Long idSertifikasi);
}
