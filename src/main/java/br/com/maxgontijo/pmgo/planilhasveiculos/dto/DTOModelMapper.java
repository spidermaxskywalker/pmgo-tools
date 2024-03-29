package br.com.maxgontijo.pmgo.planilhasveiculos.dto;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;

import javax.persistence.EntityManager;
import javax.persistence.Id;

import org.modelmapper.ModelMapper;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DTOModelMapper extends RequestResponseBodyMethodProcessor {
	private static final ModelMapper modelMapper = new ModelMapper();

	private EntityManager entityManager;

	public DTOModelMapper(ObjectMapper objectMapper, EntityManager entityManager) {
		super(Collections.singletonList(new MappingJackson2HttpMessageConverter(objectMapper)));
		this.entityManager = entityManager;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(DTO.class);
	}

//	@Override
//	public boolean supportsReturnType(MethodParameter returnType) {
//		return returnType.hasMethodAnnotation(DTO.class);
//	}

	@Override
	protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
		binder.validate();
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Object dto = super.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
		Object id = getEntityId(dto);
		if (id == null) {
			return modelMapper.map(dto, parameter.getParameterType());
		} else {
			Object persistedObject = entityManager.find(parameter.getParameterType(), id);
			modelMapper.map(dto, persistedObject);
			return persistedObject;
		}
	}

	@Override
	protected Object readWithMessageConverters(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType) throws IOException, HttpMediaTypeNotSupportedException, HttpMessageNotReadableException {
		for (Annotation ann : parameter.getParameterAnnotations()) {
			DTO dtoType = AnnotationUtils.getAnnotation(ann, DTO.class);
			if (dtoType != null) {
				return super.readWithMessageConverters(inputMessage, parameter, dtoType.value());
			}
		}
		throw new RuntimeException("Não foi encontrada a anotação " + DTO.class.getName() + " em nenhum argumento do método " + parameter.getMethod().getDeclaringClass().getName() + "." + parameter.getMethod().getName() + ".");
	}

//	@Override
//	@SuppressWarnings("unchecked")
//	protected <T> void writeWithMessageConverters(T value, MethodParameter returnType, ServletServerHttpRequest inputMessage, ServletServerHttpResponse outputMessage) throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {
//		for (Annotation ann : returnType.getParameterAnnotations()) {
//			DTO dtoType = AnnotationUtils.getAnnotation(ann, DTO.class);
//			if (dtoType != null) {
//				Class<?> returnTypeClass = returnType.getMethod().getReturnType();
//
//				Object valueDto;
//				if (List.class.isAssignableFrom(returnTypeClass)) {
//					valueDto = modelMapper.map(value, List.class);
//				} else {
//					valueDto = modelMapper.map(value, dtoType.value());
//				}
//
//				super.writeWithMessageConverters(valueDto, returnType, inputMessage, outputMessage);
//				return;
//			}
//		}
//		throw new RuntimeException("Não foi encontrada a anotação " + DTO.class.getName() + " no retorno do método " + returnType.getMethod().getDeclaringClass().getName() + "." + returnType.getMethod().getName() + ".");
//	}

	private Object getEntityId(Object dto) {
		for (Field field : dto.getClass().getDeclaredFields()) {
			if (field.getAnnotation(Id.class) != null) {
				try {
					field.setAccessible(true);
					return field.get(dto);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return null;
	}
}