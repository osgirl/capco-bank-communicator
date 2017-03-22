package com.capco.communicator.schema;

public enum State {

    DECODE,
    VALIDATE,
    TRANSFORM,
    TRANSFORM_ERROR,
    DUPLICATE_CHECK,
    CORRELATE,
    RULES,
    APPROVE,
    DISPATCH,
    DONE

}
