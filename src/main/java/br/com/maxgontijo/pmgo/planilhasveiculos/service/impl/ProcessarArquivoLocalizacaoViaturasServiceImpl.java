package br.com.maxgontijo.pmgo.planilhasveiculos.service.impl;

import br.com.maxgontijo.pmgo.planilhasveiculos.dto.LocalizacaoViaturaDto;
import br.com.maxgontijo.pmgo.planilhasveiculos.service.ProcessarArquivoLocalizacaoViaturasService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("processarArquivoLocalizacaoViaturasService")
public class ProcessarArquivoLocalizacaoViaturasServiceImpl extends AbstractService implements ProcessarArquivoLocalizacaoViaturasService {
    @Value("${pmgo.tomtom.api.services.reverseGeocode}")
    private String tomtomApiServiceUrlReverseGeocode;

    @Value("${pmgo.tomtom.api.key}")
    private String tomtomApiKey;

    @Value("${pmgo.google.maps.url_location}")
    private String googleMapsUrl;

    @Override
    public List<LocalizacaoViaturaDto> processarArquivo(InputStream input, OutputStream output) {
        List<LocalizacaoViaturaDto> registros = new ArrayList<>();

        XSSFWorkbook workbook;

        try {
            workbook = new XSSFWorkbook(input);
        } catch (Exception e) {
            throw new RuntimeException("Arquivo inválido.", e);
        }

        CreationHelper createHelper = workbook.getCreationHelper();

        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            XSSFSheet sheet = workbook.getSheetAt(i);

            if (output != null) {
                int CHARACTER_WIDTH_APROX = 256;
                int[] newWidths = new int[]{30, 20, 20, 15};

                XSSFRow row = sheet.getRow(0);
                XSSFCell cell;
                int indexCol = 6;

                for (int j = 0; j < newWidths.length; j++) {
                    sheet.setColumnWidth(j + indexCol, newWidths[j] * CHARACTER_WIDTH_APROX);
                }

                cell = row.createCell(indexCol++);
                cell.setCellValue("Endereço");

                cell = row.createCell(indexCol++);
                cell.setCellValue("Bairro");

                cell = row.createCell(indexCol++);
                cell.setCellValue("Município");

                cell = row.createCell(indexCol++);
                cell.setCellValue("Google Maps");
            }

            for (int j = 1; j <= sheet.getLastRowNum(); j++) {
                XSSFRow row = sheet.getRow(j);

                if (row != null) {
                    int colIndex = 0;
                    Integer sequencia = getCellInt(row.getCell(colIndex++));
                    String prefixo = getCellString(row.getCell(colIndex++));
                    String unidade = getCellString(row.getCell(colIndex++));
                    Date dataHora = getCellDataTime(row.getCell(colIndex++));
                    Double latitude = getCellDouble(row.getCell(colIndex++));
                    Double longitude = getCellDouble(row.getCell(colIndex++));

                    LocalizacaoViaturaDto viatura = new LocalizacaoViaturaDto();

                    viatura.setSequencia(sequencia);
                    viatura.setPrefixo(prefixo);
                    viatura.setUnidade(unidade);
                    viatura.setDataHora(dataHora);
                    viatura.setLatitude(latitude);
                    viatura.setLongitude(longitude);

//                    switch (sequencia) {
//                        case 227:
//                        case 228:
//                        case 175:
//                            carregarEndereco(viatura);
//                            break;
//                    }
                    carregarEndereco(viatura);

                    if (output != null) {
                        XSSFCell cell;
                        cell = row.createCell(colIndex++);
                        cell.setCellValue(viatura.getEndRua());

                        cell = row.createCell(colIndex++);
                        cell.setCellValue(viatura.getEndBairro());

                        cell = row.createCell(colIndex++);
                        cell.setCellValue(viatura.getEndMunicipio());

                        String googleUrl;
                        if (viatura.getLatitude() != null && viatura.getLongitude() != null) {
                            googleUrl = MessageFormat.format(googleMapsUrl,
                                    String.format("%.15f", viatura.getLatitude()).replaceAll(",", "."),
                                    String.format("%.15f", viatura.getLongitude()).replaceAll(",", "."));
                        } else {
                            googleUrl = "";
                        }

                        cell = row.createCell(colIndex++);
                        Hyperlink link = createHelper.createHyperlink(HyperlinkType.URL);
                        link.setAddress(googleUrl);
                        cell.setCellValue(googleUrl);
                        cell.setHyperlink(link);
                    }

                    registros.add(viatura);
                }
            }
        }

