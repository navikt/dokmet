package no.nav.dokmet.core.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "xsd_fil")
public class XsdFil {

	@Id
	@Column(name = "filsti", nullable = false)
	private String filsti;

	@Column(name = "filnavn", nullable = false)
	private String filnavn;

	@Column(name = "xsdfil", nullable = false)
	@Lob
	private byte[] xsdfil;

	@Column(name = "brevpakke", nullable = false)
	private String brevpakke;

	@Builder.Default
	@Column(name = "oppdatert_tidspunkt")
	private LocalDateTime oppdatertTidspunkt = LocalDateTime.now();
}