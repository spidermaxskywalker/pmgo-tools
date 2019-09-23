//package br.com.maxgontijo.pmgo.planilhasveiculos.controller;
//
//import java.io.IOException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import br.com.maxgontijo.iunes.dto.arquivo.ArquivoClienteDocumentoDto;
//import br.com.maxgontijo.iunes.model.arquivo.ArquivoClienteDocumento;
//import br.com.maxgontijo.iunes.model.arquivo.ArquivoDados;
//import br.com.maxgontijo.iunes.model.arquivo.ModelArquivo;
//import br.com.maxgontijo.iunes.model.cliente.ClienteCurso;
//import br.com.maxgontijo.iunes.model.cliente.ClienteDocumentacao;
//import br.com.maxgontijo.iunes.ws.repository.ArquivoRepository;
//import br.com.maxgontijo.iunes.ws.util.ModelDtoMapper;
//import br.com.maxgontijo.iunes.ws.util.RestException;
//
//@RestController
//@RequestMapping("/api/file-upload")
//public class FileUploadController extends GenericController {
//	@Autowired
//	private ArquivoRepository arquivoRepository;
//
//	@PostMapping("/cliente-curso/documento/{idClienteCurso}/{tipoDocumento}")
//	@PreAuthorize("hasAuthority('CLIENTE_CURSO_WRITE')")
//	public ArquivoClienteDocumentoDto uploadClienteCursoDocumento(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
//		ClienteCurso cc = getGenericCrudService().find(ClienteCurso.class, idClienteCurso);
//
//		if (cc == null) {
//			throw new RestException("Cliente/Curso " + idClienteCurso + " não encontrado.", RestException.ERROR_NAO_ENCONTRADO);
//		}
//
//		if (file != null) {
//			if (cc.getDocumentacao() == null) {
//				cc.setDocumentacao(new ClienteDocumentacao());
//			}
//
//			ArquivoClienteDocumento doc = cc.getDocumentacao().getDocumento(tipoDocumento);
//			final ArquivoDados dados;
//
//			if (doc != null) {
//				if (doc.getDados() == null) {
//					dados = new ArquivoDados();
//				} else {
//					dados = doc.getDados();
//				}
//			} else {
//				doc = new ArquivoClienteDocumento();
//				doc.setTipoDocumento(tipoDocumento);
//				dados = new ArquivoDados();
//				cc.getDocumentacao().putDocumento(doc, doc.getTipoDocumento());
//			}
//
//			doc.setNome(file.getOriginalFilename());
//			doc.setTipo(file.getContentType());
//
//			dados.setBytes(file.getBytes());
//
//			doc.setDados(dados);
//			getGenericCrudService().save(doc);
//			getGenericCrudService().save(cc.getDocumentacao());
//			getGenericCrudService().save(cc);
//
//			ModelDtoMapper modelMapper = createModelMapperWhiteList("*");
//			ArquivoClienteDocumentoDto docDto = modelToDto(doc, ArquivoClienteDocumentoDto.class, modelMapper);
//
//			return docDto;
//		} else {
//			if (cc.getDocumentacao() != null) {
//				ArquivoClienteDocumento doc = cc.getDocumentacao().getDocumento(tipoDocumento);
//				if (doc != null) {
//					cc.getDocumentacao().putDocumento(null, tipoDocumento);
//					getGenericCrudService().save(cc);
//
//					getGenericCrudService().delete(doc);
//				}
//			}
//
//			return null;
//		}
//	}
//
//	@PreAuthorize("hasAuthority('CLIENTE_CURSO_READ')")
//	@GetMapping(value = "/download/{idArquivo}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//	public HttpEntity<byte[]> download(@PathVariable("idArquivo") Integer idArquivo) throws IOException {
//		ModelArquivo arquivo = arquivoRepository.findOne(idArquivo);
//		if (arquivo == null) {
//			throw new RestException("Arquivo não encontrado.", 404);
//		}
//		HttpHeaders httpHeaders = new HttpHeaders();
//		httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + arquivo.getNome().trim().replace(' ', '_') + "\"");
//		HttpEntity<byte[]> entity = new HttpEntity<byte[]>(arquivo.getDados().getBytes(), httpHeaders);
//		return entity;
//	}
//}
