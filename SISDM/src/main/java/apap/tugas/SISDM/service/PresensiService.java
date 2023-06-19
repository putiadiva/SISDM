package apap.tugas.SISDM.service;

import apap.tugas.SISDM.model.PresensiModel;

import java.util.List;
import java.util.Optional;

public interface PresensiService {

    List<PresensiModel> getDaftarPresensi();

    void addPresensi(PresensiModel presensi);

    Optional<PresensiModel> getPresensiByIdPresensi(Long idPresensi);

    PresensiModel updatePresensi(PresensiModel presensi);
}
