package br.com.maxgontijo.pmgo.planilhasveiculos.model;

import java.io.Serializable;

public abstract class Model<ID extends Serializable> implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String SCHEMA_AUTENTICACAO = "autenticacao";

    protected ID id;

    public abstract ID getId();

    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : super.hashCode();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Model)) {
            return false;
        }

        if (!getClass().isAssignableFrom(obj.getClass()) && !obj.getClass().isAssignableFrom(getClass())) {
            return false;
        }

        return id != null ? id.equals(((Model) obj).id) : super.equals(obj);
    }
}
