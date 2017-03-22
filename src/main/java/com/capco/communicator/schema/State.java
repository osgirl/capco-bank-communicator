package com.capco.communicator.schema;

public enum State {

    //Valid states
    DECODE,
    VALIDATE,
    TRANSFORM,
    DUPLICATE_CHECK,
    CORRELATE,
    RULES,
    APPROVE,
    DISPATCH,
    DONE,

    //Error states
    DECODE_ERROR,
    VALIDATE_ERROR,
    TRANSFORM_ERROR,
    DUPLICATE_CHECK_ERROR,
    CORRELATE_ERROR,
    RULES_ERROR,
    APPROVE_ERROR,
    DISPATCH_ERROR

}
