//package br.com.maxgontijo.pmgo.planilhasveiculos.controller;
//
//import java.io.Serializable;
//import java.lang.reflect.InvocationTargetException;
//import java.sql.SQLException;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import org.hibernate.exception.ConstraintViolationException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//import br.com.maxgontijo.iunes.commons.util.DadosInvalidosException;
//import br.com.maxgontijo.iunes.dto.ErrorDto;
//import br.com.maxgontijo.iunes.dto.ModelWithIdDto;
//import br.com.maxgontijo.iunes.model.Model;
//import br.com.maxgontijo.iunes.model.oauth2.User;
//import br.com.maxgontijo.iunes.ws.controller.util.AbstractListSortComparator;
//import br.com.maxgontijo.iunes.ws.controller.util.DefaultListSort;
//import br.com.maxgontijo.iunes.ws.repository.UtilRepository;
//import br.com.maxgontijo.iunes.ws.repository.util.Constraint;
//import br.com.maxgontijo.iunes.ws.service.GenericCrudService;
//import br.com.maxgontijo.iunes.ws.service.UserService;
//import br.com.maxgontijo.iunes.ws.util.IunesModelMapper;
//import br.com.maxgontijo.iunes.ws.util.ModelDtoMapper;
//import br.com.maxgontijo.iunes.ws.util.RestException;
//
//public abstract class GenericController {
//	@Autowired
//	protected UserService userService;
//
//	private Logger log = Logger.getLogger(getClass().getName());
//
//	@Autowired
//	private GenericCrudService genericCrudService;
//
//	@Autowired
//	protected UtilRepository utilRepository;
//
//	protected final GenericCrudService getGenericCrudService() {
//		return genericCrudService;
//	}
//
//	protected void logInfo(String message) {
//		log.info(message);
//	}
//
//	protected void logDebug(String message) {
//		log.log(Level.FINE, message);
//	}
//
//	protected void logWarn(String message) {
//		log.warning(message);
//	}
//
//	protected void logError(String message) {
//		log.severe(message);
//	}
//
//	protected void logException(Throwable ex) {
//		log.log(Level.SEVERE, ex.getMessage(), ex);
//	}
//
//	protected void logException(String message, Throwable ex) {
//		log.log(Level.SEVERE, message, ex);
//	}
//
//	@ExceptionHandler(Throwable.class)
//	public ResponseEntity<ErrorDto> exceptionHandle(Throwable t) {
//		RestException rex = findCause(t, RestException.class);
//
//		if (rex == null) {
//			DadosInvalidosException ex = findCause(t, DadosInvalidosException.class);
//			if (ex != null) {
//				rex = new RestException(ex.getMessage(), RestException.ERROR_VALIDACAO, ex, ex.getMessages().toArray(new String[0]));
//			}
//		}
//
//		if (rex == null) {
//			Throwable cause;
//
//			if ((cause = findCause(t, AccessDeniedException.class)) != null) {
//				User user = getCurrentUser();
//				final String msg;
//				if (user != null && user.getUsername() != null) {
//					msg = String.format("Usuário %s não tem acesso ao recurso.", user.getUsername());
//				} else {
//					msg = String.format("Usuário %s não tem acesso ao recurso.", user.getUsername());
//				}
//				rex = new RestException(msg, RestException.ERROR_SEM_ACESSO, cause);
//				logDebug(msg);
//			} else if ((cause = findCause(t, ConstraintViolationException.class)) != null) {
//				String constraintName = ((ConstraintViolationException) cause).getConstraintName().toLowerCase();
//
//				try {
//					List<Constraint> constraints = utilRepository.listConstraints();
//					Constraint cons = constraints.stream().filter(c -> constraintName.equals(c.getName())).findAny().get();
//
//					if (cons.getColumns().length > 1) {
//						rex = new RestException("Já existe um registro com os mesmos valores para os seguintes atributos da tabela " + cons.getTable() + " e essa combinação deveria ser única.", RestException.ERROR_VALIDACAO, cause, cons.getColumns());
//					} else {
//						rex = new RestException("Já existe um registro com o mesmo valor para o atributo " + cons.getColumns()[0] + " da tabela " + cons.getTable() + ".", RestException.ERROR_VALIDACAO, cause);
//					}
//				} catch (Exception e) {
//					rex = new RestException("A seguinte constraint foi violada: " + constraintName, RestException.ERROR_VALIDACAO, cause);
//				}
//
//				logException(t);
//			} else if ((cause = findCause(t, SQLException.class)) != null) {
//				SQLException sqlException = (SQLException) cause;
//				if ("23505".equals(sqlException.getSQLState())) {
//					if (sqlException.getMessage().contains("unique")) {
////						int from = sqlException.getMessage().indexOf("Key (") + "Key (".length();
////						int to = sqlException.getMessage().indexOf(")", from);
////						String fieldDuplicated = sqlException.getMessage().substring(from, to);
//						// rex = new RestException("Já existe um registro com o campo " +
//						// fieldDuplicated + " informado.", cause, RestException.ERROR_VALIDACAO);
//
//						rex = new RestException(sqlException.getMessage(), RestException.ERROR_VALIDACAO, cause);
//					} else {
//						rex = new RestException(sqlException.getMessage(), RestException.ERROR_VALIDACAO, cause);
//					}
//				} else {
//					rex = new RestException(sqlException.getMessage(), RestException.ERROR_VALIDACAO, cause);
//				}
//				logException(cause);
//			}
//		}
//
//		ErrorDto error;
//		if (rex != null) {
//			error = new ErrorDto(rex.getCode(), rex.getMessage());
//			if (rex.getDetail().length > 0) {
//				error.setDetail(Arrays.asList(rex.getDetail()));
//			}
//		} else {
//			error = new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), t.getMessage());
//			logException(t);
//		}
//
//		return new ResponseEntity<ErrorDto>(error, HttpStatus.valueOf(error.getCode()));
//	}
//
//	@SuppressWarnings("unchecked")
//	private <THROWABLE extends Throwable> THROWABLE findCause(Throwable t, Class<THROWABLE> throwableClass) {
//		int countDown = 15;
//		while (countDown > 0 && t != null) {
//			if (throwableClass.isAssignableFrom(t.getClass())) {
//				return (THROWABLE) t;
//			} else {
//				if (t != t.getCause()) {
//					t = t.getCause();
//					countDown--;
//				} else {
//					break;
//				}
//			}
//		}
//		return null;
//	}
//
//	protected <MODEL extends Model<? extends Serializable>, DTO extends ModelWithIdDto<? extends Serializable>> DTO modelToDto(MODEL model, Class<DTO> dtoType, ModelDtoMapper modelMapper) {
//		return getGenericCrudService().map(modelMapper, model, dtoType);
//	}
//
//	protected <MODEL extends Model<? extends Serializable>, DTO extends ModelWithIdDto<? extends Serializable>> MODEL dtoToModel(DTO dto, Class<MODEL> modelType, ModelDtoMapper modelMapper) {
//		return modelMapper.map(dto, modelType);
//	}
//
//	protected <MODEL extends Model<? extends Serializable>, DTO extends ModelWithIdDto<? extends Serializable>> List<DTO> modelToDto(List<MODEL> modelList, Class<DTO> dtoType, ModelDtoMapper modelMapper) {
//		return getGenericCrudService().mapList(modelMapper, modelList, dtoType);
//	}
//
//	protected <MODEL extends Model<? extends Serializable>, DTO extends ModelWithIdDto<? extends Serializable>> List<MODEL> dtoToModel(List<DTO> dtoList, Class<MODEL> modelType, ModelDtoMapper modelMapper) {
//		List<MODEL> modelList = new LinkedList<>();
//		for (DTO dto : dtoList) {
//			modelList.add(dtoToModel(dto, modelType, modelMapper));
//		}
//		return modelList;
//	}
//
//	protected User getCurrentUser() {
//		return getCurrentUser(false);
//	}
//
//	protected User getCurrentUser(boolean fromDatabase) {
//		try {
//			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			if (fromDatabase) {
//				user = (User) userService.loadUserByUsername(user.getUsername());
//			}
//			return user;
//		} catch (NullPointerException e) {
//			return null;
//		}
//	}
//
//	protected String getCurrentUsername() {
//		User user = getCurrentUser();
//		if (user != null) {
//			return user.getUsername();
//		} else {
//			return null;
//		}
//	}
//
//	protected String getParameter(String key, Map<String, String> requestParams) {
//		return requestParams.get(key);
//	}
//
//	protected Integer getParameterInteger(String key, Map<String, String> requestParams) {
//		String value = getParameter(key, requestParams);
//		if (value != null) {
//			return Integer.parseInt(value.trim());
//		} else {
//			return null;
//		}
//	}
//
//	protected Boolean getParameterBoolean(String key, Map<String, String> requestParams) {
//		String value = getParameter(key, requestParams);
//		if (value != null) {
//			return Boolean.parseBoolean(value.trim());
//		} else {
//			return null;
//		}
//	}
//
//	protected Character getParameterChar(String key, Map<String, String> requestParams) {
//		String value = getParameter(key, requestParams);
//		if (value != null) {
//			return value.trim().charAt(0);
//		} else {
//			return null;
//		}
//	}
//
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	protected <E> void sortList(List<E> list) {
//		DefaultListSort ann = getClass().getAnnotation(DefaultListSort.class);
//		if (ann != null) {
//			Comparator comparator;
//			try {
//				Class<? extends Comparator<?>> clazz = ann.comparator();
//				if (AbstractListSortComparator.class.isAssignableFrom(clazz)) {
//					comparator = (Comparator<E>) ann.comparator().getDeclaredConstructor(String[].class).newInstance(new Object[] { ann.value() != null && ann.value().length > 0 ? ann.value() : ann.args() });
//				} else {
//					comparator = (Comparator<E>) ann.comparator().newInstance();
//				}
//			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
//				throw new RuntimeException(e);
//			}
//			Collections.sort(list, comparator);
//		}
//	}
//
//	protected ModelDtoMapper createModelMapper() {
//		return this.createModelMapper(new String[0], new String[0]);
//	}
//
//	protected ModelDtoMapper createModelMapper(String[] whiteList, String[] blackList) {
//		return new IunesModelMapper(whiteList, blackList);
//	}
//
//	protected ModelDtoMapper createModelMapperWhiteList(String... attributes) {
//		return this.createModelMapper(attributes, new String[0]);
//	}
//
//	protected ModelDtoMapper createModelMapperBlackList(String... attributes) {
//		return this.createModelMapper(new String[0], attributes);
//	}
//
////
////	protected static class ModelMapperBuilder {
////		private final ModelMapperOrgImpl modelMapper;
////
////		private ModelMapperBuilder() {
////			this.modelMapper = new ModelMapperOrgImpl();
////			this.modelMapper.getModelMapper().getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
////		}
////
////		public static ModelMapperBuilder create() {
////			return new ModelMapperBuilder();
////		}
////
////		public <S, D> Mapping<S, D> mappingType(Class<S> sourceType, Class<D> destinationType) {
////			return new Mapping<>(this, sourceType, destinationType);
////		}
////
////		public ModelDtoMapper mapper() {
////			return this.modelMapper;
////		}
////	}
////
////	protected static class Mapping<S, D> {
////		private final ModelMapperBuilder modelMapperBuilder;
////		private final TypeMap<S, D> typeMap;
////
////		public Mapping(ModelMapperBuilder modelMapperBuilder, Class<S> sourceType, Class<D> destinationType) {
////			this.modelMapperBuilder = modelMapperBuilder;
////			this.typeMap = modelMapperBuilder.modelMapper.getModelMapper().createTypeMap(sourceType, destinationType);
////		}
////
////		public <V> Mapping<S, D> ignore(DestinationSetter<D, V> setter) {
////			this.typeMap.addMappings(mapper -> mapper.skip(setter));
////			return this;
////		}
////
////		public ModelMapperBuilder finish() {
////			return modelMapperBuilder;
////		}
////	}
//}
