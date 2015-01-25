package tpms;

import tol.ConfigException;

public class TpmsConfigException extends ConfigException {
    public TpmsConfigException(String scope) {
        super(scope);
    }

    public TpmsConfigException(String msg, String scope) {
        super(msg, scope);
    }
}