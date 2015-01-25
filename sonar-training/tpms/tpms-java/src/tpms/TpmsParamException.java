package tpms;

import tol.ParamException;

public class TpmsParamException extends ParamException {
    public TpmsParamException(String name) {
        super(name);
    }

    public TpmsParamException(String name, int type) {
        super(name, type);
    }

    public TpmsParamException(String msg, String name) {
        super(msg, name);
    }

    public TpmsParamException(String msg, String name, int type) {
        super(msg, name, type);
    }
}
