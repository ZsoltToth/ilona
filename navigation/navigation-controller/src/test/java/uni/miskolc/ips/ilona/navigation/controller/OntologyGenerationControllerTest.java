package uni.miskolc.ips.ilona.navigation.controller;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import uni.miskolc.ips.ilona.navigation.service.OntologyGenerationService;

public class OntologyGenerationControllerTest {

	private OntologyGenerationService serviceMock;
	private OntologyGenerationController test;
	private MockHttpServletResponse response;
	private File testFile;
	
	@Before
	public void setUp() throws Exception {
		serviceMock = EasyMock.mock(OntologyGenerationService.class);
		test = new OntologyGenerationController(serviceMock);
		testFile = new File("src/resources/TestFile");
		response=new MockHttpServletResponse();
		
		EasyMock.expect(serviceMock.generateRawOntology()).andReturn(testFile);
		EasyMock.replay(serviceMock);
	}

	@Test
	public void testDownloadTemplateOntology() throws IOException {
		assertEquals(FileUtils.readFileToString((test.downloadTemplateOntology(response).getFile())), FileUtils.readFileToString(testFile));
	}

}
