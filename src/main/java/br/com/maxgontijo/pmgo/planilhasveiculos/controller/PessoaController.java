//package br.com.maxgontijo.pmgo.planilhasveiculos.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import br.com.maxgontijo.iunes.dto.PessoaDto;
//import br.com.maxgontijo.iunes.model.Pessoa;
//import br.com.maxgontijo.iunes.ws.service.PessoaService;
//import br.com.maxgontijo.iunes.ws.util.RestException;
//
//@RestController
//@RequestMapping("/api/pessoa")
//public class PessoaController extends GenericController {
//	@Autowired
//	private PessoaService pessoaService;
//
//	@GetMapping(value = "/me")
//	public PessoaDto findMe() {
//		String username = getCurrentUsername();
//
//		Pessoa pessoa = userService.loadPessoaByUsername(username);
//
//		if (pessoa == null) {
//			throw new RestException("Usuário não encontrado: " + username, RestException.ERROR_NAO_ENCONTRADO);
//		}
//
//		return modelToDto(pessoa, PessoaDto.class, createMapper_Find());
//	}
//
//	@Override
//	protected PessoaService getCrudService() {
//		return pessoaService;
//	}
//}