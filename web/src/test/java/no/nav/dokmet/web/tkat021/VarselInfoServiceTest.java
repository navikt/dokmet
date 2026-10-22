package no.nav.dokmet.web.tkat021;

import no.nav.dokmet.core.repository.VarselInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static no.nav.dokmet.web.TestUtils.createVarselInfo;
import static no.nav.dokmet.web.TestUtils.createVarselInfoTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VarselInfoServiceTest {

	@Mock
	private VarselInfoRepository varselInfoRepository;

	@Mock
	private VarselInfoMapper varselInfoMapper;

	@InjectMocks
	private VarselInfoService varselInfoService;

	@Test
	void skalHenteVarselinfo() {
		var varselInfo = createVarselInfo();
		var varselInfoTo = createVarselInfoTo();

		when(varselInfoRepository.findByVarseltypeId(any())).thenReturn(varselInfo);
		when(varselInfoMapper.map(varselInfo)).thenReturn(varselInfoTo);

		var result = varselInfoService.hentVarselinfo(varselInfoTo.getVarseltypeId());

		assertEquals(varselInfoTo, result);
		verify(varselInfoRepository).findByVarseltypeId(varselInfoTo.getVarseltypeId());
		verify(varselInfoMapper).map(varselInfo);
		verifyNoMoreInteractions(varselInfoRepository);
		verifyNoMoreInteractions(varselInfoMapper);
	}

}