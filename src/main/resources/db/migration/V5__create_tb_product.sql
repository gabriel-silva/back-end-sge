CREATE TABLE IF NOT EXISTS TB_PRODUCT(
    ID BIGSERIAL PRIMARY KEY NOT NULL,
    ID_CATEGORY BIGINT NOT NULL REFERENCES TB_CATEGORY(ID),
    ID_PROVIDER BIGINT NOT NULL REFERENCES TB_PROVIDER(ID),
    ID_MEASUREMENT_UNIT BIGINT NOT NULL REFERENCES TB_MEASUREMENT_UNIT(ID),
    NAME CHARACTER VARYING(255) NOT NULL,
    MIN_STOCK INTEGER NOT NULL,
    MAX_STOCK INTEGER NOT NULL,
    STATUS BOOLEAN NOT NULL,
    CREATED_AT TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    UPDATED_AT TIMESTAMP WITHOUT TIME ZONE NOT NULL
);