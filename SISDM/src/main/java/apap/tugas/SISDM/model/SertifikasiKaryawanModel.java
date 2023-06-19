package apap.tugas.SISDM.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sertifikasi_karyawan")
public class SertifikasiKaryawanModel {

    @Id
    @Column(name = "id_sertifikasi_karyawan")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSertifikasiKaryawan;

    @ManyToOne
    @JoinColumn(name = "id_sertifikasi")
    SertifikasiModel sertifikasi;

    @ManyToOne
    @JoinColumn(name = "id_karyawan")
    KaryawanModel karyawan;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "tanggal_pengambilan")
    private LocalDate tanggalPengambilan;

    @Size(max = 14)
    @Column(name = "no_sertifikasi")
    private String noSertifikasi;
}
