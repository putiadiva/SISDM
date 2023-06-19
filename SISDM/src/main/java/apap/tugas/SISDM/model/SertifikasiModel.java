package apap.tugas.SISDM.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sertifikasi")
public class SertifikasiModel {

    @Id
    @Column(name = "id_sertifikasi")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSertifikasi;

    @NotNull
    @Column(name = "nama")
    @Size(max = 255)
    private String nama;

    @OneToMany(mappedBy = "sertifikasi")
    private List<SertifikasiKaryawanModel> listKaryawan;
}