        try {
            workbook.write(output);
        } catch (IOException e) {
            try {
                workbook.close();
            } catch (IOException e2) {
                throw new RuntimeException("Erro ao fechar a arquivo.", e);
            }
            throw new RuntimeException("Erro ao gerar arquivo.", e);
        }

        return registros;
    }

    private void carregarEndereco(LocalizacaoViaturaDto viatura) {
        boolean tentarDeNovo = false;
        do {
            try {
                RestTemplate restTemplate = new RestTemplate();

                String url = MessageFormat.format(tomtomApiServiceUrlReverseGeocode,
                        String.format("%.15f", viatura.getLatitude()).replaceAll(",", "."),
                        String.format("%.15f", viatura.getLongitude()).replaceAll(",", "."),
                        tomtomApiKey);

                Map location = restTemplate.getForObject(url, Map.class);

                if (location != null) {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.writerWithDefaultPrettyPrinter();
                        mapper.enable(SerializationFeature.INDENT_OUTPUT);
                        String json = mapper.writeValueAsString(location);
                        viatura.setEndJson(json);
                    } catch (JsonProcessingException e) {
                    }

                    List<Object> list = ((List<Object>) location.get("addresses"));
                    if (list != null && !list.isEmpty()) {
                        Map<String, Object> addressMap = (Map<String, Object>) list.get(0);
                        Map<String, Object> address = (Map<String, Object>) addressMap.get("address");

                        if (address != null) {
                            String buildingNumber = (String) address.get("buildingNumber");
                            String streetNumber = (String) address.get("streetNumber");
                            String street = (String) address.get("street");
                            String streetName = (String) address.get("streetName");
                            String streetNameAndNumber = (String) address.get("streetNameAndNumber");
                            String speedLimit = (String) address.get("speedLimit");
                            String countryCode = (String) address.get("countryCode");
                            String countrySubdivision = (String) address.get("countrySubdivision");
                            String countrySecondarySubdivision = (String) address.get("countrySecondarySubdivision");
                            String countryTertiarySubdivision = (String) address.get("countryTertiarySubdivision");
                            String municipality = (String) address.get("municipality");
                            String postalCode = (String) address.get("postalCode");
                            String municipalitySubdivision = (String) address.get("municipalitySubdivision");
                            String sideOfStreet = (String) address.get("sideOfStreet");
                            String offsetPosition = (String) address.get("offsetPosition");
                            String country = (String) address.get("country");
                            String countryCodeISO3 = (String) address.get("countryCodeISO3");
                            String freeformAddress = (String) address.get("freeformAddress");
                            String countrySubdivisionName = (String) address.get("countrySubdivisionName");
                            String localName = (String) address.get("localName");
                            String extendedPostalCode = (String) address.get("extendedPostalCode");

                            if (freeformAddress != null) {
                                viatura.setEndRua(freeformAddress);
                            } else if (streetNameAndNumber != null) {
                                viatura.setEndRua(streetNameAndNumber);
                            } else if (streetName != null) {
                                viatura.setEndRua(streetName);
                            } else if (street != null) {
                                viatura.setEndRua(street);
                            }

                            viatura.setEndBairro(municipalitySubdivision);
                            viatura.setEndMunicipio(municipality);
                            viatura.setEndCep(extendedPostalCode);
                            viatura.setEndEstado(countrySubdivision);
                        }
                    }
                }

                return;
            } catch (Exception e) {
                if (e instanceof HttpClientErrorException.TooManyRequests) {
                    tentarDeNovo = !tentarDeNovo;
                } else {
                    tentarDeNovo = false;
                }

                if (tentarDeNovo) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                    System.out.println("Erro ao carregar endereço do registro " + viatura.getSequencia() + ". Tentando uma segunda vez...");
                } else {
                    e.printStackTrace();

                    if (viatura.getEndRua() == null) {
                        viatura.setEndRua("ERRO ao carregar endereço: " + e.getMessage());
                    }
                }
            }
        } while (tentarDeNovo);
    }
}
