package br.com.maxgontijo.pmgo.planilhasveiculos.service.impl;

import org.apache.poi.xssf.usermodel.XSSFCell;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AbstractService {
    protected final SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
    protected final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
    protected final SimpleDateFormat sdfDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    protected Date getCellDate(XSSFCell cell) {
        try {
            try {
                return cell.getDateCellValue();
            } catch (Exception e) {
                String value = cell.getStringCellValue();
                return sdfDate.parse(value);
            }
        } catch (Exception e) {
            return null;
        }
    }

    protected Date getCellTime(XSSFCell cell) {
        try {
            try {
                return cell.getDateCellValue();
            } catch (Exception e) {
                String value = cell.getStringCellValue();
                return sdfTime.parse(value);
            }
        } catch (Exception e) {
            return null;
        }
    }

    protected Date getCellDataTime(XSSFCell cell) {
        try {
            try {
                return cell.getDateCellValue();
            } catch (Exception e) {
                String value = cell.getStringCellValue();
                return sdfDateTime.parse(value);
            }
        } catch (Exception e) {
            return null;
        }
    }

    protected Date getCellDataTime(XSSFCell cellDate, XSSFCell cellTime) {
        try {
            Date data = getCellDate(cellDate);
            Date time = getCellTime(cellTime);

            if (time == null) {
                return data;
            } else {
                return sdfDateTime.parse(sdfDate.format(data) + " " + sdfTime.format(time));
            }
        } catch (Exception e) {
            return null;
        }
    }

    protected Long getCellLong(XSSFCell cell) {
        try {
            try {
                return (long) cell.getNumericCellValue();
            } catch (Exception e) {
                String value = cell.getStringCellValue();
                return (long) Double.parseDouble(value);
            }
        } catch (Exception e) {
            return null;
        }
    }

    protected Integer getCellInt(XSSFCell cell) {
        try {
            try {
                return (int) cell.getNumericCellValue();
            } catch (Exception e) {
                String value = cell.getStringCellValue();
                return (int) Double.parseDouble(value);
            }
        } catch (Exception e) {
            return null;
        }
    }

    protected Double getCellDouble(XSSFCell cell) {
        try {
            try {
                return cell.getNumericCellValue();
            } catch (Exception e) {
                String value = cell.getStringCellValue();
                return Double.parseDouble(value);
            }
        } catch (Exception e) {
            return null;
        }
    }

    protected String getCellString(XSSFCell cell) {
        try {
            return cell.getStringCellValue();
        } catch (Exception e) {
            return null;
        }
    }

    private String limparTexto(String nome) {
        nome = Normalizer
                .normalize(nome, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
        nome = nome.replaceAll("[^a-zA-Z0-9 ]", " ").replaceAll("\\s+", " ").trim().toUpperCase();
        return nome;
    }
}
