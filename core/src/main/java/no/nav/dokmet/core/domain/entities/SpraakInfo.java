package no.nav.dokmet.core.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.dokmet.core.domain.AbstractDomainObject;

@Entity
@Getter
@Setter
@Table(name = "SPRAAK_INFO")
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class SpraakInfo extends AbstractDomainObject {

	private static final String SPRAAK_INFO_SEQ = "SPRAAK_INFO_SEQ";
	private static final long serialVersionUID = 7086025747405646842L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SPRAAK_INFO_SEQ)
	@SequenceGenerator(name = SPRAAK_INFO_SEQ, sequenceName = SPRAAK_INFO_SEQ, allocationSize = 1)
	@Column(name = "id", nullable = false)
	protected Long id;

	@Column(name = "spraaklag", nullable = false)
	private String spraaklag;

}
